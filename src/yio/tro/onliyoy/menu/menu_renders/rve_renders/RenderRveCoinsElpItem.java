package yio.tro.onliyoy.menu.menu_renders.rve_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RceiInnerItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveCoinsElpItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderRveCoinsElpItem extends AbstractRveRender{

    private RveCoinsElpItem rveCoinsElpItem;
    private TextureRegion moneyTexture;
    private TextureRegion elpTexture;


    @Override
    public void loadTextures() {
        moneyTexture = GraphicsYio.loadTextureRegion("menu/icons/money.png", true);
        elpTexture = GraphicsYio.loadTextureRegion("menu/icons/elp.png", true);
    }


    @Override
    public void renderItem(AbstractRveItem rveItem, double alpha) {
        rveCoinsElpItem = (RveCoinsElpItem) rveItem;

        for (RceiInnerItem item : rveCoinsElpItem.items) {
            switch (item.type) {
                default:
                    System.out.println("RenderCoinsElpViewElement.render: problem");
                    break;
                case elp:
                    GraphicsYio.setBatchAlpha(batch, alpha);
                    GraphicsYio.drawByCircle(batch, elpTexture, item.position);
                    break;
                case text:
                    GraphicsYio.renderTextOptimized(batch, blackPixel, item.title, (float) alpha);
                    break;
            }
        }
        GraphicsYio.setBatchAlpha(batch, 1);
    }
}
