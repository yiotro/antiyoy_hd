package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.EconomyListItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderEconomyListItem extends AbstractRenderCustomListItem{


    private EconomyListItem economyListItem;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        economyListItem = (EconomyListItem) item;

        renderDarken();
        GraphicsYio.renderTextOptimized(batch, blackPixel, economyListItem.name, alpha);
        GraphicsYio.renderTextOptimized(batch, blackPixel, economyListItem.value, alpha);
        renderDefaultSelection(economyListItem);
    }


    private void renderDarken() {
        if (!economyListItem.darken) return;
        GraphicsYio.setBatchAlpha(batch, 0.04 * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, economyListItem.viewPosition);
    }
}
