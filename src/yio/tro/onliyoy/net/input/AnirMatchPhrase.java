package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetPhraseData;

public class AnirMatchPhrase extends AbstractNetInputReaction{

    @Override
    public void apply() {
        NetPhraseData netPhraseData = new NetPhraseData();
        netPhraseData.decode(value);
        if (Scenes.netOverlay.isCurrentlyVisible()) {
            Scenes.netOverlay.onPhraseReceived(netPhraseData);
        }
        if (Scenes.matchLobby.isCurrentlyVisible()) {
            Scenes.matchLobby.onPhraseReceived(netPhraseData);
        }
    }
}
