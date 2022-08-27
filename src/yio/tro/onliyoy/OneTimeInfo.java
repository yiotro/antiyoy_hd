package yio.tro.onliyoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class OneTimeInfo {

    private static OneTimeInfo instance;

    public boolean syncComplete;
    public boolean iosRelease;
    public boolean tutorial;
    public boolean privacyPolicy;
    public boolean hintProfile;
    public boolean hintActivateItem;
    public boolean fishExplanation;
    public boolean tabsInCustomization;
    public boolean howToSupport;
    public boolean hintFreeFish;
    public boolean onlineDisclaimer;
    public boolean shprotyRelease;


    public static OneTimeInfo getInstance() {
        if (instance == null) {
            instance = new OneTimeInfo();
            instance.load();
        }

        return instance;
    }


    public static void initialize() {
        instance = null;
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences("onliyoy.oneTimeInfo");
    }


    public void load() {
        Preferences preferences = getPreferences();
        syncComplete = preferences.getBoolean("sync_complete", false);
        iosRelease = preferences.getBoolean("ios_release", false);
        tutorial = preferences.getBoolean("tutorial", false);
        privacyPolicy = preferences.getBoolean("privacy_policy", false);
        hintProfile = preferences.getBoolean("hint_profile", false);
        hintActivateItem = preferences.getBoolean("hint_activate", false);
        fishExplanation = preferences.getBoolean("fish_explanation", false);
        tabsInCustomization = preferences.getBoolean("tabs_in_customization", false);
        howToSupport = preferences.getBoolean("how_to_support", false);
        hintFreeFish = preferences.getBoolean("hint_free_fish", false);
        onlineDisclaimer = preferences.getBoolean("online_disclaimer", false);
        shprotyRelease = preferences.getBoolean("shproty_release", false);
    }


    public void save() {
        Preferences preferences = getPreferences();
        preferences.putBoolean("sync_complete", syncComplete);
        preferences.putBoolean("ios_release", iosRelease);
        preferences.putBoolean("tutorial", tutorial);
        preferences.putBoolean("privacy_policy", privacyPolicy);
        preferences.putBoolean("hint_profile", hintProfile);
        preferences.putBoolean("hint_activate", hintActivateItem);
        preferences.putBoolean("fish_explanation", fishExplanation);
        preferences.putBoolean("tabs_in_customization", tabsInCustomization);
        preferences.putBoolean("how_to_support", howToSupport);
        preferences.putBoolean("hint_free_fish", hintFreeFish);
        preferences.putBoolean("online_disclaimer", onlineDisclaimer);
        preferences.putBoolean("shproty_release", shprotyRelease);
        preferences.flush();
    }
}
