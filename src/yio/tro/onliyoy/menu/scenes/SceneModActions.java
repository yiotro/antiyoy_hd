package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.PlaceholderListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.ScrollListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.net.shared.NetModActionsData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneModActions extends SceneYio{

    private CustomizableListYio customizableListYio;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getOpenSceneReaction(Scenes.admin));
        createList();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        customizableListYio.clearItems();
        customizableListYio.addItem(new PlaceholderListItem("..."));
        netRoot.sendMessage(NmType.request_mod_actions, "");
    }


    public void onDataReceived(NetModActionsData netModActionsData) {
        customizableListYio.clearItems();

        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle("Mod actions");
        customizableListYio.addItem(titleListItem);

        for (int i = netModActionsData.list.size() - 1; i >= 0; i--) {
            String string = netModActionsData.list.get(i);
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setTitle(string);
            scrollListItem.setFont(Fonts.miniFont);
            scrollListItem.setHeight(0.055f * GraphicsYio.height);
            scrollListItem.setColored(false);
            customizableListYio.addItem(scrollListItem);
        }
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.96, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAnimation(AnimationYio.from_touch);
    }
}
