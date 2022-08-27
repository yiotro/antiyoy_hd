package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;
import java.util.Random;

public class LgPlanManager {

    AbstractLevelGenerator abstractLevelGenerator;
    public ArrayList<LgNode> nodes;
    private RectangleYio internalBounds;
    ArrayList<LgCluster> clusters;
    ObjectPoolYio<LgCluster> poolClusters;
    ArrayList<LgNode> propagationList;
    PointYio tempPoint;
    PointYio deltaPoint;
    PointYio centerPoint;


    public LgPlanManager(AbstractLevelGenerator abstractLevelGenerator) {
        this.abstractLevelGenerator = abstractLevelGenerator;
        nodes = new ArrayList<>();
        internalBounds = new RectangleYio();
        clusters = new ArrayList<>();
        propagationList = new ArrayList<>();
        tempPoint = new PointYio();
        deltaPoint = new PointYio();
        centerPoint = new PointYio();
        initPools();
    }


    private void initPools() {
        poolClusters = new ObjectPoolYio<LgCluster>(clusters) {
            @Override
            public LgCluster makeNewObject() {
                return new LgCluster();
            }
        };
    }


    void clear() {
        poolClusters.clearExternalList();
        nodes.clear();
    }


    void generateNodes() {
        nodes.clear();
        addFewRandomNodes();
    }


    void linkNodes() {
        for (LgNode lgNode : nodes) {
            LgNode closestUnlinkedNode = getClosestUnlinkedNode(lgNode);
            if (closestUnlinkedNode == null) continue;
            applyLink(lgNode, closestUnlinkedNode);
        }
    }


    void guaranteeLinkedState() {
        while (true) {
            updateClusters();
            if (clusters.size() < 2) break;
            linkTwoFirstClusters();
        }
    }


    void alignToCenter() {
        LgNode firstNode = nodes.get(0);
        float left = firstNode.position.x;
        float right = firstNode.position.x;
        float top = firstNode.position.y;
        float bottom = firstNode.position.y;
        for (LgNode lgNode : nodes) {
            if (lgNode.position.x < left) {
                left = lgNode.position.x;
            }
            if (lgNode.position.x > right) {
                right = lgNode.position.x;
            }
            if (lgNode.position.y < bottom) {
                bottom = lgNode.position.y;
            }
            if (lgNode.position.y > top) {
                top = lgNode.position.y;
            }
        }
        tempPoint.x = (left + right) / 2;
        tempPoint.y = (bottom + top) / 2;
        updateCenterPoint();
        deltaPoint.x = centerPoint.x - tempPoint.x;
        deltaPoint.y = centerPoint.y - tempPoint.y;
        relocateAllNodes(deltaPoint);
    }


    void connectTwoOppositeNodes() {
        if (!abstractLevelGenerator.parameters.loop) return;
        LgNode node1 = findNodeWithLeastAmountOfLinks();
        if (node1 == null) return;
        LgNode node2 = findClosestOppositeNode(node1);
        if (node1 == node2) return;
        applyLink(node1, node2);
    }


    private LgNode findClosestOppositeNode(LgNode startNode) {
        resetCounters();
        populateCountersViaWave(startNode);
        return findClosestNodeWithMaxCounter(startNode);
    }


    private LgNode findClosestNodeWithMaxCounter(LgNode startNode) {
        int maxCounter = getMaxCounter();
        LgNode bestNode = null;
        double minDistance = 0;
        for (LgNode lgNode : nodes) {
            if (lgNode.counter != maxCounter) continue;
            double currentDistance = lgNode.position.distanceTo(startNode.position);
            if (bestNode == null || currentDistance < minDistance) {
                bestNode = lgNode;
                minDistance = currentDistance;
            }
        }
        return bestNode;
    }


    private int getMaxCounter() {
        int maxValue = 0;
        for (LgNode lgNode : nodes) {
            if (lgNode.counter <= maxValue) continue;
            maxValue = lgNode.counter;
        }
        return maxValue;
    }


    private void populateCountersViaWave(LgNode startNode) {
        boolean expanded;
        int step = 1;
        startNode.counter = step;
        while (true) {
            expanded = false;
            for (LgNode lgNode : nodes) {
                if (lgNode.counter != step) continue;
                for (LgLink link : lgNode.links) {
                    LgNode adjacentNode = link.getOpposite(lgNode);
                    if (adjacentNode.counter != 0) continue;
                    adjacentNode.counter = step + 1;
                    expanded = true;
                }
            }
            if (!expanded) break;
            step++;
        }
    }


    private void resetCounters() {
        for (LgNode lgNode : nodes) {
            lgNode.counter = 0;
        }
    }


    private LgNode findNodeWithLeastAmountOfLinks() {
        LgNode bestNode = null;
        for (LgNode lgNode : nodes) {
            if (bestNode == null || lgNode.links.size() < bestNode.links.size()) {
                bestNode = lgNode;
            }
        }
        return bestNode;
    }


    private void relocateAllNodes(PointYio deltaPoint) {
        for (LgNode lgNode : nodes) {
            lgNode.position.add(deltaPoint);
        }
    }


    private void updateCenterPoint() {
        centerPoint.set(
                internalBounds.x + internalBounds.width / 2,
                internalBounds.y + internalBounds.height / 2
        );
    }


    private void linkTwoFirstClusters() {
        LgCluster firstCluster = clusters.get(0);
        LgCluster secondCluster = clusters.get(1);
        LgNode node1 = firstCluster.findClosestNode(secondCluster.center);
        LgNode node2 = secondCluster.findClosestNode(firstCluster.center);
        applyLink(node1, node2);
    }


    private void updateClusters() {
        poolClusters.clearExternalList();
        resetFlags();
        for (LgNode lgNode : nodes) {
            if (lgNode.flag) continue;
            addCluster(lgNode);
        }
    }


    private void addCluster(LgNode startNode) {
        LgCluster freshObject = poolClusters.getFreshObject();
        propagationList.clear();
        addToPropagationList(startNode);
        while (propagationList.size() > 0) {
            LgNode firstNode = propagationList.get(0);
            freshObject.nodes.add(firstNode);
            propagationList.remove(0);
            for (LgLink link : firstNode.links) {
                LgNode oppositeNode = link.getOpposite(firstNode);
                if (oppositeNode.flag) continue;
                addToPropagationList(oppositeNode);
            }
        }
        freshObject.updateCenter();
    }


    private void addToPropagationList(LgNode lgNode) {
        lgNode.flag = true;
        propagationList.add(lgNode);
    }


    private void resetFlags() {
        for (LgNode lgNode : nodes) {
            lgNode.flag = false;
        }
    }


    private LgNode getClosestUnlinkedNode(LgNode argNode) {
        LgNode bestNode = null;
        double minDistance = 0;
        for (LgNode lgNode : nodes) {
            if (lgNode == argNode) continue;
            if (argNode.isLinkedTo(lgNode)) continue;
            double currentDistance = lgNode.position.distanceTo(argNode.position);
            if (bestNode == null || currentDistance < minDistance) {
                bestNode = lgNode;
                minDistance = currentDistance;
            }
        }
        return bestNode;
    }


    private void addFewRandomNodes() {
        int quantity = calculateQuantity();
        for (int i = 0; i < quantity; i++) {
            addRandomNode();
        }
    }


    private int calculateQuantity() {
        CoreModel coreModel = abstractLevelGenerator.coreModel;
        RectangleYio bounds = coreModel.bounds;
        float distance = (float) Math.sqrt(bounds.width * bounds.width + bounds.height * bounds.height);
        float hexRadius = coreModel.getHexRadius();
        float value = distance / hexRadius;
        LgParameters parameters = abstractLevelGenerator.parameters;
        if (value > 100) { // giant level size
            value *= 2.2;
        }
        if (value > 70) {
            value *= 1.4;
        }
        return (int) (parameters.nodesValue * value);
    }


    private void applyLink(LgNode node1, LgNode node2) {
        node1.addLink(node1, node2);
        node2.addLink(node1, node2);
    }


    private void addRandomNode() {
        LgNode lgNode = new LgNode();
        internalBounds.setBy(abstractLevelGenerator.coreModel.bounds);
        float delta = 0.03f * (internalBounds.width + internalBounds.height);
        internalBounds.increase(-delta);
        Random random = abstractLevelGenerator.random;
        lgNode.position.set(
                internalBounds.x + random.nextFloat() * internalBounds.width,
                internalBounds.y + random.nextFloat() * internalBounds.height
        );
        nodes.add(lgNode);
    }

}
