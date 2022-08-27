package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.campaign.CampaignManager;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.elements.multi_button.TemporaryMbeItem;

public class SceneCampaignPauseMenu extends AbstractPauseMenu {


    private LabelElement labelElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    public void initialize() {
        super.initialize();
        createLevelIndexLabel();
    }


    private void createLevelIndexLabel() {
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
        int currentLevelIndex = CampaignManager.getInstance().currentLevelIndex;
        String prefix = languagesManager.getString("level");
        labelElement.setTitle(prefix + " " + currentLevelIndex);
    }


    @Override
    protected TemporaryMbeItem[] getMbeItems() {
        return new TemporaryMbeItem[]{
                new TemporaryMbeItem("resume", BackgroundYio.green, getResumeReaction()),
                new TemporaryMbeItem("restart", BackgroundYio.yellow, getOpenSceneReaction(Scenes.confirmRestart)),
                new TemporaryMbeItem("save", BackgroundYio.magenta, getOpenSceneReaction(Scenes.saveCampaign)),
                new TemporaryMbeItem("main_lobby", BackgroundYio.red, getOpenSceneReaction(Scenes.mainLobby)),
        };
    }

}
