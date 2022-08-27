package yio.tro.onliyoy.game.save_system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.menu.LanguagesManager;

import java.util.ArrayList;
import java.util.Map;

public class SavesManager {

    GameController gameController;
    public ArrayList<SmItem> items;
    private static final String PREFS = "yio.tro.onliyoy.saves";


    public SavesManager(GameController gameController) {
        this.gameController = gameController;
        items = new ArrayList<>();
        loadValues();
    }


    public SmItem addItem(SaveType saveType, String name, String levelCode) {
        SmItem smItem = new SmItem();
        smItem.key = getKeyForNewSlot();
        smItem.name = name;
        smItem.levelCode = levelCode;
        smItem.type = saveType;
        items.add(smItem);
        saveValues();
        return smItem;
    }


    public void rewriteLevelCode(String key, String levelCode) {
        SmItem item = getItem(key);
        if (item == null) return;

        item.levelCode = levelCode;
        saveValues();
    }


    public void renameItem(SmItem item, String name) {
        item.name = name;
        saveValues();
    }


    public void removeItem(String key) {
        SmItem smItem = getItem(key);
        if (smItem == null) return;
        removeItem(smItem);
    }


    public void removeItem(SmItem item) {
        items.remove(item);
        saveValues();
    }


    public void applyAutosave(String levelCode) {
        removeItem("autosave");
        SmItem smItem = new SmItem();
        smItem.key = "autosave";
        smItem.name = LanguagesManager.getInstance().getString("autosave");
        smItem.levelCode = levelCode;
        smItem.type = SaveType.normal;
        items.add(smItem);
        saveValues();
    }


    public void saveValues() {
        Preferences preferences = getPreferences();
        preferences.putString("keys", createKeysString());
        for (SmItem item : items) {
            preferences.putString("name_" + item.key, item.name);
            preferences.putString("level_code_" + item.key, item.levelCode);
            preferences.putString("type_" + item.key, "" + item.type);
        }
        preferences.flush();
    }


    private String createKeysString() {
        StringBuilder builder = new StringBuilder();
        for (SmItem item : items) {
            builder.append(item.key).append(" ");
        }
        return builder.toString();
    }


    public void loadValues() {
        Preferences preferences = getPreferences();
        String keys = preferences.getString("keys", "");
        items.clear();
        for (String key : keys.split(" ")) {
            if (key.length() == 0) continue;
            SmItem smItem = new SmItem();
            smItem.key = key;
            smItem.name = preferences.getString("name_" + key, "-");
            smItem.levelCode = preferences.getString("level_code_" + key, "");
            smItem.type = SaveType.valueOf(preferences.getString("type_" + key, ""));
            items.add(smItem);
        }
    }


    public SmItem getItem(String key) {
        if (key == null) return null;
        for (SmItem item : items) {
            if (item.key.equals(key)) return item;
        }
        return null;
    }


    public SmItem getLastItem(SaveType saveType) {
        if (items.size() == 0) return null;
        for (int i = items.size() - 1; i >= 0; i--) {
            SmItem smItem = items.get(i);
            if (smItem.isNot(saveType)) continue;
            return smItem;
        }
        return null;
    }


    public String getLevelCode(String key) {
        SmItem item = getItem(key);
        if (item == null) return null;
        return item.levelCode;
    }


    private boolean isKeyUsed(String key) {
        return getItem(key) != null;
    }


    private String getKeyForNewSlot() {
        while (true) {
            int value = YioGdxGame.random.nextInt(1000000);
            String key = "" + value;
            if (isKeyUsed(key)) continue;
            return key;
        }
    }


    private void showPreferencesInConsole() {
        Preferences preferences = getPreferences();
        for (Map.Entry<String, ?> entry : preferences.get().entrySet()) {
            System.out.println(entry.getKey() + ": [" + entry.getValue() + "]");
        }
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences(PREFS);
    }
}
