package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.OneTimeInfo;
import yio.tro.onliyoy.PlatformType;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.tutorial.TutorialType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuParams;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.CircleButtonYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.net.CoinsElpViewElement;
import yio.tro.onliyoy.menu.elements.net.NicknameViewElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.NetRole;
import yio.tro.onliyoy.net.shared.AvatarType;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneMainLobby extends SceneYio {

    public CircleButtonYio exitButton;
    public CircleButtonYio settingsButton;
    public CircleButtonYio playButton;
    private double verticalPosition;
    private double iconSize;
    private double playButtonSize;
    private ButtonYio logoButton;
    private double logoWidth;
    private double iconOffset;
    private double touchOffset;
    int secretCount;
    public NicknameViewElement nicknameViewElement;
    public CoinsElpViewElement coinsElpViewElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.yellow;
    }


    @Override
    public void initialize() {
        initMetrics();

        createRightButton();
        createSettingsButton();
        createPlayButton();
        createLogo();
        createNicknameElement();
        createCoinsElpElement();
    }


    private void createCoinsElpElement() {
        coinsElpViewElement = uiFactory.getCoinsElpViewElement()
                .setSize(0.01)
                .alignTop(GraphicsYio.convertToHeight(0.02))
                .alignLeft(0.02)
                .setAllowedToAppear(getCoinsElpCondition())
                .setAnimation(AnimationYio.up_then_fade)
                .setReaction(getCoinsElpReaction());
    }


    private Reaction getCoinsElpReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.chooseNetRatingPanel.create();
            }
        };
    }


    private ConditionYio getCoinsElpCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                if (netRoot.userData.role == NetRole.guest) return false;
                if (netRoot.offlineMode) return false;
                return true;
            }
        };
    }


    private void createNicknameElement() {
        nicknameViewElement = uiFactory.getNicknameViewElement()
                .setSize(0.01)
                .alignTop(GraphicsYio.convertToHeight(0.03))
                .alignRight(0.03)
                .setAnimation(AnimationYio.up_then_fade)
                .setAllowedToAppear(getNotIosCondition())
                .setReaction(getNicknameReaction());
    }


    private Reaction getNicknameReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onNicknameClicked();
            }
        };
    }


    private void onNicknameClicked() {
        if (netRoot.offlineMode) {
            Scenes.offlineProfilePanel.create();
            return;
        }
        switch (netRoot.userData.role) {
            default:
                break;
            case guest:
                Scenes.guestProfilePanel.create();
                break;
            case admin:
            case moderator:
            case normal:
                Scenes.normalProfilePanel.create();
                break;
        }
    }


    private void createRightButton() {
        if (YioGdxGame.platformType == PlatformType.ios) {
            createInfoButton();
            return;
        }
        createExitButton();
    }


    private void createInfoButton() {
        uiFactory.getCircleButton()
                .setSize(iconSize)
                .alignBottom(verticalPosition - GraphicsYio.convertToHeight(iconSize) / 2)
                .alignRight(iconOffset)
                .setTouchOffset(touchOffset)
                .setAnimation(AnimationYio.center)
                .loadTexture("menu/main_menu/help_icon.png")
                .setAppearParameters(MenuParams.ANIM_TYPE, 0.8 * MenuParams.ANIM_SPEED)
                .setReaction(getOpenSceneReaction(Scenes.aboutGame));
    }


    private void createLogo() {
        double logoHeight = GraphicsYio.convertToHeight(logoWidth) / 2;
        double logoY = 0.5;

        uiFactory.getButton()
                .setSize(0.7, GraphicsYio.convertToHeight(0.005))
                .centerHorizontal()
                .alignBottom(logoY + 0.01)
                .loadCustomTexture("menu/main_menu/black_line.png")
                .setAnimation(AnimationYio.center)
                .setAppearParameters(MenuParams.ANIM_TYPE, 0.9 * MenuParams.ANIM_SPEED)
                .setTouchable(false);

        logoButton = uiFactory.getButton()
                .setSize(logoWidth, logoHeight)
                .centerHorizontal()
                .alignBottom(logoY)
                .loadCustomTexture("menu/main_menu/mm_logo.png")
                .setAnimation(AnimationYio.center)
                .setTouchable(false);
    }


    private void createPlayButton() {
        playButton = uiFactory.getCircleButton()
                .loadTexture("menu/main_menu/play_button.png")
                .setPosition((1 - playButtonSize) / 2, verticalPosition - GraphicsYio.convertToHeight(playButtonSize) / 2)
                .setSize(playButtonSize)
                .setTouchOffset(touchOffset)
                .setHotkeyKeycode(Input.Keys.ENTER)
                .setAnimation(AnimationYio.center)
                .setReaction(getPlayReaction());
    }


    private Reaction getPlayReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onPlayButtonPressed();
            }
        };
    }


    private void createSettingsButton() {
        settingsButton = uiFactory.getCircleButton()
                .loadTexture("menu/main_menu/settings_icon.png")
                .clone(previousElement)
                .alignBottom(verticalPosition - GraphicsYio.convertToHeight(iconSize) / 2)
                .alignLeft(iconOffset)
                .setAnimation(AnimationYio.center)
                .setAppearParameters(MenuParams.ANIM_TYPE, 0.9 * MenuParams.ANIM_SPEED)
                .setReaction(getOpenSceneReaction(Scenes.settings));
    }


    private void createExitButton() {
        exitButton = uiFactory.getCircleButton()
                .setSize(iconSize)
                .alignBottom(verticalPosition - GraphicsYio.convertToHeight(iconSize) / 2)
                .alignRight(iconOffset)
                .setTouchOffset(touchOffset)
                .loadTexture("menu/main_menu/quit_icon.png")
                .setHotkeyKeycode(Input.Keys.BACK)
                .setAnimation(AnimationYio.center)
                .setAppearParameters(MenuParams.ANIM_TYPE, 0.9 * MenuParams.ANIM_SPEED)
                .setReaction(getExitReaction());
    }


    private Reaction getExitReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.exitApp();
            }
        };
    }


    private void initMetrics() {
        verticalPosition = 0.4;
        iconSize = 0.16;
        playButtonSize = 0.32;
        logoWidth = 0.5;
        iconOffset = 0.07;
        touchOffset = 0.05;
        secretCount = 0;
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        secretCount = 0;
        updateNicknameView();
        yioGdxGame.rejoinWorker.onEnteredMainLobby();
        netRoot.currentMatchData.reset();
    }


    @Override
    protected void onEndCreation() {
        super.onEndCreation();
        checkForAttraction();
    }


    private void checkForAttraction() {
        if (OneTimeInfo.getInstance().shprotyRelease) return;
        if (YioGdxGame.platformType == PlatformType.ios) return;
        OneTimeInfo.getInstance().shprotyRelease = true;
        OneTimeInfo.getInstance().save();
        Scenes.attraction.create();
    }


    public void updateCoinsElpView() {
        coinsElpViewElement.update();
    }


    public void updateNicknameView() {
        if (netRoot.offlineMode) {
            nicknameViewElement.setValues(LanguagesManager.getInstance().getString("offline"), AvatarType.empty);
            return;
        }
        String nameRaw = netRoot.userData.name;
        String nameLocalized = CharLocalizerYio.getInstance().apply(nameRaw);
        AvatarType avatarType = netRoot.customizationData.avatarType;
        nicknameViewElement.setValues(nameLocalized, avatarType);
    }


    @Override
    public void move() {
        super.move();

        if (secretCount > 0) {
            secretCount--;
        }
    }


    private void onPlayButtonPressed() {
        if (checkForTutorial()) return;
        MenuSwitcher.getInstance().createChooseGameModeMenu();
    }


    private boolean checkForTutorial() {
        if (OneTimeInfo.getInstance().tutorial) return false;
        OneTimeInfo.getInstance().tutorial = true;
        OneTimeInfo.getInstance().save();
        long hoursOnline = netRoot.initialStatisticsData.getHoursOnline();
        if (hoursOnline > 1) return false;
        yioGdxGame.gameController.tutorialManager.launch(TutorialType.basics);
        return true;
    }

}
