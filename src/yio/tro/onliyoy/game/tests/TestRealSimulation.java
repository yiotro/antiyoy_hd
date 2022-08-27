package yio.tro.onliyoy.game.tests;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.general.GameRules;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.scenes.Scenes;

import java.util.Random;

public class TestRealSimulation extends AbstractTest {

    int counter;
    long startTime;


    @Override
    public boolean isInstant() {
        return false;
    }


    @Override
    protected String getName() {
        return "Real simulation";
    }


    @Override
    protected void execute() {
        System.out.println();
        System.out.println("Test started");
        GameRules.fastForwardSpeed = 20;
//        DebugFlags.analyzeEventFlows = true;
        counter = 0;
        startTime = System.currentTimeMillis();
        launchBattle();
    }


    private void decideRandomSituation() {
        // called at launch of each battle
        // the real outcome of battle actually depends on random in abstract ai
//        DebugFlags.determinedRandom = true;
//        YioGdxGame.random = new Random(0);
    }


    @Override
    public void move() {
        checkForMatchResults();
        tryToDetectProblem();
        checkToSpeedUp();
    }


    private void checkToSpeedUp() {
        if (gameController.speedManager.getSpeed() == GameRules.fastForwardSpeed) return;
        if (gameController.objectsLayer == null) return;
        gameController.speedManager.setSpeed(GameRules.fastForwardSpeed);
    }


    private void tryToDetectProblem() {
        if (gameController.yioGdxGame.gamePaused) return;
        if (!gameController.yioGdxGame.gameView.coversAllScreen()) return;
        ViewableModel viewableModel = getViewableModel();

    }


    private ViewableModel getViewableModel() {
        return gameController.objectsLayer.viewableModel;
    }


    private void checkForMatchResults() {
        if (!Scenes.matchResults.isCurrentlyVisible()) return;
        if (Scenes.loadingTraining.isCurrentlyVisible()) return;
        if (DebugFlags.detectedMoveZoneUpdateProblem) {
            SoundManager.playSound(SoundType.alert);
            end();
            return;
        }
        if (counter < 25) {
            launchBattle();
        } else {
            long deltaTime = System.currentTimeMillis() - startTime;
            int framesTime = Yio.convertMillisIntoFrames(deltaTime);
            String timeString = Yio.convertTimeToUnderstandableString(framesTime);
            System.out.println("--> TestRealSimulation finished in " + timeString + " <--");
            end();
        }
    }


    private void launchBattle() {
        decideRandomSituation();
        counter++;
        long deltaTime = System.currentTimeMillis() - startTime;
        System.out.println("TestRealSimulation.launchBattle (" + counter + ", " + deltaTime + " ms)");
        Scenes.loadingTraining.create();
        Scenes.loadingTraining.setLevelCode(getLevelCode());
    }


    private String getLevelCode() {
        return "onliyoy_level_code#client_init:small,-1#camera:0.65 1.0 1.2#core_init:546.0 873.60004 29.4#hexes:0 0 gray palm -1,1 0 gray,0 1 gray,-1 1 gray,-1 0 gray,0 -1 gray,1 -1 gray,1 1 brown farm -1,2 -1 aqua farm -1,-1 2 blue farm -1,-2 1 yellow farm -1,-1 -1 purple farm -1,1 -2 cyan farm -1,3 0 gray,2 1 gray,3 -1 gray,1 2 gray,3 -2 gray,0 3 gray,-1 3 gray,-2 3 gray,-3 3 gray,-3 2 gray,-3 1 gray,-3 0 gray,-2 -1 gray,-1 -2 gray,0 -3 gray,1 -3 gray,2 -3 gray,3 -3 gray,4 0 aqua city -1,3 1 gray,4 -1 gray,1 3 gray,4 -3 gray,0 4 brown city -1,-1 4 gray,-3 4 gray,-4 4 blue city -1,-4 3 gray,-4 1 gray,-4 0 yellow city -1,-3 -1 gray,-1 -3 gray,0 -4 purple city -1,1 -4 gray,3 -4 gray,4 -4 cyan city -1,5 0 aqua palm -1,5 -2 gray,2 3 gray,0 5 brown palm -1,-3 5 gray,-5 5 blue palm -1,-5 2 gray,-5 0 yellow palm -1,-2 -3 gray,0 -5 purple palm -1,3 -5 gray,5 -5 cyan palm -1,#core_current_ids:0#player_entities:ai_balancer>yellow>Roynme,ai_balancer>brown>Modovo,ai_balancer>aqua>Odyeda,ai_balancer>cyan>Boykata,ai_balancer>blue>Abnedosk,ai_balancer>purple>Menma,#provinces:6>4<0<0<15<Odyeda,0<4<1<15<Modovo,-4<4<2<15<Abnedosk,-4<0<3<15<Roynme,0<-4<4<15<Menma,4<-4<5<15<Boykata,#ready:-#rules:def 1#turn:0 0#diplomacy:off#mail_basket:0,#starting_hexes:0 0 gray palm -1,1 0 gray,0 1 gray,-1 1 gray,-1 0 gray,0 -1 gray,1 -1 gray,1 1 brown farm -1,2 -1 aqua farm -1,-1 2 blue farm -1,-2 1 yellow farm -1,-1 -1 purple farm -1,1 -2 cyan farm -1,3 0 gray,2 1 gray,3 -1 gray,1 2 gray,3 -2 gray,0 3 gray,-1 3 gray,-2 3 gray,-3 3 gray,-3 2 gray,-3 1 gray,-3 0 gray,-2 -1 gray,-1 -2 gray,0 -3 gray,1 -3 gray,2 -3 gray,3 -3 gray,4 0 aqua city -1,3 1 gray,4 -1 gray,1 3 gray,4 -3 gray,0 4 brown city -1,-1 4 gray,-3 4 gray,-4 4 blue city -1,-4 3 gray,-4 1 gray,-4 0 yellow city -1,-3 -1 gray,-1 -3 gray,0 -4 purple city -1,1 -4 gray,3 -4 gray,4 -4 cyan city -1,5 0 aqua palm -1,5 -2 gray,2 3 gray,0 5 brown palm -1,-3 5 gray,-5 5 blue palm -1,-5 2 gray,-5 0 yellow palm -1,-2 -3 gray,0 -5 purple palm -1,3 -5 gray,5 -5 cyan palm -1,#events_list:#starting_provinces:6>4<0<0<15<Odyeda,0<4<1<15<Modovo,-4<4<2<15<Abnedosk,-4<0<3<15<Roynme,0<-4<4<15<Menma,4<-4<5<15<Boykata,#editor:1044000#";
    }

}
