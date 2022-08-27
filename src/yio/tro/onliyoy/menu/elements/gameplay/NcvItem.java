package yio.tro.onliyoy.menu.elements.gameplay;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NcvItem implements ReusableYio {

    NetChatViewElement netChatViewElement;
    public FactorYio appearFactor;
    public RenderableTextYio title;
    public RectangleYio incBounds;
    long deathTime;
    PointYio delta;


    public NcvItem(NetChatViewElement netChatViewElement) {
        this.netChatViewElement = netChatViewElement;
        appearFactor = new FactorYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        incBounds = new RectangleYio();
        delta = new PointYio();
    }


    @Override
    public void reset() {
        appearFactor.reset();
        appearFactor.appear(MovementType.approach, 2.5);
        incBounds.reset();
        title.setString("-");
        delta.reset();
        deathTime = System.currentTimeMillis() + 3000;
    }


    void move() {
        appearFactor.move();
        updateTitlePosition();
        updateIncBounds();
    }


    private void updateTitlePosition() {
        title.position.x = netChatViewElement.getViewPosition().x + delta.x;
        title.position.y = netChatViewElement.getViewPosition().y + delta.y;
        title.updateBounds();
    }


    void setTitle(String string) {
        title.setString(string);
        title.updateMetrics();
    }


    public void setDeathTime(long deathTime) {
        this.deathTime = deathTime;
    }


    void updateIncBounds() {
        incBounds.setBy(title.bounds);
        incBounds.increase(0.015f * GraphicsYio.width);
    }
}
