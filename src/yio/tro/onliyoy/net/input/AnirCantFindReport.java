package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;

public class AnirCantFindReport extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.waitForReport.isCurrentlyVisible()) return;
        PostponedReactionsManager.aprModeratorPanel.launch();
    }
}
