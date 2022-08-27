package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.EconomicsViewElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.EveTouchArea;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderEconomicsViewElement extends RenderInterfaceElement{


    private TextureRegion backgroundTexture;
    private EconomicsViewElement evElement;
    private TextureRegion coinTexture;
    private TextureRegion whitePixel;
    private TextureRegion selectEffectTexture;


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/province_ui/economics_background.png", false);
        coinTexture = GraphicsYio.loadTextureRegion("menu/province_ui/coin.png", false);
        whitePixel = GraphicsYio.loadTextureRegion("pixels/white.png", false);
        selectEffectTexture = GraphicsYio.loadTextureRegion("menu/selection.png", true);
    }


    @Override
    public void render(InterfaceElement element) {
        evElement = (EconomicsViewElement) element;
        GraphicsYio.drawByRectangle(batch, backgroundTexture, evElement.getViewPosition());
        GraphicsYio.drawByCircle(batch, coinTexture, evElement.iconPosition);
        renderWhiteText(evElement.moneyViewText, whitePixel, 1);
        renderWhiteText(evElement.profitViewText, whitePixel, 1);
        renderTouchAreas();
    }


    private void renderTouchAreas() {
        for (EveTouchArea touchArea : evElement.touchAreas) {
            if (!touchArea.selectionEngineYio.isSelected()) continue;
            GraphicsYio.setBatchAlpha(batch, alpha * touchArea.selectionEngineYio.getAlpha());
            GraphicsYio.drawByCircle(batch, selectEffectTexture, touchArea.position);
        }
        GraphicsYio.setBatchAlpha(batch, 1);
    }
}
