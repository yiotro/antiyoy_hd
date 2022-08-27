package yio.tro.onliyoy.menu.menu_renders.rve_renders;

import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveAddConditionItem;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

public class RenderRveAddConditionItem extends AbstractRveRender{

    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractRveItem rveItem, double alpha) {
        RveAddConditionItem rveAddConditionItem = (RveAddConditionItem) rveItem;
        GraphicsYio.setFontAlpha(rveAddConditionItem.renderableTextYio.font, alpha * alpha);
        GraphicsYio.renderText(batch, rveAddConditionItem.renderableTextYio);
        renderSelection(rveAddConditionItem, alpha);
    }


    private void renderSelection(RveAddConditionItem rveAddConditionItem, double alpha) {
        SelectionEngineYio selectionEngineYio = rveAddConditionItem.selectionEngineYio;
        if (!selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * selectionEngineYio.getAlpha());
        GraphicsYio.drawByRectangle(batch, blackPixel, rveAddConditionItem.position);
        GraphicsYio.setBatchAlpha(batch, 1);
    }
}
