package yio.tro.onliyoy.game.touch_modes;

import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.view.game_renders.GameRender;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.stuff.PointYio;

import java.util.ArrayList;

public abstract class TouchMode {

    protected GameController gameController;
    public static ArrayList<TouchMode> touchModes;
    protected boolean alive;

    public static TmDefault tmDefault;
    public static TmDebug tmDebug;
    public static TmReplay tmReplay;
    public static TmEditor tmEditor;
    public static TmDiplomacy tmDiplomacy;
    public static TmChooseLands tmChooseLands;
    public static TmVerification tmVerification;
    public static TmReport tmReport;
    public static TmSpectator tmSpectator;
    public static TmNone tmNone;
    public static TmEditRelations tmEditRelations;
    // don't forget to initialize touch mode lower


    public TouchMode(GameController gameController) {
        this.gameController = gameController;
        alive = true;

        touchModes.add(this);
    }


    public static void createModes(GameController gameController) {
        touchModes = new ArrayList<>();

        tmDefault = new TmDefault(gameController);
        tmDebug = new TmDebug(gameController);
        tmReplay = new TmReplay(gameController);
        tmEditor = new TmEditor(gameController);
        tmDiplomacy = new TmDiplomacy(gameController);
        tmChooseLands = new TmChooseLands(gameController);
        tmVerification = new TmVerification(gameController);
        tmReport = new TmReport(gameController);
        tmSpectator = new TmSpectator(gameController);
        tmNone = new TmNone(gameController);
        tmEditRelations = new TmEditRelations(gameController);
    }


    public abstract void onModeBegin();


    public abstract void onModeEnd();


    public abstract void move();


    public abstract boolean isCameraMovementEnabled();


    public boolean isDoubleClickDisabled() {
        return isCameraMovementEnabled();
    }


    public void touchDownReaction() {
        if (isCameraMovementEnabled()) {
            gameController.cameraController.onTouchDown(gameController.currentTouch);
        }

        onTouchDown();
    }


    public abstract void onTouchDown();


    public void touchDragReaction() {
        if (isCameraMovementEnabled()) {
            gameController.cameraController.onTouchDrag(gameController.currentTouch);
        }

        onTouchDrag();
    }


    public abstract void onTouchDrag();


    public void touchUpReaction() {
        if (isCameraMovementEnabled()) {
            gameController.cameraController.onTouchUp(gameController.currentTouch);
        }

        onTouchUp();
    }


    public abstract void onTouchUp();


    public abstract boolean onClick();


    public abstract String getNameKey();


    public boolean onMouseWheelScrolled(int amount) {
        return false;
    }


    public GameRender getRender() {
        return null;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }


    public boolean isReadyToBeRemoved() {
        return !alive;
    }


    public boolean isAlive() {
        return alive;
    }


    public void kill() {
        setAlive(false);
    }


    public void setAlive(boolean alive) {
        this.alive = alive;
    }


    protected PointYio getCurrentTouchConverted() {
        return gameController.currentTouchConverted;
    }


    protected ViewableModel getViewableModel() {
        return getObjectsLayer().viewableModel;
    }


    protected ObjectsLayer getObjectsLayer() {
        return gameController.objectsLayer;
    }

}
