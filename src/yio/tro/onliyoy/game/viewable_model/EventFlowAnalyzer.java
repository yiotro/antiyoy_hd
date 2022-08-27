package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Letter;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.general.GameController;

import java.util.ArrayList;

public class EventFlowAnalyzer implements IEventListener {

    public static final int OFFSET = 80;
    private static EventFlowAnalyzer instance;
    public ArrayList<AbstractEvent> actualEvents;
    ArrayList<AbstractEvent> reproducedEvents;
    private CoreModel coreModel;
    private final StringBuilder stringBuilder;
    CoreModel actualModel;
    CoreModel reproducedModel;


    public EventFlowAnalyzer() {
        actualEvents = new ArrayList<>();
        reproducedEvents = new ArrayList<>();
        coreModel = null;
        stringBuilder = new StringBuilder();
        actualModel = null;
        reproducedModel = null;
    }


    public static void initialize() {
        instance = null;
    }


    public static EventFlowAnalyzer getInstance() {
        if (instance == null) {
            instance = new EventFlowAnalyzer();
        }
        return instance;
    }


    public void applyComparisonWithEventsList(ArrayList<AbstractEvent> sourceList) {
        System.out.println();
        System.out.println("EventFlowAnalyzer.applyComparisonWithEventsList");
        DebugFlags.compareClientVsServerEventLogs = false; // to enable console output
        reproducedEvents.clear();
        for (AbstractEvent actualEvent : actualEvents) {
            if (actualEvent instanceof EventTurnEnd) {
                ((EventTurnEnd) actualEvent).setTargetEndTime(0); // for comparison
            }
        }
        for (AbstractEvent srcEvent : sourceList) {
            reproducedEvents.add(srcEvent);
            applyComparison();
        }
    }


    private void applyComparison() {
        if (DebugFlags.compareClientVsServerEventLogs) return;
        int index = reproducedEvents.size() - 1;
        AbstractEvent reproducedEvent = reproducedEvents.get(index);
        if (index > actualEvents.size() - 1) {
            System.out.println(generateComparativeString(null, reproducedEvent));
            return;
        }
        AbstractEvent actualEvent = actualEvents.get(index);
        boolean equal = areEventsEqual(actualEvent, reproducedEvent);
        if (equal) {
            System.out.println(generateEqualString(reproducedEvent));
        } else {
            System.out.println(generateComparativeString(actualEvent, reproducedEvent));
        }
    }


    private String generateEqualString(AbstractEvent reproducedEvent) {
        stringBuilder.setLength(0);
        while (stringBuilder.length() < OFFSET) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString() + " " + reproducedEvent;
    }


    private String generateComparativeString(AbstractEvent actualEvent, AbstractEvent reproducedEvent) {
        stringBuilder.setLength(0);
        stringBuilder.append(actualEvent);
        stringBuilder.append(" ");
        while (stringBuilder.length() < OFFSET) {
            stringBuilder.append("-");
        }
        return stringBuilder.toString() + " " + reproducedEvent;
    }


    private boolean areEventsEqual(AbstractEvent event1, AbstractEvent event2) {
        // yes, this method is slow but for testing it'll do
        return event1.encode().equals(event2.encode());
    }


    public void onReproducedModelCreated(CoreModel reproducedModel) {
        if (!DebugFlags.analyzeEventFlows) return;
        reproducedEvents.clear();
        this.reproducedModel = reproducedModel;
        coreModel = reproducedModel;
        reproducedModel.eventsManager.addListener(this);
        System.out.println();
        System.out.println("EventFlowAnalyzer.onReproducedModelCreated");
    }


    public void onActualModelCreated(CoreModel actualModel) {
        if (!DebugFlags.analyzeEventFlows) return;
        actualEvents.clear();
        this.actualModel = actualModel;
        reproducedModel = null;
        coreModel = actualModel;
        actualModel.eventsManager.addListener(this);
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        if (event.isReusable()) return;
        EventsFactory factory = coreModel.eventsManager.factory;
        AbstractEvent copyEvent = factory.createCopy(event);
        if (event.getCoreModel() == actualModel) {
            actualEvents.add(copyEvent);
        }
        if (event.getCoreModel() == reproducedModel) {
            reproducedEvents.add(copyEvent);
            applyComparison();
        }
    }


    @Override
    public int getListenPriority() {
        return 11;
    }
}
