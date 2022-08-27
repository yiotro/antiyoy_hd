package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.StoreLinksYio;

public class SceneAlternativeUpdate extends ModalSceneYio{

    @Override
    protected void initialize() {
        createDefaultPanel(0.22);
        createAdvancedLabel();
        createDownloadButton();
    }


    private void createDownloadButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.5, 0.05)
                .setBackground(BackgroundYio.gray)
                .centerHorizontal()
                .alignBottom(0.02)
                .setTouchOffset(0.05)
                .applyText("load")
                .setReaction(getDownloadReaction());
    }


    private Reaction getDownloadReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                String link = StoreLinksYio.getInstance().getGoogleDriveLink();
                Gdx.app.getClipboard().setContents(link);
                Scenes.notification.show("link_copied_to_clipboard");
                Gdx.net.openURI(link);
            }
        };
    }


    private void createAdvancedLabel() {
        uiFactory.getAdvancedLabelElement()
                .setParent(defaultPanel)
                .setSize(0.9, 0.01)
                .centerHorizontal()
                .alignTop(0.03)
                .setFont(Fonts.miniFont)
                .applyText(languagesManager.getString("alternative_update"));
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
