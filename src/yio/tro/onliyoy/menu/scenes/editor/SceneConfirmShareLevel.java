package yio.tro.onliyoy.menu.scenes.editor;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneConfirmShareLevel extends ModalSceneYio {

    @Override
    protected void initialize() {
        createCloseButton();
        createDefaultPanel(0.25);
        createAdvancedLabel();
        createNextButton();
    }


    private void createNextButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.3, 0.05)
                .setBackground(BackgroundYio.gray)
                .alignRight(0.05)
                .alignBottom(0.02)
                .setTouchOffset(0.05)
                .applyText("next")
                .setReaction(getNextReaction());
    }


    private Reaction getNextReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onNextButtonPressed();
            }
        };
    }


    private void onNextButtonPressed() {
        destroy();
        yioGdxGame.applyFullTransitionToUI();
        yioGdxGame.gameController.objectsLayer.editorManager.applySaveToCurrentSlot();
        Scenes.waitCompletionCheckLoading.create();
        netRoot.sendMessage(NmType.ask_if_can_upload_new_level, "");
    }


    private void createAdvancedLabel() {
        uiFactory.getAdvancedLabelElement()
                .setParent(defaultPanel)
                .setSize(0.9, 0.01)
                .centerHorizontal()
                .alignTop(0.03)
                .setFont(Fonts.miniFont)
                .applyText(languagesManager.getString("confirm_share_level"));
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
