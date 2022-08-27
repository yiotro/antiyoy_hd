package yio.tro.onliyoy.game.campaign;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.export_import.IwCampaignInit;
import yio.tro.onliyoy.menu.scenes.Scenes;

import java.util.ArrayList;

public class CampaignManager {

    private static CampaignManager instance;
    private YioGdxGame yioGdxGame;
    private static String PREFS = "yio.tro.onliyoy.campaign";
    ArrayList<Integer> completedLevels;
    public int currentLevelIndex;


    public CampaignManager() {
        yioGdxGame = null;
        completedLevels = new ArrayList<>();
        currentLevelIndex = -1;
        loadValues();
    }


    public static void initialize(YioGdxGame yioGdxGame) {
        instance = null;
        getInstance().yioGdxGame = yioGdxGame;
    }


    public static CampaignManager getInstance() {
        if (instance == null) {
            instance = new CampaignManager();
        }
        return instance;
    }


    public void loadValues() {
        Preferences preferences = getPreferences();
        String source = preferences.getString("progress");
        String[] split = source.split(" ");
        completedLevels.clear();
        for (String token : split) {
            if (token.length() < 1) continue;
            completedLevels.add(Integer.valueOf(token));
        }
    }


    public void onLevelCompleted(int index) {
        if (isLevelCompleted(index)) return;
        completedLevels.add(index);
        Scenes.campaign.onLevelMarkedAsCompleted();
        saveValues();
    }


    public int getNextLevelIndex(int index) {
        if (index >= getLastLevelIndex()) return -1;
        int nextIndex = index + 1;
        while (nextIndex != index && isLevelCompleted(nextIndex)) {
            nextIndex++;
            if (nextIndex > getLastLevelIndex()) {
                nextIndex = 0;
            }
        }
        return nextIndex;
    }


    public boolean isLevelCompleted(int index) {
        if (index == -1) return true;
        for (int completedLevel : completedLevels) {
            if (completedLevel == index) return true;
        }
        return false;
    }


    public boolean areAllLevelsCompleted() {
        return completedLevels.size() >= getLastLevelIndex();
    }


    public void saveValues() {
        Preferences preferences = getPreferences();
        StringBuilder builder = new StringBuilder();
        for (Integer completedLevel : completedLevels) {
            builder.append(completedLevel).append(" ");
        }
        preferences.putString("progress", builder.toString());
        preferences.flush();
    }


    public int getIndexOfTargetUnlockedLevel() {
        for (int index = 0; index < getLastLevelIndex(); index++) {
            if (getLevelType(index) == CciType.unlocked) return index;
        }
        return getLastLevelIndex();
    }


    public CciType getLevelType(int levelIndex) {
        if (isLevelCompleted(levelIndex)) {
            return CciType.completed;
        }
        if (getDifficulty(levelIndex) != getDifficulty(levelIndex - 1)) {
            // start of new section
            return CciType.unlocked;
        }
        if (isLevelCompleted(levelIndex - 1) || isLevelCompleted(levelIndex - 2) || isLevelCompleted(levelIndex - 3)) {
            return CciType.unlocked;
        }
        return CciType.unknown;
    }


    public int getLastLevelIndex() {
        return CampaignLevels.getInstance().quantity;
    }


    public Difficulty getDifficulty(int levelIndex) {
        // new difficulty should start at level index that is divided by 6
        if (levelIndex < 12) return Difficulty.easy;
        if (levelIndex < 24) return Difficulty.average;
        if (levelIndex < 60) return Difficulty.hard;
        if (levelIndex < 120) return Difficulty.expert;
        return Difficulty.balancer;
    }


    public void launchCampaignLevel(int index) {
        currentLevelIndex = index;
        String levelCode = CampaignLevels.getInstance().getLevelCode(index);
        (new IwCampaignInit(yioGdxGame, index)).perform(levelCode);
    }


    public HColor convertDifficultyIntoColor(Difficulty difficulty) {
        switch (difficulty) {
            default:
                return HColor.gray;
            case tutorial:
                return HColor.ice;
            case easy:
                return HColor.yellow;
            case average:
                return HColor.mint;
            case hard:
                return HColor.cyan;
            case expert:
                return HColor.purple;
            case balancer:
                return HColor.rose;
        }
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences(PREFS);
    }

}
