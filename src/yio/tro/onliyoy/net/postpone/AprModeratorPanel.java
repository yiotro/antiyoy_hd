package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AprModeratorPanel extends AbstractPostponedReaction{

    public AprModeratorPanel(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return !areNetWaitScenesMovingCurrently();
    }


    @Override
    void apply() {
        Scenes.moderator.create();
    }
}
