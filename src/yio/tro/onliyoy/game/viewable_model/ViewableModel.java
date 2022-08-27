package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.export_import.*;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.NetTimeSynchronizer;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.TimeMeasureYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewableModel extends CoreModel {

    public ObjectsLayer objectsLayer;
    public UnitsManager unitsManager;
    private ArrayList<AbstractEvent> buffer;
    public DirectionsManager directionsManager;
    public ProvinceSelectionManager provinceSelectionManager;
    public MoveZoneViewer moveZoneViewer;
    public DefenseViewer defenseViewer;
    public HumanControlsManager humanControlsManager;
    public LandsManager landsManager;
    public CoreModel refModel;
    public VmCacheManager cacheManager;
    public MassMarchManager massMarchManager;
    public ViewableRelationsManager viewableRelationsManager;
    public PigeonsManager pigeonsManager;
    private IwEventsList iwEventsList;
    public ExclamationsManager exclamationsManager;
    public QuickInfoManager quickInfoManager;
    private HashMap<AbstractEvent, RefData> refMap;
    private ObjectPoolYio<RefData> poolRefData;
    public String pauseName;
    public boolean transitionsEnabled;


    public ViewableModel(ObjectsLayer objectsLayer) {
        super();
        this.objectsLayer = objectsLayer;
        unitsManager = new UnitsManager(this);
        buffer = new ArrayList<>();
        directionsManager = new DirectionsManager(this);
        provinceSelectionManager = new ProvinceSelectionManager(this);
        moveZoneViewer = new MoveZoneViewer(this);
        defenseViewer = new DefenseViewer(this);
        humanControlsManager = new HumanControlsManager(this);
        eventsManager.setAutomaticUtilization(false);
        landsManager = new LandsManager(this);
        cacheManager = new VmCacheManager(this);
        massMarchManager = new MassMarchManager(this);
        viewableRelationsManager = new ViewableRelationsManager(this);
        pigeonsManager = new PigeonsManager(this);
        iwEventsList = new IwEventsList(this, null);
        exclamationsManager = new ExclamationsManager(this);
        quickInfoManager = new QuickInfoManager(this);
        refModel = null;
        refMap = new HashMap<>();
        pauseName = "-";
        transitionsEnabled = true;
        initPools();
    }


    private void initPools() {
        poolRefData = new ObjectPoolYio<RefData>() {
            @Override
            public RefData makeNewObject() {
                return new RefData();
            }
        };
    }


    public void move() {
        moveBuffer();
        unitsManager.move();
        provinceSelectionManager.move();
        moveZoneViewer.move();
        defenseViewer.move();
        landsManager.move();
        cacheManager.move();
        viewableRelationsManager.move();
        pigeonsManager.move();
        exclamationsManager.move();
        quickInfoManager.move();
    }


    private void moveBuffer() {
        while (buffer.size() > 0) {
            AbstractEvent event = buffer.get(0);
            if (!isReadyToBeDisplayed(event)) break;
            buffer.remove(0);
            doReactToEvent(event);
        }
    }


    private void doReactToEvent(AbstractEvent event) {
        pretendAsIfEventWasJustValidated(event);
        syncRefModel(event);
        unitsManager.onEventApplied(event);
        landsManager.onEventApplied(event);
        cacheManager.onEventApplied(event);
        pigeonsManager.onEventApplied(event);
        if (event.getType() == EventType.turn_end) {
            onEndTurnEventApplied(event);
        }
        checkToNotifyTmDiplomacy(event);
        eventsRefrigerator.utilizeEvent(event);
    }


    private void syncRefModel(AbstractEvent event) {
        if (refModel == null) return;
        if (objectsLayer.gameController.gameMode == GameMode.editor) return;
        RefData refData = refMap.get(event);
        refData.applyTo(refModel);
        refMap.remove(event);
        poolRefData.add(refData);
    }


    private void syncRefModelWithCurrentState() {
        RefData next = poolRefData.getNext();
        next.setBy(this);
        next.applyTo(refModel);
        poolRefData.add(next);
    }


    private void checkToNotifyTmDiplomacy(AbstractEvent event) {
        if (objectsLayer.gameController.touchMode != TouchMode.tmDiplomacy) return;
        TouchMode.tmDiplomacy.onEventApplied(event);
    }


    private void pretendAsIfEventWasJustValidated(AbstractEvent event) {
        // in reality event was validated long time ago
        // but in some cases managers need to prepare for event before it was applied
        landsManager.onEventValidated(event);
        cacheManager.onEventValidated(event);
    }


    private void onEndTurnEventApplied(AbstractEvent event) {
        GameController gameController = objectsLayer.gameController;
        gameController.resetTouchMode();
        gameController.syncMechanicsOverlayWithCurrentTurn();
        objectsLayer.checkForAutosave();
        if (Scenes.mechanicsOverlay.isCurrentlyVisible()) {
            Scenes.mechanicsOverlay.onEndTurnEventApplied();
        }
        updateNetUiOnEndTurn(event);
        checkToNotifyAboutTurnStart();
        objectsLayer.checkToEndMatch();
    }


    private void checkToNotifyAboutTurnStart() {
        if (!SettingsManager.getInstance().alertTurnStart) return;
        if (!isNetMatch()) return;
        if (!entitiesManager.getCurrentEntity().isHuman()) return;
        if (provincesManager.getProvince(entitiesManager.getCurrentColor()) == null) return;
        SoundManager.playSound(SoundType.alert);
    }


    private void updateNetUiOnEndTurn(AbstractEvent event) {
        if (!Scenes.netOverlay.isCurrentlyVisible()) return;
        Scenes.netOverlay.onEndTurnEventApplied();
        if (event == null) return;
        EventTurnEnd eventTurnEnd = (EventTurnEnd) event;
        // turn ends on client slightly sooner to decrease probability of desync problem
        // eventTurnEnd.targetEndTime was updated on server, so it's a server time
        long clientTime = NetTimeSynchronizer.getInstance().convertToClientTime(eventTurnEnd.targetEndTime);
        getNetRoot().currentMatchData.turnEndTime = clientTime - 1000;
    }


    public void onBasicStuffCreated() {
        eventsManager.applyEvent(eventsRefrigerator.getGraphCreatedEvent());
    }


    public void onAdvancedStuffCreated() {
        if (objectsLayer.gameController.gameMode != GameMode.editor) {
            applyMatchStartedEvent();
        }
        initRefModel();
        cacheManager.onAdvancedStuffCreated();
        checkToDisableTransitions();
    }


    private void checkToDisableTransitions() {
        LevelSize levelSize = objectsLayer.gameController.sizeManager.initialLevelSize;
        if (levelSize.ordinal() < LevelSize.giant.ordinal()) return;
        transitionsEnabled = false;
    }


    public void clearGraph() {
        hexes.clear();
        posMapYio.clear();
    }


    public void clearUnitsManually() {
        // this method should be called only on rewind
        for (Hex hex : hexes) {
            if (!hex.hasUnit()) continue;
            hex.piece = null;
            hex.unitId = -1;
        }
        unitsManager.clear();
    }


    public void initRefModel() {
        refModel = new CoreModel("ref");
        refModel.buildSimilarGraph(this);
        refModel.initBy(this);
    }


    public boolean isReadyToBeDisplayed(AbstractEvent event) {
        if (cacheManager.lazyUpdateRequired) return false; // wait for cache to update
        if (event.isQuick()) return true;
        if (event.isReusable()) return true;
        ViewableUnit unit, unit1, unit2;
        switch (event.getType()) {
            default:
                return true;
            case unit_move:
                EventUnitMove eventUnitMove = (EventUnitMove) event;
                unit = unitsManager.getUnit(eventUnitMove.start);
                return unit != null && !unit.isRelocating() && unit.appearFactor.getValue() == 1;
            case piece_delete:
                EventPieceDelete eventPieceDelete = (EventPieceDelete) event;
                unit = unitsManager.getUnit(eventPieceDelete.hex);
                if (unit == null) return true;
                return !unit.isRelocating() && unit.appearFactor.getValue() == 1;
            case turn_end:
                return isTurnEndEventReadyToBeDisplayed();
            case merge:
                EventMerge eventMerge = (EventMerge) event;
                unit1 = unitsManager.getUnit(eventMerge.startHex);
                if (unit1 == null) return false;
                if (unit1.isRelocating()) return false;
                if (unit1.appearFactor.getValue() < 1) return false;
                unit2 = unitsManager.getUnit(eventMerge.targetHex);
                if (unit2 == null) return false;
                if (unit2.isRelocating()) return false;
                if (unit2.appearFactor.getValue() < 1) return false;
                return true;
        }
    }


    private boolean isTurnEndEventReadyToBeDisplayed() {
        if (!transitionsEnabled) return true;
        if (isFastModeEnabled() && !refModel.turnsManager.isTurnIndexInTheEndOfLap()) {
            return true;
        }
        if (entitiesManager.isSingleplayerHumanMatch() && entitiesManager.getCurrentEntity().isArtificialIntelligence()) {
            return true;
        }
        return !isSomethingMovingCurrently();
    }


    private boolean isFastModeEnabled() {
        if (getGameMode() == GameMode.replay && objectsLayer.replayManager.fast) return true;
        if (objectsLayer.viewableModel.entitiesManager.isInAiOnlyMode()) return true;
        return false;
    }


    public boolean isNetMatch() {
        return entitiesManager.contains(EntityType.net_entity) || entitiesManager.contains(EntityType.spectator);
    }


    public boolean isSomethingMovingCurrently() {
        for (ViewableUnit unit : unitsManager.units) {
            if (unit.isRelocating()) return true;
            if (unit.alive && unit.appearFactor.getValue() < 1) return true;
        }
        for (ViewableHex viewableHex : landsManager.viewableHexes) {
            if (viewableHex.isColorFactorInMovementMode()) return true;
            if (viewableHex.isPieceFactorInMovementMode()) return true;
        }
        if (Scenes.incomeGraph.isInTransitionCurrently()) return true;
        if (objectsLayer.gameController.cameraController.isMovingCurrently()) return true;
        if (pigeonsManager.isSomethingMovingCurrently()) return true;
        return false;
    }


    @Override
    public void resetHexes() {
        super.resetHexes();
        System.out.println("ViewableModel.resetHexes: warning, this method shouldn't be called in viewable model");
    }


    @Override
    public void rebuildGraph() {
        super.rebuildGraph();
        unitsManager.clear();
        landsManager.sync();
    }


    public boolean isHexCurrentlyVisible(Hex hex) {
        return objectsLayer.gameController.cameraController.frame.intersects(hex.position);
    }


    @Override
    public void onEventValidated(AbstractEvent event) {
        super.onEventValidated(event); // nothing
    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        super.onEventApplied(event);
        updateRefMap(event);
        if (buffer.size() == 0 && isReadyToBeDisplayed(event)) {
            doReactToEvent(event);
        } else {
            buffer.add(event);
        }
        checkToNotifyServerAboutEvent(event);
    }


    private void updateRefMap(AbstractEvent event) {
        if (refModel == null) return;
        if (objectsLayer.gameController.gameMode == GameMode.editor) return;
        RefData next = poolRefData.getNext();
        next.setBy(this);
        refMap.put(event, next);
    }


    private void clearRefMap() {
        for (RefData refData : refMap.values()) {
            poolRefData.add(refData);
        }
        refMap.clear();
    }


    @Override
    public int getListenPriority() {
        return 9;
    }


    private void checkToNotifyServerAboutEvent(AbstractEvent event) {
        if (!isNetMatch()) return;
        if (event.isReusable()) return;
        if (!event.isNotable()) return;
        if (event.isQuick()) return;
        if (event.author == null) return;
        String matchId = getNetRoot().currentMatchData.matchId;
        getNetRoot().sendMessage(NmType.event, matchId.substring(1, 5) + "/" + event.encode());
    }


    public void onReceivedEventFromServer(String code) {
        if (code.length() < 3) return;
        String[] split = code.split(" ");
        String eventKey = split[0];
        EventType eventType = EventKeys.convertKeyToType(eventKey);
        AbstractEvent event = eventsManager.factory.createEvent(eventType);
        iwEventsList.restoreSingleEvent(event, split);
        if (isWrongTurnEndEvent(event)) {
            System.out.println("ViewableModel.onReceivedEventFromServer: wrong end turn event " + event);
            if (DebugFlags.humanImitation) {
                getNetRoot().sendMessage(NmType.debug_text, "Wrong te event: " + event + ", current=" + entitiesManager.getCurrentColor());
            }
            return;
        }
        if (!event.isValid() || DebugFlags.treatNextServerEventAsInvalid) {
            onReceivedInvalidEvent(event);
            return;
        }
        eventsManager.applyEvent(event);
    }


    private void onReceivedInvalidEvent(AbstractEvent event) {
        DebugFlags.treatNextServerEventAsInvalid = false;
        System.out.println("ViewableModel.onReceivedEventFromServer, invalid: " + event);
        getNetRoot().sendMessage(NmType.request_sync, "invalid event: " + event);
        SoundManager.playSound(SoundType.alert, true);
    }


    private boolean isWrongTurnEndEvent(AbstractEvent event) {
        if (!(event instanceof EventTurnEnd)) return false;
        EventTurnEnd eventTurnEnd = (EventTurnEnd) event;
        if (eventTurnEnd.currentColor == null) return false;
        return eventTurnEnd.currentColor != entitiesManager.getCurrentColor();
    }


    private NetRoot getNetRoot() {
        return objectsLayer.gameController.yioGdxGame.netRoot;
    }


    public GameMode getGameMode() {
        return objectsLayer.gameController.gameMode;
    }


    public boolean onUndoRequested() {
        if (!isBufferEmpty()) return false; // to prevent from messing up events list
        ArrayList<UndoItem> items = objectsLayer.undoManager.items;
        if (items.size() == 0) {
            onRequestedUndoWhenListIsEmpty();
            return false;
        }
        UndoItem lastItem = items.get(items.size() - 1);
        if (!lastItem.event.canBeUndone()) return false;
        objectsLayer.historyManager.onUndoRequested();
        objectsLayer.undoManager.onUndoRequested();
        unitsManager.onClickedOutside(); // to deselect
        return true;
    }


    private void onRequestedUndoWhenListIsEmpty() {
        if (!DebugFlags.humanImitation) return;
        getNetRoot().sendMessage(NmType.debug_text, "undo items list is empty");
    }


    @Override
    public void onUndoApplied(UndoItem undoItem) {
        provinceSelectionManager.onUndoApplied(undoItem.hookHex);
        (new IwCoreProvinces(refModel)).perform(undoItem.getLevelCode());
        viewableRelationsManager.onUndoApplied();
        fogOfWarManager.onUndoApplied();
    }


    @Override
    public Province getSelectedProvince() {
        return provinceSelectionManager.selectedProvince;
    }


    public void onSyncApplied(String levelCode) {
        onEndTurnEventApplied(null); // to update UI
        refModel.onQuickEventApplied(); // to rebuild provinces
        (new IwCoreProvinces(refModel)).perform(levelCode);
        (new IwCoreTurn(refModel)).perform(levelCode);
        viewableRelationsManager.onSyncApplied();
    }


    @Override
    public void onQuickEventApplied() {
        super.onQuickEventApplied();
        refModel.onQuickEventApplied();
    }


    public void onReplayRewindApplied(CoreModel backupModel) {
        turnsManager.reset();
        provincesManager.setBy(backupModel.provincesManager);
        readinessManager.update();
        buffer.clear(); // start from scratch
        syncRefModelWithCurrentState();
        clearRefMap();
        cacheManager.onReplayRewindApplied();
    }


    public void onPieceChosenInConstructionView(PieceType pieceType) {
        moveZoneViewer.onPieceChosenInConstructionView(pieceType);
        unitsManager.onPieceChosenInConstructionView(pieceType);
    }


    public void onUnitSelected(ViewableUnit viewableUnit) {
        unitsManager.selector.onUnitSelected(viewableUnit);
        moveZoneViewer.onUnitSelected(viewableUnit);
        Scenes.provinceManagement.onUnitSelected();
    }


    public void onClickedOutside() {
        unitsManager.onClickedOutside();
        provinceSelectionManager.onClickedOutside();
    }


    public void onHexClicked(Hex hex) {
        defenseViewer.onHexClicked(hex);
        humanControlsManager.onHexClicked(hex);
        provinceSelectionManager.onHexClicked(hex);
        quickInfoManager.onHexClicked(hex);
    }


    public void onProvinceDeselected() {
        moveZoneViewer.onProvinceDeselected();
        viewableRelationsManager.onProvinceDeselected();
    }


    public void onProvinceSelected() {
        viewableRelationsManager.onProvinceSelected();
    }


    public PieceType getViewedPieceType(PieceType pieceType) {
        if (ruleset.getRulesType() == RulesType.classic && pieceType == PieceType.city) {
            return PieceType.farm;
        }
        return pieceType;
    }


    @Override
    public void onHexColorChanged(Hex hex, HColor previousColor) {
        super.onHexColorChanged(hex, previousColor);
        provinceSelectionManager.onHexColorChanged(hex);
    }


    public boolean isBufferEmpty() {
        return buffer.size() == 0;
    }


    public ArrayList<AbstractEvent> getBuffer() {
        // this method shouldn't be used in normal circumstances
        return buffer;
    }


    public void setPauseName(String pauseName) {
        this.pauseName = pauseName;
    }


    public void doShowBufferInConsole() {
        System.out.println();
        System.out.println("ViewableModel.doShowBufferInConsole");
        for (AbstractEvent event : buffer) {
            System.out.println("- " + event);
        }
    }

}
