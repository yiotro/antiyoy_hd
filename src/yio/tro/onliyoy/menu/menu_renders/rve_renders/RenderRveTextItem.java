package yio.tro.onliyoy.menu.menu_renders.rve_renders;

import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveTextItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderRveTextItem extends AbstractRveRender{


    private RveTextItem rveTextItem;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractRveItem rveItem, double alpha) {
        rveTextItem = (RveTextItem) rveItem;
        renderColorBounds(rveItem);
        GraphicsYio.renderTextOptimized(batch, blackPixel, rveTextItem.title, (float) alpha);
    }
}
