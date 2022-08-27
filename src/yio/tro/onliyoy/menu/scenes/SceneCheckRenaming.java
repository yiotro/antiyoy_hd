package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.*;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetRenamingData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneCheckRenaming extends SceneYio{

    NetRenamingData netRenamingData;
    private AnnounceViewElement mainView;
    private LabelElement labelElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    protected void initialize() {
        createMainView();
        createLabel();
        createButtons();
    }


    private void createButtons() {
        double w = 0.2;
        double delta = (0.9 - 2 * w) / 3;

        uiFactory.getButton()
                .setParent(mainView)
                .setSize(w, 0.05)
                .setTouchOffset(0.05)
                .alignBottom(0.02)
                .alignLeft(delta)
                .setBackground(BackgroundYio.gray)
                .applyText("no")
                .setReaction(getNoReaction());

        uiFactory.getImportantConfirmationButton()
                .setParent(mainView)
                .setSize(w, 0.05)
                .alignRight(delta)
                .alignBottom(0.02)
                .setTouchOffset(0.05)
                .setCounterValue(5)
                .applyText("yes")
                .setReaction(getYesReaction());
    }


    private Reaction getYesReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                String id = netRenamingData.targetClientId;
                String newName = netRenamingData.desiredName;
                netRoot.sendMessage(NmType.request_rename, newName + "/" + id);
                Scenes.moderator.create();
            }
        };
    }


    private Reaction getNoReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.moderator.create();
            }
        };
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        String rawCurrentName = netRenamingData.currentName;
        String rawDesiredName = netRenamingData.desiredName;
        String localizedCurrentName = CharLocalizerYio.getInstance().apply(rawCurrentName);
        String localizedDesiredName = CharLocalizerYio.getInstance().apply(rawDesiredName);
        labelElement.setTitle(localizedCurrentName + " -> " + localizedDesiredName);
    }


    private void createLabel() {
        labelElement = uiFactory.getLabelElement()
                .setParent(mainView)
                .setSize(0.01)
                .centerHorizontal()
                .alignTop(0.1)
                .setFont(Fonts.miniFont)
                .setTitle("-");
    }


    private void createMainView() {
        double h = 0.24;
        mainView = uiFactory.getAnnounceViewElement()
                .setSize(0.9, h)
                .alignBottom(0.45 - h / 2)
                .centerHorizontal()
                .setAnimation(AnimationYio.from_touch)
                .setTitle("check_renaming")
                .setText(" ")
                .setTouchable(false);
    }


    public void setNetRenamingData(NetRenamingData netRenamingData) {
        this.netRenamingData = netRenamingData;
    }
}
