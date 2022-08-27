package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.CmWaveWorker;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class DefenseViewer {

    ViewableModel viewableModel;
    ArrayList<Hex> defensiveStructures;
    CmWaveWorker cmWaveWorker;
    public ArrayList<DefenseIndicator> indicators;
    ObjectPoolYio<DefenseIndicator> poolIndicators;
    RepeatYio<DefenseViewer> repeatRemoveDeadIndicators;


    public DefenseViewer(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
        defensiveStructures = new ArrayList<>();
        indicators = new ArrayList<>();
        initPools();
        initWaveWorker();
        initRepeats();
    }


    private void initRepeats() {
        repeatRemoveDeadIndicators = new RepeatYio<DefenseViewer>(this, 60) {
            @Override
            public void performAction() {
                parent.removeDeadIndicators();
            }
        };
    }


    private void initPools() {
        poolIndicators = new ObjectPoolYio<DefenseIndicator>(indicators) {
            @Override
            public DefenseIndicator makeNewObject() {
                return new DefenseIndicator(DefenseViewer.this);
            }
        };
    }


    private void initWaveWorker() {
        cmWaveWorker = new CmWaveWorker() {
            @Override
            protected boolean condition(Hex parentHex, Hex hex) {
                return parentHex.counter < 8 && parentHex.color == hex.color;
            }


            @Override
            protected void action(Hex parentHex, Hex hex) {
                if (parentHex != null) {
                    hex.counter = parentHex.counter + 1;
                } else {
                    hex.counter = 0;
                }
                if (isDefensiveStructure(hex.piece)) {
                    defensiveStructures.add(hex);
                }
            }
        };
    }


    public void move() {
        moveIndicators();
        repeatRemoveDeadIndicators.move();
    }


    private void removeDeadIndicators() {
        for (int i = indicators.size() - 1; i >= 0; i--) {
            DefenseIndicator defenseIndicator = indicators.get(i);
            if (defenseIndicator.alive) continue;
            indicators.remove(defenseIndicator);
        }
    }


    private void moveIndicators() {
        for (DefenseIndicator indicator : indicators) {
            indicator.move();
        }
    }


    public void onHexClicked(Hex clickedHex) {
        if (!clickedHex.hasTower()) return;
        if (viewableModel.provinceSelectionManager.selectedProvince != clickedHex.getProvince()) return;
        poolIndicators.clearExternalList();
        updateDefensiveStructures(clickedHex);
        resetFlags();
        for (int value = 3; value >= 1; value--) {
            doSpawnIndicators(value);
        }
    }


    private void doSpawnIndicators(int filterDefenseValue) {
        for (Hex hex : defensiveStructures) {
            int defenseValue = viewableModel.ruleset.getDefenseValue(hex.piece);
            if (defenseValue != filterDefenseValue) continue;
            for (Hex adjacentHex : hex.adjacentHexes) {
                if (adjacentHex.color != hex.color) continue;
                if (adjacentHex.flag) continue;
                if (isDefensiveStructure(adjacentHex.piece)) continue;
                spawnIndicator(hex, adjacentHex, defenseValue);
                adjacentHex.flag = true;
            }
        }
    }


    private void spawnIndicator(Hex parentHex, Hex targetHex, int defenseValue) {
        poolIndicators.getFreshObject().spawn(
                parentHex,
                targetHex,
                defenseValue,
                20 * parentHex.counter
        );
    }


    private void updateDefensiveStructures(Hex clickedHex) {
        defensiveStructures.clear();
        resetFlags();
        cmWaveWorker.apply(clickedHex);
    }


    private void resetFlags() {
        for (Hex hex : viewableModel.hexes) {
            hex.flag = false;
        }
    }


    private boolean isDefensiveStructure(PieceType pieceType) {
        if (pieceType == null) return false;
        switch (pieceType) {
            default:
                return false;
            case city:
            case tower:
            case strong_tower:
                return true;
        }
    }
}
