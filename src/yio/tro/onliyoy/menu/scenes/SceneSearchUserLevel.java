package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.PlaceholderListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.UserLevelListItem;
import yio.tro.onliyoy.net.shared.NetUlCacheData;
import yio.tro.onliyoy.net.shared.NmType;

import java.util.ArrayList;

public class SceneSearchUserLevel extends ModalSceneYio{

    String searchString;
    private CustomizableListYio customizableListYio;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.5);
        createList();
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setParent(defaultPanel)
                .setSize(0.98, 0.5)
                .centerHorizontal()
                .centerVertical();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        customizableListYio.clearItems();
        customizableListYio.addItem(new PlaceholderListItem("..."));
        netRoot.sendMessage(NmType.search_user_level, searchString);
    }


    public void onDataReceived(ArrayList<NetUlCacheData> list) {
        customizableListYio.clearItems();

        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle(languagesManager.getString("user_levels"));
        customizableListYio.addItem(titleListItem);

        for (NetUlCacheData netUlCacheData : list) {
            UserLevelListItem userLevelListItem = new UserLevelListItem();
            userLevelListItem.setNetUlCacheData(netUlCacheData);
            userLevelListItem.applyCompactMode();
            customizableListYio.addItem(userLevelListItem);
            userLevelListItem.applyCompactMode(); // yes, it should be applied again to fix background
        }
    }


    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
