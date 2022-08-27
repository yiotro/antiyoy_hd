package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.events.EventPieceAdd;
import yio.tro.onliyoy.game.core_model.events.EventsFactory;
import yio.tro.onliyoy.game.core_model.events.EventsManager;

import java.util.ArrayList;

public class LgDefaultPiecesSpawner extends AbstractLgPiecesSpawner{

    ArrayList<LgTreeCluster> treeClusters;
    ArrayList<Hex> propagationList;


    public LgDefaultPiecesSpawner(AbstractLevelGenerator abstractLevelGenerator) {
        super(abstractLevelGenerator);
        treeClusters = new ArrayList<>();
        propagationList = new ArrayList<>();
    }


    @Override
    void spawnNeutralCities() {
        if (abstractLevelGenerator.random.nextFloat() > 0.25) return;
        Hex hex = getHexForNeutralCity();
        if (hex == null) return;
        if (hex.isColored()) return;
        spawnNeutralCity(hex);
    }


    private void spawnNeutralCity(Hex cityHex) {
        ensurePiece(cityHex, PieceType.city);
        for (Hex adjacentHex : cityHex.adjacentHexes) {
            if (abstractLevelGenerator.random.nextFloat() > 0.4) continue;
            if (adjacentHex.isColored()) continue;
            ensurePiece(adjacentHex, PieceType.farm);
            if (adjacentHex.adjacentHexes.size() < 6) {
                spawnFarCityStuff(adjacentHex);
            }
        }
    }


    private void spawnFarCityStuff(Hex hex) {
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (abstractLevelGenerator.random.nextFloat() > 0.3) continue;
            if (adjacentHex.isColored()) continue;
            if (adjacentHex.hasPiece()) continue;
            if (adjacentHex.adjacentHexes.size() == 6) continue;
            ensurePiece(adjacentHex, getFarmOrTower());
        }
    }


    private PieceType getFarmOrTower() {
        switch (abstractLevelGenerator.random.nextInt(3)) {
            default:
            case 0:
            case 1:
                return PieceType.farm;
            case 2:
                return PieceType.tower;
        }
    }


    private Hex getHexForNeutralCity() {
        CoreModel copyModel = abstractLevelGenerator.provinceAnalyzer.copyModel;
        Hex bestCopyHex = null;
        for (Hex copyHex : copyModel.hexes) {
            if (copyHex.adjacentHexes.size() < 6) continue;
            if (!isHexNearEnemy(copyHex)) continue;
            if (bestCopyHex == null || copyHex.counter > bestCopyHex.counter) {
                bestCopyHex = copyHex;
            }
        }
        if (bestCopyHex == null) return null;
        return getCoreModel().getHexWithSameCoordinates(bestCopyHex);
    }


    private boolean isHexNearEnemy(Hex hex) {
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.isNeutral()) continue;
            if (adjacentHex.color == hex.color) continue;
            return true;
        }
        return false;
    }


    @Override
    void spawnTowers() {
        for (Hex hex : getCoreModel().hexes) {
            if (!isHexGoodForTower(hex)) continue;
            if (abstractLevelGenerator.random.nextFloat() > 0.8) continue;
            ensurePiece(hex, PieceType.tower);
        }
    }


    private void ensurePiece(Hex hex, PieceType pieceType) {
        EventsManager eventsManager = getCoreModel().eventsManager;
        EventsFactory factory = eventsManager.factory;
        if (hex.hasPiece()) {
            eventsManager.applyEvent(factory.createDeletePieceEvent(hex));
        }
        EventPieceAdd event = factory.createAddPieceEvent(hex, pieceType);
        eventsManager.applyEvent(event);
    }


    private boolean isHexGoodForTower(Hex hex) {
        if (hex.hasPiece()) return false;
        if (hex.adjacentHexes.size() > 1) return false;
        if (hex.isColored()) return false;
        return true;
    }


    @Override
    void spawnGraves() {
        updateTreeClusters();
        forgetAboutInvalidTreeClusters();
        turnTreeClustersIntoGraves();
    }


    private void turnTreeClustersIntoGraves() {
        for (LgTreeCluster lgTreeCluster : treeClusters) {
            if (abstractLevelGenerator.random.nextFloat() > 0.4) continue;
            for (Hex hex : lgTreeCluster.hexes) {
                spawnGrave(hex);
            }
        }
    }


    private void forgetAboutInvalidTreeClusters() {
        for (int i = treeClusters.size() - 1; i >= 0; i--) {
            LgTreeCluster lgTreeCluster = treeClusters.get(i);
            if (lgTreeCluster.hexes.size() == 1) continue;
            treeClusters.remove(lgTreeCluster);
        }
    }


    private void updateTreeClusters() {
        resetFlags();
        treeClusters.clear();
        for (Hex hex : getCoreModel().hexes) {
            if (!hex.hasTree()) continue;
            if (hex.flag) continue;
            addTreeCluster(hex);
        }
    }


    private void addTreeCluster(Hex startHex) {
        LgTreeCluster lgTreeCluster = new LgTreeCluster();
        addToPropagationList(startHex);
        while (propagationList.size() > 0) {
            Hex firstHex = propagationList.get(0);
            propagationList.remove(0);
            lgTreeCluster.hexes.add(firstHex);
            for (Hex adjacentHex : firstHex.adjacentHexes) {
                if (adjacentHex.flag) continue;
                if (!adjacentHex.hasTree()) continue;
                addToPropagationList(adjacentHex);
            }
        }
        treeClusters.add(lgTreeCluster);
    }


    private void resetFlags() {
        for (Hex hex : getCoreModel().hexes) {
            hex.flag = false;
        }
    }


    void addToPropagationList(Hex hex) {
        hex.flag = true;
        propagationList.add(hex);
    }


    @Override
    void spawnTrees() {
        double treesDensity = abstractLevelGenerator.parameters.treesDensity;
        int quantity = (int) (treesDensity * getCoreModel().hexes.size());
        CmSearchWorker searchWorker = getCoreModel().searchWorker;
        for (int i = 0; i < quantity; i++) {
            Hex hex = searchWorker.getRandomEmptyNeutralHex();
            if (hex == null) continue;
            treeManager.doSpawnTree(hex);
        }
    }

}
