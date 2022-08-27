package yio.tro.onliyoy.game.general;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class SpeedManager {

    GameController gameController;
    int speed;
    boolean normal;


    public SpeedManager(GameController gameController) {
        this.gameController = gameController;
    }


    public void setSpeed(int speed) {
        if (this.speed == speed) return;

        this.speed = speed;
        normal = (speed == 1);

        notifyUiAboutSpeedChange();
    }


    public void defaultValues() {
        resetSpeed();
    }


    public void setPlayState(boolean state) {
        if (state) {
            resetSpeed();
        } else {
            applyTacticalPause();
        }
    }


    private void applyTacticalPause() {
        setSpeed(0);
    }


    private void resetSpeed() {
        setSpeed(1);
    }


    public void onPlayPauseButtonPressed() {
        if (speed == 0) {
            resetSpeed();
        } else {
            applyTacticalPause();
        }
    }


    public void onFastForwardButtonPressed() {
        if (speed == GameRules.fastForwardSpeed) {
            resetSpeed();
        } else {
            setSpeed(GameRules.fastForwardSpeed);
        }
    }


    private void notifyUiAboutSpeedChange() {
        Scenes.aiOnlyOverlay.onSpeedChanged();
    }


    public int getSpeed() {
        return speed;
    }
}
