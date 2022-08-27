package yio.tro.onliyoy.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.Fonts;

public class CustomLanguageLoader {


    public static final String prefs = "onliyoy.language";
    private static boolean autoDetect;
    private static String langName;


    public static void loadLanguage() {
        System.out.println();
        System.out.println("CustomLanguageLoader.loadLanguage");
        Preferences preferences = Gdx.app.getPreferences(prefs);

        autoDetect = preferences.getBoolean("auto", true);

        if (!autoDetect) {
            langName = preferences.getString("lang_name");
            LanguagesManager.getInstance().setLanguage(langName);
        }
    }


    public static void setAndSaveLanguage(String langName) {
        Preferences preferences = Gdx.app.getPreferences(prefs);

        preferences.putBoolean("auto", false);
        preferences.putString("lang_name", langName);

        preferences.flush();

        Fonts.init(); // load language and recreate fonts
    }


}
