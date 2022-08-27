package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.net.CoinsElpViewElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NetMatchResults;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneQmfResults extends ModalSceneYio{

    private ButtonYio mainView;
    private LabelElement label;
    private CoinsElpViewElement coinsElpViewElement;

    // qmf = quick match finished


    @Override
    protected void initialize() {
        createMainView();
        createLabel();
        createCoinsElpElement();
        createDelayedAction();
    }


    private void createDelayedAction() {
        uiFactory.getDelayedActionElement()
                .setSize(0.01)
                .setDelay(2000)
                .setReaction(new Reaction() {
                    @Override
                    protected void apply() {
                        destroy();
                    }
                });
    }


    private void createCoinsElpElement() {
        coinsElpViewElement = uiFactory.getCoinsElpViewElement()
                .setParent(mainView)
                .setSize(0.01)
                .alignUnder(previousElement, 0.025)
                .alignLeft(0.03);
    }


    private void createLabel() {
        label = uiFactory.getLabelElement()
                .setParent(mainView)
                .setSize(0.01)
                .alignLeft(0.03)
                .alignTop(0.02)
                .setFont(Fonts.miniFont)
                .setLeftAlignEnabled(true)
                .setTitle(languagesManager.getString("recent_match_finished"));
    }


    private void createMainView() {
        mainView = uiFactory.getButton()
                .setSize(1.02, 0.085)
                .centerHorizontal()
                .alignTop(-0.001)
                .setCornerRadius(0)
                .setAlphaEnabled(false)
                .setAnimation(AnimationYio.up)
                .setTouchable(false)
                .setAppearParameters(MovementType.inertia, 2)
                .setDestroyParameters(MovementType.inertia, 2);
    }


    public void setNetMatchResults(NetMatchResults netMatchResults) {
        coinsElpViewElement.update(netMatchResults);
    }
}
