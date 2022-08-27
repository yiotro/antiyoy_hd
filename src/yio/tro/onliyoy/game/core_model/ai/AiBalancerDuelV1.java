package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.campaign.Difficulty;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AiBalancerDuelV1 extends AbstractAI{

    public static final int MAX_FARM_COST = 80;
    private ArrayList<Hex> result;
    private ArrayList<Hex> hexesInPerimeter;
    protected ArrayList<Province> nearbyProvinces;
    protected ArrayList<Hex> unitsReadyToMove;
    Comparator<Hex> comparator;


    public AiBalancerDuelV1(CoreModel coreModel) {
        super(coreModel);
        result = new ArrayList<>();
        hexesInPerimeter = new ArrayList<>();
        nearbyProvinces = new ArrayList<>();
        unitsReadyToMove = new ArrayList<>();
        initComparator();
    }


    private void initComparator() {
        comparator = new Comparator<Hex>() {
            @Override
            public int compare(Hex a, Hex b) {
                int aDefense = numberOfAdjacentUnits(a);
                int bDefense = numberOfAdjacentUnits(b);

                if (aDefense == bDefense) {
                    return getOwnedHexesQuantity(b.color) - getOwnedHexesQuantity(a.color);
                }

                return bDefense - aDefense;
            }
        };
    }


    @Override
    public int getVersionCode() {
        return 1;
    }


    @Override
    protected void apply() {
        coreModel.quickStatsManager.update();
        moveUnits();
        spendMoneyAndMergeUnits();
        checkToKillRedundantUnits();
        moveAfkUnits();
    }


    @Override
    protected DiplomaticAI getDiplomaticAI() {
        return new DiplomaticAiNormal(coreModel);
    }


    private void checkToKillRedundantUnits() {
        for (Province province : coreModel.provincesManager.provinces) {
            checkToKillRedundantUnits(province);
        }
    }


    private void checkToKillRedundantUnits(Province province) {
        if (isDifficultyLessThan(Difficulty.hard)) return;
        boolean detectedStrongUnits = false;
        for (Hex hex : province.getHexes()) {
            if (!hex.hasUnit()) continue;
            if (!isReady(hex)) return;
            if (getStrength(hex) < 3) continue;
            detectedStrongUnits = true;
        }
        if (!detectedStrongUnits) return;

        // so units are not doing anything. Time to kill them
        killRedundantUnits(province);
    }


    private void killRedundantUnits(Province province) {
        int c = 100;
        while (c > 0 && province.getMoney() >= getRuleset().getPrice(province, PieceType.peasant) && coreModel.economicsManager.calculateProvinceProfit(province) >= 0) {
            c--;
            Hex hex = findStrongestUnit(province, PieceType.knight);
            if (hex == null) break;
            if (!canAffordUnit(province, 1)) break;
            commandUnitBuild(province, hex, 1);
        }
    }


    protected boolean isOkToBuildNewFarm(Province srcProvince) {
        if (!isThereEnoughFreeSpaceForNewFarm(srcProvince)) return false;
        if (srcProvince.getMoney() > 2 * getRuleset().getPrice(srcProvince, PieceType.farm)) return true;

        int srcArmyStrength = getArmyStrength(srcProvince);
        updateNearbyProvinces(srcProvince);
        for (Province province : nearbyProvinces) {
            if (province == srcProvince) continue;
            int armyStrength = getArmyStrength(province);
            if (srcArmyStrength < armyStrength / 2) return false;
        }

        if (findHexThatNeedsTower(srcProvince) != null) return false;

        return true;
    }


    private boolean isThereEnoughFreeSpaceForNewFarm(Province province) {
        // in experimental rules kingdoms should leave some free space to be able to build new units
        if (province.getHexes().size() < 6) return true;
        int emptyHexes = 0;
        for (Hex hex : province.getHexes()) {
            if (hex.hasPiece()) continue;
            emptyHexes++;
        }
        return emptyHexes > 3;
    }


    private Hex findStrongestUnit(Province province, PieceType ignoredPieceType) {
        Hex bestHex = null;
        int maxStrength = 0;

        for (Hex hex : province.getHexes()) {
            if (!hex.hasUnit()) continue;
            if (hex.piece == ignoredPieceType) continue;
            int strength = getStrength(hex);
            if (bestHex == null || strength > maxStrength) {
                bestHex = hex;
                maxStrength = strength;
            }
        }

        return bestHex;
    }


    void decideAboutUnit(Hex hex, ArrayList<Hex> moveZone, Province province) {
        if (!isReady(hex)) return;

        // cleaning palms has highest priority
        if (getStrength(hex) <= 2 && checkToCleanSomePalms(hex, moveZone)) return;

        boolean cleanedTrees = checkToCleanSomeTrees(hex, moveZone);
        if (cleanedTrees) return;

        ArrayList<Hex> attackableHexes = findAttackableHexes(hex.color, moveZone);
        if (attackableHexes.size() > 0) { // attack something
            tryToAttackSomething(hex, attackableHexes);
        } else { // nothing to attack
            if (isAdjacentToEnemy(hex)) {
                pushUnitToBetterDefense(hex);
            }
        }
    }


    void pushUnitToBetterDefense(Hex hex) {
        if (isDifficultyLessThan(Difficulty.expert)) return;
        if (!isReady(hex)) return;

        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color != hex.color) continue;
            if (adjacentHex.hasPiece()) continue;
            if (predictDefenseGainWithUnitMove(hex, adjacentHex) < 3) continue;
            commandUnitMove(hex, adjacentHex);
            break;
        }
    }


    protected int predictDefenseGainWithUnitMove(Hex currentHex, Hex targetHex) {
        int defenseGain = 0;
        int strength = getStrength(currentHex.piece);

        defenseGain -= getDefenseValue(targetHex);
        defenseGain += strength;

        for (Hex adjacentHex : currentHex.adjacentHexes) {
            if (adjacentHex.color != currentHex.color) continue;
            defenseGain -= getDefenseValue(adjacentHex);
            defenseGain += strength;
        }

        return defenseGain;
    }


    protected void tryToAttackSomething(Hex hex, ArrayList<Hex> attackableHexes) {
        if (!canUnitMoveSafely(hex)) return;
        Hex mostAttackableHex = findMostAttractiveHex(attackableHexes, hex.color, getStrength(hex));
        if (mostAttackableHex == null) return;
        commandUnitMove(hex, mostAttackableHex);
    }


    Hex findMostAttractiveHex(ArrayList<Hex> attackableHexes, HColor color, int strength) {
        if (isDifficultyLessThan(Difficulty.average)) return getRandomHexFromList(attackableHexes);

        if (strength == 3 || strength == 4) {
            Hex hex = findHexAttractiveToBaron(attackableHexes, strength);
            if (hex != null) return hex;
        }

        Hex result = null;
        int currMax = -1;
        for (Hex attackableHex : attackableHexes) {
            int currNum = getAttackAllure(attackableHex, color);
            if (currNum > currMax) {
                currMax = currNum;
                result = attackableHex;
            }
        }
        return result;
    }


    private Hex getRandomHexFromList(ArrayList<Hex> hexes) {
        int index = random.nextInt(hexes.size());
        return hexes.get(index);
    }


    int getAttackAllure(Hex hex, HColor color) {
        int c = 0;
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color != color) continue;
            c++;
            if (adjacentHex.piece == PieceType.city) {
                c += 5;
            }
        }
        if (hex.piece == PieceType.farm) {
            c *= 2;
        }
        return c;
    }


    void tryToBuildUnits(Province province) {
        if (!province.isValid()) return;
        tryToBuildUnitsOnPalms(province);
        tryToReinforceUnits(province);

        for (int strength = 1; strength <= 4; strength++) {
            if (!canAffordUnit(province, strength, 5)) break;
            if (isDifficultyLessThan(Difficulty.average) && strength == 2) break;
            if (isDifficultyLessThan(Difficulty.hard) && strength == 3) break;
            if (isDifficultyLessThan(Difficulty.expert) && strength == 4) break;
            int c = 50;
            while (c > 0 && canAffordUnit(province, strength)) {
                c--;
                if (!tryToAttackWithStrength(province, strength)) break;
                if (!province.isValid()) return;
            }
        }

        // this is to kick-start province
        if (canAffordUnit(province, 1) && howManyUnitsInProvince(province) <= 1) {
            tryToAttackWithStrength(province, 1);
        }

        tryToBuildPeasantsForStock(province);
    }


    private void tryToBuildPeasantsForStock(Province province) {
        if (!province.isValid()) return;
        int income = coreModel.economicsManager.calculateProvinceIncome(province);
        if (province.getMoney() < income + 5) return;
        int limitCounter = countUnits(province);
        while (canAffordUnit(province, 1)) {
            limitCounter++;
            coreModel.ruleset.updateMoveZoneForUnitConstruction(province, 1);
            copyMoveZoneToTempList();
            boolean unitBuilt = false;
            for (int i = tempList.size() - 1; i >= 0; i--) {
                Hex hex = tempList.get(i);
                if (hex.hasPiece()) continue;
                if (hex.color != province.getColor()) continue;
                commandUnitBuild(province, hex, 1);
                unitBuilt = true;
                break;
            }
            if (!unitBuilt) break;
            if (limitCounter >= 14) break;
        }
    }


    private int countUnits(Province province) {
        int c = 0;
        for (Hex hex : province.getHexes()) {
            if (!hex.hasUnit()) continue;
            c++;
        }
        return c;
    }


    private void tryToReinforceUnits(Province province) {
        if (isDifficultyLessThan(Difficulty.balancer)) return;
        for (Hex hex : province.getHexes()) {
            if (!hex.hasUnit()) continue;
            int strength = getStrength(hex);
            if (unitHasToBeReinforced(hex) && canAffordUnit(province, strength + 1)) {
                commandUnitBuild(province, hex, 1);
            }
        }
    }


    private boolean unitHasToBeReinforced(Hex unitHex) {
        int strength = getStrength(unitHex);
        if (strength == 4) return false;

        getMoveZoneManager().updateForUnit(unitHex);
        ArrayList<Hex> moveZone = getMoveZoneManager().hexes;
        if (!moveZoneContainsEnemyHexes(moveZone, unitHex.color)) return false;

        ArrayList<Hex> attackableHexes = findAttackableHexes(unitHex.color, moveZone);
        if (attackableHexes.size() > 0) return false;

        return true;
    }


    private boolean moveZoneContainsEnemyHexes(ArrayList<Hex> moveZone, HColor color) {
        for (Hex hex : moveZone) {
            if (hex.color != color) return true;
        }
        return false;
    }


    protected boolean isHexDefendedBySomethingElse(Hex hex, Hex ignoredHex) {
        if (getDefenseValue(hex, ignoredHex) == 0) return false;
        return getDefenseValue(hex) - getDefenseValue(hex, ignoredHex) < 2;
    }


    ArrayList<Hex> findAttackableHexes(HColor color, ArrayList<Hex> moveZone) {
        result.clear();
        for (Hex hex : moveZone) {
            if (hex.color == color) continue;
            result.add(hex);
        }
        // top players will be attacked first
        Collections.sort(result, comparator);
        return result;
    }


    private int numberOfAdjacentUnits(Hex hex) {
        int c = 0;

        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color != hex.color) continue;
            if (!adjacentHex.hasUnit() || !adjacentHex.hasTower()) continue; // yes, it was like this in original code
            c++;
        }

        return c;
    }


    protected int getOwnedHexesQuantity(HColor color) {
        if (color == null) return -1;
        return coreModel.quickStatsManager.getQuantity(color);
    }


    boolean needTowerOnHex(Hex hex) {
        if (hex.hasPiece()) return false;

        updateNearbyProvinces(hex);
        if (nearbyProvinces.size() == 0) return false; // build towers only at front line
        if (coreModel.turnsManager.lap == 0) return false; // don't build towers on first lap

        return getPredictedDefenseGainByNewTower(hex) >= 3;
    }


    // expert


    protected boolean canUnitMoveSafely(Hex hex) {
        if (isDifficultyLessThan(Difficulty.hard)) return true;
        int leftBehindNumber = 0;
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color == hex.color && !isHexDefendedBySomethingElse(adjacentHex, hex) && isAdjacentToEnemy(adjacentHex)) {
                leftBehindNumber++;
            }
        }
        return leftBehindNumber <= 3;
    }


    private Hex findRandomHexInPerimeter(Province province) {
        hexesInPerimeter.clear();
        for (Hex hex : province.getHexes()) {
            if (!isAdjacentToEnemy(hex)) continue;
            hexesInPerimeter.add(hex);
        }
        if (hexesInPerimeter.size() == 0) return null;
        return hexesInPerimeter.get(random.nextInt(hexesInPerimeter.size()));
    }


    void moveAfkUnit(Province province, Hex hex) {
        Hex targetHex = findRandomHexInPerimeter(province);
        if (targetHex == null) return;
        if (!emulateMassMarchForSingleUnit(hex, targetHex)) {
            moveAfkUnitQuickly(hex); // to prevent infinite loop
        }
    }


    void moveAfkUnitQuickly(Hex hex) {
        getMoveZoneManager().updateForUnit(hex);
        ArrayList<Hex> moveZone = getMoveZoneManager().hexes;
        excludeFriendlyUnitsFromMoveZone(moveZone);
        excludeFriendlyBuildingsFromMoveZone(moveZone);
        if (moveZone.size() == 0) return;
        Hex targetHex = moveZone.get(random.nextInt(moveZone.size()));
        commandUnitMove(hex, targetHex);
    }


    void tryToBuildTowers(Province province) {
        if (isDifficultyLessThan(Difficulty.average)) return;
        // try to build normal towers
        int towerPrice = getRuleset().getPrice(province, PieceType.tower);
        int c = 100;
        while (c > 0 && province.getMoney() >= towerPrice) {
            c--;
            Hex hex = findHexThatNeedsTower(province);
            if (hex == null) break;
            commandPieceBuild(province, hex, PieceType.tower);
        }

        // try to build strong towers
        if (isDifficultyLessThan(Difficulty.expert)) return;
        while (c > 0 && provinceCanAffordStrongTower(province)) {
            c--;
            Hex hex = findHexForStrongTower(province);
            if (hex == null) break;
            commandPieceBuild(province, hex, PieceType.strong_tower);
        }
    }


    protected Hex findHexForStrongTower(Province province) {
        for (Hex hex : province.getHexes()) {
            if (hex.piece != PieceType.tower) continue;
            if (!needsStrongTowerOnHex(province, hex)) continue;
            return hex;
        }
        return null;
    }


    protected boolean needsStrongTowerOnHex(Province province, Hex hex) {
        updateNearbyProvinces(hex);
        if (nearbyProvinces.size() == 0) return false;

        for (Province nearbyProvince : nearbyProvinces) {
            if (nearbyProvince.getHexes().size() > province.getHexes().size() / 2) {
                return true;
            }
        }

        return false;
    }


    protected boolean provinceCanAffordStrongTower(Province province) {
        int strongTowerPrice = getRuleset().getPrice(province, PieceType.strong_tower);
        if (province.getMoney() < strongTowerPrice) return false;
        int peasantPrice = getRuleset().getPrice(province, PieceType.peasant);
        int strongTowerConsumption = getRuleset().getConsumption(PieceType.strong_tower);
        int profit = coreModel.economicsManager.calculateProvinceProfit(province);
        return profit - strongTowerConsumption >= peasantPrice / 2;
    }


    // ai generic


    protected void spendMoney(Province province) {
        if (!province.isValid()) return;
        tryToBuildTowers(province);
        tryToBuildFarms(province);
        tryToBuildUnits(province);
    }


    protected void tryToBuildFarms(Province province) {
        if (isDifficultyLessThan(Difficulty.average)) return;
        if (getRuleset().getPrice(province, PieceType.farm) > MAX_FARM_COST) return;

        int c = 50;
        while (c > 0 && province.getMoney() >= getRuleset().getPrice(province, PieceType.farm)) {
            c--;
            if (!isOkToBuildNewFarm(province)) return;
            Hex hex = findGoodHexForFarm(province);
            if (hex == null) return;
            commandPieceBuild(province, hex, PieceType.farm);
        }
    }


    protected int getArmyStrength(Province province) {
        int sum = 0;
        for (Hex hex : province.getHexes()) {
            if (!hex.hasUnit()) continue;
            sum += getStrength(hex);
        }
        return sum;
    }


    protected Hex findGoodHexForFarm(Province province) {
        if (!hasProvinceGoodHexForFarm(province)) return null;
        int c = 0;
        while (c < 1000) {
            c++;
            Hex hex = province.getHexes().get(random.nextInt(province.getHexes().size()));
            if (isHexGoodForFarm(hex)) return hex;
        }
        return null;
    }


    protected boolean hasProvinceGoodHexForFarm(Province province) {
        for (Hex hex : province.getHexes()) {
            if (!isHexGoodForFarm(hex)) continue;
            return true;
        }
        return false;
    }


    protected boolean isHexGoodForFarm(Hex hex) {
        if (hex.hasPiece()) return false;
        if (hasAdjacentFriendlyPiece(hex, PieceType.city)) return true;
        if (hasAdjacentFriendlyPiece(hex, PieceType.farm)) return true;
        return false;
    }


    // ai


    void updateUnitsReadyToMove() {
        unitsReadyToMove.clear();
        for (Province province : coreModel.provincesManager.provinces) {
            if (province.getColor() != getCurrentColor()) continue;
            for (Hex hex : province.getHexes()) {
                if (!hex.hasUnit()) continue;
                if (!isReady(hex)) continue;
                unitsReadyToMove.add(hex);
            }
        }
    }


    void moveUnits() {
        updateUnitsReadyToMove();
        for (Hex hex : unitsReadyToMove) {
            if (!isReady(hex)) {
                System.out.println("AiBalancerDefault.moveUnits: problem");
                continue;
            }

            getMoveZoneManager().updateForUnit(hex);
            ArrayList<Hex> moveZone = getMoveZoneManager().hexes;
            excludeFriendlyBuildingsFromMoveZone(moveZone);
            excludeFriendlyUnitsFromMoveZone(moveZone);
            if (moveZone.size() == 0) continue;
            Province province = hex.getProvince();
            if (province == null) continue;
            decideAboutUnit(hex, moveZone, province);
        }
    }


    void spendMoneyAndMergeUnits() {
        ArrayList<Province> provinces = coreModel.provincesManager.provinces;
        for (int i = 0; i < provinces.size(); i++) {
            Province province = provinces.get(i);
            if (!province.isValid()) continue;
            if (province.getColor() != getCurrentColor()) continue;
            spendMoney(province);
            mergeUnits(province);
        }
    }


    void moveAfkUnits() {
        if (isDifficultyLessThan(Difficulty.expert)) return;
        updateUnitsReadyToMove();
        for (Hex hex : unitsReadyToMove) {
            if (!isReady(hex)) continue;
            Province province = hex.getProvince();
            if (province.getHexes().size() <= 20) continue;
            moveAfkUnit(province, hex);
        }
    }


    void mergeUnits(Province province) {
        if (isDifficultyLessThan(Difficulty.hard)) return;
        if (!province.isValid()) return;
        for (Hex hex : province.getHexes()) {
            if (!hex.hasUnit()) continue;
            if (!isReady(hex)) continue;
            tryToMergeWithSomeone(province, hex);
        }
    }


    private void tryToMergeWithSomeone(Province province, Hex unitHex) {
        getMoveZoneManager().updateForUnit(unitHex);
        ArrayList<Hex> moveZone = getMoveZoneManager().hexes;
        if (moveZone.size() == 0) return;
        for (Hex hex : moveZone) {
            if (!mergeConditions(province, unitHex, hex)) continue;
            commandMergeUnits(unitHex, hex);
            break;
        }
    }


    protected boolean mergeConditions(Province province, Hex start, Hex finish) {
        if (start.color != finish.color) return false;
        if (!start.hasUnit()) return false;
        if (!finish.hasUnit()) return false;
        if (!isReady(start)) return false;
        if (start.hasSameCoordinatesAs(finish)) return false;
        PieceType mergeResult = Core.getMergeResult(start.piece, finish.piece);
        if (mergeResult == null) return false;
        int mergedStrength = getStrength(mergeResult);
        if (!canAffordUnit(province, mergedStrength)) return false;
        return true;
    }


    protected Hex findHexThatNeedsTower(Province province) {
        for (Hex hex : province.getHexes()) {
            if (needTowerOnHex(hex)) return hex;
        }
        return null;
    }


    protected int getPredictedDefenseGainByNewTower(Hex hex) {
        int c = 0;
        if (!isDefendedByTower(hex)) c++;
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color != hex.color) continue;
            if (!isDefendedByTower(adjacentHex)) {
                c++;
            }
            if (adjacentHex.hasTower()) {
                c--;
            }
        }
        return c;
    }


    protected void updateNearbyProvinces(Province srcProvince) {
        nearbyProvinces.clear();
        for (Hex hex : srcProvince.getHexes()) {
            for (Hex adjacentHex : hex.adjacentHexes) {
                checkToAddNearbyProvince(hex, adjacentHex);
            }
        }
    }


    protected void updateNearbyProvinces(Hex srcHex) {
        nearbyProvinces.clear();
        int rotatedDir;
        for (int dir = 0; dir < 6; dir++) {
            Hex adjacentHex = getAdjacentHex(srcHex, dir);
            if (adjacentHex == null) continue;

            Hex adjacentHex2 = getAdjacentHex(adjacentHex, dir);
            rotatedDir = dir + 1;
            if (rotatedDir >= 6) rotatedDir = 0;
            Hex adjacentHex3 = getAdjacentHex(adjacentHex, rotatedDir);

            checkToAddNearbyProvince(srcHex, adjacentHex);
            checkToAddNearbyProvince(srcHex, adjacentHex2);
            checkToAddNearbyProvince(srcHex, adjacentHex3);
        }
    }


    private void checkToAddNearbyProvince(Hex srcHex, Hex adjacentHex) {
        if (adjacentHex == null) return;
        if (adjacentHex.isNeutral()) return;
        if (adjacentHex.color == srcHex.color) return;
        addProvinceToNearbyProvinces(adjacentHex.getProvince());
    }


    private void addProvinceToNearbyProvinces(Province province) {
        if (province == null) return;
        if (nearbyProvinces.contains(province)) return;
        nearbyProvinces.add(province);
    }


    boolean tryToAttackWithStrength(Province province, int strength) {
        coreModel.ruleset.updateMoveZoneForUnitConstruction(province, strength);
        ArrayList<Hex> moveZone = getMoveZoneManager().hexes;
        ArrayList<Hex> attackableHexes = findAttackableHexes(province.getColor(), moveZone);
        if (attackableHexes.size() == 0) return false;

        Hex bestHexForAttack = findMostAttractiveHex(attackableHexes, province.getColor(), strength);
        commandUnitBuild(province, bestHexForAttack, strength);
        return true;
    }


    void tryToBuildUnitsOnPalms(Province province) {
        if (isDifficultyLessThan(Difficulty.hard)) return;
        int c = 50;
        while (c > 0 && canAffordUnit(province, 1)) {
            c--;
            coreModel.ruleset.updateMoveZoneForUnitConstruction(province, 1);
            copyMoveZoneToTempList();
            boolean killedPalm = false;
            for (Hex hex : tempList) {
                if (hex.piece != PieceType.palm) continue;
                if (hex.color != province.getColor()) continue;
                commandUnitBuild(province, hex, 1);
                killedPalm = true;
                break;
            }
            if (!killedPalm) break;
        }
    }


    boolean checkToCleanSomeTrees(Hex unitHex, ArrayList<Hex> moveZone) {
        if (isDifficultyLessThan(Difficulty.expert)) return false;
        for (Hex hex : moveZone) {
            if (!hex.hasTree()) continue;
            if (hex.color != unitHex.color) continue;
            commandUnitMove(unitHex, hex);
            return true;
        }
        return false;
    }


    boolean checkToCleanSomePalms(Hex unitHex, ArrayList<Hex> moveZone) {
        if (isDifficultyLessThan(Difficulty.hard)) return false;
        for (Hex hex : moveZone) {
            if (hex.piece != PieceType.palm) continue;
            if (hex.color != unitHex.color) continue;
            commandUnitMove(unitHex, hex);
            return true;
        }
        return false;
    }


    Hex findHexAttractiveToBaron(ArrayList<Hex> attackableHexes, int strength) {
        if (isDifficultyLessThan(Difficulty.balancer)) return null;
        for (Hex attackableHex : attackableHexes) {
            if (attackableHex.piece == PieceType.tower) return attackableHex;
            if (strength == 4 && attackableHex.piece == PieceType.strong_tower) return attackableHex;
        }
        for (Hex attackableHex : attackableHexes) {
            if (!isDefendedByTower(attackableHex)) continue;
            return attackableHex;
        }
        return null;
    }


    private void excludeFriendlyBuildingsFromMoveZone(ArrayList<Hex> moveZone) {
        tempList.clear();
        HColor currentColor = getCurrentColor();
        for (Hex hex : moveZone) {
            if (hex.color != currentColor) continue;
            if (hex.hasStaticPiece() && !hex.hasTree()) {
                tempList.add(hex);
            }
        }
        moveZone.removeAll(tempList);
    }


    private void excludeFriendlyUnitsFromMoveZone(ArrayList<Hex> moveZone) {
        tempList.clear();
        HColor currentColor = getCurrentColor();
        for (Hex hex : moveZone) {
            if (hex.color != currentColor) continue;
            if (!hex.hasUnit()) continue;
            tempList.add(hex);
        }
        moveZone.removeAll(tempList);
    }


    int howManyUnitsInProvince(Province province) {
        int c = 0;
        for (Hex hex : province.getHexes()) {
            if (hex.hasUnit()) c++;
        }
        return c;
    }

}
