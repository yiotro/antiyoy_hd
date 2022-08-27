package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.general.GameRules;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NetValues;
import yio.tro.onliyoy.stuff.StoreLinksYio;

public class SceneSecretScreen extends SceneYio{


    private ButtonYio mainLabel;
    private int[] speedValues;
    private SliderElement speedSlider;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.red;
    }


    @Override
    public BackgroundYio getButtonBackground() {
        return BackgroundYio.white;
    }


    @Override
    protected void initialize() {
        initSpeedValues();
        createBackButton();
        createMainLabel();
        createSlider();
        createButtons();
        createVersionLabel();
    }


    private void createVersionLabel() {
        uiFactory.getLabelElement()
                .setSize(0.12, 0.05)
                .alignTop(0)
                .alignRight(0.02)
                .setAnimation(AnimationYio.up)
                .setFont(Fonts.miniFont)
                .setRightAlignEnabled(true)
                .setTitle("[" + NetValues.PROTOCOL + "]");
    }


    private void createButtons() {
        uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.7, 0.06)
                .centerHorizontal()
                .alignUnder(previousElement, 0.05)
                .setBackground(BackgroundYio.gray)
                .setShadow(false)
                .setTouchOffset(0.02)
                .applyText("Debug tests")
                .setReaction(getOpenSceneReaction(Scenes.debugTests));

        uiFactory.getButton()
                .clone(previousElement)
                .alignUnder(previousElement, 0.02)
                .centerHorizontal()
                .applyText("Show fps")
                .setReaction(getShowFpsReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignUnder(previousElement, 0.02)
                .centerHorizontal()
                .applyText("Test screen")
                .setReaction(getOpenSceneReaction(Scenes.testScreen));

        uiFactory.getButton()
                .clone(previousElement)
                .alignUnder(previousElement, 0.02)
                .centerHorizontal()
                .applyText("Super user")
                .setReaction(getSuperUserReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignUnder(previousElement, 0.02)
                .centerHorizontal()
                .applyText("Action")
                .setReaction(getActionReaction());
    }


    private Reaction getActionReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Gdx.net.openURI(StoreLinksYio.getInstance().getLink("onliyoy"));
            }
        };
    }


    private Reaction getSuperUserReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                switchSuperUser();
            }
        };
    }


    private Reaction getShowFpsReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                switchShowFps();
            }
        };
    }


    private void createSlider() {
        speedSlider = uiFactory.getSlider()
                .setParent(mainLabel)
                .alignTop(0.23)
                .centerHorizontal()
                .setTitle("Fast forward speed")
                .setPossibleValues(speedValues)
                .setValueChangeReaction(getSpeedSliderReaction());
    }


    private Reaction getSpeedSliderReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                GameRules.fastForwardSpeed = speedValues[speedSlider.getValueIndex()];
            }
        };
    }


    private void loadValues() {
        speedSlider.setValueIndex(convertSpeedIntoSliderIndex(GameRules.fastForwardSpeed));
    }


    @Override
    protected void onAppear() {
        loadValues();
    }


    private int convertSpeedIntoSliderIndex(int speed) {
        for (int i = 0; i < speedValues.length; i++) {
            if (speedValues[i] == speed) {
                return i;
            }
        }

        return 0;
    }


    private void createMainLabel() {
        mainLabel = uiFactory.getButton()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignTop(0.15)
                .setAnimation(AnimationYio.from_touch)
                .setTouchable(false)
                .applyManyTextLines(convertStringToArray("- Secret screen - #Load time: " + YioGdxGame.initialLoadingTime));
    }


    private void createBackButton() {
        spawnBackButton(getOpenSceneReaction(Scenes.mainLobby));
    }


    private void initSpeedValues() {
        speedValues = new int[]{4, 10, 20, 30, 50, 75, 100};
    }


    private void switchSuperUser() {
        if (DebugFlags.superUserEnabled) {
            DebugFlags.superUserEnabled = false;
            Scenes.notification.show("Super user disabled");
        } else {
            DebugFlags.superUserEnabled = true;
            Scenes.notification.show("Super user enabled");
        }
    }


    private void switchShowFps() {
        if (DebugFlags.showFps) {
            DebugFlags.showFps = false;
            Scenes.notification.show("Show fps disabled");
        } else {
            DebugFlags.showFps = true;
            Scenes.notification.show("Show fps enabled");
        }
    }
}
