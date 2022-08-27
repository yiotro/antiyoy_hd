package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.TopCoverElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderTopCoverElement extends RenderInterfaceElement{


    private TextureRegion backgroundTexture;
    private TopCoverElement tcElement;


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/round_shape/white.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        tcElement = (TopCoverElement) element;
        if (tcElement.visibilityFactor.getValue() == 0) return;
        renderShadow();
        renderBackground();
        renderTitle();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderShadow() {
        GraphicsYio.setBatchAlpha(batch, tcElement.getShadowAlpha());
        MenuRenders.renderShadow.renderBottomPart(tcElement.shadowPosition);
    }


    private void renderTitle() {
        GraphicsYio.renderTextOptimized(
                batch,
                blackPixel,
                tcElement.title,
                alpha * tcElement.visibilityFactor.getValue()
        );
    }


    private void renderBackground() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(batch, backgroundTexture, tcElement.getViewPosition());
    }
}
