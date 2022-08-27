package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.OfflineUlevItem;
import yio.tro.onliyoy.net.shared.NetUserLevelData;

import java.util.ArrayList;

public class SceneOfflineUlevSearch extends ModalSceneYio {

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
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();
        ArrayList<NetUserLevelData> dataList = Scenes.offlineUserLevels.dataList;
        String lowerCaseSearchString = searchString.toLowerCase();
        for (NetUserLevelData netUserLevelData : dataList) {
            if (!netUserLevelData.name.toLowerCase().contains(lowerCaseSearchString)) continue;
            OfflineUlevItem offlineUlevItem = new OfflineUlevItem();
            offlineUlevItem.setNetUserLevelData(netUserLevelData);
            customizableListYio.addItem(offlineUlevItem);
            offlineUlevItem.setBackgroundYio(null);
        }
    }


    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
