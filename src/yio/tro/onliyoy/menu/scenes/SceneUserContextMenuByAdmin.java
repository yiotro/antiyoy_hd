package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneUserContextMenuByAdmin extends ModalSceneYio{

    String id;
    String name;
    private LabelElement nameLabel;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.4);
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
                .applyText("Rename")
                .setReaction(getRenameReaction());

        addContextButton("Give money", getNumericImpactReaction(NmType.admin_give_money));
        addContextButton("Give fish", getNumericImpactReaction(NmType.admin_give_fish));
        addContextButton("Set money", getNumericImpactReaction(NmType.admin_set_money));
        addContextButton("Set ELP", getNumericImpactReaction(NmType.admin_set_elp));
    }


    private Reaction getNumericImpactReaction(final NmType nmType) {
        return new Reaction() {
            @Override
            protected void apply() {
                onNumericImpactRequested(nmType);
            }
        };
    }


    private void addContextButton(String name, Reaction reaction) {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .clone(previousElement)
                .centerHorizontal()
                .alignUnder(previousElement, 0.01)
                .applyText(name)
                .setReaction(reaction);
    }


    private void onNumericImpactRequested(final NmType nmType) {
        Scenes.keyboard.create();
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) return;
                if (!Yio.isNumeric(input)) return;
                int value = Integer.valueOf(input);
                netRoot.sendMessage(nmType, id + "/" + value);
                destroy();
                Scenes.notification.show("Request sent");
            }
        });
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
        Scenes.keyboard.setHint("New name");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) return;
                netRoot.sendMessage(NmType.request_rename, input + "/" + id);
                Scenes.notification.show("Rename request successfully sent");
                destroy();
            }
        });
    }


    public void setId(String id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }
}
