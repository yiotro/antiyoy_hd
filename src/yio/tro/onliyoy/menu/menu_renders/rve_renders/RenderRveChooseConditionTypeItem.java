package yio.tro.onliyoy.menu.menu_renders.rve_renders;

import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveClickableItem;
import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveConditionItem;
import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveItem;

public class RenderRveChooseConditionTypeItem extends AbstractRveClickableRender{


    @Override
    public void renderItem(AbstractRveItem rveItem, double alpha) {
        renderIcons((AbstractRveClickableItem) rveItem, alpha);
    }

}
