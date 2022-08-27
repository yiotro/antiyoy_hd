package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.net.NetProcessViewElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneSearchingForDuel extends SceneYio{

    public NetProcessViewElement netProcessViewElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getBackReaction());
        netProcessViewElement = uiFactory.getNetProcessViewElement()
                .setSize(0.01)
                .centerHorizontal()
                .alignBottom(0.45)
                .setClockMode(true)
                .setTitle("searching_for_duel");
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                netRoot.sendMessage(NmType.cancel_duel, "");
                MenuSwitcher.getInstance().createChooseGameModeMenu();
            }
        };
    }


    @Override
    protected void onAppear() {
        PostponedReactionsManager.aprSendDuelRequest.launch();
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
