package yio.tro.onliyoy.game.touch_modes;

import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.core_model.EntitiesManager;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.view.game_renders.GameRender;
import yio.tro.onliyoy.game.view.game_renders.GameRendersList;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.elements.forefinger.ForefingerElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.LongTapDetector;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class TmDefault extends TouchMode {

    LongTapDetector longTapDetector;
    public FactorYio highlightFactor;
    public Hex highlightHex;


    public TmDefault(GameController gameController) {
        super(gameController);
        highlightFactor = new FactorYio();
        highlightHex = null;
        initLongTapDetector();
    }


    private void initLongTapDetector() {
        longTapDetector = new LongTapDetector() {
            @Override
            public void onLongTapDetected() {
                TmDefault.this.onLongTapDetected();
            }
        };
    }


    @Override
    public void onModeBegin() {
        highlightHex = null;
    }


    @Override
    public void onModeEnd() {

    }


    @Override
    public void move() {
        longTapDetector.move();
        highlightFactor.move();
    }


    @Override
    public boolean isCameraMovementEnabled() {
        if (checkForGiantSizeLagDisguise()) return false;
        return true;
    }


    private boolean checkForGiantSizeLagDisguise() {
        LevelSize size = gameController.sizeManager.initialLevelSize;
        if (size != LevelSize.giant && size != LevelSize.giant_landscape) return false;
        ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
        EntitiesManager entitiesManager = viewableModel.entitiesManager;
        if (!entitiesManager.isSingleplayerHumanMatch()) return false;
        if (entitiesManager.getCurrentEntity().isHuman()) return false;
        return true;
    }


    @Override
    public boolean isDoubleClickDisabled() {
        return false;
    }


    @Override
    public void onTouchDown() {
        longTapDetector.onTouchDown(gameController.currentTouch);
    }


    private boolean isTouchAllowedByScripts() {
        if (gameController.scriptManager.isDirectPlayerControlAllowed()) return true;
        ForefingerElement forefinger = Scenes.forefinger.forefinger;
        return forefinger != null && forefinger.getAlpha() > 0.95;
    }


    @Override
    public void onTouchDrag() {
        longTapDetector.onTouchDrag(gameController.currentTouch);
    }


    @Override
    public void onTouchUp() {
        longTapDetector.onTouchUp(gameController.currentTouch);
    }


    @Override
    public boolean onClick() {
        if (!isTouchAllowedByScripts()) return false;
        ViewableModel viewableModel = getViewableModel();
        Hex hex = viewableModel.getClosestHex(getCurrentTouchConverted());
        if (hex == null || !isCurrentTouchInsideHex(hex)) {
            viewableModel.onClickedOutside();
            return true;
        }
        viewableModel.onHexClicked(hex);
        return true;
    }


    private boolean isCurrentTouchInsideHex(Hex hex) {
        return hex.position.center.distanceTo(getCurrentTouchConverted()) < getViewableModel().getHexRadius();
    }


    private void onLongTapDetected() {
        if (!SettingsManager.getInstance().longTap) return;
        if (!isTouchAllowedByScripts()) return;
        ViewableModel viewableModel = getViewableModel();
        Hex hex = viewableModel.getClosestHex(getCurrentTouchConverted());
        if (hex == null) return;
        if (hex.getProvince() == null) return;
        if (hex.color != viewableModel.entitiesManager.getCurrentColor()) return;
        if (viewableModel.provinceSelectionManager.selectedProvince != hex.getProvince()) return;
        SoundManager.playSound(SoundType.hold_to_march);
        viewableModel.massMarchManager.apply(hex);
    }


    @Override
    public GameRender getRender() {
        return GameRendersList.getInstance().renderTmDefault;
    }


    public void doHighlight(Hex hex) {
        highlightHex = hex;
        highlightFactor.reset();
        highlightFactor.setValue(1);
        highlightFactor.destroy(MovementType.lighty, 2.1);
    }


    @Override
    public String getNameKey() {
        return null;
    }

}
