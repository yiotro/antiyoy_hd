package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.*;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.gameplay.RankWorker;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.NetExperienceManager;
import yio.tro.onliyoy.net.NetRole;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetPlayerStatisticsData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneNormalProfilePanel extends ModalSceneYio{

    private ButtonYio mainLabel;
    private LabelElement matchesLabel;
    private LabelElement timeLabel;
    private ButtonYio nicknameButton;
    private LabelElement levelLabel;


    @Override
    protected void initialize() {
        createCloseButton();
        createMainLabel();
        createNicknameButton();
        createPlayerStatisticsLabels();
        createSignOutButton();
        createCustomizationButton();
        createAdminButton();
        createModButton();
    }


    private void createModButton() {
        uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.2, 0.04)
                .alignTop(0.02)
                .alignRight(0.03)
                .setTouchOffset(0.03)
                .applyText("Mod")
                .setAllowedToAppear(getModeratorCondition())
                .setReaction(getOpenSceneReaction(Scenes.moderator));
    }


    private void createPlayerStatisticsLabels() {
        matchesLabel = uiFactory.getLabelElement()
                .setParent(mainLabel)
                .setSize(0.01, 0.03)
                .alignUnder(previousElement, 0.01)
                .alignLeft(0.05)
                .setFont(Fonts.miniFont)
                .setLeftAlignEnabled(true)
                .setTitle("-");

        timeLabel = uiFactory.getLabelElement()
                .setParent(mainLabel)
                .setSize(0.01, 0.03)
                .alignUnder(previousElement, 0)
                .alignLeft(0.05)
                .setFont(Fonts.miniFont)
                .setLeftAlignEnabled(true)
                .setTitle("-");

        levelLabel = uiFactory.getLabelElement()
                .setParent(mainLabel)
                .setSize(0.01, 0.03)
                .alignUnder(previousElement, 0)
                .alignLeft(0.05)
                .setFont(Fonts.miniFont)
                .setLeftAlignEnabled(true)
                .setTitle("-");
    }


    private void createSignOutButton() {
        uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(GraphicsYio.convertToWidth(0.035))
                .setTouchOffset(0.04)
                .alignLeft(0.035)
                .alignTop(0.02)
                .loadCustomTexture("menu/main_menu/sign_out.png")
                .setReaction(getSignOutReaction());
    }


    private Reaction getSignOutReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Scenes.confirmLogout.create();
            }
        };
    }


    private void createAdminButton() {
        uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.2, 0.04)
                .alignTop(0.02)
                .alignRight(0.03)
                .setTouchOffset(0.03)
                .applyText("Admin")
                .setAllowedToAppear(getAdminCondition())
                .setReaction(getOpenSceneReaction(Scenes.admin));
    }


    private ConditionYio getAdminCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                if (netRoot.isInLocalMode() && YioGdxGame.platformType == PlatformType.pc) return true;
                return netRoot.userData.role == NetRole.admin;
            }
        };
    }


    private ConditionYio getModeratorCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return netRoot.userData.role == NetRole.moderator;
            }
        };
    }


    private void createCustomizationButton() {
        uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.66, 0.05)
                .centerHorizontal()
                .alignBottom(0.015)
                .applyText("customization")
                .setReaction(getCustomizationReaction());
    }


    private Reaction getCustomizationReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.shop.create();
            }
        };
    }


    private void createNicknameButton() {
        nicknameButton = uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.44, 0.04)
                .alignTop(0.02)
                .centerHorizontal()
                .setTouchOffset(0.03)
                .applyText("-")
                .setReaction(getNicknameReaction());
    }


    private Reaction getNicknameReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onNicknameButtonPressed();
            }
        };
    }


    private void onNicknameButtonPressed() {
        Scenes.keyboard.create();
        Scenes.keyboard.setHint("new_nickname");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() < 3) return;
                destroy();
                Scenes.confirmAskForRenaming.setNickname(input);
                Scenes.confirmAskForRenaming.create();
            }
        });
    }


    public void onPlayerStatisticsReceived(NetPlayerStatisticsData netPlayerStatisticsData) {
        matchesLabel.setTitle(generateMatchesString(netPlayerStatisticsData));
        timeLabel.setTitle(generateTimeString(netPlayerStatisticsData));
        levelLabel.setTitle(generateLevelString(netPlayerStatisticsData));
    }


    private String generateMatchesString(NetPlayerStatisticsData netPlayerStatisticsData) {
        int matchesWon = netPlayerStatisticsData.matchesWon;
        int matchesPlayed = netPlayerStatisticsData.matchesPlayed;
        double winRatio = 0;
        if (matchesPlayed > 0) {
            winRatio = (double) matchesWon / (double) matchesPlayed;
        }
        double winPercent = Yio.roundUp(winRatio * 100, 2);
        String matchesPrefix = languagesManager.getString("matches");
        return matchesPrefix + ": " + matchesWon + "/" + matchesPlayed + ", " + winPercent + "%";
    }


    private String generateTimeString(NetPlayerStatisticsData netPlayerStatisticsData) {
        String timePrefix = languagesManager.getString("time");
        int timeFrames = Yio.convertMillisIntoFrames(netPlayerStatisticsData.timeOnline);
        return timePrefix + ": " + Yio.convertTimeToUnderstandableString(timeFrames);
    }


    private String generateLevelString(NetPlayerStatisticsData netPlayerStatisticsData) {
        String levelPrefix = languagesManager.getString("level");
        int level = NetExperienceManager.convertExperienceToLevel(netPlayerStatisticsData.experience);
        String rank = RankWorker.apply(netRoot.customizationData.rankType, netRoot.userData.elp);
        return levelPrefix + " " + level + ", " + rank;
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
        netRoot.sendMessage(NmType.request_player_statistics, "");
    }


    private void loadValues() {
        String nameRaw = netRoot.userData.name;
        String nameLocalized = CharLocalizerYio.getInstance().apply(nameRaw);
        nicknameButton.applyText(nameLocalized);

        matchesLabel.setTitle(languagesManager.getString("matches") + ": -");
        timeLabel.setTitle(languagesManager.getString("time") + ": -");
    }


    private void createMainLabel() {
        mainLabel = uiFactory.getButton()
                .setSize(1.02, 0.24)
                .centerHorizontal()
                .alignTop(-0.001)
                .setCornerRadius(0)
                .setAlphaEnabled(false)
                .setAnimation(AnimationYio.up)
                .setSilentReactionMode(true)
                .setAppearParameters(MovementType.inertia, 1.5)
                .setDestroyParameters(MovementType.inertia, 1.5);
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
