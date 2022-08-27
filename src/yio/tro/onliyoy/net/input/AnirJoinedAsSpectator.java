package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;

public class AnirJoinedAsSpectator extends AbstractNetInputReaction{

    @Override
    public void apply() {
        PostponedReactionsManager.aprServerEvent.clearBuffer();
        if (!Scenes.waitMatchJoining.isCurrentlyVisible()) return;
        PostponedReactionsManager.aprJoinAsSpectator.setBattleDataCode(value);
        PostponedReactionsManager.aprJoinAsSpectator.launch();
    }
}
