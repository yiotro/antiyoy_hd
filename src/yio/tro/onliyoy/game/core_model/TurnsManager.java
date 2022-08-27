package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.EventType;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.game.export_import.Encodeable;

public class TurnsManager implements IEventListener, Encodeable {

    CoreModel coreModel;
    public int turnIndex;
    public int lap;


    public TurnsManager(CoreModel coreModel) {
        this.coreModel = coreModel;
        coreModel.eventsManager.addListener(this);
        reset();
    }


    public void reset() {
        turnIndex = 0;
        lap = 0;
    }


    public void setBy(TurnsManager source) {
        turnIndex = source.turnIndex;
        lap = source.lap;
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
                // shouldn't reset here because current turn is importing in basic stuff
                break;
            case turn_end:
                onTurnEndEventApplied();
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 8;
    }


    private void onTurnEndEventApplied() {
        
    }


    public void doSwitchTurnIndex() {
        if (isTurnIndexInTheEndOfLap()) {
            turnIndex = 0;
            lap++;
            return;
        }
        turnIndex++;
    }


    public boolean isTurnIndexInTheEndOfLap() {
        return turnIndex == coreModel.entitiesManager.entities.length - 1;
    }


    @Override
    public String encode() {
        return turnIndex + " " + lap;
    }


    public void decode(String source) {
        String[] split = source.split(" ");
        turnIndex = Integer.valueOf(split[0]);
        lap = Integer.valueOf(split[1]);
    }
}
