package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RveSampleItem extends AbstractRveItem{

    @Override
    protected void initialize() {

    }


    @Override
    protected void onMove() {

    }


    @Override
    protected float getHeight() {
        return 0.08f * GraphicsYio.height;
    }


    @Override
    public AbstractRveRender getRender() {
        return MenuRenders.renderRveSampleItem;
    }
}
