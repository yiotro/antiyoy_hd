package yio.tro.onliyoy.menu.scenes.gameplay;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.net.NetProcessViewElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;

public class SceneWaitForServerSync extends ModalSceneYio {

    public NetProcessViewElement netProcessViewElement;


    @Override
    protected void initialize() {
        createBlocker();
        createNetProcessView();
    }


    private void createNetProcessView() {
        netProcessViewElement = uiFactory.getNetProcessViewElement()
                .setSize(0.01)
                .centerHorizontal()
                .centerVertical()
                .setBackgroundEnabled(true)
                .setTitle("-");
    }


    private void createBlocker() {
        uiFactory.getButton()
                .setSize(1, 1)
                .centerHorizontal()
                .centerVertical()
                .setAnimation(AnimationYio.none)
                .loadCustomTexture("pixels/blocker.png")
                .setReaction(getEmptyReaction());
    }


    private Reaction getEmptyReaction() {
        return new Reaction() {
            @Override
            protected void apply() {

            }
        };
    }


    public void setKey(String key) {
        netProcessViewElement.setTitle(key);
    }
}
