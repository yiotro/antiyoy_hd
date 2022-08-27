package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.multi_button.TemporaryMbeItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneVerificationPauseMenu extends AbstractPauseMenu{

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected TemporaryMbeItem[] getMbeItems() {
        return new TemporaryMbeItem[]{
                new TemporaryMbeItem("resume", BackgroundYio.green, getResumeReaction()),
                new TemporaryMbeItem("main_lobby", BackgroundYio.red, getQuitReaction()),
        };
    }


    private Reaction getQuitReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.mainLobby.create();
                netRoot.sendMessage(NmType.cancel_verification, "");
            }
        };
    }
}
