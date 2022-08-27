package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.OneTimeInfo;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.choose_game_mode.CgmElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.NetRole;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneChooseGameMode extends SceneYio {


    public CgmElement cgmElement;
    long lastTimeAskedForFreeFish;
    public ButtonYio fishButton;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    public void initialize() {
        createCgmElement();
        createFishButton();
        spawnBackButton(getBackReaction());
    }


    private void createFishButton() {
        fishButton = uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignRight(0)
                .alignTop(0)
                .setTouchOffset(0.05)
                .loadCustomTexture("menu/shop/fish_bounds.png")
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.up)
                .setAllowedToAppear(getFishCondition())
                .setSelectionTexture(getSelectionTexture())
                .setLongTapReaction(getOpenSceneReaction(Scenes.secretScreen))
                .setClickSound(SoundType.coin)
                .setReaction(getFishReaction());
    }


    private ConditionYio getFishCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return netRoot.userData.role.ordinal() >= NetRole.normal.ordinal();
            }
        };
    }


    private Reaction getFishReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                if (System.currentTimeMillis() - lastTimeAskedForFreeFish < 750) return;
                netRoot.sendMessage(NmType.ask_for_free_fish, "");
                lastTimeAskedForFreeFish = System.currentTimeMillis();
                if (!OneTimeInfo.getInstance().hintFreeFish) {
                    OneTimeInfo.getInstance().hintFreeFish = true;
                    OneTimeInfo.getInstance().save();
                }
            }
        };
    }


    private void createCgmElement() {
        double h = 0.08 * 5;
        cgmElement = uiFactory.getCgmElement()
                .setSize(0.7, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAnimation(AnimationYio.from_touch);
    }


    private void createSecretButton() {
        uiFactory.getButton()
                .setSize(0.08)
                .alignTop(0)
                .alignRight(0)
                .setHidden(true)
                .setReaction(getOpenSceneReaction(Scenes.secretScreen));
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.setGamePaused(true);
                Scenes.mainLobby.create();
            }
        };
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        netRoot.currentMatchData.reset();
        lastTimeAskedForFreeFish = 0;
        checkForFreeFishHint();
    }


    private void checkForFreeFishHint() {
        if (netRoot.userData.role.ordinal() < NetRole.normal.ordinal()) return;
        PostponedReactionsManager.aprHintFreeFish.launch();
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
