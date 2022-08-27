package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.export_import.Encodeable;

import java.util.ArrayList;

public class PlayerEntity implements Encodeable {

    public EntitiesManager entitiesManager;
    public EntityType type;
    public HColor color;
    public String name;
    public ArrayList<Relation> relations;


    public PlayerEntity(EntitiesManager entitiesManager, EntityType type, HColor color) {
        this.entitiesManager = entitiesManager;
        this.type = type;
        this.color = color;
        relations = new ArrayList<>();
        name = "-";
    }


    public Relation getRelation(PlayerEntity otherEntity) {
        if (otherEntity == this) {
            System.out.println("PlayerEntity.getRelation: problem");
        }
        for (Relation relation : relations) {
            if (relation.contains(otherEntity)) return relation;
        }
        return null;
    }


    @Override
    public String encode() {
        return type + ">" + color + ">" + name;
    }


    public boolean isHuman() {
        return type == EntityType.human;
    }


    public boolean isArtificialIntelligence() {
        switch (type) {
            default:
                return false;
            case ai_random:
            case ai_easy:
            case ai_average:
            case ai_hard:
            case ai_expert:
            case ai_balancer:
                return true;
        }
    }


    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "[" +
                getClass().getSimpleName() + ": " + encode() +
                "]";
    }
}
