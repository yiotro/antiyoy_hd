package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;

public class SceneOnSignedOut extends SceneYio{


    private ButtonYio mainLabel;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.orange;
    }


    @Override
    protected void initialize() {
        mainLabel = uiFactory.getButton()
                .setSize(0.9, 0.25)
                .centerVertical()
                .centerHorizontal()
                .setAnimation(AnimationYio.from_touch)
                .applyText(" ")
                .setTouchable(false);

        String nameRaw = netRoot.userData.name;
        String nameLocalized = CharLocalizerYio.getInstance().apply(nameRaw);
        uiFactory.getLabelElement()
                .setParent(mainLabel)
                .setSize(0.01, 0.06)
                .centerHorizontal()
                .alignTop(0.01)
                .setTitle(nameLocalized);

        uiFactory.getAdvancedLabelElement()
                .setParent(mainLabel)
                .setSize(0.85, 0.01)
                .centerHorizontal()
                .alignUnder(previousElement, 0.01)
                .setFont(Fonts.miniFont)
                .applyText(languagesManager.getString("you_signed_out"));

        uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.25, 0.05)
                .alignRight(0.03)
                .alignBottom(0.015)
                .applyText("quit")
                .setReaction(getQuitReaction());
    }


    private Reaction getQuitReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.exitApp();
            }
        };
    }
}
