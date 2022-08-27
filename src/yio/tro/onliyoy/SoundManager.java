package yio.tro.onliyoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import yio.tro.onliyoy.game.debug.DebugFlags;

import java.util.HashMap;

public class SoundManager {

    public static SoundThreadYio soundThreadYio;
    private static HashMap<SoundType, Sound> mapSounds;


    public static void loadSounds() {
        startThread();

        mapSounds = new HashMap<>();
        for (SoundType soundType : SoundType.values()) {
            mapSounds.put(soundType, loadSound("" + soundType));
        }
    }


    private static void startThread() {
        soundThreadYio = new SoundThreadYio();
        soundThreadYio.start();
        soundThreadYio.setPriority(1);
    }


    private static Sound loadSound(String name) {
        Sound sound;
        try {
            sound = Gdx.audio.newSound(Gdx.files.internal("sound/" + name + getExtension()));
        } catch (Exception e) {
            sound = null;
            System.out.println("SoundManager.loadSound: can't load " + name);
        }
        return sound;
    }


    private static String getExtension() {
        if (YioGdxGame.platformType == PlatformType.ios) {
            return ".mp3";
        }
        return ".ogg";
    }


    public static void playSound(SoundType soundType) {
        playSound(soundType, false);
    }


    public static void playSound(SoundType soundType, boolean force) {
        if (!force && DebugFlags.humanImitation) return;
        playSound(mapSounds.get(soundType));
    }


    private static void playSound(Sound sound) {
        if (sound == null) return;
        soundThreadYio.playSound(sound);
    }


    protected static void playSoundDirectly(Sound sound) {
        if (!SettingsManager.getInstance().soundEnabled) return;
        if (DebugFlags.testingModeEnabled) return;
        sound.play();
    }
}
