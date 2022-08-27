package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;

import java.util.ArrayList;

public class Appraiser {

    CoreModel coreModel;
    CmWaveWorker cmWaveWorker;
    ArrayList<Hex> tempList;


    public Appraiser(CoreModel coreModel) {
        this.coreModel = coreModel;
        tempList = new ArrayList<>();
        initWaveWorker();
    }


    private void initWaveWorker() {
        cmWaveWorker = new CmWaveWorker() {
            @Override
            protected boolean condition(Hex parentHex, Hex hex) {
                return hex.lgFlag;
            }


            @Override
            protected void action(Hex parentHex, Hex hex) {
                tempList.add(hex);
            }
        };
    }


    public boolean isAbuseDetected(PlayerEntity standpointEntity, Letter letter) {
        for (Condition condition : letter.conditions) {
            switch (condition.type) {
                default:
                    break;
                case give_lands:
                    if (condition.argHexes.size() == 1) return true;
                    break;
            }
        }
        return false;
    }


    public int estimate(PlayerEntity standpointEntity, Letter letter) {
        int sum = 0;
        for (Condition condition : letter.conditions) {
            int estimation = estimate(standpointEntity, condition);
            sum += estimation * 1.1f;
        }
        return sum;
    }


    public int estimate(PlayerEntity standpointEntity, Condition condition) {
        if (!coreModel.lettersManager.isValid(condition)) return 0;
        PlayerEntity executor = coreModel.entitiesManager.getEntity(condition.executorColor);
        int profitMultiplier = 1;
        if (executor == standpointEntity) {
            profitMultiplier = -1;
        }
        switch (condition.type) {
            default:
                System.out.println("Appraiser.estimate: problem");
                return 0;
            case notification:
                return 0;
            case give_money:
                return profitMultiplier * condition.argMoney;
            case give_lands:
                return profitMultiplier * estimate(condition.argHexes);
            case change_relation:
                return profitMultiplier * estimateChangeRelationCondition(standpointEntity, condition);
            case smileys:
                return 0;
        }
    }


    public int estimateChangeRelationCondition(PlayerEntity standpointEntity, Condition condition) {
        Letter letter = condition.letter;
        if (condition.argColor == null) {
            // simple situation
            HColor oppositeColor = letter.getOppositeColor(standpointEntity.color);
            PlayerEntity oppositeEntity = coreModel.entitiesManager.getEntity(oppositeColor);
            int standpointWealth = estimate(standpointEntity);
            int oppositeWealth = estimate(oppositeEntity);
            float wealthRatio = (float) oppositeWealth / (float) standpointWealth;
            RelationType wantedRelationType = getWantedRelationType(wealthRatio);
            RelationType proposedRelationType = condition.argRelationType;
            int distance = Math.abs(wantedRelationType.ordinal() - proposedRelationType.ordinal());
            float scale = 0.05f * standpointWealth;
            return (int) (scale - distance * scale);
        }
        return 200; // laziness
    }


    private RelationType getWantedRelationType(float wealthRatio) {
        if (wealthRatio < 0.7f) return RelationType.war;
        if (wealthRatio < 1.05f) return RelationType.neutral;
        if (wealthRatio < 1.2f) return RelationType.friend;
        return RelationType.alliance;
    }


    public int estimate(PlayerEntity entity) {
        int wealth = 0;
        for (Province province : coreModel.provincesManager.provinces) {
            if (province.getColor() != entity.color) continue;
            wealth += estimate(province.getHexes());
        }
        return wealth;
    }


    public int estimate(ArrayList<Hex> hexes) {
        int sumValue = 0;
        for (Hex hex : hexes) {
            sumValue += estimate(hex);
        }
        if (!isLinked(hexes)) {
            sumValue /= 5;
        }
        return sumValue;
    }


    private boolean isLinked(ArrayList<Hex> hexes) {
        if (hexes.size() == 0) return false;
        for (Hex hex : coreModel.hexes) {
            hex.lgFlag = false;
            hex.flag = false;
        }
        for (Hex hex : hexes) {
            hex.lgFlag = true;
        }
        tempList.clear();
        cmWaveWorker.apply(hexes.get(0));
        return tempList.size() == hexes.size();
    }


    public int estimate(Hex hex) {
        if (hex.piece == null) return 10;
        switch (hex.piece) {
            default:
                return 10;
            case pine:
                return 5;
            case palm:
                return 2;
            case farm:
                return 25;
            case knight:
                return 30;
            case baron:
                return 25;
            case spearman:
                return 20;
            case peasant:
                return 15;
            case strong_tower:
                return 30;
            case tower:
                return 20;
            case city:
                return 15;
            case grave:
                return 7;
        }
    }
}
