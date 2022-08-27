package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.game.core_model.EntitiesManager;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.InGameEntityListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.ScrollListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SliReaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.net.shared.NmbdItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneChooseEntity extends ModalSceneYio {


    private CustomizableListYio customizableListYio;
    public IEntityChoiceListener listener;
    private SliReaction clickReaction;


    public SceneChooseEntity() {
        listener = null;
    }


    @Override
    protected void initialize() {
        createCloseButton();
        initReactions();
        createCustomizableList();
    }


    private void initReactions() {
        clickReaction = new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                destroy();
                if (listener == null) return;
                HColor color = HColor.valueOf(item.key);
                EntitiesManager entitiesManager = customizableListYio.getViewableModel().entitiesManager;
                PlayerEntity playerEntity = entitiesManager.getEntity(color);
                listener.onEntityChosen(playerEntity);
            }
        };
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        customizableListYio.clearItems();
        PlayerEntity[] entities = customizableListYio.getViewableModel().entitiesManager.entities;
        boolean darken = false;
        for (PlayerEntity playerEntity : entities) {
            InGameEntityListItem entityListItem = new InGameEntityListItem();
            entityListItem.setKey("" + playerEntity.color);
            entityListItem.setDarken(darken);
            entityListItem.setTitle(getEntityName(playerEntity));
            entityListItem.setClickReaction(clickReaction);
            entityListItem.setHeight(0.06f * GraphicsYio.height);
            entityListItem.setColored(false); // applies to background yio
            entityListItem.setColor(playerEntity.color);
            darken = !darken;
            customizableListYio.addItem(entityListItem);
        }
    }


    private String getEntityName(PlayerEntity playerEntity) {
        if (getViewableModel().isNetMatch()) {
            NmbdItem item = netRoot.currentMatchData.getItem(playerEntity.color);
            if (item != null) {
                return item.name;
            }
        }
        return playerEntity.name;
    }


    private void createCustomizableList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.7, 0.4)
                .centerHorizontal()
                .alignBottom(0.03)
                .setAnimation(AnimationYio.down);
    }


    public void setListener(IEntityChoiceListener listener) {
        this.listener = listener;
    }
}
