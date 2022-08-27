package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.campaign.Difficulty;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.viewable_model.DirectionsManager;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractAI {

    CoreModel coreModel;
    Random random;
    private DirectionsManager directionsManager;
    protected ArrayList<Hex> tempList;
    protected DiplomaticAI diplomaticAI;
    protected Difficulty difficulty; // used internally to adapt to difficulty


    public AbstractAI(CoreModel coreModel) {
        this.coreModel = coreModel;
        random = new Random();
        directionsManager = new DirectionsManager(coreModel);
        tempList = new ArrayList<>();
        diplomaticAI = getDiplomaticAI();
    }


    public void perform() {
        apply();
        checkToApplyDiplomaticAI();
        commandTurnEnd();
    }


    public void checkToDetermineRandom() {
        if (!DebugFlags.determinedRandom) return;
        int seed = (new Random()).nextInt();
//        System.out.println("seed = " + seed);
//        seed = -1060474092;
        random = new Random(seed);
    }


    private void checkToApplyDiplomaticAI() {
        if (!coreModel.diplomacyManager.enabled) return;
        diplomaticAI.apply();
    }


    public abstract int getVersionCode();


    protected abstract void apply();


    protected abstract DiplomaticAI getDiplomaticAI();


    protected EventsFactory getEventsFactory() {
        return coreModel.eventsManager.factory;
    }


    protected void commandTurnEnd() {
        EventTurnEnd endTurnEvent = getEventsFactory().createEndTurnEvent();
        endTurnEvent.setAuthor(coreModel.entitiesManager.getCurrentEntity());
        coreModel.eventsManager.applyEvent(endTurnEvent);
    }


    protected void commandPieceBuild(Province parentProvince, Hex hex, PieceType pieceType) {
        if (Core.isUnit(pieceType)) {
            System.out.println("AbstractAI.commandPieceBuild: wrong method called");
            return;
        }
        EventPieceBuild buildPieceEvent = getEventsFactory().createBuildPieceEvent(
                hex,
                pieceType,
                coreModel.getIdForNewUnit(),
                parentProvince.getId()
        );
        buildPieceEvent.setAuthor(coreModel.entitiesManager.getCurrentEntity());
        coreModel.eventsManager.applyEvent(buildPieceEvent);
    }


    protected void commandUnitBuild(Province province, Hex hex, int strength) {
        PieceType pieceType = getUnitPieceType(strength);
        if (hex.hasUnit() && hex.color == province.getColor()) {
            EventMergeOnBuild eventMergeOnBuild = getEventsFactory().createEventMergeOnBuild(
                    hex,
                    pieceType,
                    coreModel.getIdForNewUnit(),
                    province.getId()
            );
            eventMergeOnBuild.setAuthor(coreModel.entitiesManager.getCurrentEntity());
            coreModel.eventsManager.applyEvent(eventMergeOnBuild);
            return;
        }
        EventPieceBuild buildPieceEvent = getEventsFactory().createBuildPieceEvent(
                hex,
                pieceType,
                coreModel.getIdForNewUnit(),
                province.getId()
        );
        buildPieceEvent.setAuthor(coreModel.entitiesManager.getCurrentEntity());
        coreModel.eventsManager.applyEvent(buildPieceEvent);
    }


    protected void commandUnitMove(Hex start, Hex finish) {
        EventUnitMove moveUnitEvent = getEventsFactory().createMoveUnitEvent(start, finish);
        moveUnitEvent.setAuthor(coreModel.entitiesManager.getCurrentEntity());
        coreModel.eventsManager.applyEvent(moveUnitEvent);
    }


    protected void commandMergeUnits(Hex start, Hex finish) {
        int idForNewUnit = coreModel.getIdForNewUnit();
        EventMerge mergeEvent = getEventsFactory().createMergeEvent(start, finish, idForNewUnit);
        mergeEvent.setAuthor(coreModel.entitiesManager.getCurrentEntity());
        coreModel.eventsManager.applyEvent(mergeEvent);
    }


    protected boolean isOwned(Province province) {
        return province.isOwnedByCurrentEntity();
    }


    protected MoveZoneManager getMoveZoneManager() {
        return coreModel.moveZoneManager;
    }


    protected boolean isReady(Hex hex) {
        return coreModel.readinessManager.isReady(hex);
    }


    protected AbstractRuleset getRuleset() {
        return coreModel.ruleset;
    }


    protected int getStrength(Hex hex) {
        return Core.getStrength(hex.piece);
    }


    protected int getStrength(PieceType pieceType) {
        return Core.getStrength(pieceType);
    }


    protected boolean isAdjacentToEnemy(Hex hex) {
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.isNeutral()) continue;
            if (adjacentHex.color == hex.color) continue;
            if (adjacentHex.getProvince() == null) continue;
            return true;
        }
        return false;
    }


    protected int getDefenseValue(Hex hex) {
        return getRuleset().getDefenseValue(hex);
    }


    protected int getDefenseValue(Hex hex, Hex ignoredHex) {
        int maxValue = getRuleset().getDefenseValue(hex.piece);
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex == ignoredHex) continue;
            if (adjacentHex.color != hex.color) continue;
            int value = getRuleset().getDefenseValue(adjacentHex.piece);
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }


    protected boolean canAffordUnit(Province province, int strength) {
        return canAffordUnit(province, strength, strength + 1);
    }


    protected boolean canAffordUnit(Province province, int strength, int turnsToSurvive) {
        int currentProfit = coreModel.economicsManager.calculateProvinceProfit(province);
        PieceType unitPieceType = getUnitPieceType(strength);
        if (province.getMoney() < getRuleset().getPrice(province, unitPieceType)) return false;
        int consumption = getRuleset().getConsumption(unitPieceType);
        int newProfit = currentProfit - consumption;
        return province.getMoney() + turnsToSurvive * newProfit >= 0;
    }


    protected boolean emulateMassMarchForSingleUnit(Hex hex, Hex targetHex) {
        getMoveZoneManager().updateForUnit(hex);
        Hex closestHex = findClosestEmptyFriendlyHex(getMoveZoneManager().hexes, targetHex, hex.color);
        if (closestHex == null) return false;
        commandUnitMove(hex, closestHex);
        return true;
    }


    private Hex findClosestEmptyFriendlyHex(ArrayList<Hex> list, Hex targetHex, HColor color) {
        Hex bestHex = null;
        float minDistance = 0;
        for (Hex hex : list) {
            if (hex.hasPiece() && !hex.hasTree()) continue;
            if (hex.color != color) continue;
            float currentDistance = hex.position.center.fastDistanceTo(targetHex.position.center);
            if (bestHex == null || currentDistance < minDistance) {
                bestHex = hex;
                minDistance = currentDistance;
            }
        }
        return bestHex;
    }


    protected boolean hasAdjacentFriendlyPiece(Hex hex, PieceType pieceType) {
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color != hex.color) continue;
            if (adjacentHex.piece != pieceType) continue;
            return true;
        }
        return false;
    }


    protected boolean isDefendedByTower(Hex hex) {
        if (hex.piece == PieceType.tower) return true;
        if (hex.piece == PieceType.strong_tower) return true;
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color != hex.color) continue;
            if (adjacentHex.piece == PieceType.tower) return true;
            if (adjacentHex.piece == PieceType.strong_tower) return true;
        }
        return false;
    }


    protected Hex getAdjacentHex(Hex hex, int direction) {
        return directionsManager.getAdjacentHex(hex, direction);
    }


    protected PieceType getUnitPieceType(int strength) {
        switch (strength) {
            default:
            case 0:
                return null;
            case 1:
                return PieceType.peasant;
            case 2:
                return PieceType.spearman;
            case 3:
                return PieceType.baron;
            case 4:
                return PieceType.knight;
        }
    }


    protected void copyMoveZoneToTempList() {
        tempList.clear();
        tempList.addAll(getMoveZoneManager().hexes);
    }


    protected HColor getCurrentColor() {
        return coreModel.entitiesManager.getCurrentColor();
    }


    protected boolean isDifficultyLessThan(Difficulty targetDifficulty) {
        return difficulty.ordinal() < targetDifficulty.ordinal();
    }


    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
