package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AprShowRecentlyLaunchedMatches extends AbstractPostponedReaction{

    public AprShowRecentlyLaunchedMatches(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return !areNetWaitScenesMovingCurrently();
    }


    @Override
    void apply() {
        Scenes.chooseMatchToSpectate.create();
    }
}
