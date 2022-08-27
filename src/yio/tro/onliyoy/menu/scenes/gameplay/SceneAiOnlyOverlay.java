package yio.tro.onliyoy.menu.scenes.gameplay;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.general.SpeedManager;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneAiOnlyOverlay extends ModalSceneYio {


    private double bSize;
    private ButtonYio playPauseButton;
    private TextureRegion playTexture;
    private TextureRegion pauseTexture;


    @Override
    protected void initialize() {
        bSize = GraphicsYio.convertToWidth(0.05);
        loadTextures();
        createPlayPauseButton();
        createSuperUserButton();
        createCoinButton();
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


    private void createSuperUserButton() {
        uiFactory.getButton()
                .setSize(bSize)
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


    private void loadTextures() {
        playTexture = getTextureFromAtlas("play");
        pauseTexture = getTextureFromAtlas("pause");
    }


    private void createPlayPauseButton() {
        playPauseButton = uiFactory.getButton()
                .setSize(bSize)
                .alignRight(0)
                .alignBottom(0)
                .setTouchOffset(0.06)
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.down)
                .setCustomTexture(null)
                .setSelectionTexture(getSelectionTexture())
                .setHotkeyKeycode(Input.Keys.SPACE)
                .setReaction(getPlayPauseReaction());
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        applySync();
    }


    public void onSpeedChanged() {
        if (!isCurrentlyVisible()) return;
        applySync();
    }


    public void applySync() {
        SpeedManager speedManager = getGameController().speedManager;
        if (speedManager.getSpeed() == 1) {
            playPauseButton.setCustomTexture(pauseTexture);
            return;
        }
        playPauseButton.setCustomTexture(playTexture);
    }


    private Reaction getPlayPauseReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onPlayPauseButtonPressed();
            }
        };
    }


    private void onPlayPauseButtonPressed() {
        SpeedManager speedManager = getGameController().speedManager;
        speedManager.onPlayPauseButtonPressed();
    }

}
