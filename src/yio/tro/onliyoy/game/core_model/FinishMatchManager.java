package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class FinishMatchManager implements ReusableYio {

    CoreModel coreModel;
    private ArrayList<Province> provinces;


    public FinishMatchManager() {
        reset();
    }


    @Override
    public void reset() {
        coreModel = null;
        provinces = null;
    }


    public MatchResults getMatchResults() {
        PlayerEntity winner = getWinner();
        if (winner == null) return null;
        MatchResults matchResults = new MatchResults();
        matchResults.winnerColor = winner.color;
        matchResults.entityType = winner.type;
        return matchResults;
    }


    public PlayerEntity getWinner() {
        provinces = coreModel.provincesManager.provinces;
        if (provinces.size() == 0) return null;
        if (!doesContainOnlyProvincesOfOneColor()) return null;
        HColor firstProvinceColor = provinces.get(0).getColor();
        return coreModel.entitiesManager.getEntity(firstProvinceColor);
    }


    private boolean doesContainOnlyProvincesOfOneColor() {
        HColor firstProvinceColor = provinces.get(0).getColor();
        for (Province province : provinces) {
            if (province.getColor() != firstProvinceColor) return false;
        }
        return true;
    }


    public void setCoreModel(CoreModel coreModel) {
        this.coreModel = coreModel;
    }
}
