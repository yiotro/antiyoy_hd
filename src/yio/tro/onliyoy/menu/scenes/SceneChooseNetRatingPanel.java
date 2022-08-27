package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NetRatingType;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneChooseNetRatingPanel extends ModalSceneYio{


    private ButtonYio mainView;


    @Override
    protected void initialize() {
        createCloseButton();
        createMainView();
        createLabel();
        createButtons();
    }


    private void createButtons() {
        double vDelta = 0.035;
        for (final NetRatingType netRatingType : NetRatingType.values()) {
            uiFactory.getButton()
                    .setParent(mainView)
                    .setSize(0.6, 0.05)
                    .centerHorizontal()
                    .alignUnder(previousElement, vDelta)
                    .applyText(getTitle(netRatingType))
                    .setReaction(getLeaderboardReaction(netRatingType));
            vDelta = 0.01;
        }
    }


    private Reaction getLeaderboardReaction(final NetRatingType netRatingType) {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.leaderboard.setNetRatingType(netRatingType);
                Scenes.leaderboard.create();
            }
        };
    }


    private String getTitle(NetRatingType netRatingType) {
        switch (netRatingType) {
            default:
                return languagesManager.getString("" + netRatingType);
            case elp:
                return "ELP";
        }
    }


    private void createLabel() {
        uiFactory.getLabelElement()
                .setParent(mainView)
                .setSize(0.01)
                .alignTop(0.03)
                .centerHorizontal()
                .setTitle(languagesManager.getString("ratings"));
    }


    private void createMainView() {
        mainView = uiFactory.getButton()
                .setSize(1.02, 0.26)
                .centerHorizontal()
                .alignTop(-0.001)
                .setCornerRadius(0)
                .setAlphaEnabled(false)
                .setAnimation(AnimationYio.up)
                .setSilentReactionMode(true)
                .setAppearParameters(MovementType.inertia, 1.6)
                .setDestroyParameters(MovementType.inertia, 1.6);
    }
}
