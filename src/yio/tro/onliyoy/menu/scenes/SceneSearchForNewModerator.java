package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.customizable_list.*;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetSearchResultData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.Map;

public class SceneSearchForNewModerator extends ModalSceneYio{

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
                .setSize(1, 0.5)
                .centerHorizontal()
                .centerVertical();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        customizableListYio.clearItems();
        customizableListYio.addItem(new PlaceholderListItem("..."));
        netRoot.sendMessage(NmType.do_find_user, searchString);
    }


    public void onDataReceived(NetSearchResultData netSearchResultData) {
        customizableListYio.clearItems();

        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle("Users");
        customizableListYio.addItem(titleListItem);

        for (Map.Entry<String, String> entry : netSearchResultData.map.entrySet()) {
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setKey(entry.getKey());
            scrollListItem.setTitle(CharLocalizerYio.getInstance().apply(entry.getValue()));
            scrollListItem.setColored(false);
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
                Scenes.confirmAddModerator.setValues(item.getKey(), item.title.string);
                Scenes.confirmAddModerator.create();
            }
        };
    }


    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
