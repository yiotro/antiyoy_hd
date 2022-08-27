package yio.tro.onliyoy.game.core_model.events;

public interface IEventListener {


    void onEventValidated(AbstractEvent event);


    void onEventApplied(AbstractEvent event);


    int getListenPriority();


}
