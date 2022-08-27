package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.*;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NetUlCacheData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.ArrayList;

public class SceneUserLevels extends SceneYio{

    private CustomizableListYio customizableListYio;


    public SceneUserLevels() {
        customizableListYio = null;
    }


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.yellow;
    }


    @Override
    protected void initialize() {
        createList();
        createSearchButton();
        spawnBackButton(getBackReaction());
    }


    private void createSearchButton() {
        uiFactory.getCircleButton()
                .setSize(GraphicsYio.convertToWidth(0.09))
                .alignRight(0.04)
                .alignTop(0.02)
                .setTouchOffset(0.05)
                .loadTexture("menu/user_levels/search.png")
                .setAnimation(AnimationYio.up)
                .setReaction(getSearchReaction());
    }


    private Reaction getSearchReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onSearchButtonPressed();
            }
        };
    }


    private void onSearchButtonPressed() {
        Scenes.keyboard.create();
        Scenes.keyboard.setHint("level_name");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() < 2) return;
                Scenes.searchUserLevel.setSearchString(input);
                Scenes.searchUserLevel.create();
            }
        });
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                MenuSwitcher.getInstance().createChooseGameModeMenu();
            }
        };
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        customizableListYio.clearItems();
        customizableListYio.addItem(new PlaceholderListItem("..."));
        netRoot.sendMessage(NmType.get_user_levels_list, "");
    }


    public void onCodeReceived(ArrayList<NetUlCacheData> dataList) {
        customizableListYio.clearItems();
        if (dataList.size() == 0) return;

        addTitleItem("fresh_levels");
        for (NetUlCacheData netUlCacheData : dataList) {
            if (netUlCacheData.id.equals("-")) {
                addTitleItem("archive");
                continue;
            }
            AbstractCustomListItem ulItem = createUlItem(netUlCacheData);
            customizableListYio.addItem(ulItem);
        }
        addBottomHint();
    }


    private void addBottomHint() {
        if (netRoot.initialStatisticsData.getHoursOnline() >= 6) return;
        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setHeight(0.04f * GraphicsYio.height);
        titleListItem.setAlpha(0.5f);
        titleListItem.setTitle(languagesManager.getString("user_levels_bottom_hint"));
        customizableListYio.addItem(titleListItem);
    }


    private AbstractCustomListItem createUlItem(NetUlCacheData netUlCacheData) {
        if (SettingsManager.getInstance().detailedUserLevels) {
            DetailedUlListItem detailedUlListItem = new DetailedUlListItem();
            detailedUlListItem.setNetUlCacheData(netUlCacheData);
            return detailedUlListItem;
        }
        UserLevelListItem userLevelListItem = new UserLevelListItem();
        userLevelListItem.setNetUlCacheData(netUlCacheData);
        return userLevelListItem;
    }


    private void addTitleItem(String key) {
        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle(LanguagesManager.getInstance().getString(key));
        customizableListYio.addItem(titleListItem);
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAnimation(AnimationYio.from_touch);
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
