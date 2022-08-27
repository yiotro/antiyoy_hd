package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.posmap.AbstractPmObjectYio;
import yio.tro.onliyoy.stuff.posmap.PmSectorIndex;
import yio.tro.onliyoy.stuff.posmap.PosMapLooper;
import yio.tro.onliyoy.stuff.posmap.PosMapYio;

import java.util.ArrayList;

public class PmWorker {

    CoreModel coreModel;
    public ArrayList<Hex> nearbyHexes;
    PointYio tempPoint;
    PosMapLooper<PmWorker> looperAddNearby;
    PmSectorIndex sectorIndex;


    public PmWorker(CoreModel coreModel) {
        this.coreModel = coreModel;
        nearbyHexes = new ArrayList<>();
        tempPoint = new PointYio();
        sectorIndex = new PmSectorIndex();
        initLoopers();
    }


    private void initLoopers() {
        looperAddNearby = new PosMapLooper<PmWorker>(this, 1) {
            @Override
            public void performAction(ArrayList<AbstractPmObjectYio> posMapObjects) {
                for (AbstractPmObjectYio posMapObject : posMapObjects) {
                    Hex hex = (Hex) posMapObject;
                    nearbyHexes.add(hex);
                }
            }
        };
    }


    public Hex getClosestHex(PointYio pointYio) {
        updateNearbyHexes(pointYio);
        Hex closestHex = null;
        double minDistance = 0;
        for (Hex nearbyHex : nearbyHexes) {
            double currentDistance = nearbyHex.position.center.distanceTo(pointYio);
            if (closestHex == null || currentDistance < minDistance) {
                closestHex = nearbyHex;
                minDistance = currentDistance;
            }
        }
        return closestHex;
    }


    public void updateNearbyHexes(PointYio pointYio) {
        sectorIndex.updateByPoint(getPosMap(), pointYio);
        nearbyHexes.clear();
        looperAddNearby.forNearbySectors(getPosMap(), sectorIndex);
    }


    private PosMapYio getPosMap() {
        return coreModel.posMapYio;
    }
}
