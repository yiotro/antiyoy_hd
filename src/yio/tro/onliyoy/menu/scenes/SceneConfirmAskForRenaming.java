package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneConfirmAskForRenaming extends AbstractConfirmationScene{

    public String nickname;


    @Override
    protected Reaction getNoReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Scenes.normalProfilePanel.create();
            }
        };
    }


    @Override
    protected Reaction getYesReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                netRoot.sendMessage(NmType.ask_for_renaming, nickname);
                Scenes.notification.show("rename_request_sent");
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        return LanguagesManager.getInstance().getString("rename_to") + " '" + nickname + "'?";
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
