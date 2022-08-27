package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.net.CevType;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RceiInnerItem {

    RveCoinsElpItem rveCoinsElpItem;
    public RceiInnerType type;
    public CircleYio position;
    public RenderableTextYio title;
    PointYio delta;


    public RceiInnerItem(RveCoinsElpItem rveCoinsElpItem, RceiInnerType type) {
        this.rveCoinsElpItem = rveCoinsElpItem;
        this.type = type;
        position = new CircleYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        delta = new PointYio();
    }


    void move() {
        updatePosition();
        updateTitlePosition();
    }


    private void updateTitlePosition() {
        if (type != RceiInnerType.text) return;
        title.position.x = position.center.x - title.width / 2;
        title.position.y = position.center.y + title.height / 2;
        title.updateBounds();
    }


    private void updatePosition() {
        RectangleYio srcPos = rveCoinsElpItem.position;
        position.center.x = srcPos.x + delta.x;
        position.center.y = srcPos.y + srcPos.height - delta.y;
    }
}
