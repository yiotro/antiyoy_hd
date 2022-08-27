package yio.tro.onliyoy.game.touch_modes;

import yio.tro.onliyoy.game.general.GameController;

public class TmNone extends TouchMode{

    // this touch mode is used as a buffer
    // to make sure that onBegin() method is called when match is loaded


    public TmNone(GameController gameController) {
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
