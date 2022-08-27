package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.BigTextItem;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RenderBigTextItem extends AbstractRenderCustomListItem {

    RectangleYio screenPosition;
    private BigTextItem btItem;
    PointYio tempPoint;
    private float offset;


    public RenderBigTextItem() {
        super();
        screenPosition = new RectangleYio(0, 0, GraphicsYio.width, GraphicsYio.height);
        tempPoint = new PointYio();
        offset = 0.1f * GraphicsYio.height;
    }


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        btItem = (BigTextItem) item;

        for (RenderableTextYio renderableTextYio : btItem.visualTextContainer.viewList) {
            tempPoint.set(
                    renderableTextYio.position.x + renderableTextYio.width / 2,
                    renderableTextYio.position.y - renderableTextYio.height / 2
            );
            if (!screenPosition.isPointInside(tempPoint, offset)) continue;
            GraphicsYio.setFontAlpha(renderableTextYio.font, alpha);
            GraphicsYio.renderText(batch, renderableTextYio);
        }
    }
}
