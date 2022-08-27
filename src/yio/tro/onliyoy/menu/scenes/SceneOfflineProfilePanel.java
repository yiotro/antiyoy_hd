package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneOfflineProfilePanel extends ModalSceneYio{

    private ButtonYio mainLabel;


    @Override
    protected void initialize() {
        createCloseButton();
        createMainLabel();
        createSkinsButton();
    }


    private void createSkinsButton() {
        uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.5, 0.05)
                .centerHorizontal()
                .alignTop(0.03)
                .applyText("skins")
                .setReaction(getSkinsReaction());
    }


    private Reaction getSkinsReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.chooseLocalSkin.create();
            }
        };
    }


    private void createMainLabel() {
        mainLabel = uiFactory.getButton()
                .setSize(1.02, 0.11)
                .centerHorizontal()
                .alignTop(-0.001)
                .setCornerRadius(0)
                .setAlphaEnabled(false)
                .setAnimation(AnimationYio.up)
                .setSilentReactionMode(true)
                .setAppearParameters(MovementType.inertia, 1.5)
                .setDestroyParameters(MovementType.inertia, 1.5);
    }
}
