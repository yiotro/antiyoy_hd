package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.ImportantConfirmationButton;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public abstract class AbstractConfirmationScene extends ModalSceneYio{

    protected LabelElement questionLabel;
    private ImportantConfirmationButton confirmationButton;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.18);
        createLabel();
        createButtons();
    }


    private void createButtons() {
        double w = 0.2;
        double delta = (1 - 2 * w) / 3;
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(w, 0.06)
                .alignLeft(delta)
                .setTouchOffset(0.05)
                .alignUnder(questionLabel, 0.015)
                .setBackground(BackgroundYio.gray)
                .setKey("no")
                .applyText("no")
                .setReaction(getNoReaction());

        confirmationButton = uiFactory.getImportantConfirmationButton()
                .setParent(defaultPanel)
                .setSize(w, 0.06)
                .alignRight(delta)
                .alignUnder(questionLabel, 0.015)
                .setTouchOffset(0.05)
                .setCounterValue(getCountDown())
                .applyText("yes")
                .setReaction(getYesReaction());
    }


    protected abstract Reaction getNoReaction();


    protected abstract Reaction getYesReaction();


    @Override
    protected void onAppear() {
        super.onAppear();
        updateQuestionLabel();
        confirmationButton.setCounterValue(getCountDown());
    }


    private void updateQuestionLabel() {
        questionLabel.setTitle(languagesManager.getString(getQuestionKey()));
    }


    protected abstract String getQuestionKey();


    protected int getCountDown() {
        return 5;
    }


    private void createLabel() {
        questionLabel = uiFactory.getLabelElement()
                .setParent(defaultPanel)
                .setSize(0.01, 0.06)
                .alignTop(0.015)
                .centerHorizontal()
                .setFont(Fonts.gameFont)
                .setTitle("-");
    }


}
