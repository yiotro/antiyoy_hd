package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.InGameEntityListItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderInGameEntityListItem extends AbstractRenderCustomListItem{

    InGameEntityListItem entityListItem;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        entityListItem = (InGameEntityListItem) item;

        renderBackground();
        renderTextOptimized(entityListItem.title, alpha);
        renderDefaultSelection(entityListItem);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderBackground() {
        GraphicsYio.drawByRectangle(
                batch,
                MenuRenders.renderUiColors.map.get(entityListItem.color),
                entityListItem.viewPosition
        );
    }
}
