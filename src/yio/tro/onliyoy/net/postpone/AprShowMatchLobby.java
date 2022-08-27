package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetMatchLobbyData;

public class AprShowMatchLobby extends AbstractPostponedReaction{

    NetMatchLobbyData netMatchLobbyData;


    public AprShowMatchLobby(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return !areNetWaitScenesMovingCurrently();
    }


    @Override
    void apply() {
        Scenes.matchLobby.setNetMatchLobbyData(netMatchLobbyData);
        Scenes.matchLobby.create();
    }


    public void setNetMatchLobbyData(NetMatchLobbyData netMatchLobbyData) {
        this.netMatchLobbyData = netMatchLobbyData;
    }
}
