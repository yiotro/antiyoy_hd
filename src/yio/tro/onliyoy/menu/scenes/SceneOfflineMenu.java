package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.PlatformType;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.multi_button.MultiButtonElement;
import yio.tro.onliyoy.menu.elements.multi_button.TemporaryMbeItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneOfflineMenu extends SceneYio{

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    protected void initialize() {
        createMultiButtonElement();
        createSecretButton();
        createOfflineLabel();
        createCalendarButton();
        spawnBackButton(getBackReaction());
    }


    private void createCalendarButton() {
        uiFactory.getCircleButton()
                .setSize(GraphicsYio.convertToWidth(0.09))
                .alignRight(0.04)
                .alignTop(0.02)
                .setTouchOffset(0.05)
                .loadTexture("menu/calendar/calendar.png")
                .setAnimation(AnimationYio.up)
                .setReaction(getOpenSceneReaction(Scenes.calendar));
    }


    private void createOfflineLabel() {
        uiFactory.getLabelElement()
                .setRightAlignEnabled(true)
                .setSize(0.01)
                .setFont(Fonts.miniFont)
                .setTitle(LanguagesManager.getInstance().getString("offline"))
                .setAllowedToAppear(getNotIosCondition())
                .alignRight(0.02)
                .alignBottom(0.015)
                .setAnimation(AnimationYio.down);
    }


    private void createSecretButton() {
        uiFactory.getButton()
                .setSize(0.08)
                .alignBottom(0)
                .alignRight(0)
                .setHidden(true)
                .setReaction(getOpenSceneReaction(Scenes.secretScreen));
    }


    private void createMultiButtonElement() {
        TemporaryMbeItem[] mbeItems = initMbeItems();

        double h = 0.08 * mbeItems.length;
        MultiButtonElement multiButtonElement = uiFactory.getMultiButtonElement()
                .setSize(0.7, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAnimation(AnimationYio.from_touch);

        for (TemporaryMbeItem mbeItem : mbeItems) {
            if (mbeItem == null) continue;
            multiButtonElement.addLocalButton(mbeItem);
        }
    }


    private TemporaryMbeItem[] initMbeItems() {
        if (YioGdxGame.platformType == PlatformType.ios) {
            return new TemporaryMbeItem[]{
                    new TemporaryMbeItem("skirmish", BackgroundYio.magenta, getOpenSceneReaction(Scenes.setupSkirmish)),
                    new TemporaryMbeItem("editor", BackgroundYio.yellow, getOpenSceneReaction(Scenes.editorLobby)),
                    new TemporaryMbeItem("user_levels", BackgroundYio.cyan, getOpenSceneReaction(Scenes.offlineUserLevels)),
                    new TemporaryMbeItem("campaign", BackgroundYio.orange, getOpenSceneReaction(Scenes.campaign)),
                    new TemporaryMbeItem("load", BackgroundYio.green, getOpenSceneReaction(Scenes.loadFromSlot)),
            };
        }
        return new TemporaryMbeItem[]{
                new TemporaryMbeItem("skirmish", BackgroundYio.magenta, getOpenSceneReaction(Scenes.setupSkirmish)),
                new TemporaryMbeItem("editor", BackgroundYio.yellow, getOpenSceneReaction(Scenes.editorLobby)),
                new TemporaryMbeItem("campaign", BackgroundYio.orange, getOpenSceneReaction(Scenes.campaign)),
                new TemporaryMbeItem("load", BackgroundYio.green, getOpenSceneReaction(Scenes.loadFromSlot)),
        };
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
}
