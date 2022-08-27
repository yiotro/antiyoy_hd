package yio.tro.onliyoy.net;

import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.input.NetBufferManager;
import yio.tro.onliyoy.net.input.NetInputHandler;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.net.shared.*;

import java.io.PrintWriter;
import java.net.Socket;

public class NetRoot {

    public YioGdxGame yioGdxGame;
    public Socket socket;
    PrintWriter printWriter;
    public NetUserData userData;
    public NetConnectionManager connectionManager;
    public NetBufferManager bufferManager;
    public NetInputHandler inputHandler;
    private NetMessage sampleMessage;
    public PostponedReactionsManager postponedReactionsManager;
    public NetMatchBattleData currentMatchData;
    public NetProblemsDetector problemsDetector;
    public NetSignInData signInData;
    public NetUlTransferData tempUlTransferData;
    public NetVerificationInfo verificationInfo;
    public boolean offlineMode;
    public NetExperienceManager netExperienceManager;
    private NetKickPreventionWorker kickPreventionWorker;
    public NetCustomizationData customizationData;
    public BillingBuffer billingBuffer;
    public NetPlayerStatisticsData initialStatisticsData;


    public NetRoot(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        userData = new NetUserData();
        connectionManager = new NetConnectionManager(this);
        bufferManager = new NetBufferManager(this);
        inputHandler = new NetInputHandler(this);
        sampleMessage = new NetMessage();
        postponedReactionsManager = new PostponedReactionsManager(this);
        currentMatchData = new NetMatchBattleData();
        problemsDetector = new NetProblemsDetector(this);
        signInData = new NetSignInData();
        tempUlTransferData = new NetUlTransferData();
        verificationInfo = new NetVerificationInfo();
        netExperienceManager = new NetExperienceManager(this);
        offlineMode = false;
        kickPreventionWorker = new NetKickPreventionWorker(this);
        customizationData = new NetCustomizationData();
        billingBuffer = new BillingBuffer(yioGdxGame);
        initialStatisticsData = new NetPlayerStatisticsData();
    }


    public void move() {
        bufferManager.move();
        postponedReactionsManager.move();
        problemsDetector.move();
        netExperienceManager.move();
        kickPreventionWorker.move();
        billingBuffer.move();
    }


    public void sendMessage(NmType type, String value) {
        if (offlineMode) return;
        if (value.length() == 0) {
            value = "-";
        }
        sampleMessage.type = type;
        sampleMessage.value = value;
        String code = sampleMessage.encode();
        printWriter.println(code);
        System.out.println("[You]: " + code);
    }


    public boolean isSpectatorCurrently() {
        if (currentMatchData == null) return false;
        return currentMatchData.getColor(userData.id) == null;
    }


    public boolean isInLocalMode() {
        return NetValues.LOCATION_TYPE == NetLocationType.local;
    }


    public void enableOfflineMode() {
        offlineMode = true;
        userData.role = NetRole.undefined;
        if (Scenes.exceptionReport.isCurrentlyVisible()) return;
        if (Scenes.entry.isCurrentlyVisible()) {
            Scenes.mainLobby.create();
            return;
        }
        if (Scenes.mainLobby.isCurrentlyVisible()) {
            Scenes.mainLobby.updateNicknameView();
        }
        SceneYio focusScene = yioGdxGame.menuControllerYio.getFocusScene();
        if (focusScene != null && focusScene.isOnlineTargeted()) {
            MenuSwitcher.getInstance().createChooseGameModeMenu();
            Scenes.notification.show("offline_mode_enabled");
        }
    }


    public void onAppExit() {
        if (printWriter == null) return;
        printWriter.println("stop");
    }
}
