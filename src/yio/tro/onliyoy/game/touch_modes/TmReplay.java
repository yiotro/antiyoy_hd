package yio.tro.onliyoy.game.touch_modes;

import yio.tro.onliyoy.game.general.GameController;

public class TmReplay extends TouchMode{

    public TmReplay(GameController gameController) {
        super(gameController);
    }


    @Override
    public void onModeBegin() {

    }


    @Override
    public void onModeEnd() {

    }


    @Override
    public void move() {

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
