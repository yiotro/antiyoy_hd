package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RveMoneyItem extends AbstractRveConditionItem {

    public int value;


    public void setValue(int value) {
        this.value = value;
        setTitle(LanguagesManager.getInstance().getString("money") + ": $" + value);
    }


    @Override
    protected void applyClickReaction() {
        RveChooseConditionTypeItem rveChooseConditionTypeItem = new RveChooseConditionTypeItem();
        Scenes.setupMoneyCondition.setRveChooseConditionTypeItem(rveChooseConditionTypeItem);
        Scenes.setupMoneyCondition.create();
        Scenes.setupMoneyCondition.setValue(value);
        resizableViewElement.swapItem(this, rveChooseConditionTypeItem);
    }


    @Override
    public AbstractRveRender getRender() {
        return MenuRenders.renderRveMoneyItem;
    }
}