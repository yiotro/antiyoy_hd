package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.gameplay.NcvItem;
import yio.tro.onliyoy.menu.elements.gameplay.NetChatViewElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RenderNetChatViewElement extends RenderInterfaceElement{


    private NetChatViewElement ncvElement;
    private TextureRegion whitePixel;


    @Override
    public void loadTextures() {
        whitePixel = GraphicsYio.loadTextureRegion("pixels/white.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        ncvElement = (NetChatViewElement) element;
        for (NcvItem ncvItem : ncvElement.items) {
            GraphicsYio.setBatchAlpha(batch, 0.3 * alpha * ncvItem.appearFactor.getValue());
            GraphicsYio.drawByRectangle(batch, blackPixel, ncvItem.incBounds);
            GraphicsYio.setBatchAlpha(batch, 1);
            RenderableTextYio title = ncvItem.title;
            title.font.setColor(0.9f, 0.9f, 0.9f, alpha * ncvItem.appearFactor.getValue());
            GraphicsYio.renderText(batch, title);
            title.font.setColor(Color.BLACK);
        }
    }
}
