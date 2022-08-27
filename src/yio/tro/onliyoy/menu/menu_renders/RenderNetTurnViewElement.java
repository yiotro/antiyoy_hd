package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.gameplay.NetTurnViewElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderNetTurnViewElement extends RenderInterfaceElement{


    private NetTurnViewElement ntvElement;
    private TextureRegion whitePixel;
    private TextureRegion darkPixel;


    @Override
    public void loadTextures() {
        whitePixel = GraphicsYio.loadTextureRegion("pixels/white.png", false);
        darkPixel = GraphicsYio.loadTextureRegion("pixels/dark.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        ntvElement = (NetTurnViewElement) element;
        renderBackground();
        renderTitle();
        renderSelection();
    }


    private void renderSelection() {
        if (!ntvElement.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * ntvElement.selectionEngineYio.getAlpha());
        GraphicsYio.drawByRectangle(batch, blackPixel, ntvElement.touchPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderBackground() {
        float f = ntvElement.colorFactor.getValue();
        if (f == 1) {
            renderColoredBackground(ntvElement.currentColor, 1);
            return;
        }
        renderColoredBackground(ntvElement.previousColor, 1);
        renderColoredBackground(ntvElement.currentColor, f);
    }


    private void renderColoredBackground(HColor color, double f) {
        GraphicsYio.setBatchAlpha(batch, alpha * f);
        GraphicsYio.drawByRectangle(
                batch,
                getBackgroundTexture(color),
                ntvElement.incBounds
        );
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderTitle() {
        if (ntvElement.currentColor == null) {
            renderWhiteText(ntvElement.iconTextYio.renderableTextYio, whitePixel, alpha);
            return;
        }
        GraphicsYio.renderItyOptimized(
                batch,
                blackPixel,
                MenuRenders.renderAvatars.getTexture(ntvElement.avatarType, ntvElement.iconTextYio),
                ntvElement.iconTextYio,
                alpha
        );
    }


    private TextureRegion getBackgroundTexture(HColor color) {
        if (color == null) return darkPixel;
        return MenuRenders.renderUiColors.map.get(color);
    }
}
