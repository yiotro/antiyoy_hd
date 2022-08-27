package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AnirKickedForTwoTurnSkip extends AbstractNetInputReaction{

    @Override
    public void apply() {
        yioGdxGame.applyFullTransitionToUI();
        Scenes.kickedForTwoTurnSkip.create();
    }
}
