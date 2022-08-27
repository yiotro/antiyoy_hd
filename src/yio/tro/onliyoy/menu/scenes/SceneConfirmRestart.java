package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.game.general.RestartManager;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneConfirmRestart extends AbstractConfirmationScene{

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
                (new RestartManager(gameController)).apply();
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        return "confirm_restart";
    }


    @Override
    protected int getCountDown() {
        return 0;
    }
}
