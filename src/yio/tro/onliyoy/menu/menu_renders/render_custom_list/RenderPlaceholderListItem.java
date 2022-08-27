package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.PlaceholderListItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderPlaceholderListItem extends AbstractRenderCustomListItem{

    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        PlaceholderListItem elItem = (PlaceholderListItem) item;
        GraphicsYio.renderTextOptimized(batch, blackPixel, elItem.title, 0.4f * alpha);
    }
}
