package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.icon_label_element.IconLabelElement;
import yio.tro.onliyoy.menu.elements.icon_label_element.IleIcon;
import yio.tro.onliyoy.menu.elements.icon_label_element.IleTextureType;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

import java.util.HashMap;

public class RenderIconLabelElement extends RenderInterfaceElement{

    HashMap<IleTextureType, TextureRegion> mapTextures;
    private IconLabelElement iconLabelElement;
    private TextureRegion backgroundTexture;
    private BitmapFont font;


    @Override
    public void loadTextures() {
        mapTextures = new HashMap<>();
        for (IleTextureType ileTextureType : IleTextureType.values()) {
            mapTextures.put(ileTextureType, GraphicsYio.loadTextureRegion("menu/icon_label/" + ileTextureType + ".png", true));
        }
        backgroundTexture = GraphicsYio.loadTextureRegion("pixels/dark.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        iconLabelElement = (IconLabelElement) element;

        if (iconLabelElement.getFactor().getValue() < 0.01) return;

        renderSlowly();
        renderSelection();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderSelection() {
        if (!iconLabelElement.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * iconLabelElement.selectionEngineYio.getAlpha());
        GraphicsYio.drawByRectangle(batch, blackPixel, iconLabelElement.touchArea);
    }


    private void renderSlowly() {
        renderBackground();
        renderTexts();
        renderIcons();
    }


    private void renderBackground() {
        if (!iconLabelElement.backgroundEnabled) return;
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(batch, backgroundTexture, iconLabelElement.incBounds);
    }


    private void renderIcons() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        for (IleIcon icon : iconLabelElement.icons) {
            GraphicsYio.drawByRectangle(batch, mapTextures.get(icon.textureType), icon.viewPosition);
        }
    }


    private void renderTexts() {
        for (RenderableTextYio text : iconLabelElement.texts) {
            if (!iconLabelElement.backgroundEnabled) {
                GraphicsYio.renderTextOptimized(batch, blackPixel, text, alpha);
                continue;
            }
            font = text.font;
            font.setColor(1, 1, 1, alpha);
            GraphicsYio.renderText(batch, text);
            font.setColor(0, 0, 0, 1);
        }
    }

}
