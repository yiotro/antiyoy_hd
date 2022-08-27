package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.net.NetProcessViewElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderNetProcessViewElement extends RenderInterfaceElement{


    private NetProcessViewElement npvElement;
    private TextureRegion iconTexture;
    private TextureRegion backgroundPixel;


    @Override
    public void loadTextures() {
        iconTexture = GraphicsYio.loadTextureRegion("menu/net/processing.png", true);
        backgroundPixel = GraphicsYio.loadTextureRegion("pixels/white.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        npvElement = (NetProcessViewElement) element;
        renderBackground();
        GraphicsYio.renderTextOptimized(batch, blackPixel, npvElement.title, alpha);
        renderIcon();
    }


    private void renderBackground() {
        if (!npvElement.backgroundEnabled) return;
        GraphicsYio.setBatchAlpha(batch, 0.33f * alpha);
        GraphicsYio.drawByRectangle(batch, backgroundPixel, npvElement.backgroundPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderIcon() {
        if (npvElement.clockMode) return;
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByCircle(batch, iconTexture, npvElement.iconPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }
}
