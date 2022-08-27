package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.OneTimeInfo;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetExperienceManager;
import yio.tro.onliyoy.net.shared.NetRejoinMatchData;

import java.util.ArrayList;

public class AnirRejoinList extends AbstractNetInputReaction{

    ArrayList<NetRejoinMatchData> list;


    public AnirRejoinList() {
        list = new ArrayList<>();
    }


    @Override
    public void apply() {
        if (value.equals("-")) {
            checkForHowToSupportButton();
            return;
        }
        updateList();
        yioGdxGame.rejoinWorker.onReceivedRejoinList(list);
        if (Scenes.mainLobby.isCurrentlyVisible()) {
            Scenes.rejoinButton.setYourTurn(isYourTurnInAtLeastOneMatch());
            Scenes.rejoinButton.create();
        }
    }


    private void checkForHowToSupportButton() {
        if (OneTimeInfo.getInstance().howToSupport) return;
        long hoursOnline = root.initialStatisticsData.getHoursOnline();
        if (hoursOnline < 4) return;
        Scenes.howToSupportButton.create();
    }


    private void updateList() {
        list.clear();
        for (String token : value.split(",")) {
            NetRejoinMatchData netRejoinMatchData = new NetRejoinMatchData();
            netRejoinMatchData.decode(token);
            list.add(netRejoinMatchData);
        }
    }


    private boolean isYourTurnInAtLeastOneMatch() {
        for (NetRejoinMatchData netRejoinMatchData : list) {
            if (netRejoinMatchData.yourTurn) return true;
        }
        return false;
    }
}
