package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.TimeMeasureYio;

import java.util.ArrayList;
import java.util.Random;

public class LgChunkSpawner {

    AbstractLevelGenerator abstractLevelGenerator;
    ArrayList<Hex> propagationList;
    PointYio tempPoint;
    PointYio delta;


    public LgChunkSpawner(AbstractLevelGenerator abstractLevelGenerator) {
        this.abstractLevelGenerator = abstractLevelGenerator;
        propagationList = new ArrayList<>();
        tempPoint = new PointYio();
        delta = new PointYio();
    }


    void turnPlanIntoLand() {
        clearLgFlags();
        for (LgNode node : abstractLevelGenerator.planManager.nodes) {
            Hex hex = getCoreModel().getClosestHex(node.position);
            tag(hex, abstractLevelGenerator.parameters.landsDensity + 3);
            for (LgLink link : node.links) {
                if (link.node1 != node) continue;
                tagLandAlongLink(link);
            }
        }
        removeNonFlaggedHexes();
        removeExtremeHexes();
    }


    private void removeExtremeHexes() {
        ArrayList<Hex> hexes = getCoreModel().hexes;
        for (int i = hexes.size() - 1; i >= 0; i--) {
            Hex hex = hexes.get(i);
            if (!isHexExtreme(hex)) continue;
            getCoreModel().removeHex(hex);
        }
    }


    private boolean isHexExtreme(Hex hex) {
        // extreme = too close to level bounds
        RectangleYio bounds = getCoreModel().bounds;
        PointYio center = hex.position.center;
        float delta = 1.5f * hex.position.radius;
        if (center.x - delta < bounds.x) return true;
        if (center.x + delta > bounds.x + bounds.width) return true;
        if (center.y - delta < bounds.y) return true;
        if (center.y + delta > bounds.y + bounds.height) return true;
        return false;
    }


    private void tagLandAlongLink(LgLink link) {
        PointYio pos1 = link.node1.position;
        PointYio pos2 = link.node2.position;
        tempPoint.setBy(pos1);
        float distance = pos1.distanceTo(pos2);
        double angle = pos1.angleTo(pos2);
        delta.reset();
        float step = getCoreModel().getHexRadius();
        delta.relocateRadial(step, angle);
        float travelDistance = 0;
        while (travelDistance < distance) {
            travelDistance += step;
            tempPoint.add(delta);
            Hex closestHex = getCoreModel().getClosestHex(tempPoint);
            tag(closestHex, abstractLevelGenerator.parameters.landsDensity);
        }
    }


    private void removeNonFlaggedHexes() {
        ArrayList<Hex> hexes = getCoreModel().hexes;
        // I can also just use removeHex() method, it will be easier but much slower
        // remove links
        for (Hex hex : hexes) {
            if (!hex.lgFlag) continue;
            ArrayList<Hex> adjacentHexes = hex.adjacentHexes;
            for (int i = adjacentHexes.size() - 1; i >= 0; i--) {
                Hex adjacentHex = adjacentHexes.get(i);
                if (adjacentHex.lgFlag) continue;
                adjacentHexes.remove(i);
            }
        }
        // manually remove non flagged hexes
        for (int i = hexes.size() - 1; i >= 0; i--) {
            Hex hex = hexes.get(i);
            if (hex.lgFlag) continue;
            hexes.remove(i);
            getCoreModel().posMapYio.removeObject(hex);
        }
    }


    private void clearLgFlags() {
        for (Hex hex : getCoreModel().hexes) {
            hex.lgFlag = false;
        }
    }


    private void tag(Hex startHex, int radius) {
        if (startHex.lgFlag) return;
        clearFlags();
        addToPropagationList(startHex, radius);
        Random random = abstractLevelGenerator.random;
        while (propagationList.size() > 0) {
            Hex firstHex = propagationList.get(0);
            propagationList.remove(0);
            if (random.nextInt(radius) >= firstHex.counter) continue;
            firstHex.lgFlag = true; // marked to be turned into land
            for (Hex adjacentHex : firstHex.adjacentHexes) {
                if (adjacentHex.lgFlag) continue;
                if (adjacentHex.flag) continue;
                addToPropagationList(adjacentHex, firstHex.counter - 1);
            }
        }
    }


    private void clearFlags() {
        for (Hex hex : getCoreModel().hexes) {
            hex.flag = false;
        }
    }


    private void addToPropagationList(Hex hex, int counter) {
        hex.flag = true;
        hex.counter = counter;
        propagationList.add(hex);
    }


    private CoreModel getCoreModel() {
        return abstractLevelGenerator.coreModel;
    }
}
