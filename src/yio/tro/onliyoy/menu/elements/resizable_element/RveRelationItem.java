package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.RelationType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RveRelationItem extends AbstractRveConditionItem {

    public PlayerEntity playerEntity;
    public RelationType relationType;
    public int lock;


    public void setValues(PlayerEntity playerEntity, RelationType relationType, int lock) {
        this.playerEntity = playerEntity;
        this.relationType = relationType;
        this.lock = lock;
        String lockString = "";
        if (lock > 0) {
            lockString = ", " + lock + "x";
        }
        String relationString = LanguagesManager.getInstance().getString("" + relationType);
        String nameString = "";
        if (playerEntity != null) {
            nameString = playerEntity.name + ": ";
            enableColor(title.font, "", playerEntity.name, playerEntity.color);
        }
        setTitle(nameString + relationString + lockString);
    }


    @Override
    protected void applyClickReaction() {
        RveChooseConditionTypeItem rveChooseConditionTypeItem = new RveChooseConditionTypeItem();
        Scenes.setupRelationCondition.setRveChooseConditionTypeItem(rveChooseConditionTypeItem);
        Scenes.setupRelationCondition.create();
        Scenes.setupRelationCondition.setTargetEntity(resizableViewElement.getViewableModel().entitiesManager.getCurrentEntity());
        Scenes.setupRelationCondition.setRelationType(relationType);
        Scenes.setupRelationCondition.setLock(lock);
        resizableViewElement.swapItem(this, rveChooseConditionTypeItem);
    }


    @Override
    public AbstractRveRender getRender() {
        return MenuRenders.renderRveRelationItem;
    }
}