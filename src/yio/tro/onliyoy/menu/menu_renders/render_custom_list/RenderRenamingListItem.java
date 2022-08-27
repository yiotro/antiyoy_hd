package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.RenamingListItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderRenamingListItem extends AbstractRenderCustomListItem{


    private TextureRegion redPixel;


    @Override
    public void loadTextures() {
        redPixel = GraphicsYio.loadTextureRegion("pixels/red.png", false);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        RenamingListItem rlItem = (RenamingListItem) item;
        if (rlItem.darken) {
            GraphicsYio.setBatchAlpha(batch, 0.04 * alpha);
            GraphicsYio.drawByRectangle(batch, blackPixel, rlItem.viewPosition);
            GraphicsYio.setBatchAlpha(batch, alpha);
        }
        if (rlItem.declined) {
            GraphicsYio.setBatchAlpha(batch, 0.25 * alpha);
            GraphicsYio.drawByRectangle(batch, redPixel, rlItem.viewPosition);
            GraphicsYio.setBatchAlpha(batch, alpha);
            GraphicsYio.drawByRectangle(batch, blackPixel, rlItem.strikethrough);
        }
        renderTextOptimized(rlItem.currentNameView, alpha);
        renderTextOptimized(rlItem.arrowView, alpha);
        renderTextOptimized(rlItem.desiredNameView, alpha);
        renderDefaultSelection(item);
    }
}
