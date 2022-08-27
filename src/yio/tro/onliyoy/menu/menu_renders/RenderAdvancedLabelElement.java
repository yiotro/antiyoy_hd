package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.AdvancedLabelElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RenderAdvancedLabelElement extends RenderInterfaceElement{

    private AdvancedLabelElement alElement;
    RenderableTextYio tempRenderableText;
    private TextureRegion backgroundTexture;
    private TextureRegion redPixel;


    public RenderAdvancedLabelElement() {
        tempRenderableText = new RenderableTextYio();
    }


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("pixels/dark.png", false);
        redPixel = GraphicsYio.loadTextureRegion("pixels/red.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        alElement = (AdvancedLabelElement) element;

        renderBackground();
        renderText();
    }


    private void renderText() {
        for (RenderableTextYio renderableTextYio : alElement.visualTextContainer.textList) {
            GraphicsYio.setFontAlpha(renderableTextYio.font, alpha);
            tempRenderableText.setBy(renderableTextYio);
            if (alElement.backgroundEnabled) {
                tempRenderableText.font.setColor(alElement.textOpacity, alElement.textOpacity, alElement.textOpacity, alpha);
            }
            tempRenderableText.position.x += alElement.getViewPosition().x;
            tempRenderableText.position.y += alElement.getViewPosition().y;
            tempRenderableText.updateBounds();
            if (alElement.hasParent() && !alElement.getParent().getViewPosition().contains(tempRenderableText.bounds)) continue;
            GraphicsYio.renderText(batch, tempRenderableText);
            GraphicsYio.setFontAlpha(renderableTextYio.font, 1);
            if (alElement.backgroundEnabled) {
                tempRenderableText.font.setColor(Color.BLACK);
            }
        }
    }


    private void renderBackground() {
        if (!alElement.backgroundEnabled) return;
        GraphicsYio.setBatchAlpha(batch, alElement.backgroundOpacity * alpha);
        GraphicsYio.drawByRectangle(batch, backgroundTexture, alElement.viewBounds);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


}
