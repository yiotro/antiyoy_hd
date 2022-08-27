package yio.tro.onliyoy.game.core_model.core_provinces;

import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.TimeMeasureYio;
import yio.tro.onliyoy.stuff.name_generator.NameGenerator;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;
import java.util.HashMap;

public class ProvincesManager implements IEventListener, Encodeable {

    CoreModel coreModel;
    public ArrayList<Province> provinces;
    private ObjectPoolYio<Province> poolProvinces;
    public ProvincesBuilder builder;
    private EventUnitMove tempUnitMoveEvent;
    HColor previousColor;
    public ProvincesEnlargementWorker enlargementWorker;
    public ProvincesReductionWorker reductionWorker;
    public int currentId;
    private StringBuilder stringBuilder;
    private NameGenerator nameGenerator;


    public ProvincesManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        coreModel.eventsManager.addListener(this);
        provinces = new ArrayList<>();
        builder = new ProvincesBuilder(this);
        enlargementWorker = new ProvincesEnlargementWorker(this);
        reductionWorker = new ProvincesReductionWorker(this);
        stringBuilder = new StringBuilder();
        currentId = 0;
        initNameGenerator();
        initPools();
    }


    private void initNameGenerator() {
        nameGenerator = new NameGenerator();
        nameGenerator.setCapitalize(true);
        nameGenerator.setMasks(getMasks());
        nameGenerator.setGroups(getGroups());
    }


    private HashMap<String, String> getGroups() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("k", "r t p s d k b n m");
        hashMap.put("m", "rb kr tr br bn cl rt t p s d k b nm mn sh ch gh j");
        hashMap.put("a", "o a ay e oy o e a ye ee i");
        hashMap.put("o", "vo va msk sk nsk -city rg ro");
        return hashMap;
    }


    private ArrayList<String> getMasks() {
        ArrayList<String> list = new ArrayList<>();
        list.add("kama");
        list.add("kakao");
        list.add("kamao");
        list.add("kakkao");
        list.add("kakka");
        list.add("amao");
        list.add("amaka");
        list.add("kamak");
        list.add("kakaka");
        list.add("kaakao");
        list.add("akaka");
        list.add("amakao");
        return list;
    }


    private void initPools() {
        poolProvinces = new ObjectPoolYio<Province>(provinces) {
            @Override
            public Province makeNewObject() {
                return new Province();
            }
        };
        poolProvinces.setHoldSize(25);
    }


    public void setBy(ProvincesManager srcProvinceManager) {
        clearProvinces();
        for (Province srcProvince : srcProvinceManager.provinces) {
            Province province = addProvince();
            province.setCityName(srcProvince.getCityName());
            province.setMoney(srcProvince.getMoney());
            province.setId(srcProvince.getId());
            for (Hex srcHex : srcProvince.getHexes()) {
                Hex hex = coreModel.getHexWithSameCoordinates(srcHex);
                province.addHex(hex);
            }
        }
        setCurrentId(srcProvinceManager.currentId);
    }


    public Province addProvince() {
        Province freshObject = poolProvinces.getFreshObject();
        freshObject.setId(currentId);
        currentId++;
        freshObject.setCityName(nameGenerator.generate());
        return freshObject;
    }


    public void clearProvinces() {
        while (provinces.size() > 0) {
            removeProvince(provinces.get(0));
        }
    }


    public Province getProvince(int id) {
        for (Province province : provinces) {
            if (province.getId() == id) return province;
        }
        return null;
    }


    public Province getProvince(HColor color) {
        for (Province province : provinces) {
            if (province.getColor() == color) return province;
        }
        return null;
    }


    public Province getLargestProvince(HColor color) {
        Province bestProvince = null;
        for (Province province : provinces) {
            if (province.getColor() != color) continue;
            if (bestProvince == null || province.getHexes().size() > bestProvince.getHexes().size()) {
                bestProvince = province;
            }
        }
        return bestProvince;
    }


    public Province getRichestProvince(HColor color) {
        Province bestProvince = null;
        for (Province province : provinces) {
            if (province.getColor() != color) continue;
            if (bestProvince == null || province.getMoney() > bestProvince.getMoney()) {
                bestProvince = province;
            }
        }
        return bestProvince;
    }


    public int getSumMoney(HColor color) {
        int sum = 0;
        for (Province province : provinces) {
            if (province.getColor() != color) continue;
            sum += province.getMoney();
        }
        return sum;
    }


    public Province findProvinceSlowly(Hex hex) {
        for (Province province : provinces) {
            if (province.contains(hex)) return province;
        }
        return null;
    }


    public void removeProvince(Province province) {
        province.setValid(false);
        poolProvinces.removeFromExternalList(province);
    }


    public void onQuickEventApplied() {
        builder.doGrantPermission();
        builder.apply();
    }


    @Override
    public void onEventValidated(AbstractEvent event) {
        tempUnitMoveEvent = null;
        switch (event.getType()) {
            default:
                break;
            case hex_change_color:
                EventHexChangeColor eventHexChangeColor = (EventHexChangeColor) event;
                previousColor = eventHexChangeColor.hex.color;
                break;
            case unit_move:
                EventUnitMove eventUnitMove = (EventUnitMove) event;
                if (eventUnitMove.areColorTransferConditionsSatisfied()) {
                    tempUnitMoveEvent = eventUnitMove;
                    previousColor = eventUnitMove.finish.color;
                }
                break;
            case piece_build:
                EventPieceBuild eventPieceBuild = (EventPieceBuild) event;
                previousColor = eventPieceBuild.hex.color;
                break;
        }
    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        if (event.isQuick()) return; // update happens immediately after all quick events were applied
        switch (event.getType()) {
            default:
                break;
            case hex_change_color:
                EventHexChangeColor eventHexChangeColor = (EventHexChangeColor) event;
                onHexColorChanged(eventHexChangeColor.hex);
                break;
            case unit_move:
                if (tempUnitMoveEvent == event) {
                    onHexColorChanged(tempUnitMoveEvent.finish);
                }
                break;
            case piece_build:
                EventPieceBuild eventPieceBuild = (EventPieceBuild) event;
                if (!Core.isUnit(eventPieceBuild.pieceType)) break;
                onHexColorChanged(eventPieceBuild.hex);
                break;
            case graph_created:
                builder.doGrantPermission();
                builder.apply();
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 8;
    }


    private void onHexColorChanged(Hex hex) {
        if (provinces.size() == 0) return;
        if (previousColor == hex.color) return;
        reductionWorker.onHexColorChanged(hex, previousColor);
        enlargementWorker.onHexColorChanged(hex);
        coreModel.onHexColorChanged(hex, previousColor);
    }


    @Override
    public String encode() {
        stringBuilder.setLength(0);
        for (Province province : provinces) {
            stringBuilder.append(province.encode()).append(",");
        }
        return currentId + ">" + stringBuilder.toString();
    }


    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }


    public void showProvincesInConsole() {
        System.out.println();
        System.out.println("ProvincesManager.showProvincesInConsole");
        for (Province province : provinces) {
            System.out.println("- " + province);
        }
    }
}
