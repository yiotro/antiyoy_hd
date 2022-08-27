package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;

public class AnirRejoinedMatch extends AbstractNetInputReaction{

    @Override
    public void apply() {
        PostponedReactionsManager.aprServerEvent.clearBuffer();
        if (!isCurrentSceneValid()) return;
        PostponedReactionsManager.aprRejoinMatch.setBattleDataCode(value);
        PostponedReactionsManager.aprRejoinMatch.launch();
    }


    private boolean isCurrentSceneValid() {
        if (Scenes.waitForRejoin.isCurrentlyVisible()) return true;
        if (Scenes.entry.isCurrentlyVisible()) return true;
        return false;
    }
}
