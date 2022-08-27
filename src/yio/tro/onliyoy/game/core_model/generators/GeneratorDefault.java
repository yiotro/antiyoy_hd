package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.game.core_model.CoreModel;

public class GeneratorDefault extends AbstractLevelGenerator{

    public GeneratorDefault(CoreModel coreModel) {
        super(coreModel);
    }


    @Override
    void createManagers() {
        provinceSpawner = new LgDefaultProvinceSpawner(this);
        provinceBalancer = new LgDefaultProvinceBalancer(this);
        provinceAnalyzer = new LgProvinceAnalyzer(this);
        piecesSpawner = new LgDefaultPiecesSpawner(this);
    }


    @Override
    public void onProcessStarted() {

    }

}
