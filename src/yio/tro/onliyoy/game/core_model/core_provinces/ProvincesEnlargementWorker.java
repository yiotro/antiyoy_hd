package yio.tro.onliyoy.game.core_model.core_provinces;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.events.EventPieceDelete;
import yio.tro.onliyoy.game.core_model.events.EventsRefrigerator;

import java.util.ArrayList;

public class ProvincesEnlargementWorker {

    ProvincesManager provincesManager;
    private ArrayList<Province> adjacentProvinces;
    private ArrayList<Hex> previouslyLonelyHexes;
    private ArrayList<Hex> tempList;
    Hex modifiedHex;
    Province targetProvince;


    public ProvincesEnlargementWorker(ProvincesManager provincesManager) {
        this.provincesManager = provincesManager;
        adjacentProvinces = new ArrayList<>();
        previouslyLonelyHexes = new ArrayList<>();
        tempList = new ArrayList<>();
    }


    public void onHexColorChanged(Hex hex) {
        if (hex.color == HColor.gray) return;
        if (!hex.isAdjacentToHexesOfSameColor()) return;
        modifiedHex = hex;
        updateLists();
        guaranteeSingleProvince();
        pourInPreviouslyLonelyHexes();
        checkToRemoveExcessiveCities();
    }


    private void checkToRemoveExcessiveCities() {
        if (targetProvince == null) return;
        int c = 0;
        while (c < 1000) {
            c++;
            updateTempListByCities(targetProvince);
            if (tempList.size() < 2) break;
            Hex hex = findCityWithLeastAmountOfAdjacentFarms(tempList);
            EventsRefrigerator eventsRefrigerator = provincesManager.coreModel.eventsRefrigerator;
            EventPieceDelete event = eventsRefrigerator.getDeletePieceEvent(hex);
            provincesManager.coreModel.eventsManager.applyEvent(event);
        }
    }


    private Hex findCityWithLeastAmountOfAdjacentFarms(ArrayList<Hex> list) {
        Hex bestHex = null;
        int minValue = -1;
        for (Hex hex : list) {
            int currentValue = getNumberOfAdjacentFriendlyFarms(hex);
            if (bestHex == null || currentValue < minValue) {
                bestHex = hex;
                minValue = currentValue;
            }
        }
        return bestHex;
    }


    private int getNumberOfAdjacentFriendlyFarms(Hex hex) {
        int c = 0;
        for (Hex adjacentHex : hex.adjacentHexes) {
            if (adjacentHex.color != hex.color) continue;
            if (adjacentHex.piece != PieceType.farm) continue;
            c++;
        }
        return c;
    }


    private void updateTempListByCities(Province province) {
        tempList.clear();
        for (Hex hex : province.getHexes()) {
            if (hex.piece != PieceType.city) continue;
            tempList.add(hex);
        }
    }


    private void pourInPreviouslyLonelyHexes() {
        for (Hex previouslyLonelyHex : previouslyLonelyHexes) {
            if (previouslyLonelyHex.getProvince() != null) continue;
            targetProvince.addHex(previouslyLonelyHex);
        }
    }


    private void guaranteeSingleProvince() {
        targetProvince = null;
        if (adjacentProvinces.size() == 0) {
            makeTargetProvinceFromScratch();
            return;
        }
        if (adjacentProvinces.size() == 1) {
            justSimplyAddModifiedHexToProvince();
            return;
        }
        mergeMultipleProvincesIntoLargest();
    }


    private void mergeMultipleProvincesIntoLargest() {
        targetProvince = findLargestProvince();
        targetProvince.addHex(modifiedHex);
        for (Province province : adjacentProvinces) {
            if (province == targetProvince) continue;
            for (Hex hex : province.getHexes()) {
                targetProvince.addHex(hex);
            }
            targetProvince.setMoney(targetProvince.getMoney() + province.getMoney());
            provincesManager.removeProvince(province);
        }
    }


    private Province findLargestProvince() {
        Province largestProvince = null;
        for (Province province : adjacentProvinces) {
            if (largestProvince == null || province.getHexes().size() > largestProvince.getHexes().size()) {
                largestProvince = province;
            }
        }
        return largestProvince;
    }


    private void justSimplyAddModifiedHexToProvince() {
        targetProvince = adjacentProvinces.get(0);
        targetProvince.addHex(modifiedHex);
    }


    private void makeTargetProvinceFromScratch() {
        targetProvince = provincesManager.addProvince();
        targetProvince.addHex(modifiedHex);
        for (Hex hex : previouslyLonelyHexes) {
            targetProvince.addHex(hex);
        }
    }


    private void updateLists() {
        adjacentProvinces.clear();
        previouslyLonelyHexes.clear();
        for (Hex adjacentHex : modifiedHex.adjacentHexes) {
            if (adjacentHex.color != modifiedHex.color) continue;
            Province province = adjacentHex.getProvince();
            if (province != null) {
                if (!adjacentProvinces.contains(province)) {
                    adjacentProvinces.add(province);
                }
            } else {
                previouslyLonelyHexes.add(adjacentHex);
            }
        }
    }
}
