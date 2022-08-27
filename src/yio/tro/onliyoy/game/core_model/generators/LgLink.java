package yio.tro.onliyoy.game.core_model.generators;

public class LgLink {

    public LgNode node1;
    public LgNode node2;


    public LgLink() {
        node1 = null;
        node2 = null;
    }


    boolean contains(LgNode node) {
        return node1 == node || node2 == node;
    }


    LgNode getOpposite(LgNode node) {
        if (node == node1) return node2;
        if (node == node2) return node1;
        return null;
    }
}
