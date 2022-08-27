package yio.tro.onliyoy.menu.menu_renders.rve_renders;

import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveConditionItem;
import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveItem;

public class RenderRveRelationItem extends AbstractRveClickableRender{

    @Override
    public void renderItem(AbstractRveItem rveItem, double alpha) {
        renderColorBounds(rveItem);
        renderTitle((AbstractRveConditionItem) rveItem, alpha);
        renderIcons((AbstractRveConditionItem) rveItem, alpha);
        renderSelection((AbstractRveConditionItem) rveItem, alpha);
    }
}
