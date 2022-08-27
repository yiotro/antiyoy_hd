package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.menu.TextFitParser;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.ArrayList;

public class SceneMessageDialog extends ModalSceneYio{


    private ButtonYio skipButton;


    @Override
    protected void initialize() {
        createDefaultPanel(0.3);
        createCloseButton();
    }


    private void createSkipButton() {
        skipButton = uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(GraphicsYio.convertToWidth(0.03))
                .alignRight(GraphicsYio.convertToWidth(0.03))
                .alignBottom(0.03)
                .setTouchOffset(0.05)
                .setCustomTexture(getTextureFromAtlas("skip"))
                .setIgnoreResumePause(true)
                .setReaction(getSkipReaction())
                .setKey("skip")
                .setAllowedToAppear(getSkipButtonCondition())
                .setSelectionTexture(getSelectionTexture());
    }


    private ConditionYio getSkipButtonCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return false;
            }
        };
    }


    private Reaction getSkipReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                applySkip();
            }
        };
    }


    private void applySkip() {
        YioGdxGame yioGdxGame = menuControllerYio.yioGdxGame;
        GameController gameController = yioGdxGame.gameController;
        gameController.scriptManager.applySkipMessages();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        forceElementsToTop();
    }


    public void updateText(String key) {
        String string = languagesManager.getString(key);
        ArrayList<String> strings = convertStringToArray(string);
        updateText(strings);
    }


    public void updateText(ArrayList<String> strings) {
        ArrayList<String> parsed = TextFitParser.getInstance().parseText(
                strings,
                Fonts.gameFont,
                0.92f * defaultPanel.getPosition().width,
                false
        );

        updateLabelHeight(parsed.size());
        defaultPanel.applyManyLines(parsed);
    }


    private void updateLabelHeight(int stringsQuantity) {
        double h = 0.05 + stringsQuantity * 0.055;
        h = Math.max(h, 0.2);
        defaultPanel.setSize(1, h);
    }


    @Override
    protected Reaction getCloseReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
            }
        };
    }


    @Override
    public boolean isCurrentlyVisible() {
        return defaultPanel != null && defaultPanel.getFactor().getValue() > 0;
    }

}
