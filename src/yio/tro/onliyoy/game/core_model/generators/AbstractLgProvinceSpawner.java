package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.game.core_model.CoreModel;

public abstract class AbstractLgProvinceSpawner {

    AbstractLevelGenerator abstractLevelGenerator;


    public AbstractLgProvinceSpawner(AbstractLevelGenerator abstractLevelGenerator) {
        this.abstractLevelGenerator = abstractLevelGenerator;
    }


    abstract void apply();


    protected CoreModel getCoreModel() {
        return abstractLevelGenerator.coreModel;
    }
}
