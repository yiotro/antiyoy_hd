package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.net.CeItem;
import yio.tro.onliyoy.menu.elements.net.CoinsElpViewElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderCoinsElpViewElement extends RenderInterfaceElement{


    private CoinsElpViewElement cevElement;
    private TextureRegion moneyTexture;
    private TextureRegion elpTexture;


    @Override
    public void loadTextures() {
        moneyTexture = GraphicsYio.loadTextureRegion("menu/icons/money.png", true);
        elpTexture = GraphicsYio.loadTextureRegion("menu/icons/elp.png", true);
    }


    @Override
    public void render(InterfaceElement element) {
        cevElement = (CoinsElpViewElement) element;

        renderItems();
        renderSelection();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderSelection() {
        if (!cevElement.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, cevElement.selectionEngineYio.getAlpha() * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, cevElement.touchPosition);
    }


    private void renderItems() {
        for (CeItem item : cevElement.items) {
            switch (item.type) {
                default:
                    System.out.println("RenderCoinsElpViewElement.render: problem");
                    break;
                case money:
                    GraphicsYio.setBatchAlpha(batch, alpha);
                    GraphicsYio.drawByCircle(batch, moneyTexture, item.position);
                    break;
                case elp:
                    GraphicsYio.setBatchAlpha(batch, alpha);
                    GraphicsYio.drawByCircle(batch, elpTexture, item.position);
                    break;
                case text:
                    GraphicsYio.renderTextOptimized(batch, blackPixel, item.title, alpha);
                    break;
            }
        }
    }
}
