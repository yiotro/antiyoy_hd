package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AprOpenUserLevels extends AbstractPostponedReaction{

    public AprOpenUserLevels(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return !areNetWaitScenesMovingCurrently();
    }


    @Override
    void apply() {
        Scenes.userLevels.create();
    }
}
