package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.EventType;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.menu.elements.setup_entities.EseType;
import yio.tro.onliyoy.stuff.name_generator.NameGenerator;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class EntitiesManager implements Encodeable, IEventListener {

    CoreModel coreModel;
    public PlayerEntity[] entities;
    private StringBuilder stringBuilder;
    private boolean aiOnlyMode;
    private NameGenerator nameGenerator;
    ObjectPoolYio<Relation> poolRelations;


    public EntitiesManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        if (coreModel != null) {
            coreModel.eventsManager.addListener(this);
        }
        stringBuilder = new StringBuilder();
        entities = null;
        aiOnlyMode = false;
        initPools();
        initNameGenerator();
    }


    private void initPools() {
        poolRelations = new ObjectPoolYio<Relation>() {
            @Override
            public Relation makeNewObject() {
                return new Relation();
            }
        };
    }


    public void initialize(String source) {
        ArrayList<PlayerEntity> list = new ArrayList<>();
        for (String token : source.split(",")) {
            String[] split = token.split(" ");
            if (split.length < 2) continue;
            EseType eseType = EseType.valueOf(split[0]);
            HColor color = HColor.valueOf(split[1]);
            EntityType entityType = convertFromEseType(eseType);
            PlayerEntity playerEntity = new PlayerEntity(this, entityType, color);
            playerEntity.setName(nameGenerator.generate());
            list.add(playerEntity);
        }
        initialize(list);
    }


    public void initialize(ArrayList<PlayerEntity> list) {
        entities = new PlayerEntity[list.size()];
        for (int i = 0; i < list.size(); i++) {
            entities[i] = list.get(i);
        }
        updateAiOnlyMode();
        onEntitiesInitialized();
    }


    private void onEntitiesInitialized() {
        if (coreModel == null) return;
        initRelations();
    }


    private void initRelations() {
        clearRelationsLists();
        for (int i = 0; i < entities.length; i++) {
            PlayerEntity entity1 = entities[i];
            for (int j = i + 1; j < entities.length; j++) {
                PlayerEntity entity2 = entities[j];
                Relation next = poolRelations.getNext();
                next.setEntity1(entity1);
                next.setEntity2(entity2);
                next.setType(RelationType.neutral);
                entity1.relations.add(next);
                entity2.relations.add(next);
            }
        }
    }


    private void clearRelationsLists() {
        for (PlayerEntity playerEntity : entities) {
            for (Relation relation : playerEntity.relations) {
                if (relation.entity1 != playerEntity) continue;
                poolRelations.add(relation);
            }
            playerEntity.relations.clear();
        }
    }


    public void setBy(EntitiesManager source) {
        decode(source.encode(), false);
    }


    public boolean contains(EntityType entityType) {
        for (PlayerEntity playerEntity : entities) {
            if (playerEntity.type == entityType) return true;
        }
        return false;
    }


    public int count(EntityType entityType) {
        int c = 0;
        for (PlayerEntity playerEntity : entities) {
            if (playerEntity.type != entityType) continue;
            c++;
        }
        return c;
    }


    public boolean isSingleplayerHumanMatch() {
        if (contains(EntityType.net_entity)) return false;
        if (contains(EntityType.spectator)) return false;
        if (!contains(EntityType.human)) return false;
        return true;
    }


    private void updateAiOnlyMode() {
        if (entities.length == 0) {
            aiOnlyMode = false;
            return;
        }
        aiOnlyMode = true;
        for (PlayerEntity playerEntity : entities) {
            if (playerEntity.isArtificialIntelligence()) continue;
            aiOnlyMode = false;
            break;
        }
    }


    private EntityType convertFromEseType(EseType eseType) {
        switch (eseType) {
            default:
                System.out.println("ProcessTrainingCreate.convert: problem");
                return null;
            case human:
                return EntityType.human;
            case robot:
                return EntityType.ai_balancer;
        }
    }


    public PlayerEntity getCurrentEntity() {
        return entities[coreModel.turnsManager.turnIndex];
    }


    public PlayerEntity getEntity(HColor color) {
        for (PlayerEntity playerEntity : entities) {
            if (playerEntity.color == color) return playerEntity;
        }
        return null;
    }


    public boolean canChangeBeDoneUnilaterally(RelationType currentRelationType, RelationType targetRelationType) {
        return targetRelationType.ordinal() < currentRelationType.ordinal();
    }


    public HColor getCurrentColor() {
        return getCurrentEntity().color;
    }


    public boolean isHumanTurnCurrently() {
        PlayerEntity currentEntity = getCurrentEntity();
        if (currentEntity == null) return false;
        return currentEntity.isHuman() && coreModel.provincesManager.getProvince(currentEntity.color) != null;
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        if (event.getType() == EventType.turn_end) {
            onTurnEndEventApplied();
        }
    }


    @Override
    public int getListenPriority() {
        return 8;
    }


    private void onTurnEndEventApplied() {

    }


    @Override
    public String encode() {
        if (entities == null) return "-";
        stringBuilder.setLength(0); // clear
        for (PlayerEntity playerEntity : entities) {
            stringBuilder.append(playerEntity.encode()).append(",");
        }
        return stringBuilder.toString();
    }


    public void decode(String source, boolean deadByDefault) {
        ArrayList<PlayerEntity> list = new ArrayList<>();
        for (String token : source.split(",")) {
            if (token.length() == 0) continue;
            String[] split = token.split(">");
            if (split.length < 3) continue;
            EntityType entityType = EntityType.valueOf(split[0]);
            if (deadByDefault) {
                entityType = EntityType.dead_by_default;
            }
            HColor color = HColor.valueOf(split[1]);
            if (color == HColor.gray) continue; // entity can't be neutral
            if (containsColor(list, color)) continue;
            PlayerEntity playerEntity = new PlayerEntity(this, entityType, color);
            playerEntity.setName(split[2]);
            list.add(playerEntity);
        }
        initialize(list);
    }


    private boolean containsColor(ArrayList<PlayerEntity> list, HColor color) {
        for (PlayerEntity playerEntity : list) {
            if (playerEntity.color == color) return true;
        }
        return false;
    }


    public boolean isInAiOnlyMode() {
        return aiOnlyMode;
    }


    private void initNameGenerator() {
        nameGenerator = new NameGenerator();
        nameGenerator.setCapitalize(true);
        nameGenerator.setMasks(getMasks());
        nameGenerator.setGroups(getGroups());
    }


    private HashMap<String, String> getGroups() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("k", "r t p s d k b n m");
        hashMap.put("m", "rb kr tr br bn cl rt t p s d k b nm mn sh ch gh j");
        hashMap.put("a", "o a ay e oy o e a ye ee i");
        hashMap.put("o", "na oy no in sa ya yo io io ko eo eo ao ao ek");
        return hashMap;
    }


    private ArrayList<String> getMasks() {
        ArrayList<String> list = new ArrayList<>();
        list.add("kama");
        list.add("kakako");
        list.add("kamao");
        list.add("kakkamo");
        list.add("kakko");
        list.add("amako");
        list.add("akamo");
        list.add("kamak");
        list.add("kakako");
        list.add("akako");
        list.add("amako");
        return list;
    }


    public void showInConsole() {
        System.out.println();
        System.out.println("EntitiesManager.showInConsole");
        PlayerEntity currentEntity = getCurrentEntity();
        for (PlayerEntity entity : entities) {
            if (entity == currentEntity) {
                System.out.println("+ " + entity);
                continue;
            }
            System.out.println("- " + entity);
        }
    }
}
