package yio.tro.onliyoy.stuff.scroll_engine;

import yio.tro.onliyoy.Yio;

public class SegmentYio {


    public double a;
    public double b;


    public SegmentYio() {
        a = 0;
        b = 0;
    }


    public void set(double a, double b) {
        this.a = a;
        this.b = b;
    }


    public double getLength() {
        return b - a;
    }


    @Override
    public String toString() {
        return "(" + Yio.roundUp(a, 2) + ", " + Yio.roundUp(b, 2) + ")";
    }
}
