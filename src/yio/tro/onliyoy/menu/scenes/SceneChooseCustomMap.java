package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.game.save_system.SaveType;
import yio.tro.onliyoy.game.save_system.SavesManager;
import yio.tro.onliyoy.game.save_system.SmItem;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.ScrollListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SliReaction;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NetValues;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneChooseCustomMap extends ModalSceneYio {

    protected CustomizableListYio customizableListYio;
    private SliReaction clickReaction;
    private double h;


    @Override
    protected void initialize() {
        h = 0.5;
        createCloseButton();
        createDarken();
        createDefaultPanel(h);
        initReactions();
        createList();
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setParent(defaultPanel)
                .setCornerRadius(0)
                .setSize(0.9, h - 0.02)
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
        SavesManager savesManager = getSavesManager();
        int size = savesManager.items.size();
        boolean darken = true;
        for (int i = size - 1; i >= 0; i--) {
            SmItem item = savesManager.items.get(i);
            if (item.type != SaveType.editor) continue;
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setTitle(item.name);
            scrollListItem.setKey(item.key);
            scrollListItem.setColored(false);
            scrollListItem.setDarken(darken);
            scrollListItem.setHeight(0.07f * GraphicsYio.height);
            scrollListItem.setClickReaction(clickReaction);
            customizableListYio.addItem(scrollListItem);
            darken = !darken;
        }
    }


    private void initReactions() {
        clickReaction = new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                String levelCode = getSavesManager().getLevelCode(item.key);
                if (levelCode == null || levelCode.length() >= NetValues.MAX_MESSAGE_LENGTH) {
                    Scenes.notification.show("Level code is too big");
                    return;
                }
                destroy();
                Scenes.setupCustomMatch.onCustomMapChosen(levelCode);
            }
        };
    }


    @Override
    protected Reaction getCloseReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Scenes.setupCustomMatch.onCustomMapChosen("-");
            }
        };
    }


    protected SavesManager getSavesManager() {
        return yioGdxGame.gameController.savesManager;
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
