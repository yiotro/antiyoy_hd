package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.RelationType;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RveChooseConditionTypeItem extends AbstractRveClickableItem {


    @Override
    protected void initIcons() {
        addIcon(RveIconType.money);
        addIcon(RveIconType.lands);
        addIcon(RveIconType.relation);
        addIcon(RveIconType.notification);
        addIcon(RveIconType.smileys);
    }


    @Override
    protected void updateIconPositions() {
        float rowWidth = getIconRowWidth();
        float x = position.x + position.width / 2 - rowWidth / 2;
        for (RveIcon rveIcon : icons) {
            x += rveIcon.position.radius;
            rveIcon.position.center.y = position.y + position.height / 2;
            rveIcon.position.center.x = x;
            x += rveIcon.position.radius;
        }
    }


    private float getIconRowWidth() {
        float s = 0;
        for (RveIcon rveIcon : icons) {
            s += 2 * rveIcon.position.radius;
        }
        return s;
    }


    @Override
    protected void initialize() {

    }


    @Override
    protected void onMove() {

    }


    @Override
    protected void applyIconReaction(RveIconType rveIconType) {
        ViewableModel viewableModel = resizableViewElement.getViewableModel();
        PlayerEntity recipient = Scenes.composeLetter.recipient;
        PlayerEntity currentEntity = viewableModel.entitiesManager.getCurrentEntity();
        switch (rveIconType) {
            default:
                break;
            case money:
                Scenes.setupMoneyCondition.setRveChooseConditionTypeItem(this);
                Scenes.setupMoneyCondition.create();
                break;
            case lands:
                Scenes.composeLetter.destroy();
                resizableViewElement.getGameController().setTouchMode(TouchMode.tmChooseLands);
                TouchMode.tmChooseLands.setRveChooseConditionTypeItem(this);
                break;
            case relation:
                Scenes.setupRelationCondition.setRveChooseConditionTypeItem(this);
                Scenes.setupRelationCondition.create();
                Scenes.setupRelationCondition.setTargetEntity(currentEntity);
                break;
            case notification:
                if (currentEntity.getRelation(recipient).type == RelationType.war) break;
                Scenes.setupRveNotification.setRveChooseConditionTypeItem(this);
                Scenes.setupRveNotification.setRecipient(recipient);
                Scenes.setupRveNotification.create();
                Scenes.setupRveNotification.checkToSkipThisStep();
                break;
            case smileys:
                Scenes.setupSmileysCondition.setRveChooseConditionTypeItem(this);
                Scenes.setupSmileysCondition.create();
                break;
        }
    }


    @Override
    protected float getHeight() {
        return 0.07f * GraphicsYio.height;
    }


    @Override
    public AbstractRveRender getRender() {
        return MenuRenders.renderRveChooseConditionTypeItem;
    }
}
