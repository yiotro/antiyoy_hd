package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.multi_button.TemporaryMbeItem;

public class SceneReplayPauseMenu extends AbstractPauseMenu{

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    protected TemporaryMbeItem[] getMbeItems() {
        return new TemporaryMbeItem[]{
                new TemporaryMbeItem("resume", BackgroundYio.yellow, getResumeReaction()),
                new TemporaryMbeItem("main_lobby", BackgroundYio.red, getOpenSceneReaction(Scenes.mainLobby)),
        };
    }
}
