package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRole;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.NetRejoinMatchData;
import yio.tro.onliyoy.net.shared.NmType;

import java.util.ArrayList;

public class RejoinWorker {

    YioGdxGame yioGdxGame;
    private final NetRoot netRoot;
    boolean ready;
    ArrayList<NetRejoinMatchData> rejoinList;


    public RejoinWorker(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        netRoot = yioGdxGame.netRoot;
        ready = true;
        rejoinList = new ArrayList<>();
    }


    public void onEnteredMainLobby() {
        if (!ready) return;
        NetRole role = netRoot.userData.role;
        if (role == null) return;
        if (!netRoot.isInLocalMode() && role.ordinal() < NetRole.normal.ordinal()) return;
        ready = false;
        netRoot.sendMessage(NmType.request_available_rejoin_list, "");
    }


    public void onReturnToMatchButtonPressed() {
        if (netRoot.offlineMode) return;
        if (rejoinList.size() == 0) {
            System.out.println("RejoinWorker.onReturnToMatchButtonPressed: empty list");
            return;
        }
        if (rejoinList.size() == 1) {
            applyRejoin(rejoinList.get(0).matchId);
            return;
        }
        Scenes.chooseMatchToRejoin.create();
    }


    public void applyRejoin(String matchId) {
        Scenes.waitForRejoin.create();
        netRoot.sendMessage(NmType.request_rejoin_match, matchId);
    }


    public void onReceivedRejoinList(ArrayList<NetRejoinMatchData> source) {
        updateRejoinList(source);
        if (rejoinList.size() > 0) {
            ready = true;
        }
    }


    private void updateRejoinList(ArrayList<NetRejoinMatchData> source) {
        rejoinList.clear();
        for (NetRejoinMatchData srcData : source) {
            NetRejoinMatchData netRejoinMatchData = new NetRejoinMatchData();
            netRejoinMatchData.setBy(srcData);
            rejoinList.add(netRejoinMatchData);
        }
    }


    public void setReady(boolean ready) {
        this.ready = ready;
    }


    public ArrayList<NetRejoinMatchData> getRejoinList() {
        return rejoinList;
    }
}
