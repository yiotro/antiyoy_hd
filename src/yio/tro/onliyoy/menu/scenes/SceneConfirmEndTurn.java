package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneConfirmEndTurn extends AbstractConfirmationScene{

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
                getObjectsLayer().onPlayerRequestedToEndTurn();
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        return "confirm_end_turn_question";
    }


    @Override
    protected int getCountDown() {
        return 0;
    }
}
