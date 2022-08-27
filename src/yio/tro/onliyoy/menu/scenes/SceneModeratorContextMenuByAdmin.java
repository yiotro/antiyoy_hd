package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneModeratorContextMenuByAdmin extends ModalSceneYio{

    String id;
    String name;
    private LabelElement nameLabel;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.33);
        createLabel();
        createButtons();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        nameLabel.setTitle(name);
    }


    private void createLabel() {
        nameLabel = uiFactory.getLabelElement()
                .setParent(defaultPanel)
                .setSize(0.01, 0.04)
                .centerHorizontal()
                .alignTop(0.01)
                .setTitle("-");
    }


    private void createButtons() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.6, 0.05)
                .centerHorizontal()
                .setBackground(BackgroundYio.gray)
                .alignUnder(nameLabel, 0.03)
                .applyText("Remove")
                .setReaction(getRemoveReaction());

        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.6, 0.05)
                .centerHorizontal()
                .setBackground(BackgroundYio.gray)
                .alignUnder(previousElement, 0.015)
                .applyText("Restore levels")
                .setReaction(getRestoreLevelsReaction());
    }


    private Reaction getRestoreLevelsReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Scenes.setupRestoreLevels.setId(id);
                Scenes.setupRestoreLevels.create();
            }
        };
    }


    private Reaction getRemoveReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Scenes.confirmRemoveModerator.setValues(id, name);
                Scenes.confirmRemoveModerator.create();
            }
        };
    }


    public void setId(String id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }
}
