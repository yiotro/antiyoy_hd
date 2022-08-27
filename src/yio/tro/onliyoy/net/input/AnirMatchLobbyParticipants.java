package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirMatchLobbyParticipants extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.matchLobby.isCurrentlyVisible()) return;
        Scenes.matchLobby.onListCodeReceived(value);
    }
}
