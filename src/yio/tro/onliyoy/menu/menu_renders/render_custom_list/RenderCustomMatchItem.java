package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomMatchItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderCustomMatchItem extends AbstractRenderCustomListItem{


    private CustomMatchItem cmItem;
    private TextureRegion passwordTexture;


    @Override
    public void loadTextures() {
        passwordTexture = GraphicsYio.loadTextureRegion("menu/net/password.png", true);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        cmItem = (CustomMatchItem) item;
        GraphicsYio.renderTextOptimized(batch, blackPixel, cmItem.name, alpha);
        GraphicsYio.renderTextOptimized(batch, blackPixel, cmItem.desc1, 0.5f * alpha * cmItem.selfScrollWorkerYio.getAlpha());
        GraphicsYio.renderTextOptimized(batch, blackPixel, cmItem.desc2, 0.5f * alpha);
        GraphicsYio.renderTextOptimized(batch, blackPixel, cmItem.quantityViewText, alpha);
        if (cmItem.netMatchListData.hasPassword) {
            GraphicsYio.drawByCircle(batch, passwordTexture, cmItem.passwordPosition);
        }
        renderDefaultSelection(cmItem);
    }
}
