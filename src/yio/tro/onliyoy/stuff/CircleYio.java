package yio.tro.onliyoy.stuff;

import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class CircleYio implements ReusableYio{

    public PointYio center;
    public float radius;
    public double angle;


    public CircleYio() {
        center = new PointYio();
        reset();
    }


    public CircleYio set(double x, double y, double r) {
        center.set(x, y);
        setRadius(r);
        return this;
    }


    public CircleYio setBy(CircleYio src) {
        set(src.center.x, src.center.y, src.radius);
        setAngle(src.angle);
        return this;
    }


    public CircleYio setRadius(double radius) {
        this.radius = (float) radius;
        return this;
    }


    public CircleYio setAngle(double angle) {
        this.angle = angle;
        return this;
    }


    @Override
    public void reset() {
        center.reset();
        radius = 0;
        angle = 0;
    }


    @Override
    public String toString() {
        return "[CircleYio: " +
                center +
                ", radius=" + radius +
                "]";
    }

}
