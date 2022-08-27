package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneConfirmRemoveModerator extends AbstractConfirmationScene {

    String id;
    String name;


    @Override
    protected Reaction getNoReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
            }
        };
    }


    @Override
    protected Reaction getYesReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                netRoot.sendMessage(NmType.remove_moderator, id);
                netRoot.sendMessage(NmType.request_list_of_moderators, "");
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        return "Remove '" + name + "'?";
    }


    @Override
    protected int getCountDown() {
        return 15;
    }


    public void setValues(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
