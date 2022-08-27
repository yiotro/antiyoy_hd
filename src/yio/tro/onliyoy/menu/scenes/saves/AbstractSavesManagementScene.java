package yio.tro.onliyoy.menu.scenes.saves;

import yio.tro.onliyoy.game.save_system.SaveType;
import yio.tro.onliyoy.game.save_system.SavesManager;
import yio.tro.onliyoy.game.save_system.SmItem;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.ScrollListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SliReaction;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;

public abstract class AbstractSavesManagementScene extends SceneYio {

    protected CustomizableListYio customizableListYio;
    private SliReaction clickReaction;
    private SliReaction longTapReaction;


    public AbstractSavesManagementScene() {
        customizableListYio = null;
    }


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        initReactions();
        createList();
        spawnBackButton(getCloseReaction());
    }


    private void initReactions() {
        clickReaction = new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                AbstractSavesManagementScene.this.onItemClicked(item.key);
            }
        };

        longTapReaction = new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                AbstractSavesManagementScene.this.onItemLongTapped(item.key);
            }
        };
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        updateList();
    }


    public void updateList() {
        if (customizableListYio == null) return;

        customizableListYio.clearItems();
        SavesManager savesManager = getSavesManager();

        addTitle();
        checkToAddCreationItem();

        int size = savesManager.items.size();
        for (int i = size - 1; i >= 0; i--) {
            SmItem item = savesManager.items.get(i);
            if (!isSmItemVisible(item)) continue;

            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setTitle(item.name);
            scrollListItem.setKey(item.key);
            scrollListItem.setClickReaction(clickReaction);
            scrollListItem.setLongTapReaction(longTapReaction);
            customizableListYio.addItem(scrollListItem);
        }
    }


    private void addTitle() {
        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle(languagesManager.getString(getTitleKey()));
        customizableListYio.addItem(titleListItem);
    }


    private void checkToAddCreationItem() {
        if (isInReadMode()) return;
        ScrollListItem firstListItem = new ScrollListItem();
        String string = LanguagesManager.getInstance().getString(getCreationTitleKey());
        firstListItem.setTitle("[" + string + "]");
        firstListItem.setKey("create");
        firstListItem.setClickReaction(clickReaction);
        customizableListYio.addItem(firstListItem);
    }


    protected String getCreationTitleKey() {
        return "create";
    }


    protected abstract String getTitleKey();


    protected abstract boolean isInReadMode();


    protected abstract SaveType getCurrentSaveType();


    protected abstract void onItemClicked(String key);


    protected boolean isSmItemVisible(SmItem smItem) {
        return smItem.type == getCurrentSaveType();
    }


    protected void onItemLongTapped(String key) {
        Scenes.slotContextMenu.create();
        Scenes.slotContextMenu.setValues(this, key);
    }


    protected abstract Reaction getCloseReaction();


    protected SavesManager getSavesManager() {
        return yioGdxGame.gameController.savesManager;
    }


    public void onListUpdateRequestedFromExternalSource() {
        updateList();
        customizableListYio.moveElement();
        customizableListYio.moveElement();
    }


    protected void showKeyboardForNewSave(final String exportedLevelCode) {
        Scenes.keyboard.create();
        Scenes.keyboard.setValue("");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) return;
                getSavesManager().addItem(getCurrentSaveType(), input, exportedLevelCode);
                getCloseReaction().perform(menuControllerYio);
            }
        });
    }

}
