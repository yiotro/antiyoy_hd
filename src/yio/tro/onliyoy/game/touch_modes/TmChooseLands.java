package yio.tro.onliyoy.game.touch_modes;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.view.game_renders.GameRender;
import yio.tro.onliyoy.game.view.game_renders.GameRendersList;
import yio.tro.onliyoy.game.viewable_model.BorderIndicator;
import yio.tro.onliyoy.game.viewable_model.GhDataContainer;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.elements.resizable_element.RveChooseConditionTypeItem;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class TmChooseLands extends TouchMode{

    public GhDataContainer ghDataContainer;
    public ArrayList<Hex> chosenHexes;
    public FactorYio darkenFactor;
    public ArrayList<BorderIndicator> indicators;
    ObjectPoolYio<BorderIndicator> poolIndicators;
    RepeatYio<TmChooseLands> repeatRemove;
    public RveChooseConditionTypeItem rveChooseConditionTypeItem;
    public boolean readMode;


    public TmChooseLands(GameController gameController) {
        super(gameController);
        ghDataContainer = new GhDataContainer();
        chosenHexes = new ArrayList<>();
        darkenFactor = new FactorYio();
        indicators = new ArrayList<>();
        rveChooseConditionTypeItem = null;
        initPools();
        initRepeats();
    }


    private void initPools() {
        poolIndicators = new ObjectPoolYio<BorderIndicator>(indicators) {
            @Override
            public BorderIndicator makeNewObject() {
                return new BorderIndicator(getViewableModel());
            }
        };
    }


    private void initRepeats() {
        repeatRemove = new RepeatYio<TmChooseLands>(this, 120) {
            @Override
            public void performAction() {
                parent.removeDeadIndicators();
            }
        };
    }


    @Override
    public void onModeBegin() {
        chosenHexes.clear();
        ghDataContainer.update(getViewableModel(), chosenHexes);
        darkenFactor.reset();
        killAllIndicators();
        Scenes.tmChooseLandsOverlay.create();
        readMode = false;
    }


    public void killAllIndicators() {
        for (BorderIndicator indicator : indicators) {
            indicator.kill();
        }
    }


    public void doIndicate() {
        prepareFlags();
        killInvalidIndicators();
        for (Hex hex : chosenHexes) {
            doIndicate(hex);
        }
    }


    private void killInvalidIndicators() {
        for (BorderIndicator indicator : indicators) {
            if (isValid(indicator)) continue;
            indicator.kill();
        }
    }


    private boolean isValid(BorderIndicator indicator) {
        Hex hex = indicator.hex;
        if (!shouldBeIndicated(hex, indicator.direction)) return false;
        return hex.flag;
    }


    private void doIndicate(Hex hex) {
        for (int dir = 0; dir < 6; dir++) {
            if (!shouldBeIndicated(hex, dir)) continue;
            if (isAlreadyIndicated(hex, dir)) continue;
            addIndicator(hex, dir);
        }
    }


    private BorderIndicator getActiveIndicator(Hex hex, int direction) {
        for (BorderIndicator borderIndicator : indicators) {
            if (!borderIndicator.appearFactor.isInAppearState()) continue;
            if (borderIndicator.hex != hex) continue;
            if (borderIndicator.direction != direction) continue;
            return borderIndicator;
        }
        return null;
    }


    private boolean isAlreadyIndicated(Hex hex, int direction) {
        BorderIndicator borderIndicator = getActiveIndicator(hex, direction);
        return borderIndicator != null;
    }


    private void prepareFlags() {
        resetFlags();
        for (Hex hex : chosenHexes) {
            hex.flag = true;
        }
    }


    private void resetFlags() {
        for (Hex hex : getViewableModel().hexes) {
            hex.flag = false;
        }
    }


    private boolean shouldBeIndicated(Hex hex, int direction) {
        Hex adjacentHex = getViewableModel().directionsManager.getAdjacentHex(hex, direction);
        if (adjacentHex == null) return true;
        if (adjacentHex.flag == hex.flag) return false;
        return hex.flag;
    }


    private void addIndicator(Hex hex, int direction) {
        BorderIndicator freshObject = poolIndicators.getFreshObject();
        freshObject.spawn(hex, direction);
    }


    @Override
    public void onModeEnd() {
        Scenes.tmChooseLandsOverlay.destroy();
    }


    @Override
    public void move() {
        darkenFactor.move();
        repeatRemove.move();
        moveIndicators();
    }


    private void removeDeadIndicators() {
        for (int i = indicators.size() - 1; i >= 0; i--) {
            BorderIndicator borderIndicator = indicators.get(i);
            if (!borderIndicator.isReadyToBeRemoved()) continue;
            indicators.remove(borderIndicator);
        }
    }


    private void moveIndicators() {
        for (BorderIndicator borderIndicator : indicators) {
            borderIndicator.move();
        }
    }


    @Override
    public boolean isCameraMovementEnabled() {
        return true;
    }


    @Override
    public void onTouchDown() {

    }


    @Override
    public void onTouchDrag() {

    }


    @Override
    public void onTouchUp() {

    }


    @Override
    public boolean onClick() {
        if (readMode) {
            gameController.resetTouchMode();
            Scenes.readLetter.create();
            return true;
        }
        ViewableModel viewableModel = getViewableModel();
        Hex hex = viewableModel.getClosestHex(getCurrentTouchConverted());
        if (hex == null) return true;
        if (!isCurrentTouchInsideHex(hex)) return true;
        appleChange(hex);
        return true;
    }


    public void appleChange(Hex hex) {
        if (chosenHexes.contains(hex)) {
            chosenHexes.remove(hex);
            if (chosenHexes.size() == 0) {
                darkenFactor.destroy(MovementType.lighty, 1.7);
            }
        } else {
            chosenHexes.add(hex);
            darkenFactor.appear(MovementType.approach, 2.5);
        }
        ghDataContainer.update(getViewableModel(), chosenHexes);
        doIndicate();
    }


    public void enableReadMode() {
        readMode = true;
        Scenes.tmChooseLandsOverlay.destroy();
        Scenes.gameOverlay.viewTouchModeElement.onTouchModeSet(this); // to hide top sign
    }


    private boolean isCurrentTouchInsideHex(Hex hex) {
        return hex.position.center.distanceTo(getCurrentTouchConverted()) < getViewableModel().getHexRadius();
    }


    @Override
    public String getNameKey() {
        if (readMode) return null;
        return "choose_lands";
    }


    public void setRveChooseConditionTypeItem(RveChooseConditionTypeItem rveChooseConditionTypeItem) {
        this.rveChooseConditionTypeItem = rveChooseConditionTypeItem;
    }


    @Override
    public GameRender getRender() {
        return GameRendersList.getInstance().renderTmChooseLands;
    }
}
