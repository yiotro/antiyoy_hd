package yio.tro.onliyoy.game.touch_modes;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.editor.EditorManager;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.stuff.PointYio;

public class TmEditor extends TouchMode{

    Hex previousTouchedHex;
    Hex currentlyTouchedHex;
    public boolean touchedCurrently;
    boolean clickAllowed;


    public TmEditor(GameController gameController) {
        super(gameController);
    }


    @Override
    public void onModeBegin() {
        previousTouchedHex = null;
        currentlyTouchedHex = null;
        touchedCurrently = false;
    }


    @Override
    public void onModeEnd() {

    }


    @Override
    public void move() {

    }


    @Override
    public boolean isCameraMovementEnabled() {
        return getEditorManager().isCameraMovementEnabled();
    }


    private void updateCurrentlyTouchedHex() {
        currentlyTouchedHex = getViewableModel().getClosestHex(getCurrentTouchConverted());
        if (currentlyTouchedHex != null && !isTouchInsideHex(currentlyTouchedHex, getCurrentTouchConverted())) {
            currentlyTouchedHex = null;
        }
    }


    private void applyTouch() {
        getEditorManager().onPointTouched(getCurrentTouchConverted());
        updateCurrentlyTouchedHex();
        if (currentlyTouchedHex == previousTouchedHex) return;
        if (currentlyTouchedHex != null) {
            getEditorManager().onHexTouched(currentlyTouchedHex);
        }
        previousTouchedHex = currentlyTouchedHex;
    }


    private boolean isTouchInsideHex(Hex hex, PointYio touchPoint) {
        return hex.position.center.distanceTo(touchPoint) < getViewableModel().getHexRadius();
    }


    @Override
    public void onTouchDown() {
        touchedCurrently = true;
        applyTouch();
    }


    @Override
    public void onTouchDrag() {
        applyTouch();
    }


    @Override
    public void onTouchUp() {
        touchedCurrently = false;
        applyTouch();
        previousTouchedHex = null;
    }


    @Override
    public boolean onClick() {
        updateCurrentlyTouchedHex();
        if (currentlyTouchedHex == null) {
            getEditorManager().onClickedOutside();
            return false;
        }
        getEditorManager().onHexClicked(currentlyTouchedHex);
        return true;
    }


    @Override
    public String getNameKey() {
        return null;
    }


    private EditorManager getEditorManager() {
        return getObjectsLayer().editorManager;
    }
}
