package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.debug.DebugFlags;

import java.util.ArrayList;

public class MoveZoneManager {

    CoreModel coreModel;
    CmWaveWorker waveWorker;
    public ArrayList<Hex> hexes;
    private int limit;
    private int strength;
    PlayerEntity startEntity;


    public MoveZoneManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        hexes = new ArrayList<>();
        startEntity = null;
        initWaveWorker();
    }


    public void updateForUnit(Hex startHex) {
        if (!startHex.hasUnit()) {
            System.out.println("MoveZoneManager.updateForUnit: problem, " + coreModel);
            DebugFlags.detectedMoveZoneUpdateProblem = true;
            return;
        }
        update(startHex, 4, Core.getStrength(startHex.piece));
    }


    public void update(Hex startHex, int limit, int strength) {
        this.limit = limit;
        this.strength = strength;
        updateStartEntity(startHex);
        clear();
        if (startEntity == null) return;
        for (Hex hex : coreModel.hexes) {
            hex.flag = false;
        }
        waveWorker.apply(startHex);
    }


    private void updateStartEntity(Hex startHex) {
        startEntity = coreModel.entitiesManager.getEntity(startHex.color);
    }


    public void clear() {
        hexes.clear();
    }


    private void initWaveWorker() {
        waveWorker = new CmWaveWorker() {
            @Override
            public boolean condition(Hex parentHex, Hex hex) {
                if (parentHex.color != startHex.color) return false;
                if (parentHex.counter == 0) return false;
                if (hex.color != startHex.color && !coreModel.ruleset.canHexBeCaptured(hex, strength)) return false;
                if (!coreModel.diplomacyManager.isAttackAllowed(startEntity, hex)) return false;
                return true;
            }


            @Override
            public void action(Hex parentHex, Hex hex) {
                hexes.add(hex);
                if (parentHex != null) {
                    hex.counter = parentHex.counter - 1;
                } else {
                    hex.counter = limit;
                }
            }
        };
    }


    public void updateForFarm(Province province) {
        clear();
        for (Hex hex : province.getHexes()) {
            hex.flag = false;
        }
        for (Hex hex : province.getHexes()) {
            if (hex.piece != PieceType.city && hex.piece != PieceType.farm) continue;
            enableNearbyFlags(hex);
        }
        for (Hex hex : province.getHexes()) {
            if (!hex.flag) continue;
            if (!hex.isEmpty()) continue;
            hexes.add(hex);
        }
    }


    private void enableNearbyFlags(Hex hex) {
        hex.flag = true;
        for (Hex adjacentHex : hex.adjacentHexes) {
            adjacentHex.flag = true;
        }
    }


    public boolean contains(Hex hex) {
        return hexes.contains(hex);
    }
}
