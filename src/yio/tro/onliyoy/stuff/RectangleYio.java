package yio.tro.onliyoy.stuff;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class RectangleYio implements ReusableYio{

    public float x;
    public float y;
    public float width;
    public float height;


    public RectangleYio() {
        reset();
    }


    public RectangleYio(double x, double y, double width, double height) {
        set(x, y, width, height);
    }


    public RectangleYio(RectangleYio src) {
        setBy(src);
    }


    public void set(double x, double y, double width, double height) {
        this.x = (float) x;
        this.y = (float) y;
        this.width = (float) width;
        this.height = (float) height;
    }


    public void setBy(RectangleYio src) {
        set(src.x, src.y, src.width, src.height);
    }


    public void setBy(CircleYio circleYio) {
        x = circleYio.center.x - circleYio.radius;
        y = circleYio.center.y - circleYio.radius;
        width = 2 * circleYio.radius;
        height = 2 * circleYio.radius;
    }


    public void increase(double delta) {
        x -= delta;
        y -= delta;
        width += 2 * delta;
        height += 2 * delta;
    }


    public double distanceTo(PointYio pointYio) {
        if (isPointInside(pointYio)) return 0;

        if (pointYio.x < x) {
            if (pointYio.y < y) {
                return Yio.distance(pointYio.x, pointYio.y, x, y);
            } else if (pointYio.y < y + height) {
                return x - pointYio.x;
            } else {
                return Yio.distance(pointYio.x, pointYio.y, x, y + height);
            }
        } if (pointYio.x < x + width) {
            if (pointYio.y < y) {
                return y - pointYio.y;
            } else if (pointYio.y < y + height) {
                return 0;
            } else {
                return pointYio.y - (y + height);
            }
        } else {
            if (pointYio.y < y) {
                return Yio.distance(pointYio.x, pointYio.y, x + width, y);
            } else if (pointYio.y < y + height) {
                return pointYio.x - (x + width);
            } else {
                return Yio.distance(pointYio.x, pointYio.y, x + width, y + height);
            }
        }
    }


    public double angleFrom(PointYio pointYio) {
        if (pointYio.x < x) {
            if (pointYio.y < y) {
                return Yio.angle(pointYio.x, pointYio.y, x, y);
            } else if (pointYio.y < y + height) {
                return 0;
            } else {
                return Yio.angle(pointYio.x, pointYio.y, x, y + height);
            }
        } if (pointYio.x < x + width) {
            if (pointYio.y < y) {
                return Math.PI / 2;
            } else if (pointYio.y < y + height) {
                return 0;
            } else {
                return 1.5 * Math.PI;
            }
        } else {
            if (pointYio.y < y) {
                return Yio.angle(pointYio.x, pointYio.y, x + width, y);
            } else if (pointYio.y < y + height) {
                return Math.PI;
            } else {
                return Yio.angle(pointYio.x, pointYio.y, x + width, y + height);
            }
        }
    }


    public boolean isPointInside(PointYio point, float offset) {
        if (point.x < x - offset) return false;
        if (point.x > x + width + offset) return false;
        if (point.y < y - offset) return false;
        if (point.y > y + height + offset) return false;
        return true;
    }


    public boolean isPointInside(PointYio point) {
        return isPointInside(point, 0);
    }


    public boolean intersects(RectangleYio rectangleYio) {
        if (x > rectangleYio.x + rectangleYio.width) return false;
        if (x + width < rectangleYio.x) return false;
        if (y > rectangleYio.y + rectangleYio.height) return false;
        if (y + height < rectangleYio.y) return false;
        return true;
    }


    public boolean intersects(CircleYio circle) {
        return isPointInside(circle.center, circle.radius);
    }


    public boolean contains(CircleYio circle) {
        return isPointInside(circle.center, -circle.radius);
    }


    public boolean contains(RectangleYio rectangleYio) {
        if (rectangleYio.x < x) return false;
        if (rectangleYio.x + rectangleYio.width > x + width) return false;
        if (rectangleYio.y < y) return false;
        if (rectangleYio.y + rectangleYio.height > y + height) return false;
        return true;
    }


    public boolean isFullyInside(RectangleYio rectangleYio) {
        return rectangleYio.contains(this);
    }


    @Override
    public void reset() {
        set(0, 0, 0, 0);
    }


    @Override
    public String toString() {
        return "(" + Yio.roundUp(x, 2) + ", " + Yio.roundUp(y, 2) + ", " + Yio.roundUp(width, 2) + ", " + Yio.roundUp(height, 2) + ")";
    }
}
