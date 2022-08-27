package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;

public class AnirToast extends AbstractNetInputReaction{

    @Override
    public void apply() {
        PostponedReactionsManager.aprShowInGameToast.launch();
        PostponedReactionsManager.aprShowInGameToast.setKey(value);
    }
}
