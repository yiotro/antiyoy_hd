package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.save_system.UserLevelsProgressManager;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.OfflineUlevItem;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NetUserLevelData;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.ArrayList;

public class SceneOfflineUserLevels extends SceneYio {

    private CustomizableListYio customizableListYio;
    boolean buffered;
    private String megaCode;
    public ArrayList<NetUserLevelData> dataList;
    private boolean filterHideCompleted;
    private LevelSize filterLevelSize;
    private int filterColorQuantity;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected void initialize() {
        buffered = false;
        createList();
        createSearchButton();
        createFiltersButton();
        spawnBackButton(getBackReaction());
    }


    private void createFiltersButton() {
        uiFactory.getCircleButton()
                .setSize(GraphicsYio.convertToWidth(0.09))
                .centerHorizontal()
                .alignTop(0.02)
                .setTouchOffset(0.05)
                .loadTexture("menu/user_levels/filters.png")
                .setAnimation(AnimationYio.up)
                .setReaction(getOpenSceneReaction(Scenes.ulevFilters));
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
                Scenes.offlineUlevSearch.setSearchString(input);
                Scenes.offlineUlevSearch.create();
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
        loadValues();
    }


    public void loadValues() {
        customizableListYio.clearItems();
        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle(LanguagesManager.getInstance().getString("user_levels"));
        customizableListYio.addItem(titleListItem);
        checkToBuffer();
        prepareFilters();
        for (NetUserLevelData netUserLevelData : dataList) {
            if (!isAllowedByFilters(netUserLevelData)) continue;
            OfflineUlevItem offlineUlevItem = new OfflineUlevItem();
            offlineUlevItem.setNetUserLevelData(netUserLevelData);
            customizableListYio.addItem(offlineUlevItem);
        }
        customizableListYio.moveElement();
        customizableListYio.moveElement();
    }


    private boolean isAllowedByFilters(NetUserLevelData netUserLevelData) {
        if (filterLevelSize != null && netUserLevelData.levelSize != filterLevelSize) return false;
        if (filterColorQuantity > 0 && netUserLevelData.colorsQuantity != filterColorQuantity) return false;
        if (filterHideCompleted && UserLevelsProgressManager.getInstance().isCompleted(netUserLevelData.id)) return false;
        return true;
    }


    private void prepareFilters() {
        Preferences preferences = getPreferences();
        filterHideCompleted = preferences.getBoolean("hide_completed", false);
        try {
            filterLevelSize = LevelSize.valueOf(preferences.getString("level_size"));
        } catch (Exception e) {
            filterLevelSize = null;
        }
        filterColorQuantity = preferences.getInteger("colors", 0);
    }


    private void checkToBuffer() {
        if (buffered) return;
        buffered = true;
        megaCode = readMegaCodeFromFile();
        dataList = new ArrayList<>();
        for (String token : megaCode.split("%")) {
            if (token.length() < 5) continue;
            NetUserLevelData netUserLevelData = new NetUserLevelData();
            netUserLevelData.decode(token);
            dataList.add(netUserLevelData);
        }
    }


    private String readMegaCodeFromFile() {
        FileHandle fileHandle = Gdx.files.internal("offline_user_levels.stf");
        return fileHandle.readString();
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAnimation(AnimationYio.from_touch);
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences("yio.tro.onliyoy.user_level_filters");
    }
}
