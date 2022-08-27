package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;

public class SceneTemporaryCurrentLapView extends ModalSceneYio {


    private LabelElement labelElement;


    @Override
    protected void initialize() {
        createLabel();
        createDelayedAction();
    }


    private void createDelayedAction() {
        uiFactory.getDelayedActionElement()
                .setSize(0.01)
                .setDelay(3000)
                .setReaction(new Reaction() {
                    @Override
                    protected void apply() {
                        destroy();
                    }
                });
    }


    private void createLabel() {
        labelElement = uiFactory.getLabelElement()
                .setSize(0.01)
                .alignRight(0.03)
                .alignBottom(0.02)
                .setBackgroundEnabled(true)
                .setRightAlignEnabled(true)
                .setAnimation(AnimationYio.down)
                .setFont(Fonts.miniFont)
                .setTitle("-");
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        updateLabel();
    }


    private void updateLabel() {
        String prefix = languagesManager.getString("turns_made");
        labelElement.setTitle(prefix + ": " + getViewableModel().turnsManager.lap);
    }
}
