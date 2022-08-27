package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.AnimationYio;

public class SceneModInstruction extends ModalSceneYio{

    @Override
    protected void initialize() {
        createCloseButton();
        createAnnounceView();
    }


    private void createAnnounceView() {
        double h = 0.9;
        uiFactory.getAnnounceViewElement()
                .setSize(0.95, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAnimation(AnimationYio.down)
                .setTitle("instruction")
                .setText("moderator_how_to");
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
