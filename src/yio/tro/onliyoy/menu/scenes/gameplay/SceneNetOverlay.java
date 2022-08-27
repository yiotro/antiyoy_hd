package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.gameplay.NetChatViewElement;
import yio.tro.onliyoy.menu.elements.gameplay.NetTurnViewElement;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetPhraseData;

public class SceneNetOverlay extends ModalSceneYio {

    public NetTurnViewElement netTurnViewElement;
    public NetChatViewElement netChatViewElement;


    public SceneNetOverlay() {
        netTurnViewElement = null;
        netChatViewElement = null;
    }


    @Override
    protected void initialize() {
        createNetTurnView();
        createNetChatView();
    }


    private void createNetChatView() {
        netChatViewElement = uiFactory.getNetChatViewElement()
                .setSize(0.95, 0.1)
                .alignTop(0.075)
                .centerHorizontal()
                .setAnimation(AnimationYio.up)
                .setAllowedToAppear(getNetMatchCondition());
    }


    private void createNetTurnView() {
        netTurnViewElement = uiFactory.getNetTurnViewElement()
                .setSize(1, 1)
                .setAllowedToAppear(getNetMatchCondition());
    }


    public void onEndTurnEventApplied() {
        updateTurnView();
        if (Scenes.composeLetter.isCurrentlyVisible()) {
            Scenes.composeLetter.destroy();
        }
    }


    public void updateTurnView() {
        if (netTurnViewElement == null) return;
        if (netTurnViewElement.getFactor().isInDestroyState()) return;
        netTurnViewElement.update();
    }


    public void onPhraseReceived(NetPhraseData netPhraseData) {
        if (netChatViewElement == null) return;
        netChatViewElement.onPhraseReceived(netPhraseData);
    }


    public void onNetChatMessageReceived(String value) {
        if (netChatViewElement == null) return;
        netChatViewElement.addMessage(value);
    }


    private ConditionYio getNetMatchCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return getViewableModel().isNetMatch();
            }
        };
    }
}
