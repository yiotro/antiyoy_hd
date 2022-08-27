package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class GhDataContainer {

    public ArrayList<GeometricalHexData> list;
    ObjectPoolYio<GeometricalHexData> pool;


    public GhDataContainer() {
        list = new ArrayList<>();
        initPools();
    }


    private void initPools() {
        pool = new ObjectPoolYio<GeometricalHexData>(list) {
            @Override
            public GeometricalHexData makeNewObject() {
                return new GeometricalHexData();
            }
        };
    }


    public void update(CoreModel coreModel, ArrayList<Hex> hexes) {
        if (hexes.size() == 0) return;
        pool.clearExternalList();
        float dx = coreModel.getHexRadius();
        float dy = (float) (coreModel.getHexRadius() * Math.cos(Math.PI / 6));
        for (Hex hex : hexes) {
            GeometricalHexData freshObject = pool.getFreshObject();
            freshObject.setHex(hex);
            freshObject.updateMetrics(dx, dy);
        }
    }


    public void updateByFowItems(CoreModel coreModel, ArrayList<FowItem> fowItems) {
        if (fowItems.size() == 0) return;
        pool.clearExternalList();
        float dx = coreModel.getHexRadius();
        float dy = (float) (coreModel.getHexRadius() * Math.cos(Math.PI / 6));
        for (FowItem item : fowItems) {
            GeometricalHexData freshObject = pool.getFreshObject();
            freshObject.setPosDirectly(item.pos);
            freshObject.updateMetrics(dx, dy);
        }
    }
}
