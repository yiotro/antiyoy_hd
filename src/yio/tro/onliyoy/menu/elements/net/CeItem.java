package yio.tro.onliyoy.menu.elements.net;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class CeItem {

    CoinsElpViewElement coinsElpViewElement;
    public CevType type;
    public CircleYio position;
    public RenderableTextYio title;
    PointYio delta;


    public CeItem(CoinsElpViewElement coinsElpViewElement, CevType type) {
        this.coinsElpViewElement = coinsElpViewElement;
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
        if (type != CevType.text) return;
        title.position.x = position.center.x - title.width / 2;
        title.position.y = position.center.y + title.height / 2;
        title.updateBounds();
    }


    private void updatePosition() {
        RectangleYio srcPos = coinsElpViewElement.getViewPosition();
        position.center.x = srcPos.x + delta.x;
        position.center.y = srcPos.y + srcPos.height - delta.y;
    }
}
