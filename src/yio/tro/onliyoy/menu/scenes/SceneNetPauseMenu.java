package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.multi_button.TemporaryMbeItem;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneNetPauseMenu extends AbstractPauseMenu {

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected TemporaryMbeItem[] getMbeItems() {
        return new TemporaryMbeItem[]{
                new TemporaryMbeItem("resume", BackgroundYio.green, getResumeReaction()),
                new TemporaryMbeItem("exit_match", BackgroundYio.red, getExitMatchReaction()),
        };
    }


    private Reaction getExitMatchReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.confirmExitMatch.create();
            }
        };
    }
}
