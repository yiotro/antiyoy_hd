package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractSingleLineItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderSingleListItem extends AbstractRenderCustomListItem{

    private AbstractSingleLineItem slItem;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        slItem = (AbstractSingleLineItem) item;

        renderDarken();
        renderTextOptimized(slItem.title, alpha);
        renderDefaultSelection(slItem);
    }


    private void renderDarken() {
        if (!slItem.darken) return;
        GraphicsYio.setBatchAlpha(batch, slItem.darkValue * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, slItem.viewPosition);
    }
}
