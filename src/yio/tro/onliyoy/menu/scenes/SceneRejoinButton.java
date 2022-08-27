package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneRejoinButton extends ModalSceneYio{

    boolean yourTurn;


    @Override
    protected void initialize() {
        uiFactory.getButton()
                .setSize(0.8, 0.055)
                .centerHorizontal()
                .alignBottom(0.03)
                .setAnimation(AnimationYio.down)
                .setBackground(BackgroundYio.green)
                .applyText("return_to_match_verb")
                .setAllowedToAppear(getNormalButtonCondition())
                .setReaction(getReturnToMatchReaction());

        String string = languagesManager.getString("return_to_match_verb") + " (" + languagesManager.getString("your_turn") + ")";
        uiFactory.getButton()
                .setSize(0.8, 0.055)
                .centerHorizontal()
                .alignBottom(0.03)
                .setAnimation(AnimationYio.down)
                .setBackground(BackgroundYio.red)
                .applyText(string)
                .setAllowedToAppear(getYourTurnButtonCondition())
                .setReaction(getReturnToMatchReaction());
    }


    private ConditionYio getYourTurnButtonCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return yourTurn;
            }
        };
    }


    private ConditionYio getNormalButtonCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return !yourTurn;
            }
        };
    }


    private Reaction getReturnToMatchReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.rejoinWorker.onReturnToMatchButtonPressed();
            }
        };
    }


    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
