package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.general.LevelSize;

public class LgParameters {

    PlayerEntity[] entities;
    float nodesValue;
    int landsDensity;
    double treesDensity;
    boolean loop;
    boolean neutralTowers;
    boolean neutralCities;
    boolean graves;
    boolean balancerEnabled;
    float balancerIntensity;


    public LgParameters() {
        entities = null;
        loop = true;
        nodesValue = 0.2f;
        landsDensity = 2;
        treesDensity = 0.15;
        neutralTowers = true;
        neutralCities = true;
        graves = true;
        balancerEnabled = true;
        balancerIntensity = 1;
    }


    public void setEntities(PlayerEntity[] entities) {
        this.entities = entities;
    }


    public void setLoop(boolean loop) {
        this.loop = loop;
    }


    public void setNodesValue(float nodesValue) {
        this.nodesValue = nodesValue;
    }


    public void setLandsDensity(int landsDensity) {
        this.landsDensity = landsDensity;
    }


    public void setTreesDensity(double treesDensity) {
        this.treesDensity = treesDensity;
    }


    public void setNeutralTowers(boolean neutralTowers) {
        this.neutralTowers = neutralTowers;
    }


    public void setNeutralCities(boolean neutralCities) {
        this.neutralCities = neutralCities;
    }


    public void setGraves(boolean graves) {
        this.graves = graves;
    }


    public void setBalancerEnabled(boolean balancerEnabled) {
        this.balancerEnabled = balancerEnabled;
    }


    public void setBalancerIntensity(float balancerIntensity) {
        this.balancerIntensity = balancerIntensity;
    }


    public void showInConsole() {
        System.out.println();
        System.out.println("LgParameters.showInConsole");
        System.out.println("entities.length = " + entities.length);
        System.out.println("loop = " + loop);
        System.out.println("nodesValue = " + Yio.roundUp(nodesValue, 2));
        System.out.println("landsDensity = " + landsDensity);
        System.out.println("treesDensity = " + Yio.roundUp(treesDensity, 2));
        System.out.println("neutralTowers = " + neutralTowers);
        System.out.println("neutralCities = " + neutralCities);
        System.out.println("graves = " + graves);
        System.out.println("balancer = " + balancerEnabled);
        System.out.println("balancerIntensity = " + balancerIntensity);
    }
}
