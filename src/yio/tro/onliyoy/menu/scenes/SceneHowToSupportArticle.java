package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.OneTimeInfo;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneHowToSupportArticle extends ModalSceneYio{


    private AnnounceViewElement announceViewElement;


    @Override
    protected void initialize() {
        createCloseButton();
        createAnnounceView();
        createCustomizationButton();
    }


    private void createCustomizationButton() {
        uiFactory.getButton()
                .setParent(announceViewElement)
                .setSize(0.66, 0.05)
                .centerHorizontal()
                .alignBottom(0.02)
                .setTouchOffset(0.03)
                .setBackground(BackgroundYio.gray)
                .applyText("customization")
                .setReaction(getCustomizationReaction());
    }


    private Reaction getCustomizationReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Scenes.shop.create();
            }
        };
    }


    private void createAnnounceView() {
        double h = 0.9;
        String article = languagesManager.getString("how_to_support_developer_article");
        announceViewElement = uiFactory.getAnnounceViewElement()
                .setSize(0.95, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAnimation(AnimationYio.down)
                .setTitle("how_to_support_developer")
                .setText(article + "# # ");
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        OneTimeInfo.getInstance().howToSupport = true;
        OneTimeInfo.getInstance().save();
    }
}
