package yio.tro.onliyoy.game.tutorial;

import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.GameRules;
import yio.tro.onliyoy.menu.scenes.Scenes;

import java.util.ArrayList;

public class ScriptManager {

    GameController gameController;
    public ArrayList<ScriptItem> items;
    ScriptPerformer performer;


    public ScriptManager(GameController gameController) {
        this.gameController = gameController;
        items = new ArrayList<>();
        performer = new ScriptPerformer(this);
    }


    public void move() {
        if (gameController.gameMode != GameMode.tutorial) return;
        performer.move();
    }


    public void clear() {
        items.clear();
    }


    public void removeScript(ScriptItem scriptItem) {
        items.remove(scriptItem);
    }


    public void removeScript(int index) {
        items.remove(index);
    }


    public ScriptItem addScript(ScriptType type) {
        return addScript(type, null);
    }


    public ScriptItem addScript(ScriptType type, String argument) {
        ScriptItem newItem = new ScriptItem(this);

        newItem.setType(type);
        newItem.setArgument(argument);

        items.add(newItem);

        return newItem;
    }


    public boolean isEmpty() {
        return items.size() == 0;
    }


    public void applySkipMessages() {
        gameController.cameraController.setLocked(false);
        Scenes.messageDialog.destroy();
        clear();
    }


    public boolean isDirectPlayerControlAllowed() {
        return !hasSomeAliveScripts();
    }


    public boolean hasSomeAliveScripts() {
        for (ScriptItem scriptItem : items) {
            if (scriptItem.alive) return true;
        }
        return false;
    }


    public void onMatchEnded() {
        for (ScriptItem item : items) {
            item.kill();
        }
    }


    public void prepareToExecuteNextScriptFaster() {
        performer.prepareToExecuteNextScriptFaster();
    }


    public int getNumberOfMessagesToShow() {
        int c = 0;
        for (ScriptItem item : items) {
            if (!item.alive) continue;
            if (item.type != ScriptType.message) continue;
            c++;
        }
        return c;
    }


    public int getOverallNumberOfMessages() {
        int c = 0;
        for (ScriptItem item : items) {
            if (item.type != ScriptType.message) continue;
            c++;
        }
        return c;
    }


    public void addUlevIntroMessageToStart(String name, String author) {
        ScriptItem scriptItem = new ScriptItem(this);
        scriptItem.type = ScriptType.message;
        scriptItem.argument = name + "#by " + author;
        items.add(0, scriptItem);
    }


    public void showInConsole() {
        System.out.println();
        System.out.println("ScriptManager.showInConsole");
        for (ScriptItem scriptItem : items) {
            System.out.println("- " + scriptItem);
        }
    }
}
