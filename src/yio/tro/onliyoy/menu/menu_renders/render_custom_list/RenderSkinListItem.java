package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SkinListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SliLocalPieceIcon;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderSkinListItem extends AbstractRenderCustomListItem{


    private SkinListItem slItem;
    private TextureRegion checkmarkTexture;


    @Override
    public void loadTextures() {
        checkmarkTexture = GraphicsYio.loadTextureRegion("menu/v_icon.png", true);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        slItem = (SkinListItem) item;

        renderDarken();
        renderTitle();
        renderIcons();
        renderCheckmark();
        renderDefaultSelection(item);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderCheckmark() {
        if (!slItem.active) return;
        GraphicsYio.drawByCircle(batch, checkmarkTexture, slItem.checkmarkPosition);
    }


    private void renderIcons() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        for (SliLocalPieceIcon icon : slItem.icons) {
            GraphicsYio.drawByCircle(
                    batch,
                    MenuRenders.renderShopViewElement.mapSkinTextures.get(icon.key),
                    icon.position
            );
        }
    }


    private void renderTitle() {
        GraphicsYio.renderTextOptimized(batch, blackPixel, slItem.title, alpha);
    }


    private void renderDarken() {
        if (!slItem.darken) return;
        GraphicsYio.setBatchAlpha(batch, 0.04 * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, slItem.viewPosition);
    }
}
