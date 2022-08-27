package yio.tro.onliyoy.menu.menu_renders.rve_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderRveSampleItem extends AbstractRveRender{


    private TextureRegion xTexture;


    @Override
    public void loadTextures() {
        xTexture = GraphicsYio.loadTextureRegion("game/stuff/x.png", false);
    }


    @Override
    public void renderItem(AbstractRveItem rveItem, double alpha) {
        GraphicsYio.drawByRectangle(batch, xTexture, rveItem.position);
    }
}
