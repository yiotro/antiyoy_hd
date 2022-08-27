package yio.tro.onliyoy.game.touch_modes;

import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.stuff.RepeatYio;

public class TmSpectator extends TouchMode{

    boolean ready;
    RepeatYio<TmSpectator> repeatCheckToShow;


    public TmSpectator(GameController gameController) {
        super(gameController);
        initRepeats();
    }


    private void initRepeats() {
        repeatCheckToShow = new RepeatYio<TmSpectator>(this, 60) {
            @Override
            public void performAction() {
                parent.checkToShowIndicators();
            }
        };
    }


    @Override
    public void onModeBegin() {
        ready = true;
    }


    @Override
    public void onModeEnd() {
        getViewableModel().viewableRelationsManager.hide();
    }


    @Override
    public void move() {
        repeatCheckToShow.move();
    }


    private void checkToShowIndicators() {
        if (!ready) return;
        if (!getViewableModel().diplomacyManager.enabled) return;
        if (!gameController.yioGdxGame.gameView.coversAllScreen()) return;
        ready = false;
        getViewableModel().viewableRelationsManager.update();
    }


    @Override
    public boolean isCameraMovementEnabled() {
        return true;
    }


    @Override
    public boolean isDoubleClickDisabled() {
        return false;
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
        return false;
    }


    @Override
    public String getNameKey() {
        return null;
    }
}
