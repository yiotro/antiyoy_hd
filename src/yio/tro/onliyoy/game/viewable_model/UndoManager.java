package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.DebugActionsController;
import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.DifferenceDetector;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.EventType;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.game.export_import.*;
import yio.tro.onliyoy.stuff.TimeMeasureYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class UndoManager implements IEventListener {

    CoreModel coreModel;
    public ArrayList<UndoItem> items;
    ObjectPoolYio<UndoItem> poolItems;
    protected ExportManager exportManager;
    DifferenceDetector differenceDetector;
    CoreModel tempModel;


    public UndoManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        coreModel.eventsManager.addListener(this);
        items = new ArrayList<>();
        exportManager = new ExportManager();
        differenceDetector = new DifferenceDetector();
        tempModel = new CoreModel("temp_undo");
        initPools();
    }


    private void initPools() {
        poolItems = new ObjectPoolYio<UndoItem>(items) {
            @Override
            public UndoItem makeNewObject() {
                return new UndoItem(UndoManager.this);
            }
        };
    }


    @Override
    public void onEventValidated(AbstractEvent event) {
        if (!event.isNotable()) return;
        if (!event.canBeUndone()) return;
        if (event.getType() == EventType.turn_end) return;
        UndoItem freshObject = poolItems.getFreshObject();
        freshObject.setValues(event, coreModel);
    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case turn_end:
                onTurnEndEventApplied();
                break;
            case graph_created:
                items.clear();
                poolItems.clear();
                tempModel.buildSimilarGraph(coreModel);
                differenceDetector.prepareTargetGraph(coreModel);
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 5;
    }


    private void onTurnEndEventApplied() {
        poolItems.clearExternalList();
    }


    public void onUndoRequested() {
        if (items.size() == 0) return;
        UndoItem lastItem = items.get(items.size() - 1);
        items.remove(lastItem);
        tempModel.resetHexes();
        (new IwCoreHexes(tempModel)).perform(lastItem.getLevelCode());
        (new IwCoreMailBasket(tempModel)).perform(lastItem.getLevelCode());
        differenceDetector.setCurrentModel(coreModel);
        differenceDetector.setTargetModelBy(tempModel);
        ArrayList<AbstractEvent> changes = differenceDetector.apply();
        coreModel.eventsManager.applyMultipleEvents(changes);
        (new IwCoreProvinces(coreModel)).perform(lastItem.getLevelCode());
        coreModel.diplomacyManager.resetRelations(); // import worker won't roll back to default relations otherwise
        (new IwCoreDiplomacy(coreModel)).perform(lastItem.getLevelCode());
        (new IwCoreMailBasket(coreModel)).perform(lastItem.getLevelCode());
        (new IwReadiness(coreModel)).perform(lastItem.getLevelCode());
        // important: in case if I decide to add new import worker here
        // I should not forget to apply same change to ref model in viewable model
        coreModel.onUndoApplied(lastItem);
    }


    private void doShowChangesInConsole(ArrayList<AbstractEvent> changes) {
        System.out.println();
        System.out.println("UndoManager.onUndoRequested");
        for (AbstractEvent event : changes) {
            System.out.println("- " + event);
        }
    }

}
