package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderLabelElement extends RenderInterfaceElement{


    private LabelElement labelElement;


    @Override
    public void loadTextures() {

    }


    @Override
    public void render(InterfaceElement element) {
        labelElement = (LabelElement) element;
        if (!labelElement.backgroundEnabled) {
            renderWithoutBackground();
            return;
        }
        renderWithBackground();
    }


    private void renderWithBackground() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangleUpsideDown(batch, labelElement.cacheTitleTexture, labelElement.incBounds);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderWithoutBackground() {
        GraphicsYio.renderTextOptimized(
                batch,
                blackPixel,
                labelElement.title,
                labelElement.getFactor().getValue() * labelElement.targetAlpha
        );
    }

}
