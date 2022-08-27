package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.events.EventUnitMove;

import java.util.ArrayList;
import java.util.Collections;

public class MovementConflictsResolver {

    // this class is used in difference detector
    // some units may return to hexes that are occupied by other units
    // in such situations events should be sorted in such a way that
    // units don't eat each other
    private ArrayList<EventUnitMove> list;
    private EventUnitMove event1;
    private EventUnitMove event2;


    public MovementConflictsResolver() {
        list = null;
        resetConflict();
    }


    void apply(ArrayList<EventUnitMove> source) {
        list = source;
        while (true) {
            searchForConflict();
            if (!conflictDetected()) break;
            fixConflict();
        }
    }


    private void fixConflict() {
        int index1 = list.indexOf(event1);
        int index2 = list.indexOf(event2);
        Collections.swap(list, index1, index2);
    }


    private void searchForConflict() {
        resetConflict();
        for (int i = 0; i < list.size(); i++) {
            EventUnitMove a = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                EventUnitMove b = list.get(j);
                if (a.finish == b.start) {
                    event1 = a;
                    event2 = b;
                    break;
                }
            }
            if (conflictDetected()) break;
        }
    }


    private boolean conflictDetected() {
        return event1 != null;
    }


    private void resetConflict() {
        event1 = null;
        event2 = null;
    }
}
