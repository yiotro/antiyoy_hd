package yio.tro.onliyoy.menu.menu_renders.rve_renders;

import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveColorBounds;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

public abstract class AbstractRveRender extends RenderInterfaceElement {


    public abstract void loadTextures();


    public abstract void renderItem(AbstractRveItem rveItem, double alpha);


    @Override
    public void render(InterfaceElement element) {

    }


    protected void renderColorBounds(AbstractRveItem rveItem) {
        RveColorBounds colorBounds = rveItem.colorBounds;
        if (!colorBounds.enabled) return;
        GraphicsYio.drawByRectangle(
                batch,
                MenuRenders.renderUiColors.map.get(colorBounds.color),
                colorBounds.position
        );
    }
}
