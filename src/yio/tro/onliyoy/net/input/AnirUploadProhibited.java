package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;

public class AnirUploadProhibited extends AbstractNetInputReaction{

    @Override
    public void apply() {
        if (!Scenes.waitCompletionCheckLoading.isCurrentlyVisible()) return;
        PostponedReactionsManager.aprUploadProhibited.launch();
    }
}
