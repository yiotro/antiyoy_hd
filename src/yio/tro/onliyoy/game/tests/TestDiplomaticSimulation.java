package yio.tro.onliyoy.game.tests;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.general.GameRules;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.scenes.Scenes;

import java.util.Random;

public class TestDiplomaticSimulation extends AbstractTest {

    int counter;
    long startTime;


    @Override
    public boolean isInstant() {
        return false;
    }


    @Override
    protected String getName() {
        return "Diplomatic simulation";
    }


    @Override
    protected void execute() {
        System.out.println();
        System.out.println("Test started");
        GameRules.fastForwardSpeed = 16;
//        DebugFlags.analyzeEventFlows = true;
//        DebugFlags.determinedRandom = true;
//        YioGdxGame.random = new Random(0);
        counter = 0;
        startTime = System.currentTimeMillis();
        launchBattle();
    }


    @Override
    public void move() {
        checkForMatchResults();
        checkToSpeedUp();
    }


    private void checkToSpeedUp() {
        if (gameController.speedManager.getSpeed() == GameRules.fastForwardSpeed) return;
        if (gameController.objectsLayer == null) return;
        gameController.speedManager.setSpeed(GameRules.fastForwardSpeed);
    }


    private ViewableModel getViewableModel() {
        return gameController.objectsLayer.viewableModel;
    }


    private void checkForMatchResults() {
        if (!Scenes.matchResults.isCurrentlyVisible()) return;
        if (Scenes.loadingTraining.isCurrentlyVisible()) return;
        if (counter < 20) {
            launchBattle();
        } else {
            long deltaTime = System.currentTimeMillis() - startTime;
            int framesTime = Yio.convertMillisIntoFrames(deltaTime);
            String timeString = Yio.convertTimeToUnderstandableString(framesTime);
            System.out.println("--> TestDiplomaticSimulation finished in " + timeString + " <--");
            end();
        }
    }


    private void launchBattle() {
        counter++;
        long deltaTime = System.currentTimeMillis() - startTime;
        System.out.println("TestDiplomaticSimulation.launchBattle (" + counter + ", " + deltaTime + " ms)");
        Scenes.loadingTraining.create();
        Scenes.loadingTraining.setLevelCode(getLevelCode());
    }


    private String getLevelCode() {
        return "onliyoy_level_code#client_init:small,-1#camera:0.65 0.97 1.4#core_init:546.0 873.60004 29.4#hexes:0 0 gray,1 0 gray,0 1 green city -1,-1 1 gray,-1 0 gray,0 -1 gray,1 -1 gray,2 0 gray pine -1,1 1 green farm -1,2 -1 gray,0 2 green farm -1,-1 2 gray,-2 2 gray,-2 1 gray,-2 0 gray,-1 -1 gray,0 -2 gray,1 -2 gray,2 -2 gray,3 0 gray,2 1 green farm -1,3 -1 gray pine -1,1 2 gray,3 -2 gray,0 3 gray grave -1,-1 3 gray,-3 2 gray palm -1,-3 1 gray,-3 0 gray palm -1,-2 -1 gray,-1 -2 gray pine -1,0 -3 gray,1 -3 gray,2 -3 gray,3 -3 gray,4 0 gray,3 1 gray,4 -1 gray,2 2 gray,4 -2 gray,1 3 gray,4 -3 gray pine -1,-1 4 gray,-3 4 aqua city -1,-4 3 gray palm -1,-4 2 gray,-4 1 gray,-3 -1 gray,-2 -2 gray,-1 -3 gray,0 -4 gray,1 -4 gray,2 -4 gray,4 -4 gray palm -1,5 0 gray,4 1 gray pine -1,5 -1 gray,3 2 gray,5 -2 gray,5 -3 gray,1 4 gray,5 -4 gray,-2 5 gray tower -1,-4 5 aqua,-5 5 aqua,-5 4 aqua,-5 3 gray palm -1,-5 1 gray,-4 -1 gray palm -1,-3 -2 gray,-2 -3 gray,-1 -4 gray palm -1,1 -5 gray,2 -5 gray,4 -5 gray,6 0 gray,5 1 gray,6 -1 gray,4 2 gray palm -1,6 -2 gray,6 -3 gray,2 4 gray,6 -4 gray,-6 5 aqua,-6 4 gray,-6 3 gray palm -1,-6 2 gray,-6 1 gray,-4 -2 gray,-3 -3 gray,-2 -4 gray,-1 -5 gray,7 0 gray,7 -1 cyan,5 2 gray palm -1,7 -2 cyan,4 3 gray,3 4 gray,7 -4 gray,2 5 gray,-7 5 gray palm -1,-7 4 gray,-7 3 gray,-7 2 gray,-6 -1 gray,-5 -2 gray,-4 -3 gray grave -1,-3 -4 gray,7 1 gray grave -1,8 -1 cyan,6 2 gray,8 -2 cyan city -1,5 3 gray,4 4 blue,8 -4 gray tower -1,3 5 gray,-8 5 gray,-8 4 yellow,-8 3 gray,-8 2 gray,-6 -2 gray palm -1,-4 -4 gray,-3 -5 gray,6 3 gray,9 -3 cyan,5 4 blue,4 5 blue city -1,-9 5 yellow,-9 4 yellow city -1,-5 -4 red city -1,-4 -5 gray,5 5 blue,-10 5 yellow,-5 -5 red,#core_current_ids:0#player_entities:ai_balancer>green>Adana,ai_balancer>red>Komnarg,ai_balancer>yellow>Kabmoy,ai_balancer>aqua>Tede,ai_balancer>cyan>Papoda,ai_balancer>blue>Pokemsk,#provinces:6>0<1<0<10<Adana,-3<4<1<10<Tede,7<-1<2<10<Papoda,4<4<3<10<Pokemsk,-8<4<4<10<Kabmoy,-5<-4<5<10<Komnarg,#ready:-#rules:def 1#turn:0 0#diplomacy:-#mail_basket:0,#starting_hexes:0 0 gray,1 0 gray,0 1 green city -1,-1 1 gray,-1 0 gray,0 -1 gray,1 -1 gray,2 0 gray pine -1,1 1 green farm -1,2 -1 gray,0 2 green farm -1,-1 2 gray,-2 2 gray,-2 1 gray,-2 0 gray,-1 -1 gray,0 -2 gray,1 -2 gray,2 -2 gray,3 0 gray,2 1 green farm -1,3 -1 gray pine -1,1 2 gray,3 -2 gray,0 3 gray grave -1,-1 3 gray,-3 2 gray palm -1,-3 1 gray,-3 0 gray palm -1,-2 -1 gray,-1 -2 gray pine -1,0 -3 gray,1 -3 gray,2 -3 gray,3 -3 gray,4 0 gray,3 1 gray,4 -1 gray,2 2 gray,4 -2 gray,1 3 gray,4 -3 gray pine -1,-1 4 gray,-3 4 aqua city -1,-4 3 gray palm -1,-4 2 gray,-4 1 gray,-3 -1 gray,-2 -2 gray,-1 -3 gray,0 -4 gray,1 -4 gray,2 -4 gray,4 -4 gray palm -1,5 0 gray,4 1 gray pine -1,5 -1 gray,3 2 gray,5 -2 gray,5 -3 gray,1 4 gray,5 -4 gray,-2 5 gray tower -1,-4 5 aqua,-5 5 aqua,-5 4 aqua,-5 3 gray palm -1,-5 1 gray,-4 -1 gray palm -1,-3 -2 gray,-2 -3 gray,-1 -4 gray palm -1,1 -5 gray,2 -5 gray,4 -5 gray,6 0 gray,5 1 gray,6 -1 gray,4 2 gray palm -1,6 -2 gray,6 -3 gray,2 4 gray,6 -4 gray,-6 5 aqua,-6 4 gray,-6 3 gray palm -1,-6 2 gray,-6 1 gray,-4 -2 gray,-3 -3 gray,-2 -4 gray,-1 -5 gray,7 0 gray,7 -1 cyan,5 2 gray palm -1,7 -2 cyan,4 3 gray,3 4 gray,7 -4 gray,2 5 gray,-7 5 gray palm -1,-7 4 gray,-7 3 gray,-7 2 gray,-6 -1 gray,-5 -2 gray,-4 -3 gray grave -1,-3 -4 gray,7 1 gray grave -1,8 -1 cyan,6 2 gray,8 -2 cyan city -1,5 3 gray,4 4 blue,8 -4 gray tower -1,3 5 gray,-8 5 gray,-8 4 yellow,-8 3 gray,-8 2 gray,-6 -2 gray palm -1,-4 -4 gray,-3 -5 gray,6 3 gray,9 -3 cyan,5 4 blue,4 5 blue city -1,-9 5 yellow,-9 4 yellow city -1,-5 -4 red city -1,-4 -5 gray,5 5 blue,-10 5 yellow,-5 -5 red,#events_list:#starting_provinces:6>0<1<0<10<Adana,-3<4<1<10<Tede,7<-1<2<10<Papoda,4<4<3<10<Pokemsk,-8<4<4<10<Kabmoy,-5<-4<5<10<Komnarg,#editor:24000#";
    }

}
