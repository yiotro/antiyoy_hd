package yio.tro.onliyoy.game.core_model.events;

public class EventGraphCreated extends AbstractEvent{

    @Override
    public EventType getType() {
        return EventType.graph_created;
    }


    @Override
    public boolean isValid() {
        return true;
    }


    @Override
    public void applyChange() {

    }


    @Override
    public void copyFrom(AbstractEvent srcEvent) {

    }


    @Override
    protected String getLocalEncodedInfo() {
        return "-";
    }
}
