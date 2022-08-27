package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneHowToSupportButton extends ModalSceneYio{

    @Override
    protected void initialize() {
        uiFactory.getButton()
                .setSize(0.9, 0.055)
                .centerHorizontal()
                .alignBottom(0.03)
                .setTouchOffset(0.04)
                .setAnimation(AnimationYio.down)
                .setBackground(BackgroundYio.cyan)
                .applyText("how_to_support_developer")
                .setReaction(getReaction());
    }


    private Reaction getReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Scenes.howToSupportArticle.create();
            }
        };
    }
}
