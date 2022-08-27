package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;

public class AnirMatchLaunched extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.matchLobby.isCurrentlyVisible() && !Scenes.waitMatchLaunching.isCurrentlyVisible()) return;
        PostponedReactionsManager.aprStartNetMatch.setBattleDataCode(value);
        PostponedReactionsManager.aprStartNetMatch.launch();
    }
}
