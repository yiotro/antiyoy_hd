package yio.tro.onliyoy.game.save_system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.Arrays;

public class UserLevelsProgressManager {


    private static UserLevelsProgressManager instance;
    private final String PREFS = "yio.tro.onliyoy.user_levels_progress";
    private ArrayList<String> ids;


    public UserLevelsProgressManager() {
        ids = new ArrayList<>();
        loadValues();
    }


    public static void initialize() {
        instance = null;
        getInstance(); // load
    }


    public static UserLevelsProgressManager getInstance() {
        if (instance == null) {
            instance = new UserLevelsProgressManager();
        }
        return instance;
    }


    public boolean isCompleted(String id) {
        return ids.contains(id);
    }


    public void onCompleted(String id) {
        if (isCompleted(id)) return;
        ids.add(id);
        saveValues();
    }


    private void saveValues() {
        Preferences preferences = getPreferences();
        StringBuilder stringBuilder = new StringBuilder();
        for (String token : ids) {
            stringBuilder.append(token).append("/");
        }
        preferences.putString("index", stringBuilder.toString());
        preferences.flush();
    }


    private void loadValues() {
        Preferences preferences = getPreferences();
        String index = preferences.getString("index");
        ids.clear();
        ids.addAll(Arrays.asList(index.split("/")));
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences(PREFS);
    }
}
