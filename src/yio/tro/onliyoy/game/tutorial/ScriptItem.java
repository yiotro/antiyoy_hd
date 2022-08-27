package yio.tro.onliyoy.game.tutorial;

import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class ScriptItem {

    ScriptManager scriptManager;
    public ScriptType type;
    public String argument;
    boolean alive;


    public ScriptItem(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
        reset();
    }


    public void reset() {
        type = null;
        argument = null;
        alive = true;
    }


    boolean isReady() {
        if (Scenes.messageDialog.isCurrentlyVisible()) return false;
        if (Scenes.forefinger.isCurrentlyVisible()) return false;
        if (Scenes.highlightArea.isCurrentlyVisible()) return false;
        if (Scenes.incomeGraph.isCurrentlyVisible()) return false;
        if (!checkForSpecialTypeConditions()) return false;
        if (scriptManager.gameController.currentTouchCount > 0) return false;
        if (!Scenes.mechanicsOverlay.isCurrentlyVisible()) return false;
        return true;
    }


    boolean checkForSpecialTypeConditions() {
        InterfaceElement element;
        GameController gameController = scriptManager.gameController;
        MenuControllerYio menuControllerYio = gameController.yioGdxGame.menuControllerYio;

        switch (type) {
            default:
                return true;
            case message:
                return true;
            case point_at_ui_element:
                element = menuControllerYio.getElement(argument);
                return element != null && element.getFactor().getValue() == 1;
            case point_at_ui_tag:
                element = menuControllerYio.getElement(argument.split(" ")[0]);
                return element != null && element.getFactor().getValue() == 1;
            case focus_lock_camera:
                return isGameNotPaused();
            case unlock_camera:
                return isGameNotPaused();
        }
    }


    public boolean isFastToPerform() {
        switch (type) {
            default:
                return false;
            case unlock_camera:
            case focus_lock_camera:
                return true;
        }
    }


    private boolean isGameNotPaused() {
        return !scriptManager.gameController.yioGdxGame.isGamePaused();
    }


    void kill() {
        alive = false;
    }


    public void setType(ScriptType type) {
        this.type = type;
    }


    public void setArgument(String argument) {
        this.argument = argument;
    }


    @Override
    public String toString() {
        return "[ScriptItem: " +
                type + " " + argument +
                "]";
    }
}
