package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.LightBottomPanelElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RenderLightBottomPanel extends RenderInterfaceElement{


    private TextureRegion defBackgroundTexture;
    private LightBottomPanelElement lbpElement;
    private TextureRegion sideShadowTexture;


    @Override
    public void loadTextures() {
        defBackgroundTexture = GraphicsYio.loadTextureRegion("menu/round_shape/white.png", false);
        sideShadowTexture = GraphicsYio.loadTextureRegion("menu/side_shadow.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        lbpElement = (LightBottomPanelElement) element;

        renderShadow();
        renderBackground();
        for (RenderableTextYio renderableTextYio : lbpElement.visualTextContainer.viewList) {
            GraphicsYio.renderText(batch, renderableTextYio);
        }
    }


    private void renderShadow() {
        GraphicsYio.setBatchAlpha(batch, lbpElement.getShadowAlpha());
        GraphicsYio.drawByRectangle(batch, sideShadowTexture, lbpElement.sideShadowPosition);
    }


    private void renderBackground() {
        GraphicsYio.setBatchAlpha(batch, 1);
        GraphicsYio.drawByRectangle(batch, getBackgroundTexture(), lbpElement.renderPosition);
    }


    private TextureRegion getBackgroundTexture() {
        if (lbpElement.color == BackgroundYio.white) return defBackgroundTexture;
        return MenuRenders.renderRoundShape.getBackgroundTexture(lbpElement.color);
    }

}
