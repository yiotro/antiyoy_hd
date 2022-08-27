package yio.tro.onliyoy.menu.elements.icon_label_element;

import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class IleIcon implements ReusableYio{

    IconLabelElement iconLabelElement;
    public RectangleYio viewPosition;
    PointYio delta;
    public IleTextureType textureType;


    public IleIcon(IconLabelElement iconLabelElement) {
        this.iconLabelElement = iconLabelElement;
        viewPosition = new RectangleYio();
        delta = new PointYio();
    }


    @Override
    public void reset() {
        viewPosition.reset();
        delta.reset();
        textureType = null;
    }


    void move() {
        updateViewPosition();
    }


    private void updateViewPosition() {
        viewPosition.x = iconLabelElement.getViewPosition().x + delta.x;
        viewPosition.y = iconLabelElement.getViewPosition().y + delta.y;
    }
}
