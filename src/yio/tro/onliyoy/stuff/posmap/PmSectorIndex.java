package yio.tro.onliyoy.stuff.posmap;

import yio.tro.onliyoy.stuff.PointYio;

public class PmSectorIndex {

    public int x, y;


    public PmSectorIndex() {
        set(0, 0);
    }


    public void set(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }


    void setBy(PmSectorIndex src) {
        x = src.x;
        y = src.y;
    }


    boolean equals(PmSectorIndex anotherIndex) {
        return x == anotherIndex.x && y == anotherIndex.y;
    }


    public void updateByPoint(PosMapYio posMapYio, PointYio pointYio) {
        x = (int) ((pointYio.x - posMapYio.position.x) / posMapYio.sectorSize);
        y = (int) ((pointYio.y - posMapYio.position.y) / posMapYio.sectorSize);
    }


    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
