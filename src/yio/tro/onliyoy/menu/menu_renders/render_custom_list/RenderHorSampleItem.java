package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderHorSampleItem extends AbstractRenderCustomListItem{


    private TextureRegion xTexture;


    @Override
    public void loadTextures() {
        xTexture = GraphicsYio.loadTextureRegion("game/stuff/x.png", false);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        GraphicsYio.drawByRectangle(batch, xTexture, item.viewPosition);
    }
}
