package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;

public class AnirPlayUserLevel extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.waitToPlayUserLevel.isCurrentlyVisible()) return;
        PostponedReactionsManager.aprPlayUserLevel.launch();
        PostponedReactionsManager.aprPlayUserLevel.setValue(value);
    }
}
