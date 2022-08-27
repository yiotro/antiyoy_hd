package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.ScrollListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SliReaction;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.net.NetQuickMaps;

public class SceneDebugChooseQuickMap extends SceneYio{


    private CustomizableListYio customizableListYio;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getOpenSceneReaction(Scenes.debugTests));
        createList();
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();

        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle("Quick maps");
        customizableListYio.addItem(titleListItem);

        NetQuickMaps netQuickMaps = new NetQuickMaps();
        int index = 1;
        for (String levelCode : netQuickMaps.quick) {
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setTitle("Net map " + index);
            scrollListItem.setKey(levelCode);
            scrollListItem.setColored(false);
            scrollListItem.setClickReaction(getClickReaction());
            customizableListYio.addItem(scrollListItem);
            index++;
        }
    }


    private SliReaction getClickReaction() {
        return new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                String levelCode = item.getKey();
                (new IwClientInit(yioGdxGame, LoadingType.editor_import)).perform(levelCode);
            }
        };
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.8, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAnimation(AnimationYio.from_touch);
    }


}
