package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class RefData implements ReusableYio {

    Hex[] hexes;
    ArrayList<Province> provinces;
    ObjectPoolYio<Province> poolProvinces;
    PlayerEntity[] entities;
    int turn;


    public RefData() {
        hexes = null;
        provinces = new ArrayList<>();
        entities = null;
        turn = 0;
        initPools();
    }


    private void initPools() {
        poolProvinces = new ObjectPoolYio<Province>(provinces) {
            @Override
            public Province makeNewObject() {
                return new Province();
            }
        };
    }


    @Override
    public void reset() {
        poolProvinces.clearExternalList();
    }


    void applyTo(CoreModel coreModel) {
        applyHexes(coreModel);
        applyProvinces(coreModel);
        applyEntities(coreModel);
        coreModel.turnsManager.turnIndex = turn;
    }


    private void applyEntities(CoreModel coreModel) {
        for (int i = 0; i < coreModel.entitiesManager.entities.length; i++) {
            PlayerEntity targetEntity = coreModel.entitiesManager.entities[i];
            targetEntity.setName(entities[i].name);
            targetEntity.color = entities[i].color;
            targetEntity.type = entities[i].type;
        }
    }


    private void applyProvinces(CoreModel coreModel) {
        coreModel.provincesManager.clearProvinces();
        for (Province refProvince : provinces) {
            Province targetProvince = coreModel.provincesManager.addProvince();
            targetProvince.setId(refProvince.getId());
            targetProvince.setMoney(refProvince.getMoney());
            targetProvince.setCityName(refProvince.getCityName());
            targetProvince.getHexes().clear();
            targetProvince.getHexes().addAll(refProvince.getHexes());
        }
    }


    private void applyHexes(CoreModel coreModel) {
        for (int i = 0; i < coreModel.hexes.size(); i++) {
            Hex targetHex = coreModel.hexes.get(i);
            targetHex.setColor(hexes[i].color);
            targetHex.setPiece(hexes[i].piece);
            targetHex.setUnitId(hexes[i].unitId);
        }
    }


    void setBy(CoreModel coreModel) {
        checkToInit(coreModel);
        setHexes(coreModel);
        setProvinces(coreModel);
        setEntities(coreModel);
        turn = coreModel.turnsManager.turnIndex;
    }


    private void setEntities(CoreModel coreModel) {
        for (int i = 0; i < coreModel.entitiesManager.entities.length; i++) {
            PlayerEntity srcEntity = coreModel.entitiesManager.entities[i];
            entities[i].setName(srcEntity.name);
            entities[i].color = srcEntity.color;
            entities[i].type = srcEntity.type;
        }
    }


    private void setProvinces(CoreModel coreModel) {
        poolProvinces.clearExternalList();
        for (int i = 0; i < coreModel.provincesManager.provinces.size(); i++) {
            Province srcProvince = coreModel.provincesManager.provinces.get(i);
            Province freshObject = poolProvinces.getFreshObject();
            freshObject.setValid(true);
            freshObject.setId(srcProvince.getId());
            freshObject.setMoney(srcProvince.getMoney());
            freshObject.setCityName(srcProvince.getCityName());
            freshObject.getHexes().clear();
            freshObject.getHexes().addAll(srcProvince.getHexes());
        }
    }


    private void setHexes(CoreModel coreModel) {
        for (int i = 0; i < coreModel.hexes.size(); i++) {
            Hex srcHex = coreModel.hexes.get(i);
            hexes[i].setColor(srcHex.color);
            hexes[i].setPiece(srcHex.piece);
            hexes[i].setUnitId(srcHex.unitId);
        }
    }


    private void checkToInit(CoreModel coreModel) {
        if (hexes != null) return;

        int hSize = coreModel.hexes.size();
        int eLength = coreModel.entitiesManager.entities.length;

        hexes = new Hex[hSize];
        for (int i = 0; i < hexes.length; i++) {
            hexes[i] = new Hex(null);
        }

        entities = new PlayerEntity[eLength];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = new PlayerEntity(null, null, null);
        }
    }
}
