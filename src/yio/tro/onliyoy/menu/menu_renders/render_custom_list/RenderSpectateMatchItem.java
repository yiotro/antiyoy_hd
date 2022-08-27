package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SpectateMatchItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderSpectateMatchItem extends AbstractRenderCustomListItem{

    private SpectateMatchItem smItem;
    private TextureRegion passwordTexture;


    @Override
    public void loadTextures() {
        passwordTexture = GraphicsYio.loadTextureRegion("menu/net/password.png", true);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        smItem = (SpectateMatchItem) item;
        GraphicsYio.renderTextOptimized(batch, blackPixel, smItem.name, alpha);
        GraphicsYio.renderTextOptimized(batch, blackPixel, smItem.desc1, 0.5f * alpha * smItem.selfScroll1.getAlpha());
        GraphicsYio.renderTextOptimized(batch, blackPixel, smItem.desc2, 0.5f * alpha * smItem.selfScroll2.getAlpha());
        if (smItem.netMatchSpectateData.hasPassword) {
            GraphicsYio.drawByCircle(batch, passwordTexture, smItem.passwordPosition);
        }
        renderDefaultSelection(smItem);
    }
}
