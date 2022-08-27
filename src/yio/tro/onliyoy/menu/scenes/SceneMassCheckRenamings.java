package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.RenamingListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NetMassRenamingData;
import yio.tro.onliyoy.net.shared.NetRenamingData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneMassCheckRenamings extends SceneYio{

    NetMassRenamingData netMassRenamingData;
    private CustomizableListYio customizableListYio;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    protected void initialize() {
        createList();
        createOkButton();
    }


    private void createOkButton() {
        uiFactory.getImportantConfirmationButton()
                .setParent(customizableListYio)
                .setSize(0.2, 0.055)
                .alignRight(GraphicsYio.convertToWidth(0.01))
                .alignBottom(0.01)
                .setTouchOffset(0.03)
                .setCounterValue(10)
                .applyText("Ok")
                .setReaction(getOkReaction());
    }


    private Reaction getOkReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                for (AbstractCustomListItem item : customizableListYio.items) {
                    if (!(item instanceof RenamingListItem)) continue;
                    RenamingListItem renamingListItem = (RenamingListItem) item;
                    if (renamingListItem.declined) continue;
                    String id = renamingListItem.targetClientId;
                    String newName = renamingListItem.desiredNameView.string;
                    netRoot.sendMessage(NmType.request_rename, newName + "/" + id);
                }
                Scenes.moderator.create();
            }
        };
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.68)
                .centerHorizontal()
                .centerVertical()
                .enableStaticCornersMode()
                .setAnimation(AnimationYio.from_touch)
                .setScrollingEnabled(false);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();
        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle(languagesManager.getString("check_renaming"));
        customizableListYio.addItem(titleListItem);
        boolean darken = true;
        for (NetRenamingData netRenamingData : netMassRenamingData.list) {
            RenamingListItem renamingListItem = new RenamingListItem();
            renamingListItem.setDarken(darken);
            darken = !darken;
            renamingListItem.setValues(netRenamingData);
            customizableListYio.addItem(renamingListItem);
        }
    }


    public void setNetMassRenamingData(NetMassRenamingData netMassRenamingData) {
        this.netMassRenamingData = netMassRenamingData;
    }
}
