package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.FishProductItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderFishProductItem extends AbstractRenderCustomListItem{


    private TextureRegion fishTexture;
    private FishProductItem fdItem;


    @Override
    public void loadTextures() {
        fishTexture = GraphicsYio.loadTextureRegion("menu/shop/fish.png", true);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        fdItem = (FishProductItem) item;

        renderDarken();
        GraphicsYio.renderItyOptimized(
                batch,
                blackPixel,
                fishTexture,
                fdItem.iconTextYio,
                alpha
        );
        GraphicsYio.renderTextOptimized(batch, blackPixel, fdItem.priceViewText, alpha);
        renderDefaultSelection(item);
    }


    private void renderDarken() {
        if (!fdItem.darken) return;
        GraphicsYio.setBatchAlpha(batch, 0.04 * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, fdItem.viewPosition);
    }
}
