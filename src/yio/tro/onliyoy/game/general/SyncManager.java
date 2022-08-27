package yio.tro.onliyoy.game.general;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.DifferenceDetector;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.game.export_import.*;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.stuff.TimeMeasureYio;

import java.util.ArrayList;

public class SyncManager implements IEventListener {

    ViewableModel viewableModel;
    CoreModel tempModel;
    DifferenceDetector differenceDetector;


    public SyncManager(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
        viewableModel.eventsManager.addListener(this);
        tempModel = new CoreModel("temp_sync");
        differenceDetector = new DifferenceDetector();
    }


    public void apply(String levelCode) {
        viewableModel.provinceSelectionManager.onClickedOutside();
        viewableModel.unitsManager.onClickedOutside();
        tempModel.resetHexes();
        (new IwCoreHexes(tempModel)).perform(levelCode);
        (new IwCoreMailBasket(tempModel)).perform(levelCode);
        (new IwCoreTurn(viewableModel)).perform(levelCode);
        differenceDetector.setCurrentModel(viewableModel);
        differenceDetector.setTargetModelBy(tempModel);
        ArrayList<AbstractEvent> changes = differenceDetector.apply();
        viewableModel.eventsManager.applyMultipleEvents(changes);
        (new IwCoreProvinces(viewableModel)).perform(levelCode);
        (new IwCoreDiplomacy(viewableModel)).perform(levelCode);
        (new IwCoreMailBasket(viewableModel)).perform(levelCode);
        (new IwReadiness(viewableModel)).perform(levelCode);
        (new IwCoreFogOfWar(viewableModel)).perform(levelCode);
        // important: in case if I decide to add new import worker here
        // I should not forget to apply same change to ref model in viewable model
        viewableModel.onSyncApplied(levelCode);
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case graph_created:
                tempModel.buildSimilarGraph(viewableModel);
                differenceDetector.prepareTargetGraph(viewableModel);
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 4;
    }
}
