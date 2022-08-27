package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class LgCluster implements ReusableYio {

    public ArrayList<LgNode> nodes;
    PointYio center;


    public LgCluster() {
        nodes = new ArrayList<>();
        center = new PointYio();
    }


    @Override
    public void reset() {
        nodes.clear();
        center.reset();
    }


    void updateCenter() {
        center.reset();
        for (LgNode lgNode : nodes) {
            center.add(lgNode.position);
        }
        center.x /= nodes.size();
        center.y /= nodes.size();
    }


    LgNode findClosestNode(PointYio pointYio) {
        LgNode bestNode = null;
        double minDistance = 0;
        for (LgNode lgNode : nodes) {
            double currentDistance = lgNode.position.distanceTo(pointYio);
            if (bestNode == null || currentDistance < minDistance) {
                bestNode = lgNode;
                minDistance = currentDistance;
            }
        }
        return bestNode;
    }
}
