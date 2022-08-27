package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.replay_overlay.ReplayControlElement;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneReplayOverlay extends ModalSceneYio {


    private ReplayControlElement replayControlElement;


    @Override
    protected void initialize() {
        createSuperUserButton();
        createReplayControlElement();
        createCoinButton();
    }


    private void createReplayControlElement() {
        replayControlElement = uiFactory.getReplayControlElement()
                .setSize(1, 0.05)
                .setAnimation(AnimationYio.down);
    }


    private void createCoinButton() {
        uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignLeft(0)
                .alignTop(0)
                .setTouchOffset(0.05)
                .loadCustomTexture("menu/province_ui/coin.png")
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.up)
                .setSelectionTexture(getSelectionTexture())
                .setClickSound(SoundType.coin)
                .setReaction(getOpenSceneReaction(Scenes.incomeGraph));
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        applySync();
    }


    public void applySync() {
        replayControlElement.syncWithReplayManager();
    }


    private void createSuperUserButton() {
        uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignLeft(0)
                .alignTop(0.1)
                .setTouchOffset(0.06)
                .setIgnoreResumePause(true)
                .setCustomTexture(getTextureFromAtlas("open"))
                .setReaction(getOpenSceneReaction(Scenes.debugPanel))
                .setAllowedToAppear(getSuperUserCondition());
    }


    private ConditionYio getSuperUserCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return DebugFlags.superUserEnabled;
            }
        };
    }
}
