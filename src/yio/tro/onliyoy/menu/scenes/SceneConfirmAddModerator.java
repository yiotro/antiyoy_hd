package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneConfirmAddModerator extends AbstractConfirmationScene{

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
                netRoot.sendMessage(NmType.add_moderator, id);
                Scenes.admin.create(); // some short time should pass for moderator to be kicked
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        return "Add '" + name + "' as a moderator?";
    }


    @Override
    protected int getCountDown() {
        return 9;
    }


    public void setValues(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
