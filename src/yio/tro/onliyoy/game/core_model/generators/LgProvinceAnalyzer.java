package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.core_provinces.ProvincesBuilder;
import yio.tro.onliyoy.game.export_import.ExportManager;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.general.LevelSize;

import java.util.ArrayList;

public class LgProvinceAnalyzer {

    AbstractLevelGenerator abstractLevelGenerator;
    CoreModel copyModel;
    private ArrayList<LgaItem> items;
    ArrayList<Province> tempEnemiesList;
    private ArrayList<Hex> perimeter;


    public LgProvinceAnalyzer(AbstractLevelGenerator abstractLevelGenerator) {
        this.abstractLevelGenerator = abstractLevelGenerator;
        copyModel = new CoreModel("lg_copy");
        tempEnemiesList = new ArrayList<>();
        perimeter = new ArrayList<>();
    }


    ArrayList<LgaItem> perform() {
        updateCopy();
        inflateCopy();
        detectProvincesInCopy();
        initItems();
        calculateExpansionPotentials();
        calculateNearbyEnemies();
        calculateThreats();
        calculateRelativeEnemyValues();
        calculateRelativeSafetyValues();
        calculateAttractivenessValues();
        decreaseAttractivenessForFirstEntities();
        return items;
    }


    private void decreaseAttractivenessForFirstEntities() {
        // this method is fully based on results of 'test simulate matches'
        PlayerEntity[] entities = getCoreModel().entitiesManager.entities;
        if (entities.length < 5) return;
        double advantage = 1.3 + 0.1 * (entities.length - 5);
        LgaItem item0 = getItem(entities[0].color);
        if (item0 != null) {
            item0.attractiveness *= advantage;
        }
        if (getCoreModel().hexes.size() > 75) {
            LgaItem item1 = getItem(entities[1].color);
            if (item1 != null) {
                item1.attractiveness *= advantage;
            }
        }
    }


    private LgaItem getItem(HColor color) {
        for (LgaItem lgaItem : items) {
            if (lgaItem.province.getColor() != color) continue;
            return lgaItem;
        }
        return null;
    }


    private void calculateAttractivenessValues() {
        for (LgaItem lgaItem : items) {
            lgaItem.attractiveness = 0;
            lgaItem.attractiveness += 0.25 * lgaItem.expansionPotential;
            lgaItem.attractiveness += 2 * lgaItem.relativeEnemiesValue;
            lgaItem.attractiveness += lgaItem.relativeSafetyValue;
            lgaItem.attractiveness /= 3.25;
        }
    }


    private void calculateRelativeSafetyValues() {
        double minimumThreat = getMinimumThreat();
        double maximumThreat = getMaximumThreat();
        double delta = maximumThreat - minimumThreat;
        if (delta == 0) {
            for (LgaItem lgaItem : items) {
                lgaItem.relativeSafetyValue = 1;
            }
            return;
        }
        for (LgaItem lgaItem : items) {
            double value = lgaItem.threat - minimumThreat;
            lgaItem.relativeSafetyValue = 1 - value / delta;
        }
    }


    private double getMaximumThreat() {
        double maxValue = -1;
        for (LgaItem lgaItem : items) {
            if (maxValue == -1 || lgaItem.threat > maxValue) {
                maxValue = lgaItem.threat;
            }
        }
        return maxValue;
    }


    private double getMinimumThreat() {
        double minValue = -1;
        for (LgaItem lgaItem : items) {
            if (minValue == -1 || lgaItem.threat < minValue) {
                minValue = lgaItem.threat;
            }
        }
        return minValue;
    }


    private void calculateRelativeEnemyValues() {
        int minNumber = getMinimumNearbyEnemiesNumber();
        int maxNumber = getMaximumNearbyEnemiesNumber();
        int delta = maxNumber - minNumber;
        if (delta == 0) {
            for (LgaItem lgaItem : items) {
                lgaItem.relativeEnemiesValue = 1;
            }
            return;
        }
        for (LgaItem lgaItem : items) {
            int value = lgaItem.nearbyEnemies - minNumber;
            lgaItem.relativeEnemiesValue = 1d - (double) value / delta;
        }
    }


    private int getMaximumNearbyEnemiesNumber() {
        int maxValue = -1;
        for (LgaItem lgaItem : items) {
            if (maxValue == -1 || lgaItem.nearbyEnemies > maxValue) {
                maxValue = lgaItem.nearbyEnemies;
            }
        }
        return maxValue;
    }


    private int getMinimumNearbyEnemiesNumber() {
        int minValue = -1;
        for (LgaItem lgaItem : items) {
            if (minValue == -1 || lgaItem.nearbyEnemies < minValue) {
                minValue = lgaItem.nearbyEnemies;
            }
        }
        return minValue;
    }


    private void calculateThreats() {
        for (LgaItem lgaItem : items) {
            Province originalProvince = lgaItem.province;
            Province copyProvince = getCopyProvince(originalProvince);
            updatePerimeter(copyProvince);
            int hexesNearEnemies = countHexesNearEnemies(copyProvince);
            lgaItem.threat = (double) hexesNearEnemies / (double) perimeter.size();
        }
    }


    private int countHexesNearEnemies(Province province) {
        int c = 0;
        for (Hex hex : province.getHexes()) {
            if (!isNearEnemy(hex)) continue;
            c++;
        }
        return c;
    }


    private boolean isNearEnemy(Hex hex) {
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.isNeutral()) continue;
            if (adjacentHex.color == hex.color) continue;
            return true;
        }
        return false;
    }


    private void updatePerimeter(Province province) {
        perimeter.clear();
        for (Hex hex : province.getHexes()) {
            if (!shouldBeInPerimeter(hex)) continue;
            perimeter.add(hex);
        }
    }


    private boolean shouldBeInPerimeter(Hex hex) {
        int c = 0;
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color != hex.color) continue;
            c++;
        }
        return c < 6;
    }


    private void calculateNearbyEnemies() {
        for (LgaItem lgaItem : items) {
            Province originalProvince = lgaItem.province;
            Province copyProvince = getCopyProvince(originalProvince);
            updateTempEnemiesList(copyProvince);
            lgaItem.nearbyEnemies = tempEnemiesList.size();
        }
    }


    private void updateTempEnemiesList(Province province) {
        tempEnemiesList.clear();
        for (Hex hex : province.getHexes()) {
            for (Hex adjacentHex : hex.adjacentHexes) {
                if (adjacentHex.color == hex.color) continue;
                if (adjacentHex.isNeutral()) continue;
                Province enemyProvince = adjacentHex.getProvince();
                if (tempEnemiesList.contains(enemyProvince)) continue;
                tempEnemiesList.add(enemyProvince);
            }
        }
    }


    private void calculateExpansionPotentials() {
        double maxSize = getMaxCopyProvincesSize();
        for (LgaItem lgaItem : items) {
            Province originalProvince = lgaItem.province;
            Province copyProvince = getCopyProvince(originalProvince);
            int size = copyProvince.getHexes().size();
            lgaItem.expansionPotential = (double) size / maxSize;
        }
    }


    private int getMaxCopyProvincesSize() {
        int maxSize = 0;
        for (Province province : copyModel.provincesManager.provinces) {
            if (province.getHexes().size() <= maxSize) continue;
            maxSize = province.getHexes().size();
        }
        return maxSize;
    }


    private void initItems() {
        items = new ArrayList<>();
        for (Province copyProvince : copyModel.provincesManager.provinces) {
            LgaItem lgaItem = new LgaItem();
            lgaItem.province = getOriginalProvince(copyProvince);
            if (lgaItem.province == null) {
                ExportManager exportManager = new ExportManager();
                ExportParameters exportParameters = new ExportParameters();
                exportParameters.setCoreModel(getCoreModel());
                exportParameters.setInitialLevelSize(LevelSize.tiny);
                exportParameters.setAiVersionCode(-1);
                System.out.println("LgProvinceAnalyzer.initItems, core model: " + exportManager.perform(exportParameters));
                exportParameters.setCoreModel(copyModel);
                System.out.println("LgProvinceAnalyzer.initItems, copy model: " + exportManager.perform(exportParameters));
            }
            items.add(lgaItem);
        }
    }


    private Province getCopyProvince(Province originalProvince) {
        Hex firstHex = originalProvince.getFirstHex();
        Hex copyHex = copyModel.getHexWithSameCoordinates(firstHex);
        return copyHex.getProvince();
    }


    private Province getOriginalProvince(Province copyProvince) {
        Hex cityHex = findCity(copyProvince);
        if (cityHex == null) {
            System.out.println("LgProvinceAnalyzer.getOriginalProvince: no city");
            return null;
        }
        Hex originalHex = getCoreModel().getHexWithSameCoordinates(cityHex);
        return originalHex.getProvince();
    }


    private Hex findCity(Province province) {
        for (Hex hex : province.getHexes()) {
            if (hex.piece == PieceType.city) return hex;
        }
        return null;
    }


    private void detectProvincesInCopy() {
        ProvincesBuilder builder = copyModel.provincesManager.builder;
        builder.doGrantPermission();
        builder.apply();
    }


    private void updateCopy() {
        copyModel.buildSimilarGraph(getCoreModel());
        copyModel.setBy(getCoreModel());
    }


    private void inflateCopy() {
        prepareCounters();
        boolean expanded;
        int step = 0;
        while (true) {
            expanded = false;
            for (Hex hex : copyModel.hexes) {
                if (hex.counter != step) continue;
                for (Hex adjacentHex : hex.adjacentHexes) {
                    if (adjacentHex.counter != -1) continue;
                    adjacentHex.counter = step + 1;
                    adjacentHex.setColor(hex.color);
                    expanded = true;
                }
            }
            if (!expanded) break;
            step++;
        }
    }


    private void prepareCounters() {
        for (Hex hex : copyModel.hexes) {
            if (hex.isNeutral() || !hex.isAdjacentToHexesOfSameColor()) {
                hex.counter = -1;
                continue;
            }
            hex.counter = 0;
        }
    }


    private CoreModel getCoreModel() {
        return abstractLevelGenerator.coreModel;
    }
}
