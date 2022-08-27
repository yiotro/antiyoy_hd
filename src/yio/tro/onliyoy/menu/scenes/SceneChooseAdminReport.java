package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.ScrollListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SliReaction;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SceneChooseAdminReport extends ModalSceneYio{

    private CustomizableListYio customizableListYio;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.4);
        createList();
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setParent(defaultPanel)
                .setSize(0.9, 0.4)
                .centerHorizontal()
                .centerVertical();
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();
        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle("Reports");
        customizableListYio.addItem(titleListItem);
        boolean darken = true;
        for (Map.Entry<String, NmType> entry : initHashMap().entrySet()) {
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setColored(false);
            scrollListItem.setHeight(0.06f * GraphicsYio.height);
            scrollListItem.setTitle(entry.getKey());
            scrollListItem.setKey("" + entry.getValue());
            scrollListItem.setClickReaction(getClickReaction());
            scrollListItem.setDarken(darken);
            darken = !darken;
            customizableListYio.addItem(scrollListItem);
        }
    }


    private SliReaction getClickReaction() {
        return new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                destroy();
                NmType nmType = NmType.valueOf(item.getKey());
                netRoot.sendMessage(nmType, "");
            }
        };
    }


    private HashMap<String, NmType> initHashMap() {
        HashMap<String, NmType> hashMap = new LinkedHashMap<>();
        hashMap.put("Matches", NmType.request_admin_matches_report);
        hashMap.put("Preparation", NmType.request_admin_preparation_report);
        hashMap.put("User reports", NmType.request_admin_user_reports);
        hashMap.put("Average ELP", NmType.request_admin_report_elp);
        return hashMap;
    }
}
