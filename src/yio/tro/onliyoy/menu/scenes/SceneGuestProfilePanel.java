package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneGuestProfilePanel extends ModalSceneYio {

    private ButtonYio mainLabel;
    private LabelElement nicknameLabel;


    @Override
    protected void initialize() {
        createCloseButton();
        createMainLabel();
        createNicknameLabel();
        createRatingsButton();
        createSkinsButton();
        createAdminButton();
    }


    private void createRatingsButton() {
        uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.5, 0.05)
                .centerHorizontal()
                .alignBottom(0.02)
                .applyText("ratings")
                .setReaction(getRatingsReaction());
    }


    private Reaction getRatingsReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Scenes.chooseNetRatingPanel.create();
            }
        };
    }


    private void createAdminButton() {
        uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.2, 0.04)
                .alignTop(0.01)
                .alignRight(0.02)
                .setTouchOffset(0.03)
                .applyText("Admin")
                .setAllowedToAppear(getLocalModeCondition())
                .setReaction(getOpenSceneReaction(Scenes.admin));
    }


    private ConditionYio getLocalModeCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return netRoot.isInLocalMode();
            }
        };
    }


    private void createSkinsButton() {
        uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.5, 0.05)
                .centerHorizontal()
                .alignAbove(previousElement, 0.01)
                .applyText("skins")
                .setReaction(getSkinsReaction());
    }


    private Reaction getSkinsReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.chooseLocalSkin.create();
            }
        };
    }


    private void createNicknameLabel() {
        nicknameLabel = uiFactory.getLabelElement()
                .setParent(mainLabel)
                .setSize(0.01, 0.06)
                .alignTop(0)
                .alignLeft(0.05)
                .setFont(Fonts.gameFont)
                .setLeftAlignEnabled(true)
                .setTitle("-");
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        String nameRaw = netRoot.userData.name;
        String nameLocalized = CharLocalizerYio.getInstance().apply(nameRaw);
        nicknameLabel.setTitle(nameLocalized);
    }


    private void createMainLabel() {
        mainLabel = uiFactory.getButton()
                .setSize(1.02, 0.22)
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
