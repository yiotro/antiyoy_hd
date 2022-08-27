package yio.tro.onliyoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class AlternativeUpdateWorker {

    private static AlternativeUpdateWorker instance;
    private static final String PREFS = "yio.tro.onliyoy.auw";
    public boolean ready;


    public AlternativeUpdateWorker() {
        ready = false;
        load();
    }


    public static void initialize() {
        instance = null;
    }


    public static AlternativeUpdateWorker getInstance() {
        if (instance == null) {
            instance = new AlternativeUpdateWorker();
        }
        return instance;
    }


    public void save() {
        Preferences preferences = getPreferences();
        preferences.putBoolean("ready", ready);
        preferences.flush();
    }


    public void onProtocolSuccessfullyValidated() {
        if (!ready) return;
        setReady(false);
        save();
    }


    public void onClientVersionOutOfDateMessageReceived() {
        if (!ready) {
            setReady(true);
            save();
            return;
        }
        Scenes.alternativeUpdate.create();
    }


    public void setReady(boolean ready) {
        this.ready = ready;
    }


    private void load() {
        Preferences preferences = getPreferences();
        ready = preferences.getBoolean("ready", false);
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences(PREFS);
    }

}
