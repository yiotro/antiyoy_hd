package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.TextFitParser;
import yio.tro.onliyoy.menu.reactions.Reaction;

import java.util.ArrayList;

public class SceneFishExplanation extends ModalSceneYio{

    @Override
    protected void initialize() {
        createDarken();
        createDefaultPanel(0.4);
        updateText();
        createTitle();
        createCloseButton();
    }


    private void createTitle() {
        uiFactory.getLabelElement()
                .setParent(defaultPanel)
                .setSize(0.01)
                .alignTop(0.03)
                .centerHorizontal()
                .setFont(Fonts.gameFont)
                .setTitle(languagesManager.getString("buy_fish") + "?");
    }


    @Override
    protected Reaction getCloseReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                yioGdxGame.billingManager.launch();
            }
        };
    }


    private void updateText() {
        updateText("# #" + languagesManager.getString("fish_explanation"));
    }


    private void updateText(String string) {
        ArrayList<String> strings = convertStringToArray(string);
        updateText(strings);
    }


    private void updateText(ArrayList<String> strings) {
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
        double h = 0.01 + stringsQuantity * 0.05;
        h = Math.max(h, 0.2);
        defaultPanel.setSize(1, h);
    }
}
