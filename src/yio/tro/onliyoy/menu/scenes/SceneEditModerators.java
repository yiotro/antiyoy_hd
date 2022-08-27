package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.*;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetSearchResultData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.Map;

public class SceneEditModerators extends SceneYio{


    private CustomizableListYio customizableListYio;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.red;
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
        netRoot.sendMessage(NmType.request_list_of_moderators, "");
    }


    public void onDataReceived(NetSearchResultData netSearchResultData) {
        customizableListYio.clearItems();

        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle("Moderators");
        customizableListYio.addItem(titleListItem);

        for (Map.Entry<String, String> entry : netSearchResultData.map.entrySet()) {
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setHeight(0.07f * GraphicsYio.height);
            scrollListItem.setKey(entry.getKey());
            scrollListItem.setTitle(CharLocalizerYio.getInstance().apply(entry.getValue()));
            scrollListItem.setColored(false);
            scrollListItem.setClickReaction(getClickReaction());
            customizableListYio.addItem(scrollListItem);
        }

        ScrollListItem additionItem = new ScrollListItem();
        additionItem.setTitle("+");
        additionItem.setCentered(true);
        additionItem.setColored(false);
        additionItem.setClickReaction(getAdditionReaction());
        customizableListYio.addItem(additionItem);
    }


    private SliReaction getAdditionReaction() {
        return new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                Scenes.modAdditionReminder.create();
            }
        };
    }


    private SliReaction getClickReaction() {
        return new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                Scenes.moderatorContextMenuByAdmin.setId(item.getKey());
                Scenes.moderatorContextMenuByAdmin.setName(item.title.string);
                Scenes.moderatorContextMenuByAdmin.create();
            }
        };
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAnimation(AnimationYio.from_touch);
    }
}
