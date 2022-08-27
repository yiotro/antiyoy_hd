package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.TreeManager;
import yio.tro.onliyoy.game.core_model.events.EventPieceAdd;
import yio.tro.onliyoy.game.core_model.events.EventPieceDelete;

public abstract class AbstractLgPiecesSpawner {

    AbstractLevelGenerator abstractLevelGenerator;
    protected TreeManager treeManager;


    public AbstractLgPiecesSpawner(AbstractLevelGenerator abstractLevelGenerator) {
        this.abstractLevelGenerator = abstractLevelGenerator;
        treeManager = new TreeManager(getCoreModel());
    }


    abstract void spawnNeutralCities();


    abstract void spawnTrees();


    abstract void spawnGraves();


    abstract void spawnTowers();


    protected CoreModel getCoreModel() {
        return abstractLevelGenerator.coreModel;
    }


    protected void spawnGrave(Hex hex) {
        CoreModel coreModel = getCoreModel();
        EventPieceDelete deletePieceEvent = coreModel.eventsRefrigerator.getDeletePieceEvent(hex);
        coreModel.eventsManager.applyEvent(deletePieceEvent);
        EventPieceAdd addPieceEvent = coreModel.eventsRefrigerator.getAddPieceEvent(hex, PieceType.grave);
        coreModel.eventsManager.applyEvent(addPieceEvent);
    }
}
