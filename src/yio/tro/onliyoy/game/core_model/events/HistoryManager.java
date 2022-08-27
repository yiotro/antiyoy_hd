package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.AbstractRuleset;
import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.stuff.TimeMeasureYio;

import java.util.ArrayList;

public class HistoryManager implements IEventListener {

    CoreModel coreModel;
    public ArrayList<AbstractEvent> eventsList;
    private ArrayList<AbstractEvent> currentTurnEvents;
    public CoreModel startingPosition;
    private StringBuilder stringBuilder;


    public HistoryManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        coreModel.eventsManager.addListener(this);
        eventsList = new ArrayList<>();
        startingPosition = new CoreModel("start");
        stringBuilder = new StringBuilder();
        currentTurnEvents = new ArrayList<>();
    }


    public void clearAll() {
        currentTurnEvents.clear();
        clearEventsList();
    }


    public void buildStartingGraph() {
        startingPosition.buildSimilarGraph(coreModel);
    }


    @Override
    public void onEventValidated(AbstractEvent event) {
        if (!event.isNotable()) return;
        currentTurnEvents.add(event);
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
                onGraphCreated();
                break;
            case match_started:
                onMatchStarted();
                break;
        }
    }


    private void onMatchStarted() {
        startingPosition.diplomacyManager.setBy(coreModel.diplomacyManager);
        startingPosition.fogOfWarManager.setBy(coreModel.fogOfWarManager);
    }


    @Override
    public int getListenPriority() {
        return 5;
    }


    private void onGraphCreated() {
        clearAll();
        buildStartingGraph();
        updateStartingPosition();
    }


    public void updateStartingPosition() {
        // this method is called when graph is created
        // other stuff is copied in HistoryManager.onMatchStarted()
        startingPosition.initBy(coreModel);
    }


    private void onTurnEndEventApplied() {
        eventsList.addAll(currentTurnEvents);
        currentTurnEvents.clear();
    }


    public void onMatchEnded() {
        eventsList.addAll(currentTurnEvents);
        currentTurnEvents.clear();
    }


    public void onUndoRequested() {
        if (currentTurnEvents.size() == 0) return;
        currentTurnEvents.remove(currentTurnEvents.size() - 1);
    }


    public ArrayList<AbstractEvent> getEventsListCopy() {
        return new ArrayList<>(eventsList);
    }


    public void clearEventsList() {
        eventsList.clear();
    }


    public String encodeStartingHexes() {
        return startingPosition.encodeHexes();
    }


    public String encodeStartingProvinces() {
        return startingPosition.provincesManager.encode();
    }


    public String encodeEventsList() {
        stringBuilder.setLength(0);
        for (AbstractEvent abstractEvent : eventsList) {
            stringBuilder.append(abstractEvent.encode()).append(",");
        }
        for (AbstractEvent currentTurnEvent : currentTurnEvents) {
            stringBuilder.append(currentTurnEvent.encode()).append(",");
        }
        return stringBuilder.toString();
    }


    public void showInConsole() {
        System.out.println();
        System.out.println("HistoryManager.showInConsole");
        System.out.println("Current turn:");
        for (AbstractEvent event : currentTurnEvents) {
            System.out.println("- " + event);
        }
        System.out.println("Events list:");
        for (AbstractEvent event : eventsList) {
            System.out.println("- " + event);
        }
    }
}
