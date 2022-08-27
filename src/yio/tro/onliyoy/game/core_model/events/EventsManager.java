package yio.tro.onliyoy.game.core_model.events;

import yio.tro.onliyoy.game.core_model.CoreModel;

import java.util.ArrayList;

public class EventsManager {

    CoreModel coreModel;
    public EventsFactory factory;
    private ArrayList<IEventListener> eventListeners;
    private boolean automaticUtilization;


    public EventsManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        factory = new EventsFactory(this);
        eventListeners = new ArrayList<>();
        automaticUtilization = true;
        addListener(coreModel);
    }


    public void applyEvent(AbstractEvent event) {
        if (!event.isValid()) return;
        for (IEventListener listener : eventListeners) {
            listener.onEventValidated(event);
        }
        event.applyChange();
        for (IEventListener listener : eventListeners) {
            listener.onEventApplied(event);
        }
        checkToUtilize(event);
    }


    private void checkToUtilize(AbstractEvent event) {
        if (!automaticUtilization) return;
        coreModel.eventsRefrigerator.utilizeEvent(event);
    }


    public void applyMultipleEvents(ArrayList<AbstractEvent> events) {
        for (AbstractEvent event : events) {
            applyEvent(event);
        }
        if (containsQuickEvent(events)) {
            coreModel.onQuickEventApplied();
        }
    }


    private boolean containsQuickEvent(ArrayList<AbstractEvent> events) {
        for (AbstractEvent event : events) {
            if (event.isQuick()) return true;
        }
        return false;
    }


    public void addListener(IEventListener listener) {
        if (eventListeners.size() == 0) {
            eventListeners.add(listener);
            return;
        }
        int index = 0;
        int priority = listener.getListenPriority();
        for (int i = 0; i < eventListeners.size(); i++) {
            if (eventListeners.get(i).getListenPriority() < priority) break;
            index = i;
        }
        eventListeners.add(index + 1, listener);
    }


    public void setAutomaticUtilization(boolean automaticUtilization) {
        this.automaticUtilization = automaticUtilization;
    }


    public void showListenersInConsole() {
        System.out.println("Event listeners:");
        for (IEventListener listener : eventListeners) {
            System.out.println("- [" + listener.getListenPriority() + "] " + listener);
        }
    }
}
