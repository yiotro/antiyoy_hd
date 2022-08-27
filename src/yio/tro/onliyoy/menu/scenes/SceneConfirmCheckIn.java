package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.AdvancedLabelElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneConfirmCheckIn extends ModalSceneYio{

    protected AdvancedLabelElement questionLabel;
    String name;
    String value;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.25);
        createLabel();
        createOkButton();
    }


    private void createLabel() {
        questionLabel = uiFactory.getAdvancedLabelElement()
                .setParent(defaultPanel)
                .setSize(0.9, 0.06)
                .alignTop(0.015)
                .centerHorizontal()
                .setFont(Fonts.gameFont)
                .applyText("-");
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        updateQuestionLabel();
    }


    private void updateQuestionLabel() {
        String questionRaw = languagesManager.getString("confirm_check_in_nickname");
        String string = questionRaw.replace("[name]", name);
        questionLabel.applyText(string);
    }


    private void createOkButton() {
        uiFactory.getImportantConfirmationButton()
                .setParent(defaultPanel)
                .setSize(0.15, 0.06)
                .alignRight(0.15)
                .alignBottom(0.03)
                .setTouchOffset(0.1)
                .setCounterValue(7)
                .applyText("Ok")
                .setReaction(getOkReaction());
    }


    private Reaction getOkReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                netRoot.sendMessage(NmType.check_in, value);
                Scenes.waitForCheckIn.create();
            }
        };
    }


    public void prepare(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
