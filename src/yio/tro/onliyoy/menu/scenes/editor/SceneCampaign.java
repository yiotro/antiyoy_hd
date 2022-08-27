package yio.tro.onliyoy.menu.scenes.editor;

import yio.tro.onliyoy.game.campaign.CampaignManager;
import yio.tro.onliyoy.game.campaign.Difficulty;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.*;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneCampaign extends SceneYio {

    public CustomizableListYio customizableListYio;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected void initialize() {
        createCampaignView();
        createScrollHelper();
        createBackButton();
    }


    private void createScrollHelper() {
        uiFactory.getScrollHelperElement()
                .setSize(1, 1)
                .setInverted(true)
                .setScrollEngineYio(customizableListYio.getScrollEngineYio());
    }


    private void createCampaignView() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(1, 1)
                .alignLeft(0)
                .alignBottom(0)
                .setCornerRadius(0)
                .setAnimation(AnimationYio.from_touch);
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();

        SectionStartListItem sectionStartListItem = new SectionStartListItem();
        sectionStartListItem.setValues(Difficulty.tutorial, true, 0.1f * GraphicsYio.height);
        customizableListYio.addItem(sectionStartListItem);

        addTutorialSection();

        int lastLevelIndex = CampaignManager.getInstance().getLastLevelIndex();
        int n = 1 + lastLevelIndex / CampaignCustomItem.ROW;
        Difficulty currentSectionDifficulty = Difficulty.values()[1];
        addSeparator(currentSectionDifficulty, false);

        for (int i = 0; i < n; i++) {
            int startIndex = i * CampaignCustomItem.ROW;
            int endIndex = Math.min((i + 1) * CampaignCustomItem.ROW - 1, lastLevelIndex);
            Difficulty startDifficulty = CampaignManager.getInstance().getDifficulty(startIndex);
            Difficulty endDifficulty = CampaignManager.getInstance().getDifficulty(endIndex);

            if (startDifficulty == currentSectionDifficulty) {
                addCampaignItem(startIndex, endIndex, currentSectionDifficulty);
            }

            if (endDifficulty != currentSectionDifficulty) {
                addSeparator(currentSectionDifficulty, true);
                currentSectionDifficulty = endDifficulty;
                addSeparator(currentSectionDifficulty, false);
                addCampaignItem(startIndex, endIndex, currentSectionDifficulty);
            }
        }

        addSeparator(currentSectionDifficulty, true);
    }


    private void addTutorialSection() {
        addSeparator(Difficulty.tutorial, false);
        TutorialListItem tutorialListItem = new TutorialListItem();
        customizableListYio.addItem(tutorialListItem);
    }


    private void addCampaignItem(int startIndex, int endIndex, Difficulty difficulty) {
        CampaignCustomItem campaignCustomItem = new CampaignCustomItem();
        campaignCustomItem.set(startIndex, endIndex);
        campaignCustomItem.setDifficulty(difficulty);
        customizableListYio.addItem(campaignCustomItem);
    }


    private void addSeparator(Difficulty difficulty, boolean empty) {
        float h = 0.05f * GraphicsYio.height;
        if (empty) {
            h = 0.025f * GraphicsYio.height;
        }
        SectionStartListItem sectionStartListItem = new SectionStartListItem();
        sectionStartListItem.setValues(difficulty, empty, h);
        customizableListYio.addItem(sectionStartListItem);
    }


    @Override
    protected void onEndCreation() {
        super.onEndCreation();
        scrollToTargetUnlockedLevel();
    }


    private void scrollToTargetUnlockedLevel() {
        customizableListYio.resetScroll();
        int index = CampaignManager.getInstance().getIndexOfTargetUnlockedLevel();
        CampaignCustomItem itemWithIndex = findItemWithIndex(index);
        if (itemWithIndex == null) return;
        customizableListYio.scrollToItem(itemWithIndex);
    }


    private CampaignCustomItem findItemWithIndex(int index) {
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!(item instanceof CampaignCustomItem)) continue;
            if (!((CampaignCustomItem) item).containsLevelIndex(index)) continue;
            return (CampaignCustomItem) item;
        }
        return null;
    }


    public void onLevelMarkedAsCompleted() {
        if (customizableListYio == null) return;
        loadValues();
    }


    private void createBackButton() {
        spawnBackButton(new Reaction() {
            @Override
            protected void apply() {
                MenuSwitcher.getInstance().createChooseGameModeMenu();
            }
        });
    }
}
