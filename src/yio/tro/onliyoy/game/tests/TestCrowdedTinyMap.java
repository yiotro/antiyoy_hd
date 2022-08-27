package yio.tro.onliyoy.game.tests;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.loading.LoadingManager;
import yio.tro.onliyoy.game.loading.LoadingParameters;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class TestCrowdedTinyMap extends AbstractTest{


    @Override
    public boolean isInstant() {
        return true;
    }


    @Override
    protected String getName() {
        return "Crowded tiny map";
    }


    @Override
    protected void execute() {
        System.out.println();
        System.out.println("TestCrowdedTinyMap.execute");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            if (i > 0 && i % 100 == 0) {
                System.out.println("" + i);
            }
            launchBattle();
        }
        long deltaTime = System.currentTimeMillis() - startTime;
        int frames = Yio.convertMillisIntoFrames(deltaTime);
        System.out.println("Test finished in " + Yio.convertTimeToUnderstandableString(frames));
    }


    private void launchBattle() {
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.add("level_size", "" + LevelSize.tiny);
        loadingParameters.add("entities", getEntitiesCode());
        loadingParameters.add("rules_type", "" + RulesType.def);
        loadingParameters.add("diplomacy", "" + false);
        loadingParameters.add("fog_of_war", "" + false);
        LoadingManager loadingManager = gameController.yioGdxGame.loadingManager;
        loadingManager.startInstantly(LoadingType.training_create, loadingParameters);
    }


    private String getEntitiesCode() {
        return "robot aqua,robot yellow,robot blue,robot cyan,robot green,robot red,robot purple,robot brown,";
    }
}
