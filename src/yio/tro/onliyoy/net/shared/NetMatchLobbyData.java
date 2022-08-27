package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.export_import.*;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;
import java.util.Arrays;

public class NetMatchLobbyData implements ReusableYio, Encodeable {


    public LevelSize levelSize;
    public RulesType rulesType;
    public boolean diplomacy;
    public String levelCode;
    public ArrayList<PlayerEntity> entities;
    private EntitiesManager entitiesManager; // used as utility
    public int turnSeconds;
    public String password;
    public String matchId;
    public boolean hasCreator;
    public long startTime;
    public String creatorId;
    public long creationTime;
    public boolean fog;
    public NetMatchType matchType;


    public NetMatchLobbyData() {
        entitiesManager = new EntitiesManager(null);
        entities = new ArrayList<>();
        reset();
    }


    @Override
    public void reset() {
        levelSize = null;
        rulesType = null;
        diplomacy = false;
        levelCode = "-";
        entities.clear();
        turnSeconds = 0;
        setPassword("");
        matchId = "-";
        hasCreator = false;
        startTime = 0;
        creatorId = "-";
        creationTime = 0;
        fog = false;
        matchType = null;
    }


    @Override
    public String encode() {
        entitiesManager.initialize(entities);
        return levelSize +
                "/" + rulesType +
                "/" + diplomacy +
                "/" + entitiesManager.encode() +
                "/" + turnSeconds +
                "/" + password +
                "/" + matchId +
                "/" + hasCreator +
                "/" + levelCode +
                "/" + startTime +
                "/" + creatorId +
                "/" + creationTime +
                "/" + fog +
                "/" + matchType;
    }


    public void decode(String source) {
        String[] split = source.split("/");
        if (split.length < 7) {
            System.out.println("NetMatchParameters.decode, problem: " + source);
            return;
        }
        levelSize = LevelSize.valueOf(split[0]);
        rulesType = RulesType.valueOf(split[1]);
        diplomacy = Boolean.valueOf(split[2]);
        entitiesManager.decode(split[3], false);
        entities.clear();
        entities.addAll(Arrays.asList(entitiesManager.entities));
        turnSeconds = Integer.valueOf(split[4]);
        password = split[5];
        if (split[6].length() > 3) {
            matchId = split[6];
        }
        hasCreator = Boolean.valueOf(split[7]);
        levelCode = split[8];
        startTime = Long.valueOf(split[9]);
        creatorId = split[10];
        creationTime = Long.valueOf(split[11]);
        if (split.length > 12) {
            fog = Boolean.valueOf(split[12]);
        }
        if (split.length > 13) {
            matchType = NetMatchType.valueOf(split[13]);
        }
    }


    public void initForQuickMatch() {
        levelSize = LevelSize.small;
        rulesType = RulesType.def;
        diplomacy = false;
        turnSeconds = 25;
        hasCreator = false;
        fog = false;
        entities.clear();
        entities.add(new PlayerEntity(null, EntityType.human, HColor.aqua));
        entities.add(new PlayerEntity(null, EntityType.human, HColor.cyan));
        entities.add(new PlayerEntity(null, EntityType.human, HColor.yellow));
        entities.add(new PlayerEntity(null, EntityType.human, HColor.brown));
        entities.add(new PlayerEntity(null, EntityType.human, HColor.blue));
        entities.add(new PlayerEntity(null, EntityType.human, HColor.purple));
    }


    public void fixByLevelCode(CoreModel coreModel) {
        if (levelCode.length() < 5) return;
        // level size
        IwExtractLevelSize iwExtractLevelSize = new IwExtractLevelSize();
        iwExtractLevelSize.perform(levelCode);
        levelSize = iwExtractLevelSize.levelSize;
        // rules
        (new IwCoreRules(coreModel)).perform(levelCode);
        rulesType = coreModel.ruleset.getRulesType();
        // diplomacy
        (new IwCorePlayerEntities(coreModel)).perform(levelCode);
        (new IwCoreDiplomacy(coreModel)).perform(levelCode);
        diplomacy = coreModel.diplomacyManager.enabled;
        // fog
        (new IwCoreFogOfWar(coreModel)).perform(levelCode);
        fog = coreModel.fogOfWarManager.enabled;
        // entities
        (new IwCorePlayerEntities(coreModel)).perform(levelCode);
        EntitiesManager entitiesManager = coreModel.entitiesManager;
        PlayerEntity[] cmEntities = entitiesManager.entities;
        for (PlayerEntity entity : cmEntities) {
            entity.type = EntityType.human;
        }
        this.entities = new ArrayList<>(Arrays.asList(cmEntities));
    }


    public PlayerEntity[] getEntitiesAsArray() {
        entitiesManager.initialize(entities);
        return entitiesManager.entities;
    }


    public boolean hasPassword() {
        return !password.equals("-");
    }


    public void setPassword(String string) {
        if (string == null || string.length() == 0) {
            password = "-";
            return;
        }
        password = string;
    }


    public void setEntitiesFromUI(String code) {
        entitiesManager.initialize(code);
        entities.clear();
        entities.addAll(Arrays.asList(entitiesManager.entities));
    }


    public boolean contains(HColor color) {
        for (PlayerEntity playerEntity : entities) {
            if (playerEntity.color == color) return true;
        }
        return false;
    }
}