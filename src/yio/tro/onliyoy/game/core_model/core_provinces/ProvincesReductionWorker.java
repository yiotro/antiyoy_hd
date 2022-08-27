package yio.tro.onliyoy.game.core_model.core_provinces;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.CmWaveWorker;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class ProvincesReductionWorker {

    ProvincesManager provincesManager;
    ArrayList<PrwCluster> clusters;
    ObjectPoolYio<PrwCluster> poolClusters;
    Province modifiedProvince;
    Hex modifiedHex;
    HColor previousColor;
    PrwCluster currentCluster;
    CmWaveWorker waveWorker;
    PrwCluster successorCluster;


    public ProvincesReductionWorker(ProvincesManager provincesManager) {
        this.provincesManager = provincesManager;
        clusters = new ArrayList<>();
        currentCluster = null;
        initPools();
        initWaveWorker();
    }


    private void initWaveWorker() {
        waveWorker = new CmWaveWorker() {
            @Override
            public boolean condition(Hex parentHex, Hex hex) {
                return hex.color == previousColor;
            }


            @Override
            public void action(Hex parentHex, Hex hex) {
                currentCluster.hexes.add(hex);
            }
        };
    }


    private void initPools() {
        poolClusters = new ObjectPoolYio<PrwCluster>(clusters) {
            @Override
            public PrwCluster makeNewObject() {
                return new PrwCluster();
            }
        };
    }


    void onHexColorChanged(Hex hex, HColor previousColor) {
        if (previousColor == HColor.gray) return;
        this.modifiedHex = hex;
        this.previousColor = previousColor;
        modifiedProvince = hex.getProvince();
        resetFlags();
        updateClusters();
        handleSimpleSituations();
    }


    private void handleSimpleSituations() {
        if (clusters.size() == 0) {
            if (modifiedProvince != null) {
                provincesManager.removeProvince(modifiedProvince);
            }
            return;
        }
        if (clusters.size() == 1) {
            removeSingleHex();
            return;
        }
        handleSplitSituation();
    }


    private void handleSplitSituation() {
        updateSuccessorCluster();
        modifyProvinceToMatchSuccessorCluster();
        makeProvincesForRestOfClusters();
    }


    private void makeProvincesForRestOfClusters() {
        for (PrwCluster prwCluster : clusters) {
            if (prwCluster == successorCluster) continue;
            if (prwCluster.hexes.size() < 2) continue;
            Province province = provincesManager.addProvince();
            for (Hex hex : prwCluster.hexes) {
                province.addHex(hex);
            }
        }
    }


    private void modifyProvinceToMatchSuccessorCluster() {
        for (Hex hex : modifiedProvince.getHexes()) {
            hex.flag = false;
        }
        for (Hex hex : successorCluster.hexes) {
            hex.flag = true;
        }
        for (int i = modifiedProvince.getHexes().size() - 1; i >= 0; i--) {
            Hex hex = modifiedProvince.getHexes().get(i);
            if (hex.flag) continue;
            modifiedProvince.removeHex(hex);
        }
        if (modifiedProvince.getHexes().size() < 2) {
            provincesManager.removeProvince(modifiedProvince);
        }
    }


    private void updateSuccessorCluster() {
        successorCluster = getClusterWithCity();
        if (successorCluster != null) return;
        successorCluster = getClusterWithMostFarms();
        if (successorCluster != null) return;
        successorCluster = getBiggestCluster();
    }


    PrwCluster getBiggestCluster() {
        PrwCluster biggestCluster = null;
        for (PrwCluster prwCluster : clusters) {
            if (biggestCluster == null || prwCluster.hexes.size() > biggestCluster.hexes.size()) {
                biggestCluster = prwCluster;
            }
        }
        return biggestCluster;
    }


    PrwCluster getClusterWithMostFarms() {
        PrwCluster bestCluster = null;
        int maxFarms = -1;
        for (PrwCluster prwCluster : clusters) {
            int farms = prwCluster.countFarms();
            if (bestCluster == null || farms > maxFarms) {
                bestCluster = prwCluster;
                maxFarms = farms;
            }
        }
        return bestCluster;
    }


    PrwCluster getClusterWithCity() {
        for (PrwCluster prwCluster : clusters) {
            if (prwCluster.hasCity()) return prwCluster;
        }
        return null;
    }


    private void removeSingleHex() {
        modifiedProvince.removeHex(modifiedHex);
        if (modifiedProvince.getHexes().size() < 2) {
            provincesManager.removeProvince(modifiedProvince);
        }
    }


    private void updateClusters() {
        poolClusters.clearExternalList();
        for (Hex adjacentHex : modifiedHex.adjacentHexes) {
            if (adjacentHex.flag) continue;
            if (adjacentHex.color != previousColor) continue;
            addCluster(adjacentHex);
        }
    }


    void addCluster(Hex hex) {
        currentCluster = poolClusters.getFreshObject();
        waveWorker.apply(hex);
    }


    private void resetFlags() {
        for (Hex hex : provincesManager.coreModel.hexes) {
            hex.flag = false;
        }
    }
}
