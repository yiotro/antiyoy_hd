package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.events.EventUnitMove;
import yio.tro.onliyoy.game.core_model.events.EventsFactory;
import yio.tro.onliyoy.game.core_model.events.EventsManager;
import yio.tro.onliyoy.game.viewable_model.UndoManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;

import java.util.ArrayList;
import java.util.HashMap;

public class MassMarchManager {

    ViewableModel viewableModel;
    private Province province;
    CmWaveWorker waveWorker;
    HashMap<Hex, Integer> map;
    ArrayList<Hex> tempList;


    public MassMarchManager(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
        map = new HashMap<>();
        tempList = new ArrayList<>();
        initWaveWorker();
    }


    private void initWaveWorker() {
        waveWorker = new CmWaveWorker() {
            @Override
            protected boolean condition(Hex parentHex, Hex hex) {
                return parentHex.color == hex.color;
            }


            @Override
            protected void action(Hex parentHex, Hex hex) {
                if (parentHex == null) {
                    map.put(hex, 0);
                    return;
                }
                map.put(hex, map.get(parentHex) + 1);
            }
        };
    }


    public void apply(Hex targetHex) {
        province = targetHex.getProvince();
        prepareCounters(targetHex);
        updateTempList();
        doSendUnits();
    }


    private void updateTempList() {
        tempList.clear();
        for (Hex hex : province.getHexes()) {
            if (!hex.hasUnit()) continue;
            if (!viewableModel.readinessManager.isReady(hex)) continue;
            tempList.add(hex);
        }
    }


    private void prepareCounters(Hex targetHex) {
        resetCounters();
        waveWorker.apply(targetHex);
    }


    private void doSendUnits() {
        for (Hex hex : tempList) {
            viewableModel.moveZoneManager.updateForUnit(hex);
            Hex bestHexToGo = findBestHexToGo();
            if (bestHexToGo == null) continue;
            if (map.get(bestHexToGo) >= map.get(hex)) continue;
            EventsFactory factory = viewableModel.eventsManager.factory;
            EventUnitMove moveUnitEvent = factory.createMoveUnitEvent(hex, bestHexToGo);
            viewableModel.humanControlsManager.applyHumanEvent(moveUnitEvent);
        }
    }


    private Hex findBestHexToGo() {
        Hex bestHex = null;
        int minValue = -1;
        for (Hex hex : viewableModel.moveZoneManager.hexes) {
            if (hex.hasPiece() && !hex.hasTree()) continue;
            if (!map.containsKey(hex)) continue;
            int currentValue = map.get(hex);
            if (bestHex == null || currentValue < minValue) {
                bestHex = hex;
                minValue = currentValue;
            }
        }
        return bestHex;
    }


    private void resetCounters() {
        for (Hex hex : province.getHexes()) {
            hex.flag = false;
        }
    }
}
