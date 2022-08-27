package yio.tro.onliyoy.game.tests;

import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.game.viewable_model.ReplayManager;

public class TestReplayRewind extends AbstractTest{

    int step;
    long targetTime;


    @Override
    public boolean isInstant() {
        return false;
    }


    @Override
    protected String getName() {
        return "Replay rewind";
    }


    @Override
    protected void execute() {
        step = 0;
    }


    @Override
    public void move() {
        switch (step) {
            default:
                break;
            case 0:
                (new IwClientInit(gameController.yioGdxGame, LoadingType.replay_open)).perform(getLevelCode());
                step++;
                break;
            case 1:
                if (!gameController.yioGdxGame.gameView.coversAllScreen()) break;
                getReplayManager().setFast(true);
                targetTime = System.currentTimeMillis() + 3000;
                step++;
                break;
            case 2:
                if (System.currentTimeMillis() < targetTime) break;
                getReplayManager().onStopButtonPressed();
                step++;
                break;
        }
    }


    private ReplayManager getReplayManager() {
        return gameController.objectsLayer.replayManager;
    }


    private String getLevelCode() {
        return "onliyoy_level_code#client_init:big,1#camera:0.52 1.05 0.9#core_init:840.0 1344.0 29.4#hexes:-2 2 yellow peasant 2,2 -2 red,0 3 gray palm -1,-2 3 yellow,-3 3 yellow,-3 2 yellow peasant 16,0 -3 red tower -1,1 -3 red peasant 4,2 -3 red,3 -3 red,4 -2 red,1 3 gray palm -1,4 -3 red,0 4 gray palm -1,-1 4 yellow palm -1,-2 4 yellow,-3 4 yellow,-4 4 yellow,-4 3 yellow,-4 2 yellow,-4 1 gray palm -1,0 -4 red,1 -4 red,3 -4 red,5 0 red,4 1 red,5 -1 red farm -1,3 2 red,5 -2 red farm -1,2 3 gray palm -1,5 -3 red,1 4 gray,5 -4 red,0 5 gray palm -1,-2 5 yellow,-3 5 yellow,-4 5 yellow farm -1,-5 5 yellow,-5 4 yellow,-5 3 yellow peasant 9,-5 2 yellow peasant 29,-5 1 gray palm -1,-3 -2 red peasant 14,-2 -3 red tower -1,-1 -4 red peasant 5,0 -5 aqua,1 -5 red,6 0 red farm -1,6 -1 red farm -1,4 2 red peasant 25,6 -2 red farm -1,3 3 red peasant 26,6 -3 red farm -1,2 4 gray palm -1,1 5 gray palm -1,-3 6 yellow,-4 6 yellow city -1,-5 6 yellow farm -1,-6 4 yellow,-6 3 gray pine -1,-6 2 gray pine -1,-6 1 gray palm -1,-6 0 gray palm -1,-4 -2 red tower -1,-3 -3 red peasant 18,-2 -4 aqua,-1 -5 aqua city -1,5 2 gray palm -1,7 -2 red city -1,4 3 gray,3 4 gray,-7 6 gray palm -1,-7 5 gray palm -1,-7 4 gray palm -1,-7 3 gray pine -1,-7 2 gray pine -1,-7 1 gray pine -1,-7 0 gray palm -1,-5 -2 red peasant 3,-4 -3 red,-2 -5 aqua,6 2 gray palm -1,8 -2 red farm -1,5 3 gray,8 -3 red farm -1,4 4 gray,3 5 gray,-8 6 gray palm -1,-8 4 gray palm -1,-8 3 gray palm -1,-8 2 gray palm -1,-8 1 gray palm -1,-8 0 gray palm -1,-7 -1 gray palm -1,-6 -2 aqua,-5 -3 aqua,-4 -4 aqua,-3 -5 aqua,-2 -6 aqua,7 2 gray palm -1,6 3 gray palm -1,5 4 cyan,4 5 cyan,-9 4 gray palm -1,-8 -1 gray palm -1,-7 -2 gray grave -1,-6 -3 aqua peasant 21,-5 -4 aqua peasant 7,-4 -5 aqua,-3 -6 aqua,-2 -7 gray palm -1,-1 -8 gray tower -1,6 4 cyan palm -1,5 5 cyan,4 6 cyan,3 7 cyan peasant 15,2 8 cyan peasant 1,-6 -4 aqua peasant 0,-5 -5 aqua peasant 11,-4 -6 aqua,-3 -7 aqua palm -1,8 3 gray palm -1,7 4 gray palm -1,6 5 cyan peasant 12,5 6 cyan farm -1,4 7 cyan,3 8 cyan,-4 -7 gray,5 7 cyan farm -1,-6 -6 gray tower -1,-5 -7 gray,6 7 cyan city -1,6 8 cyan farm -1,#core_current_ids:32#player_entities:human>aqua>Sebnosa,ai_balancer>cyan>Aydotyo,ai_balancer>yellow>Ribrain,ai_balancer>red>Ayghibao,#provinces:8>2<-2<4<7<Obneryeva,-2<3<5<1<Atosi,0<-5<6<23<Anmomsk,5<4<7<7<Sorse-city,#ready:-6 -3,-5 -4,-6 -4,-5 -5,#rules:def 1#turn:0 10#diplomacy:off#mail_basket:0,#starting_hexes:-2 2 gray,2 -2 gray,0 3 gray,-2 3 gray,-3 3 gray,-3 2 gray grave -1,0 -3 gray,1 -3 gray,2 -3 gray,3 -3 gray,4 -2 gray,1 3 gray palm -1,4 -3 gray,0 4 gray,-1 4 gray,-2 4 gray,-3 4 gray,-4 4 gray,-4 3 gray,-4 2 gray,-4 1 gray palm -1,0 -4 gray,1 -4 gray,3 -4 gray,5 0 gray,4 1 gray,5 -1 gray,3 2 gray,5 -2 gray,2 3 gray,5 -3 gray,1 4 gray,5 -4 gray,0 5 gray palm -1,-2 5 gray,-3 5 gray,-4 5 gray grave -1,-5 5 gray,-5 4 gray,-5 3 gray,-5 2 gray,-5 1 gray,-3 -2 gray,-2 -3 gray,-1 -4 gray,0 -5 gray,1 -5 gray,6 0 gray,6 -1 red,4 2 gray,6 -2 red,3 3 gray,6 -3 gray,2 4 gray,1 5 gray,-3 6 gray palm -1,-4 6 yellow city -1,-5 6 yellow,-6 4 gray,-6 3 gray,-6 2 gray,-6 1 gray palm -1,-6 0 gray,-4 -2 gray palm -1,-3 -3 gray,-2 -4 gray,-1 -5 aqua city -1,5 2 gray,7 -2 red city -1,4 3 gray,3 4 gray,-7 6 gray palm -1,-7 5 gray palm -1,-7 4 gray,-7 3 gray,-7 2 gray pine -1,-7 1 gray,-7 0 gray,-5 -2 gray,-4 -3 gray,-2 -5 aqua,6 2 gray palm -1,8 -2 red farm -1,5 3 gray,8 -3 red farm -1,4 4 gray,3 5 gray,-8 6 gray,-8 4 gray palm -1,-8 3 gray,-8 2 gray,-8 1 gray,-8 0 gray,-7 -1 gray,-6 -2 gray,-5 -3 gray,-4 -4 gray,-3 -5 gray,-2 -6 gray,7 2 gray,6 3 gray,5 4 gray,4 5 gray,-9 4 gray,-8 -1 gray,-7 -2 gray grave -1,-6 -3 gray,-5 -4 gray,-4 -5 gray,-3 -6 gray pine -1,-2 -7 gray,-1 -8 gray tower -1,6 4 gray,5 5 gray,4 6 gray grave -1,3 7 gray,2 8 gray palm -1,-6 -4 gray,-5 -5 gray,-4 -6 gray palm -1,-3 -7 gray,8 3 gray palm -1,7 4 gray,6 5 gray,5 6 gray,4 7 gray,3 8 gray,-4 -7 gray,5 7 gray,-6 -6 gray tower -1,-5 -7 gray,6 7 cyan city -1,6 8 cyan,#events_list:sm 4 10,sm 5 10,sm 6 10,sm 7 10,pb -2 -6 peasant 0 6,te - aqua,pb 5 7 peasant 1 7,te - cyan,pb -4 5 peasant 2 5,te - yellow,pb 5 -1 peasant 3 4,te - red,pa 0 3 palm -1,pa 0 4 palm -1,pa -4 2 palm -1,pa -2 5 palm -1,pa -5 1 palm -1,pa -7 1 pine -1,pa -4 -3 palm -1,pa -4 -5 pine -1,pa 3 7 palm -1,pa 7 4 palm -1,um -2 -6 -3 -5 t,te - aqua,um 5 7 5 6 t,te - cyan,um -4 5 -3 5 t,te - yellow,um 5 -1 5 0 t,pb 5 -2 peasant 4 4,te - red,pa -3 -2 palm -1,pa -6 0 palm -1,pa -5 -2 palm -1,pa -8 2 palm -1,pa 6 3 palm -1,pa -9 4 palm -1,pa 6 4 palm -1,pa -3 -7 palm -1,pa 4 7 palm -1,um -3 -5 -3 -6 t,te - aqua,um 5 6 4 7 t,te - cyan,um -3 5 -3 6 t,te - yellow,um 5 0 6 0 t,um 5 -2 6 -3 t,pb 5 -3 peasant 5 4,te - red,pa -2 4 palm -1,pa -2 -3 palm -1,pa -3 -3 palm -1,pa -6 -2 palm -1,pa -5 -5 palm -1,pa 6 5 palm -1,pa 3 8 palm -1,um -3 -6 -4 -4 t,te - aqua,um 4 7 4 6 t,te - cyan,um -3 6 -2 5 t,te - yellow,um 6 0 4 -2 t,um 5 -3 4 -3 t,um 6 -3 5 -4 t,pb 6 -2 farm 6 4,te - red,pa -2 3 palm -1,pa -6 4 palm -1,pa -2 -4 palm -1,pa -7 4 palm -1,pa -7 3 pine -1,pa -7 0 palm -1,pa -8 3 palm -1,pa -2 -7 palm -1,pa -6 -4 palm -1,pa 5 6 palm -1,um -4 -4 -4 -5 t,pb -4 -3 peasant 7 6,te - aqua,um 4 6 5 6 t,pb 6 8 farm 8 7,te - cyan,um -2 5 -2 4 t,pb -3 4 peasant 9 5,te - yellow,um 4 -2 3 -3 t,um 4 -3 2 -2 t,um 5 -4 2 -3 t,pb 5 -1 farm 10 4,te - red,pa -5 4 palm -1,pa 1 5 palm -1,pa -6 2 pine -1,pa -2 -5 palm -1,pa -8 6 palm -1,pa -8 1 palm -1,pa -7 -1 palm -1,pa -2 -6 palm -1,pa 7 2 palm -1,pa 4 7 palm -1,um -4 -3 -2 -5 t,um -4 -5 -2 -6 t,pb -4 -6 peasant 11 6,te - aqua,um 5 6 4 7 t,pb 5 5 peasant 12 7,te - cyan,um -2 4 -2 3 t,um -3 4 -3 3 t,te - yellow,um 2 -2 3 -4 t,um 2 -3 1 -3 t,um 3 -3 0 -3 t,pb 5 -2 farm 13 4,pb 1 -4 peasant 14 4,te - red,pa 2 3 palm -1,pa 2 4 palm -1,pa -6 3 pine -1,pa 5 2 palm -1,pa -8 0 palm -1,pa -8 -1 palm -1,pa -6 -3 palm -1,pa 4 6 palm -1,pa 5 6 palm -1,um -2 -5 -2 -4 t,um -2 -6 0 -5 t,um -4 -6 -3 -7 t,te - aqua,um 4 7 4 6 t,um 5 5 5 6 t,pb 4 5 peasant 15 7,te - cyan,um -2 3 -4 4 t,um -3 3 -5 5 t,pb -1 4 peasant 16 5,te - yellow,um 3 -4 0 -4 t,um 1 -3 1 -5 t,um 0 -3 -1 -4 t,um 1 -4 -2 -3 t,pb 0 -3 tower 17 4,pb -3 -2 peasant 18 4,te - red,pa 4 2 palm -1,pa -4 -6 palm -1,te - aqua,um 4 5 5 4 t,um 4 6 3 7 t,um 5 6 6 5 t,te - cyan,um -4 4 -5 4 t,um -1 4 -4 3 t,um -5 5 -5 3 t,te - yellow,um 0 -4 -3 -3 t,um -1 -4 -4 -2 t,um -2 -3 -4 -3 t,um -3 -2 -5 -2 t,pb -2 -3 tower 19 4,pb 6 -3 farm 20 4,um 1 -5 -1 -4 t,te - red,pa -1 4 palm -1,pa -5 2 pine -1,pa -2 -6 palm -1,pa 4 7 palm -1,um 0 -5 -2 -6 t,um -2 -4 -5 -3 t,um -3 -7 -4 -6 t,pb -6 -2 peasant 21 6,te - aqua,um 5 4 4 7 t,um 6 5 6 4 t,um 3 7 3 8 t,pb 5 7 farm 22 7,te - cyan,um -4 3 -1 4 t,um -5 4 -6 4 t,um -5 3 -4 2 t,pb -5 6 farm 23 5,te - yellow,pb 6 -1 farm 24 4,pb 4 1 peasant 25 4,pb 3 2 peasant 26 4,um -1 -4 0 -4 t,um -3 -3 -3 -2 t,um -4 -2 -1 -4 t,um -4 -3 -3 -3 t,um -5 -2 -4 -3 t,te - red,pa -5 3 pine -1,pa 3 7 palm -1,pa 6 5 palm -1,um -6 -2 -6 -3 t,um -5 -3 -5 -4 t,um -2 -6 -6 -4 t,um -4 -6 -5 -5 t,te - aqua,um 6 4 6 5 t,um 4 7 3 7 t,um 3 8 2 8 t,pb 5 6 farm 27 7,te - cyan,um -1 4 -3 2 t,um -4 2 -5 3 t,um -6 4 -2 2 t,pb -4 5 farm 28 5,pb -5 2 peasant 29 5,te - yellow,um 4 1 4 2 t,um 3 2 3 3 t,pb -4 -2 tower 30 4,pb 6 0 farm 31 4,um 0 -4 1 -3 t,um -1 -4 -5 -2 t,um -3 -2 -1 -4 t,um -3 -3 -3 -2 t,um -4 -3 -3 -3 t,te - red,pa -1 4 palm -1,pa 6 4 palm -1,pa -3 -7 palm -1,#starting_provinces:8>6<-1<4<10<Obneryeva,-4<6<5<10<Atosi,-1<-5<6<10<Anmomsk,6<7<7<10<Sorse-city,#";
    }
}
