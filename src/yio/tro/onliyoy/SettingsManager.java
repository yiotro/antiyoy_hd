package yio.tro.onliyoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SettingsManager {

    private static SettingsManager instance;
    public int graphicsQuality;
    public boolean soundEnabled;
    public boolean fullScreenMode;
    public boolean requestRestartApp;
    public boolean autosave;
    public boolean confirmToEndTurn;
    public boolean autoLogin;
    public boolean waterTexture;
    public boolean longTap;
    public boolean detailedUserLevels;
    public boolean alertTurnStart;
    public boolean automaticTurnEnd;
    public boolean grid;
    public boolean localizeTime;
    public boolean reconnection;
    public boolean constructionPanel;
    public boolean hideStatistics;
    public boolean grayVoid;


    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }

        return instance;
    }


    public static void initialize() {
        instance = null;
    }


    public void saveValues() {
        Preferences prefs = getPreferences();

        prefs.putBoolean("sound", soundEnabled);
        prefs.putBoolean("full_screen", fullScreenMode);
        prefs.putInteger("graphics_quality", graphicsQuality);
        prefs.putBoolean("autosave", autosave);
        prefs.putBoolean("confirm_end_turn", confirmToEndTurn);
        prefs.putBoolean("auto_login", autoLogin);
        prefs.putBoolean("water_texture", waterTexture);
        prefs.putBoolean("long_tap", longTap);
        prefs.putBoolean("detailed_user_levels", detailedUserLevels);
        prefs.putBoolean("alert_turn_start", alertTurnStart);
        prefs.putBoolean("automatic_turn_end", automaticTurnEnd);
        prefs.putBoolean("grid", grid);
        prefs.putBoolean("localize_time", localizeTime);
        prefs.putBoolean("reconnection", reconnection);
        prefs.putBoolean("construction_panel", constructionPanel);
        prefs.putBoolean("hide_statistics", hideStatistics);
        prefs.putBoolean("gray_void", grayVoid);

        prefs.flush();
    }


    public void loadValues() {
        Preferences prefs = getPreferences();

        soundEnabled = prefs.getBoolean("sound", true);
        fullScreenMode = prefs.getBoolean("full_screen", true);
        graphicsQuality = prefs.getInteger("graphics_quality", GraphicsYio.QUALITY_NORMAL);
        autosave = prefs.getBoolean("autosave", false);
        confirmToEndTurn = prefs.getBoolean("confirm_end_turn", false);
        autoLogin = prefs.getBoolean("auto_login", autoLogin);
        waterTexture = prefs.getBoolean("water_texture", false);
        longTap = prefs.getBoolean("long_tap", true);
        detailedUserLevels = prefs.getBoolean("detailed_user_levels", false);
        alertTurnStart = prefs.getBoolean("alert_turn_start", true);
        automaticTurnEnd = prefs.getBoolean("automatic_turn_end", true);
        grid = prefs.getBoolean("grid", false);
        localizeTime = prefs.getBoolean("localize_time", false);
        reconnection = prefs.getBoolean("reconnection", false);
        constructionPanel = prefs.getBoolean("construction_panel", false);
        hideStatistics = prefs.getBoolean("hide_statistics", false);
        grayVoid = prefs.getBoolean("gray_void", false);

        onValuesChanged();
    }


    public void resetValues() {
        Preferences preferences = getPreferences();
        preferences.clear();
        preferences.flush();
        loadValues();
    }


    public void onValuesChanged() {

    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences("onliyoy.settings");
    }

}
