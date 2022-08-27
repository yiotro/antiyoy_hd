package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;

public class SceneTestResults extends SceneYio{

    public ButtonYio label;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.white;
    }


    public SceneTestResults() {

    }


    @Override
    protected void initialize() {
        spawnBackButton(getOpenSceneReaction(Scenes.debugTests));

        label = uiFactory.getButton()
                .setSize(0.8, 0.5)
                .centerHorizontal()
                .centerVertical()
                .applyText("")
                .setTouchable(false);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        yioGdxGame.applyBackground(BackgroundYio.gray);
        label.applyText("");
    }

}
