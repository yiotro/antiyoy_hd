package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.campaign.Difficulty;
import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.EntityType;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.EventsFactory;
import yio.tro.onliyoy.game.core_model.events.IEventListener;

import java.util.ArrayList;

public class ExternalAiWorker implements IEventListener {

    private static ExternalAiWorker instance;
    private CoreModel coreModel;
    boolean catchMode;
    ArrayList<AbstractEvent> caughtEvents;
    AiManager aiManager;


    public ExternalAiWorker() {
        coreModel = null;
        caughtEvents = new ArrayList<>();
    }


    public static void initialize() {
        instance = null;
    }


    public static ExternalAiWorker getInstance() {
        if (instance == null) {
            instance = new ExternalAiWorker();
        }
        return instance;
    }


    public ArrayList<AbstractEvent> apply(CoreModel srcModel) {
        catchMode = false;
        caughtEvents.clear();
        prepareCoreModel(srcModel);
        catchMode = true;
        aiManager.move();
        return copyCaughtEvents(srcModel);
    }


    private ArrayList<AbstractEvent> copyCaughtEvents(CoreModel srcModel) {
        ArrayList<AbstractEvent> copies = new ArrayList<>();
        EventsFactory factory = srcModel.eventsManager.factory;
        for (AbstractEvent caughtEvent : caughtEvents) {
            AbstractEvent copy = factory.createEvent(caughtEvent.getType());
            copy.copyFrom(caughtEvent);
            copies.add(copy);
        }
        return copies;
    }


    private void prepareCoreModel(CoreModel srcModel) {
        if (coreModel == null || coreModel != srcModel) {
            createCoreModel(srcModel);
        }
        coreModel.initBy(srcModel);
        pretendLikeItsAiBattle();
    }


    private void pretendLikeItsAiBattle() {
        // without this change ai manager won't work properly
        for (PlayerEntity entity : coreModel.entitiesManager.entities) {
            entity.type = EntityType.ai_balancer;
        }
    }


    private void createCoreModel(CoreModel srcModel) {
        coreModel = new CoreModel("external_ai");
        coreModel.eventsManager.addListener(this); // to catch events applied by AI
        coreModel.buildSimilarGraph(srcModel);
        aiManager = new AiManager(coreModel, Difficulty.balancer);
        aiManager.createAIs();
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        if (!catchMode) return;
        if (event.isQuick()) return;
        if (event.isReusable()) return;
        caughtEvents.add(event);
    }


    @Override
    public int getListenPriority() {
        return 1;
    }
}
