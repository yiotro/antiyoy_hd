package yio.tro.onliyoy.stuff;

import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class LineYio implements ReusableYio {

    public PointYio start;
    public PointYio finish;
    public float thickness;
    public float length;
    public float angle;
    public float renderAngle;


    public LineYio() {
        start = new PointYio();
        finish = new PointYio();
        reset();
    }


    @Override
    public void reset() {
        start.reset();
        finish.reset();
        thickness = GraphicsYio.borderThickness;
        length = 0;
        angle = 0;
        renderAngle = 0;
    }


    public LineYio setThickness(float thickness) {
        this.thickness = thickness;
        return this;
    }


    public LineYio setStart(double x, double y) {
        start.set(x, y);
        updateMetrics();
        return this;
    }


    public LineYio setStart(PointYio pointYio) {
        return setStart(pointYio.x, pointYio.y);
    }


    public LineYio setFinish(double x, double y) {
        finish.set(x, y);
        updateMetrics();
        return this;
    }


    public LineYio setFinish(PointYio pointYio) {
        return setFinish(pointYio.x, pointYio.y);
    }


    public double distanceTo(PointYio pointYio) {
        if (start.equals(finish)) return 0;
        if (start.x == finish.x) return Math.abs(pointYio.x - start.x);
        if (start.y == finish.y) return Math.abs(pointYio.y - start.y);

        if (length == 0) {
            System.out.println("LineYio.distanceTo(): length = 0");
        }

        double s = 0.5 * Math.abs(
                (start.x - pointYio.x) * (finish.y - pointYio.y) - (finish.x - pointYio.x) * (start.y - pointYio.y)
        );
        return (2 * s) / length;
    }


    public void updateMetrics() {
        length = start.distanceTo(finish);
        angle = (float) start.angleTo(finish);
        renderAngle = (float) (180 / Math.PI * angle);
    }
}
