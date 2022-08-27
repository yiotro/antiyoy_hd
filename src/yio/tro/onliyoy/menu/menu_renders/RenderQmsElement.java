package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.net.QmsElement;
import yio.tro.onliyoy.menu.elements.net.QmsItem;
import yio.tro.onliyoy.menu.elements.net.QmsWave;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderQmsElement extends RenderInterfaceElement {


    private QmsElement qmsElement;
    private TextureRegion textureEmpty;
    private TextureRegion textureFilled;
    private TextureRegion backgroundTexture;


    @Override
    public void loadTextures() {
        textureEmpty = GraphicsYio.loadTextureRegion("menu/net/qms_empty.png", true);
        textureFilled = GraphicsYio.loadTextureRegion("menu/net/qms_filled.png", true);
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/background/cyan.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        qmsElement = (QmsElement) element;
        renderBackground();
        renderTitle();
        renderItems();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderBackground() {
        if (qmsElement.getFactor().isInDestroyState()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * alpha);
        GraphicsYio.drawByRectangle(
                batch,
                backgroundTexture,
                qmsElement.incBounds
        );
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderItems() {
        for (QmsItem item : qmsElement.items) {
            float iAlpha = item.appearFactor.getValue() * alpha;
            if (item.indicatorFactor.getValue() > 0) {
                GraphicsYio.setBatchAlpha(batch, item.indicatorFactor.getValue() * iAlpha);
                GraphicsYio.drawByCircle(batch, textureFilled, item.indicatorPosition);
            }
            GraphicsYio.setBatchAlpha(batch, iAlpha);
            GraphicsYio.drawByCircle(batch, textureEmpty, item.viewPosition);
        }
    }


    private void renderTitle() {
        GraphicsYio.setFontAlpha(qmsElement.title.font, alpha);
        GraphicsYio.renderText(batch, qmsElement.title);
        GraphicsYio.setFontAlpha(qmsElement.title.font, 1);
    }
}
