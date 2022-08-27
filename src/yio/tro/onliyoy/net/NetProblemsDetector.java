package yio.tro.onliyoy.net;

import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.RepeatYio;

import java.io.IOException;

public class NetProblemsDetector {

    NetRoot netRoot;
    public long lastTimeReceivedMessage;
    RepeatYio<NetProblemsDetector> repeatCheckSilence;
    private int silenceDelay;
    boolean waitingForSho;
    boolean active;
    RepeatYio<NetProblemsDetector> repeatCheckToReconnect;
    boolean readyToReconnect;
    public String reconnectionMatchId;


    public NetProblemsDetector(NetRoot netRoot) {
        this.netRoot = netRoot;
        lastTimeReceivedMessage = System.currentTimeMillis();
        silenceDelay = 20 * 1000;
        waitingForSho = false;
        active = true;
        readyToReconnect = false;
        reconnectionMatchId = "";
        checkForLocalMode();
        initRepeats();
    }


    private void checkForLocalMode() {
        if (!netRoot.isInLocalMode()) return;
        active = false;
    }


    private void initRepeats() {
        repeatCheckSilence = new RepeatYio<NetProblemsDetector>(this, 60) {
            @Override
            public void performAction() {
                parent.checkForSilence();
            }
        };
        repeatCheckToReconnect = new RepeatYio<NetProblemsDetector>(this, 60 * 60) {
            @Override
            public void performAction() {
                parent.checkToReconnect();
            }
        };
    }


    void move() {
        if (netRoot.offlineMode) return;
        repeatCheckSilence.move();
        repeatCheckToReconnect.move();
    }


    void checkToReconnect() {
        if (!readyToReconnect) return;
        readyToReconnect = false;
        ViewableModel viewableModel = getViewableModel();
        if (viewableModel == null) return;
        if (!viewableModel.isNetMatch()) return;
        try {
            netRoot.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (getYioGdxGame().gameView.coversAllScreen()) {
            reconnectionMatchId = netRoot.currentMatchData.matchId;
        }
        getYioGdxGame().applyFullTransitionToUI();
        Scenes.entry.create();
        Scenes.toast.show("reconnect_attempt");
    }


    private ViewableModel getViewableModel() {
        GameController gameController = getYioGdxGame().gameController;
        if (gameController == null) return null;
        ObjectsLayer objectsLayer = gameController.objectsLayer;
        if (objectsLayer == null) return null;
        return objectsLayer.viewableModel;
    }


    private YioGdxGame getYioGdxGame() {
        return netRoot.yioGdxGame;
    }


    void checkForSilence() {
        if (!active) return;
        if (checkToWaitForSho()) return;
        if (System.currentTimeMillis() < lastTimeReceivedMessage + silenceDelay) return;
        if (Scenes.entry.isCurrentlyVisible()) return;
        if (Scenes.checkIn.isCurrentlyVisible()) return;
        if (Scenes.exceptionReport.isCurrentlyVisible()) return;
        waitingForSho = true;
        netRoot.sendMessage(NmType.sho, "");
    }


    private boolean checkToWaitForSho() {
        if (!waitingForSho) return false;
        if (System.currentTimeMillis() < lastTimeReceivedMessage + silenceDelay + 4000) return true;
        if (Scenes.notification.isCurrentlyVisible()) return true;
        onServerNotAnswering();
        return true;
    }


    private void onServerNotAnswering() {
        waitingForSho = false;
        Scenes.notification.show("server_not_responding", true);
        lastTimeReceivedMessage = System.currentTimeMillis(); // to make a delay before next check
        if (SettingsManager.getInstance().reconnection) {
            readyToReconnect = true;
            repeatCheckToReconnect.setCountDown(60);
        } else {
            if (isInNetMatchCurrently()) return;
            PostponedReactionsManager.aprOfflineMode.launch();
        }
    }


    private boolean isInNetMatchCurrently() {
        if (Scenes.netPauseMenu.isCurrentlyVisible()) return true;
        YioGdxGame yioGdxGame = netRoot.yioGdxGame;
        if (!yioGdxGame.gameView.coversAllScreen()) return false;
        GameController gameController = yioGdxGame.gameController;
        return gameController.objectsLayer.viewableModel.isNetMatch();
    }


    public void onMessageProcessed() {
        lastTimeReceivedMessage = System.currentTimeMillis();
        waitingForSho = false;
    }
}
