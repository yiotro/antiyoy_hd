package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.stuff.RectangleYio;

public class RveColorBounds {

    public boolean enabled;
    public RectangleYio position;
    float offset;
    public HColor color;


    public RveColorBounds() {
        enabled = false;
        position = new RectangleYio();
        offset = 0;
        color = null;
    }
}
