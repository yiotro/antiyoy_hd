package yio.tro.onliyoy.menu.scenes.options;

import yio.tro.onliyoy.menu.CustomLanguageLoader;
import yio.tro.onliyoy.menu.LanguageChooseItem;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.ScrollListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SliReaction;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.ArrayList;

public class SceneLanguages extends SceneYio {

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    public void initialize() {
        initList();
        spawnBackButton(getBackReaction());
    }


    private void initList() {
        CustomizableListYio customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAnimation(AnimationYio.from_touch);

        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle(LanguagesManager.getInstance().getString("languages"));
        customizableListYio.addItem(titleListItem);

        SliReaction clickReaction = getClickReaction();
        ArrayList<LanguageChooseItem> chooseListItems = LanguagesManager.getInstance().getChooseListItems();
        for (LanguageChooseItem chooseListItem : chooseListItems) {
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setTitle(chooseListItem.title);
            scrollListItem.setKey(chooseListItem.name);
            scrollListItem.setClickReaction(clickReaction);
            scrollListItem.setHeight(0.08f * GraphicsYio.height);
            customizableListYio.addItem(scrollListItem);
        }
    }


    private SliReaction getClickReaction() {
        return new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                applyLanguage(item.key);
            }
        };
    }


    private void applyLanguage(String key) {
        CustomLanguageLoader.setAndSaveLanguage(key);
        menuControllerYio.clear();
        Scenes.mainLobby.create();
    }



    private Reaction getBackReaction() {
        return getOpenSceneReaction(Scenes.settings);
    }
}
