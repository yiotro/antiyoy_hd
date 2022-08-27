package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.game.core_model.CoreModel;

public class GeneratorClassic extends AbstractLevelGenerator{

    public GeneratorClassic(CoreModel coreModel) {
        super(coreModel);
    }


    @Override
    void createManagers() {
        provinceSpawner = new LgClassicProvinceSpawner(this);
        provinceBalancer = new LgClassicProvinceBalancer(this);
        provinceAnalyzer = null; // not used here
        piecesSpawner = new LgClassicPieceSpawner(this);
    }


    @Override
    public void onProcessStarted() {

    }

}
