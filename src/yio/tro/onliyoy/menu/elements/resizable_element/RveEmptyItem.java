package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RveEmptyItem extends AbstractRveItem{

    float height;


    public RveEmptyItem(double h) {
        height = (float) (h * GraphicsYio.height);
    }


    @Override
    protected void initialize() {

    }


    @Override
    protected void onMove() {

    }


    @Override
    protected float getHeight() {
        return height;
    }


    public void setHeight(float height) {
        this.height = height;
    }


    @Override
    public AbstractRveRender getRender() {
        return MenuRenders.renderRveEmptyItem;
    }
}
