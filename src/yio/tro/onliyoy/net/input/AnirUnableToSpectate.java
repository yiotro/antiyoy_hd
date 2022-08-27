package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;

public class AnirUnableToSpectate extends AbstractNetInputReaction{

    @Override
    public void apply() {
        PostponedReactionsManager.aprShowRecentlyLaunchedMatches.launch();
    }
}
