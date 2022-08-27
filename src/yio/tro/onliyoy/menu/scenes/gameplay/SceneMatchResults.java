package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.PlatformType;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.campaign.CampaignManager;
import yio.tro.onliyoy.game.core_model.EntitiesManager;
import yio.tro.onliyoy.game.core_model.EntityType;
import yio.tro.onliyoy.game.core_model.MatchResults;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.save_system.SaveType;
import yio.tro.onliyoy.game.save_system.SavesManager;
import yio.tro.onliyoy.game.tutorial.TutorialManager;
import yio.tro.onliyoy.game.tutorial.TutorialType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.elements.resizable_element.ResizableViewElement;
import yio.tro.onliyoy.menu.elements.resizable_element.RveEmptyItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveTextItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveWinnerItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetMatchStatisticsData;
import yio.tro.onliyoy.net.shared.NetUlTransferData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneMatchResults extends SceneYio {

    MatchResults matchResults;
    private ResizableViewElement rvElement;
    private ButtonYio saveReplayButton;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getOpenSceneReaction(Scenes.mainLobby));
        createRvElement();
        createSaveReplayButton();
    }


    private void createSaveReplayButton() {
        double vOffset = 0.042;
        saveReplayButton = uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignRight(GraphicsYio.convertToWidth(vOffset))
                .alignTop(vOffset)
                .setTouchOffset(0.05)
                .loadCustomTexture("menu/icons/save.png")
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.up)
                .setSelectionTexture(getSelectionTexture())
                .setReaction(getSaveReplayReaction());
    }


    private Reaction getSaveReplayReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onSaveReplayButtonPressed();
            }
        };
    }


    private void onSaveReplayButtonPressed() {
        Scenes.keyboard.create();
        Scenes.keyboard.setHint("replay_name");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) return;
                getSavesManager().addItem(SaveType.replay, input, performExportForReplay());
                saveReplayButton.destroy();
            }
        });
    }


    private String performExportForReplay() {
        ObjectsLayer objectsLayer = getObjectsLayer();
        GameController gameController = getGameController();
        ExportParameters instance = ExportParameters.getInstance();
        instance.setCameraCode(gameController.cameraController.encode());
        instance.setInitialLevelSize(gameController.sizeManager.initialLevelSize);
        instance.setCoreModel(objectsLayer.viewableModel);
        instance.setHistoryManager(objectsLayer.historyManager);
        return objectsLayer.exportManager.perform(instance);
    }


    private void createRvElement() {
        rvElement = uiFactory.getResizableViewElement()
                .setSize(0.8, 0.01)
                .centerHorizontal()
                .setAnimation(AnimationYio.from_touch)
                .alignBottom(0.45);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        rvElement.clearItems();
        rvElement.clearButtons();

        rvElement.addButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignBottom(0.015)
                .alignLeft(0.03)
                .setIcon(GraphicsYio.loadTextureRegion("menu/icons/stats.png", true))
                .setKey("stats")
                .setReaction(getStatsReaction());
    }


    private void addOkButton() {
        switch (getGameController().gameMode) {
            default:
                if (!isNextButtonAllowed()) break;
                rvElement.addButton()
                        .setSize(0.25, 0.05)
                        .alignBottom(0.015)
                        .alignRight(0.03)
                        .setTitle("next")
                        .setReaction(getNextReaction());
                break;
            case completion_check:
                rvElement.addButton()
                        .setSize(0.45, 0.05)
                        .alignBottom(0.015)
                        .alignRight(0.03)
                        .setTitle("send_level")
                        .setReaction(getSendLevelReaction());
                break;
        }
    }


    private boolean isNextButtonAllowed() {
        switch (getGameController().gameMode) {
            default:
                return true;
            case campaign:
                return isWinnerHuman();
        }
    }


    private Reaction getNextReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onNextButtonPressed();
            }
        };
    }


    private void onNextButtonPressed() {
        switch (matchResults.gameMode) {
            default:
                MenuSwitcher.getInstance().createChooseGameModeMenu();
                break;
            case calendar:
                Scenes.calendar.create();
                break;
            case tutorial:
                onPressedNextAfterTutorial();
                break;
            case campaign:
                CampaignManager instance = CampaignManager.getInstance();
                int nextLevelIndex = instance.getNextLevelIndex(instance.currentLevelIndex);
                if (nextLevelIndex == -1) {
                    Scenes.campaign.create();
                    break;
                }
                instance.launchCampaignLevel(nextLevelIndex);
                break;
        }
    }


    private void onPressedNextAfterTutorial() {
        TutorialManager tutorialManager = getGameController().tutorialManager;
        switch (tutorialManager.currentType) {
            default:
            case basics:
                tutorialManager.launch(TutorialType.tactics);
                break;
            case tactics:
                tutorialManager.launch(TutorialType.diplomacy);
                break;
            case diplomacy:
                MenuSwitcher.getInstance().createChooseGameModeMenu();
                break;
        }
    }


    private Reaction getSendLevelReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onSendLevelButtonPressed();
            }
        };
    }


    private void onSendLevelButtonPressed() {
        if (matchResults.entityType != EntityType.human) {
            Scenes.toast.show("win_to_send");
            return;
        }
        Scenes.keyboard.create();
        Scenes.keyboard.setHint("level_name");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) {
                    Scenes.notification.show("level_name_is_empty");
                    return;
                }
                netRoot.tempUlTransferData.name = input;
                checkToRemoveNicknameFromLevelName(netRoot.tempUlTransferData);
                if (netRoot.tempUlTransferData.name.length() == 0) {
                    Scenes.notification.show(input + "?");
                    return;
                }
                netRoot.sendMessage(NmType.add_user_level, netRoot.tempUlTransferData.encode());
                MenuSwitcher.getInstance().createChooseGameModeMenu();
                Scenes.notification.show("level_sent");
            }
        });
    }


    void checkToRemoveNicknameFromLevelName(NetUlTransferData tempUlTransferData) {
        String levelName = tempUlTransferData.name;
        String nickname = netRoot.userData.name;
        int index = levelName.toLowerCase().indexOf(nickname.toLowerCase());
        if (index == -1) return;
        String hook = levelName.substring(index, index + nickname.length());
        String fixedName = levelName.replace(hook, "");
        fixedName = suppressWhitespaces(fixedName);
        tempUlTransferData.name = Yio.getCapitalizedString(fixedName);
    }


    String suppressWhitespaces(String string) {
        while (string.length() > 0 && string.charAt(0) == ' ') {
            string = string.substring(1);
        }
        while (string.length() > 0 && string.charAt(string.length() - 1) == ' ') {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }


    private Reaction getStatsReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                doShowStatistics();
            }
        };
    }


    void doShowStatistics() {
        rvElement.deactivateButton("stats");
        rvElement.deactivateItem("def_blank");
        rvElement.addItem(new RveEmptyItem(0.03));

        RveTextItem titleItem = new RveTextItem();
        titleItem.setFont(Fonts.gameFont);
        titleItem.setTitle("statistics");
        titleItem.setHeight(0.04);
        rvElement.addItem(titleItem);

        NetMatchStatisticsData statisticsData = matchResults.statisticsData;
        addRveTextItem(languagesManager.getString("rules") + ": " + LanguagesManager.getInstance().getString("" + matchResults.rulesType));
        addRveTextItem(languagesManager.getString("level_size") + ": " + LanguagesManager.getInstance().getString("" + matchResults.levelSize));
        addRveTextItem(languagesManager.getString("turns_made") + ": " + Yio.getCompactValueString(statisticsData.turnsMade));
        addRveTextItem(languagesManager.getString("units_built") + ": " + Yio.getCompactValueString(statisticsData.unitsBuilt));
        addRveTextItem(languagesManager.getString("units_merged") + ": " + Yio.getCompactValueString(statisticsData.unitsMerged));
        addRveTextItem(languagesManager.getString("units_died") + ": " + Yio.getCompactValueString(statisticsData.unitsDied));
        addRveTextItem(languagesManager.getString("money_spent") + ": $" + Yio.getCompactValueString(statisticsData.moneySpent));
        addRveTextItem(languagesManager.getString("trees_felled") + ": " + Yio.getCompactValueString(statisticsData.treesFelled));
        addRveTextItem(languagesManager.getString("max_profit") + ": +$" + Yio.getCompactValueString(statisticsData.maxProfit));
        addRveTextItem(languagesManager.getString("first_attack") + ": " + Yio.getCompactValueString(statisticsData.firstAttackLap));

        rvElement.addItem(new RveEmptyItem(0.08));
    }


    private void addRveTextItem(String string) {
        RveTextItem rveTextItem = new RveTextItem();
        rveTextItem.setTitle(string);
        rveTextItem.setHeight(0.035);
        rvElement.addItem(rveTextItem);
    }


    public void setMatchResults(MatchResults matchResults) {
        this.matchResults = matchResults;
        updateRvElement();
        addOkButton();
    }


    private void updateRvElement() {
        rvElement.clearItems();

        switch (matchResults.gameMode) {
            default:
                RveWinnerItem rveWinnerItem = new RveWinnerItem();
                rveWinnerItem.setColor(matchResults.winnerColor);
                rveWinnerItem.setEntityType(matchResults.entityType);
                rvElement.addItem(rveWinnerItem);
                break;
            case user_level:
                if (isWinnerHuman()) {
                    addRveTextItem("user_level_completed");
                    addRatingButtons();
                } else {
                    addRveTextItem("not_completed");
                }
                break;
        }

        RveEmptyItem defBlankItem = new RveEmptyItem(0.14);
        defBlankItem.setKey("def_blank");
        rvElement.addItem(defBlankItem);
    }


    private boolean isWinnerHuman() {
        return getViewableModel().entitiesManager.getEntity(matchResults.winnerColor).isHuman();
    }


    private void addRatingButtons() {
        if (YioGdxGame.platformType == PlatformType.ios) return;
        rvElement.addButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignTop(GraphicsYio.convertToHeight(0.01))
                .alignRight(0.03)
                .setIcon(GraphicsYio.loadTextureRegion("menu/user_levels/dislike.png", true))
                .setKey("dislike")
                .setReaction(getDislikeReaction());

        rvElement.addButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignTop(GraphicsYio.convertToHeight(0.01))
                .alignRight(0.13)
                .setIcon(GraphicsYio.loadTextureRegion("menu/user_levels/like.png", true))
                .setKey("like")
                .setReaction(getLikeReaction());
    }


    private Reaction getLikeReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                netRoot.sendMessage(NmType.like_level, "" + netRoot.tempUlTransferData.id);
                onLevelRated();
            }
        };
    }


    private Reaction getDislikeReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                netRoot.sendMessage(NmType.dislike_level, "" + netRoot.tempUlTransferData.id);
                onLevelRated();
            }
        };
    }


    void onLevelRated() {
        rvElement.deactivateButton("like");
        rvElement.deactivateButton("dislike");
        Scenes.notification.show("level_rated");
    }


    protected SavesManager getSavesManager() {
        return yioGdxGame.gameController.savesManager;
    }
}
