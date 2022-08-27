package yio.tro.onliyoy.game.viewable_model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.view.GameView;
import yio.tro.onliyoy.game.view.game_renders.GameRendersList;
import yio.tro.onliyoy.stuff.*;

import java.util.ArrayList;

public class VmCacheManager implements IEventListener {

    ViewableModel viewableModel;
    public ArrayList<CacheBlock> blocks;
    CacheUpdateRule lazyUpdateRule;
    boolean matchStartDetected;
    public boolean lazyUpdateRequired;
    boolean readyToRequireLazyUpdate;
    public RectangleYio optiFrame; // smallest frame that contains all main hexes
    public ArrayList<CacheBlock> intersectedBlocks;
    public ArrayList<Hex> lazyHexes;
    public ArrayList<Hex> instantHexes;
    ShapeRenderer shapeRenderer;
    boolean instantUpdateRequired;
    public ArrayList<Hex> mainHexes;
    public ArrayList<ViewableHex> ctViewableHexes; // ct - color transition
    int bWidth;
    int bHeight;
    RepeatYio<VmCacheManager> repeatRemoveCtvHexes;
    boolean backgroundVisible;


    public VmCacheManager(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
        blocks = new ArrayList<>();
        setupBlockSize();
        lazyUpdateRule = null;
        matchStartDetected = false;
        lazyUpdateRequired = false;
        readyToRequireLazyUpdate = false;
        optiFrame = new RectangleYio();
        intersectedBlocks = new ArrayList<>();
        lazyHexes = new ArrayList<>();
        instantHexes = new ArrayList<>();
        shapeRenderer = new ShapeRenderer();
        mainHexes = null; // yes, it's just a reference
        ctViewableHexes = new ArrayList<>();
        backgroundVisible = true;
        initRepeats();
    }


    private void setupBlockSize() {
        // making block sizes smaller doesn't help at all
        // I've figured it out by measuring cache update times for different sizes
        // making it rectangular also doesn't help
        // though assigning both width and height to 1.4 * screenWidth really helps
        // but I suspect that it may cause graphic bugs
        bWidth = Gdx.graphics.getWidth();
        bHeight = Gdx.graphics.getHeight();
    }


    private void initRepeats() {
        repeatRemoveCtvHexes = new RepeatYio<VmCacheManager>(this, 60) {
            @Override
            public void performAction() {
                parent.removeCtViewableHexes();
            }
        };
    }


    @Override
    public void onEventValidated(AbstractEvent event) {
        if (!willEventAffectCacheWhenApplied(event)) return;
        if (getGameController().gameMode == GameMode.editor) return;
        lazyUpdateRule = detectLazyUpdateRule();
        // I shouldn't accumulate events here because some of them can be reusable
        Hex modifiedHex = getModifiedHex(event);
        ViewableHex viewableHex = viewableModel.landsManager.getViewableHex(modifiedHex);
        if (viewableHex == null) {
            System.out.println("VmCacheManager.onEventValidated problem: viewable hex is null, event = " + event);
            return;
        }
        if (doesEventRequireInstantUpdate(event)) {
            addToInstantHexes(modifiedHex);
            instantUpdateRequired = true;
        } else {
            addToLazyHexes(modifiedHex);
            viewableHex.inTransition = true; // transition animation will be displayed until cache is updated
        }
        logOnEventValidated(event);
    }


    private void logOnEventValidated(AbstractEvent event) {
        if (!DebugFlags.cacheInvestigation) return;
        if (doesEventRequireInstantUpdate(event)) {
            System.out.println(event + " triggered instant update");
        }
    }


    private void addToLazyHexes(Hex modifiedHex) {
        if (lazyHexes.contains(modifiedHex)) return;
        lazyHexes.add(modifiedHex);
    }


    private void addToInstantHexes(Hex modifiedHex) {
        if (instantHexes.contains(modifiedHex)) return;
        instantHexes.add(modifiedHex);
    }


    boolean doesEventRequireInstantUpdate(AbstractEvent event) {
        if (!matchStartDetected) return true;
        if (!viewableModel.transitionsEnabled) return true;
        CoreModel refModel = getRefModel();
        switch (event.getType()) {
            default:
                return true;
            case piece_add:
                return false;
            case piece_build:
                EventPieceBuild eventPieceBuild = (EventPieceBuild) event;
                if (isStaticPiece(eventPieceBuild.pieceType)) return false;
                Hex epbHex = refModel.getHexWithSameCoordinates(eventPieceBuild.hex);
                if (isStaticPiece(epbHex.piece)) return true;
                Province province = refModel.provincesManager.getProvince(eventPieceBuild.provinceId);
                return province != null && province.getColor() != epbHex.color;
        }
    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        if (lazyUpdateRequired) return;
        if (!doesEventTriggerLazyUpdate(event)) return;
        readyToRequireLazyUpdate = true; // update will be required on next call of move()
        logOnEventApplied(event);
    }


    private void logOnEventApplied(AbstractEvent event) {
        if (!DebugFlags.cacheInvestigation) return;
        System.out.println(event + " triggered lazy update with rule " + lazyUpdateRule);
    }


    @Override
    public int getListenPriority() {
        return 2;
    }


    public void onAdvancedStuffCreated() {
        matchStartDetected = true;
        initBlocks();
        forceStaticPieceAnimations();
        ctViewableHexes.clear();
        applyUpdate(false);
        backgroundVisible = false; // render background only once
    }


    public void onReplayRewindApplied() {
        readyToRequireLazyUpdate = true;
    }


    private void forceStaticPieceAnimations() {
        for (ViewableHex viewableHex : viewableModel.landsManager.viewableHexes) {
            viewableHex.pieceFactor.reset();
        }
    }


    private void removeCtViewableHexes() {
        if (ctViewableHexes.size() == 0) return;
        for (int i = ctViewableHexes.size() - 1; i >= 0; i--) {
            ViewableHex viewableHex = ctViewableHexes.get(i);
            if (viewableHex.isColorFactorInMovementMode()) continue;
            ctViewableHexes.remove(i);
        }
    }


    public void move() {
        repeatRemoveCtvHexes.move();
        if (instantUpdateRequired) {
            applyUpdate(true);
            return;
        }
        if (!lazyUpdateRequired) {
            if (!readyToRequireLazyUpdate) return;
            readyToRequireLazyUpdate = false;
            lazyUpdateRequired = true;
        }
        if (viewableModel.isSomethingMovingCurrently()) return;
        applyUpdate(false);
    }


    public void applyUpdate(boolean instant) {
        logApplyUpdate(instant);
        tagAllHexesAsInvisible();
        updateMainHexesReference(instant);
        checkToFixMainHexesList();
        tagMainHexes();
        updateOptiFrame();
        updateIntersectedBlocks();
        inflateOptiFrameToMatchIntersectedBlocks();
        tagSecondaryHexes();
        tagLazyHexes(instant);
        copyCountersToRefModel();
        GameView gameView = getGameController().yioGdxGame.gameView;
        SpriteBatch batchMovable = gameView.batchMovable;
        for (CacheBlock cacheBlock : intersectedBlocks) {
            updateCacheBlock(batchMovable, cacheBlock);
        }
        batchMovable.setProjectionMatrix(gameView.orthoCam.combined);
        mainHexes.clear();
        resetInTransitionFlags(instant);
        onUpdateApplied(instant);
    }


    private void logApplyUpdate(boolean instant) {
        if (!DebugFlags.cacheInvestigation) return;
        if (instant) {
            System.out.println("VmCacheManager.logApplyUpdate: instant");
        } else {
            System.out.println("VmCacheManager.logApplyUpdate: lazy");
        }
    }


    private void inflateOptiFrameToMatchIntersectedBlocks() {
        for (CacheBlock cacheBlock : intersectedBlocks) {
            RectangleYio pos = cacheBlock.position;
            if (pos.x < optiFrame.x) {
                optiFrame.width = optiFrame.x + optiFrame.width - pos.x;
                optiFrame.x = pos.x;
            }
            if (pos.x + pos.width > optiFrame.x + optiFrame.width) {
                optiFrame.width = pos.x + pos.width - optiFrame.x;
            }
            if (pos.y < optiFrame.y) {
                optiFrame.height = optiFrame.y + optiFrame.height - pos.y;
                optiFrame.y = pos.y;
            }
            if (pos.y + pos.height > optiFrame.y + optiFrame.height) {
                optiFrame.height = pos.y + pos.height - optiFrame.y;
            }
        }
    }


    private void copyCountersToRefModel() {
        if (getGameController().gameMode == GameMode.editor) return; // ref model not used in editor
        CoreModel refModel = getRefModel();
        // this is slow but more reliable variant
//        for (Hex refHex : refModel.hexes) {
//            Hex realHex = viewableModel.getHexWithSameCoordinates(refHex);
//            refHex.counter = realHex.counter;
//        }
        // this variant will only work properly if hexes lists are in same order
        // it's not very reliable but it's the fastest way
        for (int i = 0; i < refModel.hexes.size(); i++) {
            refModel.hexes.get(i).counter = viewableModel.hexes.get(i).counter;
        }
    }


    private void onUpdateApplied(boolean instant) {
        if (instant) {
            instantUpdateRequired = false;
            return;
        }
        lazyUpdateRequired = false;
    }


    private void updateMainHexesReference(boolean instant) {
        if (instant) {
            mainHexes = instantHexes;
            return;
        }
        mainHexes = lazyHexes;
    }


    private void tagLazyHexes(boolean instant) {
        if (!instant) return;
        for (Hex lazyHex : lazyHexes) {
            lazyHex.counter = 2;
        }
    }


    private void checkToFixMainHexesList() {
        if (mainHexes.size() > 0) return;
        mainHexes.addAll(viewableModel.hexes);
    }


    private void updateIntersectedBlocks() {
        intersectedBlocks.clear();
        if (areAllHexesTaggedAsVisible()) {
            // some blocks may not contain hexes
            // but at the start of match they have to be updated
            intersectedBlocks.addAll(blocks);
            return;
        }
        for (CacheBlock cacheBlock : blocks) {
            if (!cacheBlock.position.intersects(optiFrame)) continue;
            intersectedBlocks.add(cacheBlock);
        }
    }


    private boolean areAllHexesTaggedAsVisible() {
        for (Hex hex : viewableModel.hexes) {
            if (hex.counter == -1) return false;
        }
        return true;
    }


    private void tagSecondaryHexes() {
        for (Hex hex : viewableModel.hexes) {
            if (hex.counter != -1) continue;
            if (!optiFrame.intersects(hex.position)) continue;
            hex.counter = 1;
        }
    }


    private void updateOptiFrame() {
        Hex firstMainHex = getFirstMainHex();
        if (firstMainHex == null) return;
        PointYio fmhCenter = firstMainHex.position.center;
        float left = fmhCenter.x;
        float right = fmhCenter.x;
        float top = fmhCenter.y;
        float bottom = fmhCenter.y;
        for (Hex hex : mainHexes) {
            float x = hex.position.center.x;
            float y = hex.position.center.y;
            if (x < left) {
                left = x;
            }
            if (x > right) {
                right = x;
            }
            if (y < bottom) {
                bottom = y;
            }
            if (y > top) {
                top = y;
            }
        }
        optiFrame.set(left, bottom, right - left, top - bottom);
        optiFrame.increase(1.2f * viewableModel.getHexRadius());
    }


    private Hex getFirstMainHex() {
        for (Hex hex : viewableModel.hexes) {
            if (hex.counter == 0) return hex;
        }
        return null;
    }


    private void tagMainHexes() {
        for (Hex hex : mainHexes) {
            hex.counter = 0;
        }
    }


    private void updateCacheBlock(SpriteBatch batchMovable, CacheBlock cacheBlock) {
        cacheBlock.frameBufferYio.begin();
        batchMovable.begin();
        cacheBlock.prepareBatch(batchMovable);
        applyGameRenders(cacheBlock);
        cacheBlock.checkToCreateTextureRegion();
        batchMovable.end();
        cacheBlock.frameBufferYio.end();
    }


    private void applyGameRenders(CacheBlock cacheBlock) {
        if (backgroundVisible) {
            GameRendersList.getInstance().renderBackground.renderSingleBlock(cacheBlock.position);
        }
        GameRendersList.getInstance().renderCacheableStuff.render();
    }


    private void tagAllHexesAsInvisible() {
        for (Hex hex : viewableModel.hexes) {
            hex.counter = -1;
        }
    }


    private void resetInTransitionFlags(boolean instant) {
        if (instant) return;
        for (ViewableHex viewableHex : viewableModel.landsManager.viewableHexes) {
            viewableHex.inTransition = false;
        }
    }


    boolean doesEventTriggerLazyUpdate(AbstractEvent event) {
        // this method is called after event was applied
        if (lazyUpdateRule == null) return false;
        if (!viewableModel.transitionsEnabled) return false;
        switch (lazyUpdateRule) {
            default:
                System.out.println("VmCacheManager.doesEventTriggerUpdate: problem");
                return false;
            case manual:
                return false;
            case instant:
                return true;
            case turn:
                return event.getType() == EventType.turn_end;
            case lap:
                if (event.getType() != EventType.turn_end) return false;
                return getRefModel().turnsManager.turnIndex == 0;
        }
    }


    Hex getModifiedHex(AbstractEvent event) {
        switch (event.getType()) {
            default:
                return null;
            case piece_add:
                EventPieceAdd eventPieceAdd = (EventPieceAdd) event;
                return eventPieceAdd.hex;
            case piece_delete:
                EventPieceDelete eventPieceDelete = (EventPieceDelete) event;
                return eventPieceDelete.hex;
            case piece_build:
                EventPieceBuild eventPieceBuild = (EventPieceBuild) event;
                return eventPieceBuild.hex;
            case unit_move:
                EventUnitMove eventUnitMove = (EventUnitMove) event;
                return eventUnitMove.finish;
            case hex_change_color:
                EventHexChangeColor eventHexChangeColor = (EventHexChangeColor) event;
                return eventHexChangeColor.hex;
        }
    }


    boolean willEventAffectCacheWhenApplied(AbstractEvent event) {
        // important: this method should be called before event was applied
        if (!matchStartDetected) return false;
        CoreModel refModel = getRefModel();
        switch (event.getType()) {
            default:
                return false;
            case piece_add:
                EventPieceAdd eventPieceAdd = (EventPieceAdd) event;
                return isStaticPiece(eventPieceAdd.pieceType);
            case piece_delete:
                EventPieceDelete eventPieceDelete = (EventPieceDelete) event;
                Hex epdHex = refModel.getHexWithSameCoordinates(eventPieceDelete.hex);
                if (epdHex == null) return true;
                if (epdHex.piece == null) return true; // it helps to avoid certain graphic bugs
                return isStaticPiece(epdHex.piece);
            case piece_build:
                EventPieceBuild eventPieceBuild = (EventPieceBuild) event;
                if (isStaticPiece(eventPieceBuild.pieceType)) return true;
                Hex epbHex = refModel.getHexWithSameCoordinates(eventPieceBuild.hex);
                if (isStaticPiece(epbHex.piece)) return true;
                Province province = refModel.provincesManager.getProvince(eventPieceBuild.provinceId);
                return province != null && province.getColor() != epbHex.color;
            case unit_move:
                EventUnitMove eventUnitMove = (EventUnitMove) event;
                Hex refStartHex = refModel.getHexWithSameCoordinates(eventUnitMove.start);
                Hex refFinishHex = refModel.getHexWithSameCoordinates(eventUnitMove.finish);
                if (eventUnitMove.colorTransferEnabled && refStartHex.color != refFinishHex.color) return true;
                return isStaticPiece(refFinishHex.piece);
            case hex_change_color:
                return true;
        }
    }


    private CoreModel getRefModel() {
        return viewableModel.refModel;
    }


    private CacheUpdateRule detectLazyUpdateRule() {
        if (!matchStartDetected) return CacheUpdateRule.manual;
        switch (getGameController().gameMode) {
            default:
                if (getRefModel().entitiesManager.isHumanTurnCurrently()) {
                    return CacheUpdateRule.instant;
                }
                if (viewableModel.entitiesManager.isInAiOnlyMode()) {
                    return CacheUpdateRule.lap;
                }
                return CacheUpdateRule.turn;
            case editor:
                return CacheUpdateRule.manual;
            case replay:
                ReplayManager replayManager = getObjectsLayer().replayManager;
                if (replayManager.fast) {
                    return CacheUpdateRule.lap;
                } else {
                    return CacheUpdateRule.turn;
                }
        }
    }


    public void onViewableHexChangedColor(ViewableHex viewableHex) {
        if (ctViewableHexes.contains(viewableHex)) return;
        ctViewableHexes.add(viewableHex);
    }


    private void initBlocks() {
        blocks.clear();
        RectangleYio bounds = viewableModel.bounds;
        int x;
        int y = (int) bounds.y;
        int w = bWidth;
        int h = bHeight;
        while (y < bounds.y + bounds.height) {
            x = (int) bounds.x;
            while (x < bounds.x + bounds.width) {
                addBlock(x, y, w, h);
                x += w;
            }
            y += h;
        }
    }


    private void addBlock(int x, int y, int w, int h) {
        CacheBlock cacheBlock = new CacheBlock(this);
        cacheBlock.position.set(x, y, w, h);
        cacheBlock.onPositionChanged();
        blocks.add(cacheBlock);
    }


    private boolean isStaticPiece(PieceType pieceType) {
        return pieceType != null && !Core.isUnit(pieceType);
    }


    private ObjectsLayer getObjectsLayer() {
        return viewableModel.objectsLayer;
    }


    public void onEditorRandomGenerationBegan() {
        matchStartDetected = false; // pretend like match didn't start yet
    }


    public void setBackgroundVisible(boolean backgroundVisible) {
        // this should be only used in editor
        // because in normal game hexes cannot be removed
        this.backgroundVisible = backgroundVisible;
    }


    private GameController getGameController() {
        return getObjectsLayer().gameController;
    }
}
