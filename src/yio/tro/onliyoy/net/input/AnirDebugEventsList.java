package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.export_import.IwEventsList;
import yio.tro.onliyoy.game.viewable_model.EventFlowAnalyzer;

import java.util.ArrayList;

public class AnirDebugEventsList extends AbstractNetInputReaction {

    @Override
    public void apply() {
        ArrayList<AbstractEvent> events = extractListFromCode();
        EventFlowAnalyzer.getInstance().applyComparisonWithEventsList(events);
    }


    private ArrayList<AbstractEvent> extractListFromCode() {
        CoreModel coreModel = objectsLayer.viewableModel;
        HistoryManager historyManager = new HistoryManager(coreModel);
        String modString = "#events_list:" + value + "#";
        (new IwEventsList(coreModel, historyManager)).perform(modString);
        return historyManager.eventsList;
    }
}
