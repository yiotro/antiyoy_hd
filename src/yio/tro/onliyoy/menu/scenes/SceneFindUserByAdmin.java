package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.customizable_list.*;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetSearchResultData;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.Map;
import java.util.Set;

public class SceneFindUserByAdmin extends ModalSceneYio{


    private CustomizableListYio customizableListYio;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.6);
        createList();
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setParent(defaultPanel)
                .setSize(1, 0.6)
                .centerHorizontal()
                .centerVertical();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        customizableListYio.clearItems();
        customizableListYio.addItem(new PlaceholderListItem("..."));
    }


    public void onDataReceived(NetSearchResultData netSearchResultData) {
        customizableListYio.clearItems();

        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle("Users");
        customizableListYio.addItem(titleListItem);

        Set<Map.Entry<String, String>> entries = netSearchResultData.map.entrySet();
        if (entries.size() == 0) {
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setTitle("No results");
            scrollListItem.setColored(false);
            scrollListItem.setCentered(true);
            customizableListYio.addItem(scrollListItem);
            return;
        }

        for (Map.Entry<String, String> entry : entries) {
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setKey(entry.getKey());
            scrollListItem.setTitle(CharLocalizerYio.getInstance().apply(entry.getValue()));
            scrollListItem.setColored(false);
            scrollListItem.setCentered(true);
            scrollListItem.setHeight(0.065f * GraphicsYio.height);
            scrollListItem.setClickReaction(getClickReaction());
            customizableListYio.addItem(scrollListItem);
        }
    }


    private SliReaction getClickReaction() {
        return new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                destroy();
                Scenes.userContextMenuByAdmin.setId(item.getKey());
                Scenes.userContextMenuByAdmin.setName(item.title.string);
                Scenes.userContextMenuByAdmin.create();
            }
        };
    }
}
