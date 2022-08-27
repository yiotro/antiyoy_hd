package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.NumberedSeparatorItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderNumberedSeparatorItem extends AbstractRenderCustomListItem{


    private TextureRegion separatorTexture;
    private NumberedSeparatorItem slItem;


    @Override
    public void loadTextures() {
        separatorTexture = GraphicsYio.loadTextureRegion("menu/separator.png", true);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        slItem = (NumberedSeparatorItem) item;
        GraphicsYio.renderTextOptimized(batch, blackPixel, slItem.title, 0.4f * alpha);
        GraphicsYio.setBatchAlpha(batch, 0.33 * alpha);
        GraphicsYio.drawByRectangle(batch, separatorTexture, slItem.leftBounds);
        GraphicsYio.drawByRectangle(batch, separatorTexture, slItem.rightBounds);
        GraphicsYio.setBatchAlpha(batch, 1);
    }
}
