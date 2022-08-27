package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.menu.scenes.AbstractNetWaitScene;
import yio.tro.onliyoy.net.NetRoot;

import java.util.ArrayList;

public class PostponedReactionsManager {


    NetRoot root;
    ArrayList<AbstractPostponedReaction> reactions;
    public ArrayList<AbstractNetWaitScene> netWaitScenes;
    public static AprOpenMainLobby aprOpenMainLobby;
    public static AprSendQuickMatchRequest aprSendQuickMatchRequest;
    public static AprShowMatchLobby aprShowMatchLobby;
    public static AprShowCustomMatches aprShowCustomMatches;
    public static AprStartNetMatch aprStartNetMatch;
    public static AprFinishNetMatch aprFinishNetMatch;
    public static AprServerEvent aprServerEvent;
    public static AprShowRecentlyLaunchedMatches aprShowRecentlyLaunchedMatches;
    public static AprJoinAsSpectator aprJoinAsSpectator;
    public static AprCompletionCheck aprCompletionCheck;
    public static AprModeratorPanel aprModeratorPanel;
    public static AprUploadProhibited aprUploadProhibited;
    public static AprOpenUserLevels aprOpenUserLevels;
    public static AprPlayUserLevel aprPlayUserLevel;
    public static AprCheckUserLevel aprCheckUserLevel;
    public static AprCheckReport aprCheckReport;
    public static AprHintProfile aprHintProfile;
    public static AprRejoinMatch aprRejoinMatch;
    public static AprShowInGameToast aprShowInGameToast;
    public static AprOfflineMode aprOfflineMode;
    public static AprCheckRenaming aprCheckRenaming;
    public static AprHintTabsInCustomization aprHintTabsInCustomization;
    public static AprHintFreeFish aprHintFreeFish;
    public static AprMassCheckRenamings aprMassCheckRenamings;
    public static AprSendDuelRequest aprSendDuelRequest;
    // don't forget to initialize them lower


    public PostponedReactionsManager(NetRoot root) {
        this.root = root;
        reactions = new ArrayList<>();
        netWaitScenes = new ArrayList<>();
        createReactions();
    }


    private void createReactions() {
        aprOpenMainLobby = new AprOpenMainLobby(this);
        aprSendQuickMatchRequest = new AprSendQuickMatchRequest(this);
        aprShowMatchLobby = new AprShowMatchLobby(this);
        aprShowCustomMatches = new AprShowCustomMatches(this);
        aprStartNetMatch = new AprStartNetMatch(this);
        aprFinishNetMatch = new AprFinishNetMatch(this);
        aprServerEvent = new AprServerEvent(this);
        aprShowRecentlyLaunchedMatches = new AprShowRecentlyLaunchedMatches(this);
        aprJoinAsSpectator = new AprJoinAsSpectator(this);
        aprCompletionCheck = new AprCompletionCheck(this);
        aprModeratorPanel = new AprModeratorPanel(this);
        aprUploadProhibited = new AprUploadProhibited(this);
        aprOpenUserLevels = new AprOpenUserLevels(this);
        aprPlayUserLevel = new AprPlayUserLevel(this);
        aprCheckUserLevel = new AprCheckUserLevel(this);
        aprCheckReport = new AprCheckReport(this);
        aprHintProfile = new AprHintProfile(this);
        aprRejoinMatch = new AprRejoinMatch(this);
        aprShowInGameToast = new AprShowInGameToast(this);
        aprOfflineMode = new AprOfflineMode(this);
        aprCheckRenaming = new AprCheckRenaming(this);
        aprHintTabsInCustomization = new AprHintTabsInCustomization(this);
        aprHintFreeFish = new AprHintFreeFish(this);
        aprMassCheckRenamings = new AprMassCheckRenamings(this);
        aprSendDuelRequest = new AprSendDuelRequest(this);
    }


    public void move() {
        moveReactions();
    }


    private void moveReactions() {
        for (AbstractPostponedReaction postponedReaction : reactions) {
            postponedReaction.move();
        }
    }


    public void suspendAllReactions() {
        for (AbstractPostponedReaction postponedReaction : reactions) {
            postponedReaction.suspend();
        }
    }


    public void onNetWaitSceneInitialized(AbstractNetWaitScene abstractNetWaitScene) {
        netWaitScenes.add(abstractNetWaitScene);
    }
}
