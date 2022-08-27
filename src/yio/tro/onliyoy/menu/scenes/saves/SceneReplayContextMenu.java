package yio.tro.onliyoy.menu.scenes.saves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.save_system.SavesManager;
import yio.tro.onliyoy.game.save_system.SmItem;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneReplayContextMenu extends ModalSceneYio {

    AbstractSavesManagementScene parentScene;
    SmItem smItem;
    private LabelElement nameLabel;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.34);
        createLabel();
        createButtons();
    }


    private void createButtons() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.65, 0.06)
                .centerHorizontal()
                .alignUnder(previousElement, 0.015)
                .setBackground(BackgroundYio.gray)
                .setShadow(false)
                .setTouchOffset(0.02)
                .applyText("rename")
                .setReaction(getRenameReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignUnder(previousElement, 0.02)
                .applyText("delete")
                .setReaction(getDeleteReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignUnder(previousElement, 0.02)
                .applyText("export")
                .setReaction(getExportReaction());
    }


    private Reaction getExportReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onExportButtonPressed();
            }
        };
    }


    private void onExportButtonPressed() {
        String levelCode = getSavesManager().getLevelCode(smItem.key);
        Clipboard clipboard = Gdx.app.getClipboard();
        clipboard.setContents(levelCode);
        destroy();
        Scenes.notification.show("exported");
    }


    private Reaction getDeleteReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onDeleteButtonPressed();
            }
        };
    }


    private void onDeleteButtonPressed() {
        destroy();
        Scenes.confirmSlotDeletion.setParentScene(parentScene);
        Scenes.confirmSlotDeletion.setSmItem(smItem);
        Scenes.confirmSlotDeletion.create();
    }


    private Reaction getRenameReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onRenameButtonPressed();
            }
        };
    }


    private void onRenameButtonPressed() {
        Scenes.keyboard.create();
        Scenes.keyboard.setValue(smItem.name);
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) return;
                getSavesManager().renameItem(smItem, input);
                updateNameLabel();
                destroy();
                parentScene.onListUpdateRequestedFromExternalSource();
            }
        });
    }


    private void createLabel() {
        nameLabel = uiFactory.getLabelElement()
                .setParent(defaultPanel)
                .setSize(0.01, 0.06)
                .alignTop(0.015)
                .centerHorizontal()
                .setFont(Fonts.gameFont)
                .setTitle("-");
    }


    void setValues(AbstractSavesManagementScene savesManagementScene, String key) {
        parentScene = savesManagementScene;
        smItem = getSavesManager().getItem(key);
        updateNameLabel();
    }


    private void updateNameLabel() {
        nameLabel.setTitle(smItem.name);
    }


    protected SavesManager getSavesManager() {
        return yioGdxGame.gameController.savesManager;
    }
}
