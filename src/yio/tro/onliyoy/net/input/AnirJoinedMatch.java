package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.net.shared.NetMatchLobbyData;

public class AnirJoinedMatch extends AbstractNetInputReaction{

    @Override
    public void apply() {
        PostponedReactionsManager.aprServerEvent.clearBuffer();
        NetMatchLobbyData netMatchLobbyData = new NetMatchLobbyData();
        netMatchLobbyData.decode(value);
        PostponedReactionsManager.aprShowMatchLobby.setNetMatchLobbyData(netMatchLobbyData);
        PostponedReactionsManager.aprShowMatchLobby.launch();
    }
}
