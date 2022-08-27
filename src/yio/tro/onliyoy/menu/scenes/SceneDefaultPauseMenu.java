package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.elements.multi_button.TemporaryMbeItem;

public class SceneDefaultPauseMenu extends AbstractPauseMenu {


    private LabelElement labelElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    public void initialize() {
        super.initialize();
        createSlotNameLabel();
    }


    private void createSlotNameLabel() {
        labelElement = uiFactory.getLabelElement()
                .setSize(0.01)
                .centerHorizontal()
                .alignTop(0.02)
                .setAnimation(AnimationYio.up)
                .setFont(Fonts.miniFont)
                .setTitle("-");
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        updateLabelElement();
    }


    private void updateLabelElement() {
        String pauseName = getViewableModel().pauseName;
        if (pauseName.length() < 2) {
            pauseName = "";
        }
        labelElement.setTitle(pauseName);
    }


    @Override
    protected TemporaryMbeItem[] getMbeItems() {
        return new TemporaryMbeItem[]{
                new TemporaryMbeItem("resume", BackgroundYio.green, getResumeReaction()),
                new TemporaryMbeItem("restart", BackgroundYio.yellow, getOpenSceneReaction(Scenes.confirmRestart)),
                new TemporaryMbeItem("save", BackgroundYio.magenta, getOpenSceneReaction(Scenes.saveToSlot)),
                new TemporaryMbeItem("main_lobby", BackgroundYio.red, getOpenSceneReaction(Scenes.mainLobby)),
        };
    }

}
