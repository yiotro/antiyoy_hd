package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class NetMatchBattleData implements ReusableYio, Encodeable {


    public LevelSize levelSize;
    public String levelCode;
    public ArrayList<NmbdItem> items;
    private ObjectPoolYio<NmbdItem> poolItems;
    private StringBuilder stringBuilder;
    public long turnEndTime;
    public long turnSeconds;
    public String matchId;
    private String creatorId;
    public boolean fog;


    public NetMatchBattleData() {
        items = new ArrayList<>();
        stringBuilder = new StringBuilder();
        initPools();
        reset();
    }


    private void initPools() {
        poolItems = new ObjectPoolYio<NmbdItem>(items) {
            @Override
            public NmbdItem makeNewObject() {
                return new NmbdItem();
            }
        };
    }


    @Override
    public void reset() {
        levelSize = null;
        levelCode = "-";
        clearItems();
        stringBuilder.setLength(0);
        turnEndTime = 0;
        turnSeconds = 0;
        matchId = "-";
        creatorId = "-";
        fog = false;
    }


    public synchronized void clearItems() {
        poolItems.clearExternalList();
    }


    public synchronized void addItem(String id, String name, HColor color, AvatarType avatarType) {
        NmbdItem freshObject = poolItems.getFreshObject();
        freshObject.id = id;
        freshObject.name = name;
        freshObject.color = color;
        freshObject.avatarType = avatarType;
    }


    @Override
    public String encode() {
        return levelSize + "/" + levelCode + "/" + encodeItems() + "/" + turnSeconds + "/" + matchId + "/" + creatorId + "/" + fog;
    }


    private synchronized String encodeItems() {
        if (items.size() == 0) return "-";
        stringBuilder.setLength(0);
        for (NmbdItem nmbdItem : items) {
            stringBuilder.append(nmbdItem.encode()).append(",");
        }
        return stringBuilder.toString();
    }


    public synchronized void decode(String source) {
        String[] split = source.split("/");
        if (split.length < 3) {
            System.out.println("NetMatchBattleData.decode, problem: " + source);
            return;
        }
        levelSize = LevelSize.valueOf(split[0]);
        levelCode = split[1];
        decodeItems(split[2]);
        turnSeconds = Long.valueOf(split[3]);
        matchId = split[4];
        creatorId = split[5];
        if (split.length > 6) {
            fog = Boolean.valueOf(split[6]);
        }
    }


    private void decodeItems(String source) {
        clearItems();
        for (String token : source.split(",")) {
            if (token.length() < 5) continue;
            NmbdItem freshObject = poolItems.getFreshObject();
            freshObject.decode(token);
        }
    }


    public NmbdItem getItem(HColor color) {
        for (NmbdItem nmbdItem : items) {
            if (nmbdItem.color == color) return nmbdItem;
        }
        return null;
    }


    public NmbdItem getItem(String id) {
        for (NmbdItem nmbdItem : items) {
            if (nmbdItem.id.equals(id)) return nmbdItem;
        }
        return null;
    }


    public HColor getColor(String id) {
        NmbdItem nmbdItem = getItem(id);
        if (nmbdItem == null) return null;
        return nmbdItem.color;
    }


    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
        if (this.creatorId.length() == 0) {
            this.creatorId = "-";
        }
    }


    public String getCreatorId() {
        return creatorId;
    }


    public void showInConsole() {
        System.out.println();
        System.out.println("NetMatchBattleData.showInConsole");
        System.out.println("levelCode = " + levelCode);
        System.out.println("levelSize = " + levelSize);
        System.out.println("turnDuration = " + turnSeconds);
        System.out.println("matchId = " + matchId);
        System.out.println("creatorId = " + creatorId);
        System.out.println("fog = " + fog);
        System.out.println("Items:");
        for (NmbdItem nmbdItem : items) {
            System.out.println("- [" + nmbdItem.encode() + "]");
        }
    }
}
