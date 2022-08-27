package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirRecentlyLaunchedMatches extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.chooseMatchToSpectate.isCurrentlyVisible()) return;
        Scenes.chooseMatchToSpectate.onCodeReceived(value);
    }
}
