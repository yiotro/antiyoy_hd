package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.game.core_model.CmSearchWorker;
import yio.tro.onliyoy.game.core_model.EconomicsManager;
import yio.tro.onliyoy.game.core_model.Hex;

public class LgClassicPieceSpawner extends AbstractLgPiecesSpawner{

    public LgClassicPieceSpawner(AbstractLevelGenerator abstractLevelGenerator) {
        super(abstractLevelGenerator);
    }


    @Override
    void spawnNeutralCities() {

    }


    @Override
    void spawnTrees() {
        double treesDensity = abstractLevelGenerator.parameters.treesDensity;
        int quantity = (int) (treesDensity * getCoreModel().hexes.size());
        for (int i = 0; i < quantity; i++) {
            Hex hex = getHexForNewTree();
            if (hex == null) continue;
            treeManager.doSpawnTree(hex);
        }
    }


    Hex getHexForNewTree() {
        CmSearchWorker searchWorker = getCoreModel().searchWorker;
        if (!isThereAtLeastOneLonelyEmptyHex()) {
            return searchWorker.getRandomEmptyHex();
        }
        while (true) {
            Hex hex = searchWorker.getRandomEmptyHex();
            if (hex.hasPiece()) continue;
            if (hex.isAdjacentToHexesOfSameColor()) continue;
            return hex;
        }
    }


    boolean isThereAtLeastOneLonelyEmptyHex() {
        for (Hex hex : getCoreModel().hexes) {
            if (hex.hasPiece()) continue;
            if (hex.isAdjacentToHexesOfSameColor()) continue;
            return true;
        }
        return false;
    }


    @Override
    void spawnGraves() {
        for (Hex hex : getCoreModel().hexes) {
            if (!hex.hasTree()) continue;
            if (abstractLevelGenerator.random.nextFloat() > 0.1) continue;
            spawnGrave(hex);
        }
    }


    @Override
    void spawnTowers() {

    }
}
