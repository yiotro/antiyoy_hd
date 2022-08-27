package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractChoiceItem;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RenderChoiceListItem extends AbstractRenderCustomListItem{

    private AbstractChoiceItem choiceItem;
    private RenderableTextYio title;
    private TextureRegion vIcon;


    @Override
    public void loadTextures() {
        vIcon = GraphicsYio.loadTextureRegion("menu/v_icon.png", true);
    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        choiceItem = (AbstractChoiceItem) item;
        title = choiceItem.title;

        renderTitle();
        renderIcon();
        renderDefaultSelection(choiceItem);
    }


    private void renderIcon() {
        if (!choiceItem.active) return;
        GraphicsYio.drawByCircle(batch, vIcon, choiceItem.iconPosition);
    }


    private void renderTitle() {
        renderTextOptimized(title, alpha);
    }
}
