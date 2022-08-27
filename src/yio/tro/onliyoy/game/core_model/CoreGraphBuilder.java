package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.TimeMeasureYio;
import yio.tro.onliyoy.stuff.posmap.AbstractPmObjectYio;
import yio.tro.onliyoy.stuff.posmap.PmSectorIndex;
import yio.tro.onliyoy.stuff.posmap.PosMapLooper;

import java.util.ArrayList;

public class CoreGraphBuilder {

    CoreModel coreModel;
    protected RectangleYio bounds;
    protected float hexRadius;
    ArrayList<Hex> propagationList;
    PmSectorIndex pmSectorIndex;
    Hex currentHex;
    PosMapLooper<CoreGraphBuilder> looper;


    public CoreGraphBuilder(CoreModel coreModel) {
        this.coreModel = coreModel;
        bounds = new RectangleYio();
        propagationList = new ArrayList<>();
        pmSectorIndex = new PmSectorIndex();
        currentHex = null;
        initLoopers();
    }


    private void initLoopers() {
        looper = new PosMapLooper<CoreGraphBuilder>(this, 1) {
            @Override
            public void performAction(ArrayList<AbstractPmObjectYio> posMapObjects) {
                for (AbstractPmObjectYio posMapObject : posMapObjects) {
                    Hex hex = (Hex) posMapObject;
//                    if (hex.isLinkedTo(currentHex)) continue; // this method is reliable but slow
                    if (hex.position.center.y > currentHex.position.center.y) continue; // faster method to cut off double entries
                    if (!shouldHexesBeLinked(currentHex, hex)) continue;
                    hex.addAdjacentHex(currentHex);
                    currentHex.addAdjacentHex(hex);
                }
            }
        };
    }


    void apply(RectangleYio bounds, float hexRadius) {
        this.bounds.setBy(bounds);
        this.hexRadius = hexRadius;
        coreModel.hexes.clear();
        propagationList.clear();
        Hex startingHex = coreModel.addHex(0, 0);
        propagationList.add(startingHex);
        while (propagationList.size() > 0) {
            Hex hex = propagationList.get(0);
            propagationList.remove(0);
            applyPropagation(hex);
        }
        linkHexes();
    }


    private void linkHexes() {
        for (Hex hex : coreModel.hexes) {
            currentHex = hex;
            pmSectorIndex.updateByPoint(coreModel.posMapYio, hex.position.center);
            looper.forNearbySectors(coreModel.posMapYio, pmSectorIndex);
        }
    }


    boolean shouldHexesBeLinked(Hex hex1, Hex hex2) {
        int d1 = hex2.coordinate1 - hex1.coordinate1;
        int d2 = hex2.coordinate2 - hex1.coordinate2;
        if (d1 == 1 && d2 == 0) return true;
        if (d1 == 0 && d2 == 1) return true;
        if (d1 == -1 && d2 == 1) return true;
        if (d1 == -1 && d2 == 0) return true;
        if (d1 == 0 && d2 == -1) return true;
        if (d1 == 1 && d2 == -1) return true;
        return false;
    }


    void applyPropagation(Hex hex) {
        int coordinate1 = hex.coordinate1;
        int coordinate2 = hex.coordinate2;
        tryToAddHex(coordinate1 + 1, coordinate2);
        tryToAddHex(coordinate1, coordinate2 + 1);
        tryToAddHex(coordinate1 - 1, coordinate2 + 1);
        tryToAddHex(coordinate1 - 1, coordinate2);
        tryToAddHex(coordinate1, coordinate2 - 1);
        tryToAddHex(coordinate1 + 1, coordinate2 - 1);
    }


    void tryToAddHex(int coordinate1, int coordinate2) {
        if (coreModel.getHex(coordinate1, coordinate2) != null) return;
        if (!coreModel.areCoordinatesInsideBounds(coordinate1, coordinate2)) return;
        Hex hex = coreModel.addHex(coordinate1, coordinate2);
        propagationList.add(hex);
    }

}
