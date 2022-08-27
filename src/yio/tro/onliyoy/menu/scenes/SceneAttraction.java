package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.StoreLinksYio;

public class SceneAttraction extends ModalSceneYio{


    private AnnounceViewElement announceViewElement;


    @Override
    protected void initialize() {
        createDarken();
        createCloseButton();
        createAnnounceView();
        createDownloadButton();
    }


    private void createDownloadButton() {
        uiFactory.getButton()
                .setParent(announceViewElement)
                .setSize(0.66, 0.05)
                .centerHorizontal()
                .alignBottom(0.02)
                .setTouchOffset(0.03)
                .setBackground(BackgroundYio.gray)
                .applyText("load")
                .setReaction(getDownloadReaction());
    }


    private Reaction getDownloadReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Gdx.net.openURI(StoreLinksYio.getInstance().getLink("shproty"));
            }
        };
    }


    private void createAnnounceView() {
        double h = 0.9;
        String article = languagesManager.getString("shproty_release");
        announceViewElement = uiFactory.getAnnounceViewElement()
                .setSize(0.95, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAnimation(AnimationYio.down)
                .setTitle("new_game")
                .setText(article + "# # ");
    }
}
