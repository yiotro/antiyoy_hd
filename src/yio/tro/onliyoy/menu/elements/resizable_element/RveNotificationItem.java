package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.game.core_model.RelationType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;

import static yio.tro.onliyoy.menu.elements.resizable_element.RveIconType.executor;

public class RveNotificationItem extends AbstractRveConditionItem{

    public RelationType relationType;


    @Override
    protected void initIcons() {
        super.initIcons();
        removeIcon(executor);
        addIcon(RveIconType.notification);
    }


    @Override
    protected void applyClickReaction() {

    }


    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
        setTitle(LanguagesManager.getInstance().getString("declared_" + relationType));
    }


    @Override
    public AbstractRveRender getRender() {
        return MenuRenders.renderRveNotificationItem;
    }

}
