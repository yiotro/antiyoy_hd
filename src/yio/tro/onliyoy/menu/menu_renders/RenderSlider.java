package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RenderSlider extends RenderInterfaceElement{


    private TextureRegion sideTexture;
    private TextureRegion lineTexture;
    private SliderElement sliderElement;
    private TextureRegion indicatorTexture;
    private TextureRegion selectionTexture;


    @Override
    public void loadTextures() {
        sideTexture = loadSliderTexture("side");
        lineTexture = loadSliderTexture("line");
        indicatorTexture = loadSliderTexture("indicator");
        selectionTexture = GraphicsYio.loadTextureRegion("menu/selection.png", false);
    }


    private TextureRegion loadSliderTexture(String name) {
        return GraphicsYio.loadTextureRegion("menu/slider/" + name + ".png", true);
    }


    @Override
    public void render(InterfaceElement element) {
        sliderElement = (SliderElement) element;

        if (sliderElement.getFactor().getValue() < 0.12) return;

        renderViewText(sliderElement.title);
        renderViewText(sliderElement.valueViewText);
        renderLine();
        renderSectors();
        renderSelection();
        renderIndicator();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderViewText(RenderableTextYio renderableTextYio) {
        if (sliderElement.getViewPosition().width < renderableTextYio.width) return;
        GraphicsYio.renderTextOptimized(batch, blackPixel, renderableTextYio, alpha);
    }


    private void renderSectors() {
        if (!sliderElement.areSectorsCurrentlyVisible()) return;
        for (RectangleYio rectangleYio : sliderElement.sectors) {
            GraphicsYio.drawByRectangle(batch, lineTexture, rectangleYio);
        }
    }


    private void renderSelection() {
        if (!sliderElement.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * sliderElement.selectionEngineYio.getAlpha());
        GraphicsYio.drawByCircle(batch, selectionTexture, sliderElement.selectionPosition);
        GraphicsYio.setBatchAlpha(batch, 0.1 * alpha * sliderElement.selectionEngineYio.getAlpha());
        GraphicsYio.drawByRectangle(batch, blackPixel, sliderElement.getViewPosition());
    }


    private void renderIndicator() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByCircle(batch, indicatorTexture, sliderElement.indicatorPosition);
    }


    private void renderLine() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        if (!sliderElement.areSectorsCurrentlyVisible()) {
            GraphicsYio.drawByRectangle(batch, lineTexture, sliderElement.line);
        }
        GraphicsYio.drawByRectangle(batch, blackPixel, sliderElement.leftSide);
        GraphicsYio.drawByRectangle(batch, blackPixel, sliderElement.rightSide);
    }


    private void renderViewPosition() {
        GraphicsYio.setBatchAlpha(batch, 0.1 * alpha);
        GraphicsYio.renderBorder(batch, blackPixel, sliderElement.getViewPosition());
    }
}
