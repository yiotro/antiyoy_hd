package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.stuff.PointYio;

import java.util.ArrayList;

public class LgNode {

    public PointYio position;
    public ArrayList<LgLink> links;
    boolean flag;
    int counter;


    public LgNode() {
        position = new PointYio();
        links = new ArrayList<>();
    }


    void addLink(LgNode node1, LgNode node2) {
        LgLink lgLink = new LgLink();
        lgLink.node1 = node1;
        lgLink.node2 = node2;
        links.add(lgLink);
    }


    boolean isLinkedTo(LgNode node) {
        for (LgLink lgLink : links) {
            if (lgLink.contains(node)) return true;
        }
        return false;
    }
}
