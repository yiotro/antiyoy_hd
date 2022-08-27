package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AiBalancerClassicV1 extends AbstractAI {

    private ArrayList<Hex> result;
    private ArrayList<Hex> hexesInPerimeter;
    protected ArrayList<Province> nearbyProvinces;
    protected ArrayList<Hex> unitsReadyToMove;
    Comparator<Hex> comparator;


    public AiBalancerClassicV1(CoreModel coreModel) {
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
        moveAfkUnits();
    }


    @Override
    protected DiplomaticAI getDiplomaticAI() {
        return new DiplomaticAiNormal(coreModel);
    }


    void decideAboutUnit(Hex hex, ArrayList<Hex> moveZone, Province province) {
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


    int getAttackAllure(Hex hex, HColor color) {
        int c = 0;
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color != color) continue;
            c++;
            if (adjacentHex.piece == PieceType.city) {
                c += 5;
            }
        }
        return c;
    }


    void tryToBuildUnits(Province province) {
        if (!province.isValid()) return;
        tryToBuildUnitsOnPalms(province);

        for (int strength = 1; strength <= 4; strength++) {
            if (!canAffordUnit(province, strength, 5)) break;
            int c = 50;
            while (c > 0 && canAffordUnit(province, strength)) {
                c--;
                if (!tryToAttackWithStrength(province, strength)) break;
                if (!province.isValid()) return;
            }
        }

        // this is to kick-start province
        if (canAffordUnit(province, 1) && howManyUnitsInProvince(province) <= 1)
            tryToAttackWithStrength(province, 1);
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


    // expert


    protected boolean canUnitMoveSafely(Hex hex) {
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


    boolean needTowerOnHex(Hex hex) {
        if (hex.hasPiece()) return false;

        updateNearbyProvinces(hex);
        if (nearbyProvinces.size() == 0) return false; // build towers only at front line

        return getPredictedDefenseGainByNewTower(hex) >= 3;
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
                System.out.println("AiBalancerClassic.moveUnits: problem");
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
        updateUnitsReadyToMove();
        for (Hex hex : unitsReadyToMove) {
            if (!isReady(hex)) continue;
            Province province = hex.getProvince();
            if (province.getHexes().size() <= 20) continue;
            moveAfkUnit(province, hex);
        }
    }


    void mergeUnits(Province province) {
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
        if (!isReady(finish)) return false;
        if (start.hasSameCoordinatesAs(finish)) return false;
        PieceType mergeResult = Core.getMergeResult(start.piece, finish.piece);
        if (mergeResult == null) return false;
        int mergedStrength = getStrength(mergeResult);
        if (!canAffordUnit(province, mergedStrength)) return false;
        return true;
    }


    protected void spendMoney(Province province) {
        if (!province.isValid()) return;
        tryToBuildTowers(province);
        tryToBuildUnits(province);
    }


    void tryToBuildTowers(Province province) {
        int towerPrice = getRuleset().getPrice(province, PieceType.tower);
        int c = 100;
        while (c > 0 && province.getMoney() >= towerPrice) {
            c--;
            Hex hex = findHexThatNeedsTower(province);
            if (hex == null) break;
            commandPieceBuild(province, hex, PieceType.tower);
        }
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
        HColor color = province.getColor();
        int c = 50;
        while (c > 0 && canAffordUnit(province, 1)) {
            c--;
            coreModel.ruleset.updateMoveZoneForUnitConstruction(province, 1);
            copyMoveZoneToTempList();
            boolean killedPalm = false;
            for (Hex hex : tempList) {
                if (hex.piece != PieceType.palm) continue;
                if (hex.color != color) continue;
                commandUnitBuild(province, hex, 1);
                killedPalm = true;
                break;
            }
            if (!killedPalm) break;
        }
    }


    boolean checkToCleanSomeTrees(Hex unitHex, ArrayList<Hex> moveZone) {
        for (Hex hex : moveZone) {
            if (!hex.hasTree()) continue;
            if (hex.color != unitHex.color) continue;
            commandUnitMove(unitHex, hex);
            return true;
        }
        return false;
    }


    boolean checkToCleanSomePalms(Hex unitHex, ArrayList<Hex> moveZone) {
        for (Hex hex : moveZone) {
            if (hex.piece != PieceType.palm) continue;
            if (hex.color != unitHex.color) continue;
            commandUnitMove(unitHex, hex);
            return true;
        }
        return false;
    }


    Hex findHexAttractiveToBaron(ArrayList<Hex> attackableHexes, int strength) {
        for (Hex attackableHex : attackableHexes) {
            if (attackableHex.piece == PieceType.tower) return attackableHex;
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
