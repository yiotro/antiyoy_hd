package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class GeometricalHexData implements ReusableYio {

    public PointYio pos;
    public RectangleYio rectangle;
    public PointYio[] leftTriangle;
    public PointYio[] rightTriangle;


    public GeometricalHexData() {
        rectangle = new RectangleYio();
        leftTriangle = new PointYio[3];
        rightTriangle = new PointYio[3];
        pos = new PointYio();
        for (int i = 0; i < 3; i++) {
            leftTriangle[i] = new PointYio();
            rightTriangle[i] = new PointYio();
        }
    }


    @Override
    public void reset() {
        pos.reset();
        rectangle.reset();
        for (int i = 0; i < 3; i++) {
            leftTriangle[i].reset();
            rightTriangle[i].reset();
        }
    }


    public void setHex(Hex hex) {
        pos.setBy(hex.position.center);
    }


    public void setPosDirectly(PointYio pointYio) {
        pos.setBy(pointYio);
    }


    public void updateMetrics(float dx, float dy) {
        rectangle.x = pos.x - dx / 2;
        rectangle.y = pos.y - dy;
        rectangle.width = dx;
        rectangle.height = 2 * dy;
        leftTriangle[0].set(rectangle.x, rectangle.y);
        leftTriangle[1].set(rectangle.x, rectangle.y + rectangle.height);
        leftTriangle[2].set(pos.x - dx, pos.y);
        rightTriangle[0].set(rectangle.x + rectangle.width, rectangle.y);
        rightTriangle[1].set(rectangle.x + rectangle.width, rectangle.y + rectangle.height);
        rightTriangle[2].set(pos.x + dx, pos.y);
    }
}
