package yio.tro.onliyoy.menu.scenes.options;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.PlatformType;
import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.menu.elements.*;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetOptionsData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneSettings extends SceneYio {

    AnnounceViewElement mainLabel;
    public CheckButtonYio chkSound;
    private ButtonYio langButton;
    public CheckButtonYio chkFullScreen;
    private SliderElement graphicsSlider;
    private ScrollableAreaYio scrollableAreaYio;
    private AnnounceViewElement otherLabel;
    private CheckButtonYio chkAutosave;
    private CheckButtonYio chkConfirmEndTurn;
    private CheckButtonYio chkWaterTexture;
    private CheckButtonYio chkLongTap;
    private CheckButtonYio chkDetailedInfo;
    private CheckButtonYio chkAlertAboutTurnStart;
    private CheckButtonYio chkAutomaticTurnEnd;
    private CircleButtonYio infoButton;
    private CheckButtonYio chkGrid;
    private CheckButtonYio chkLocalizeTime;
    private CheckButtonYio chkReconnection;
    private CheckButtonYio chkConstructionPanel;
    private CheckButtonYio chkHideStatistics;
    private CheckButtonYio chkGrayVoid;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    public void initialize() {
        createScrollableArea();
        createMainLabel();
        createOtherLabel();
        createTopCover();
        createBackButton();
        createInfoButton();
    }


    private void createTopCover() {
        uiFactory.getTopCoverElement()
                .setSize(1, 0.13)
                .alignTop(0)
                .setAnimation(AnimationYio.up)
                .setTargetScrollEngine(scrollableAreaYio.getScrollEngineYio())
                .setTitle("settings");
    }


    private void createBackButton() {
        spawnBackButton(new Reaction() {
            @Override
            protected void apply() {
                applyAndSave();
                Scenes.mainLobby.create();

                if (SettingsManager.getInstance().requestRestartApp) {
                    Scenes.notification.show("restart_app");
                }
            }
        });
    }


    void applyAndSave() {
        applyValues();
        SettingsManager.getInstance().saveValues();
    }


    private void createScrollableArea() {
        scrollableAreaYio = uiFactory.getScrollableAreaYio()
                .setSize(1, 0.85)
                .setAnimation(AnimationYio.none);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    @Override
    protected void onEndCreation() {
        super.onEndCreation();
        forceElementToTop(backButton);
        if (infoButton != null) {
            forceElementToTop(infoButton);
        }
    }


    private void createOtherLabel() {
        double h = 0.81;
        if (YioGdxGame.platformType == PlatformType.ios) {
            h -= 0.36;
        }
        otherLabel = uiFactory.getAnnounceViewElement()
                .setParent(scrollableAreaYio)
                .setSize(0.9, h)
                .alignUnder(mainLabel, 0.05)
                .setAnimation(AnimationYio.from_touch)
                .centerHorizontal()
                .setText(" ")
                .setTouchable(false);

        createOtherChecksButtons();
        createResetButton();
        createSkinButton();
    }


    private void createSkinButton() {
        uiFactory.getButton()
                .setParent(otherLabel)
                .setSize(0.4, 0.05)
                .alignRight(0.033)
                .alignBottom(0.01)
                .applyText("skins")
                .setAllowedToAppear(getSkinButtonCondition())
                .setReaction(getSkinReaction());
    }


    private Reaction getSkinReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                applyAndSave();
                Scenes.chooseLocalSkin.create();
            }
        };
    }


    private ConditionYio getSkinButtonCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return YioGdxGame.platformType == PlatformType.ios;
            }
        };
    }


    private void createOtherChecksButtons() {
        chkConfirmEndTurn = uiFactory.getCheckButton()
                .setParent(otherLabel)
                .setHeight(0.06)
                .setFont(Fonts.miniFont)
                .setName("confirm_end_turn")
                .alignTop(0.02);

        chkWaterTexture = uiFactory.getCheckButton()
                .clone(previousElement)
                .setName("water_texture")
                .alignUnder(previousElement, 0);

        chkLongTap = uiFactory.getCheckButton()
                .clone(previousElement)
                .setName("long_tap")
                .setToast("explanation_long_tap")
                .alignUnder(previousElement, 0);

        if (YioGdxGame.platformType != PlatformType.ios) {
            chkDetailedInfo = uiFactory.getCheckButton()
                    .clone(previousElement)
                    .setName("detailed_info")
                    .setToast("explanation_detailed_info")
                    .alignUnder(previousElement, 0);

            chkAlertAboutTurnStart = uiFactory.getCheckButton()
                    .clone(previousElement)
                    .setName("alert_about_turn_start")
                    .alignUnder(previousElement, 0);

            chkAutomaticTurnEnd = uiFactory.getCheckButton()
                    .clone(previousElement)
                    .setName("automatic_turn_end")
                    .setToast("explanation_automatic_turn_end")
                    .alignUnder(previousElement, 0);
        }

        chkGrid = uiFactory.getCheckButton()
                .clone(previousElement)
                .setName("grid")
                .alignUnder(previousElement, 0);

        chkLocalizeTime = uiFactory.getCheckButton()
                .clone(previousElement)
                .setName("localize_time")
                .setToast("explanation_localize_time")
                .alignUnder(previousElement, 0);

        if (YioGdxGame.platformType != PlatformType.ios) {
            chkReconnection = uiFactory.getCheckButton()
                    .clone(previousElement)
                    .setName("reconnection")
                    .setToast("explanation_reconnection")
                    .alignUnder(previousElement, 0);

            chkFullScreen = uiFactory.getCheckButton()
                    .clone(previousElement)
                    .setName("full_screen")
                    .alignUnder(previousElement, 0);

            chkHideStatistics = uiFactory.getCheckButton()
                    .clone(previousElement)
                    .setName("hide_statistics")
                    .setToast("explanation_hide_statistics")
                    .setReaction(getHideStatisticsReaction())
                    .alignUnder(previousElement, 0);
        }

        chkGrayVoid = uiFactory.getCheckButton()
                .clone(previousElement)
                .setName("gray_void")
                .alignUnder(previousElement, 0)
                .setReaction(getGrayVoidReaction());
    }


    private Reaction getGrayVoidReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                applyValues();
                yioGdxGame.gameView.loadTextures();
            }
        };
    }


    private Reaction getHideStatisticsReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                applyValues();
                sendCurrentOptionsToServer();
            }
        };
    }


    private void sendCurrentOptionsToServer() {
        NetOptionsData netOptionsData = new NetOptionsData();
        netOptionsData.setBy(SettingsManager.getInstance());
        netRoot.sendMessage(NmType.options, netOptionsData.encode());
    }


    private void createResetButton() {
        ButtonYio resetButton = uiFactory.getButton()
                .setParent(otherLabel)
                .setSize(0.4, 0.05)
                .centerHorizontal()
                .alignBottom(0.01)
                .applyText("reset")
                .setReaction(getResetReaction());

        if (YioGdxGame.platformType == PlatformType.ios) {
            resetButton.alignLeft(0.033);
        }
    }


    private Reaction getResetReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.confirmResetSettings.create();
            }
        };
    }


    private void createGraphicsSlider() {
        graphicsSlider = uiFactory.getSlider()
                .setParent(mainLabel)
                .centerHorizontal()
                .alignTop(0.11)
                .setTitle("graphics")
                .setPossibleValues(new String[]{"low", "medium", "high"});
    }


    private void createInfoButton() {
        if (YioGdxGame.platformType == PlatformType.ios) return;

        infoButton = uiFactory.getCircleButton()
                .setSize(GraphicsYio.convertToWidth(0.09))
                .alignRight(0.04)
                .alignTop(0.02)
                .setTouchOffset(0.05)
                .setAnimation(AnimationYio.up)
                .loadTexture("menu/info_icon.png")
                .setReaction(getInfoReaction());
    }


    private Reaction getInfoReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                applyAndSave();
                Scenes.aboutGame.create();
            }
        };
    }


    private void createLangButton() {
        langButton = uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.4, 0.05)
                .centerHorizontal()
                .alignBottom(0.01)
                .applyText("lang")
                .setReaction(getLanguageReaction());
    }


    private Reaction getLanguageReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                applyAndSave();
                Scenes.languages.create();
            }
        };
    }


    private void createMainLabel() {
        mainLabel = uiFactory.getAnnounceViewElement()
                .setParent(scrollableAreaYio)
                .setSize(0.9, 0.5)
                .alignTop(0)
                .setAnimation(AnimationYio.from_touch)
                .centerHorizontal()
                .setTitle("settings")
                .setText(" ")
                .setTouchable(false);

        createGraphicsSlider();
        createCheckButtons();
        createLangButton();
    }


    private void createCheckButtons() {
        chkSound = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .setHeight(0.06)
                .setFont(Fonts.miniFont)
                .setName("sound")
                .alignUnder(previousElement, 0.02)
                .setReaction(getSoundReaction());

        chkConstructionPanel = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .setHeight(0.06)
                .setFont(Fonts.miniFont)
                .setName("construction_panel")
                .alignUnder(previousElement, 0);

        chkAutosave = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .setHeight(0.06)
                .setFont(Fonts.miniFont)
                .setName("autosave")
                .alignUnder(previousElement, 0);
    }


    private Reaction getSoundReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                SettingsManager.getInstance().soundEnabled = chkSound.isChecked();
            }
        };
    }


    private void applyValues() {
        SettingsManager instance = SettingsManager.getInstance();
        instance.requestRestartApp = false;

        instance.soundEnabled = chkSound.isChecked();
        instance.graphicsQuality = graphicsSlider.getValueIndex();
        instance.autosave = chkAutosave.isChecked();
        instance.confirmToEndTurn = chkConfirmEndTurn.isChecked();
        instance.waterTexture = chkWaterTexture.isChecked();
        instance.longTap = chkLongTap.isChecked();
        instance.grid = chkGrid.isChecked();
        instance.localizeTime = chkLocalizeTime.isChecked();
        instance.constructionPanel = chkConstructionPanel.isChecked();
        instance.grayVoid = chkGrayVoid.isChecked();
        if (YioGdxGame.platformType != PlatformType.ios) {
            instance.fullScreenMode = chkFullScreen.isChecked();
            instance.detailedUserLevels = chkDetailedInfo.isChecked();
            instance.alertTurnStart = chkAlertAboutTurnStart.isChecked();
            instance.automaticTurnEnd = chkAutomaticTurnEnd.isChecked();
            instance.reconnection = chkReconnection.isChecked();
            instance.hideStatistics = chkHideStatistics.isChecked();
        }

        instance.onValuesChanged();
    }


    public void loadValues() {
        SettingsManager instance = SettingsManager.getInstance();

        chkSound.setChecked(instance.soundEnabled);
        graphicsSlider.setValueIndex(instance.graphicsQuality);
        chkAutosave.setChecked(instance.autosave);
        chkConfirmEndTurn.setChecked(instance.confirmToEndTurn);
        chkWaterTexture.setChecked(instance.waterTexture);
        chkLongTap.setChecked(instance.longTap);
        chkGrid.setChecked(instance.grid);
        chkLocalizeTime.setChecked(instance.localizeTime);
        chkConstructionPanel.setChecked(instance.constructionPanel);
        chkGrayVoid.setChecked(instance.grayVoid);
        if (YioGdxGame.platformType != PlatformType.ios) {
            chkFullScreen.setChecked(instance.fullScreenMode);
            chkDetailedInfo.setChecked(instance.detailedUserLevels);
            chkAlertAboutTurnStart.setChecked(instance.alertTurnStart);
            chkAutomaticTurnEnd.setChecked(instance.automaticTurnEnd);
            chkReconnection.setChecked(instance.reconnection);
            chkHideStatistics.setChecked(instance.hideStatistics);
        }
    }

}
