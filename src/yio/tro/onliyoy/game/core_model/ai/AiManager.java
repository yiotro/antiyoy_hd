package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.campaign.Difficulty;
import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.events.AbstractEvent;
import yio.tro.onliyoy.game.core_model.events.IEventListener;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.stuff.TimeMeasureYio;

import java.util.Random;

public class AiManager implements IEventListener {

    CoreModel coreModel;
    AiFactory factory;
    AbstractAI aiRandom;
    AbstractAI aiBalancerClassic;
    AbstractAI aiBalancerDefault;
    AbstractAI aiBalancerExperimental;
    AbstractAI aiBalancerDuel;
    public int versionCode;
    protected Difficulty difficulty;
    public boolean active;


    public AiManager(CoreModel coreModel, Difficulty difficulty) {
        this.coreModel = coreModel;
        this.difficulty = difficulty;
        coreModel.eventsManager.addListener(this);
        factory = new AiFactory(this);
        versionCode = -1;
        active = true;
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
                createAIs();
                break;
        }
    }


    @Override
    public int getListenPriority() {
        return 5;
    }


    public void createAIs() {
        aiRandom = factory.createAiRandom(versionCode);
        aiBalancerClassic = factory.createAiBalancerClassic(versionCode);
        aiBalancerDefault = factory.createAiBalancerDefault(versionCode);
        aiBalancerExperimental = factory.createAiBalancerExperimental(versionCode);
        aiBalancerDuel = factory.createAiBalancerDuel(versionCode);

        aiBalancerDefault.checkToDetermineRandom();
    }


    public int getUpdatedAiVersionCode() {
        if (versionCode == -1) {
            updateVersionCode();
        }
        return versionCode;
    }


    private void updateVersionCode() {
        versionCode = -1;
        PlayerEntity[] entities = coreModel.entitiesManager.entities;
        if (entities.length == 0) return;
        for (PlayerEntity playerEntity : entities) {
            if (!playerEntity.isArtificialIntelligence()) continue;
            AbstractAI ai = getAI(playerEntity);
            if (ai == null) continue;
            versionCode = ai.getVersionCode();
            break;
        }
    }


    public void move() {
        if (!active) return;
        AbstractAI currentAI = getCurrentAI();
        if (currentAI == null) return;
        currentAI.setDifficulty(difficulty);
        currentAI.perform();
    }


    public AbstractAI getCurrentAI() {
        return getAI(coreModel.entitiesManager.getCurrentEntity());
    }


    private AbstractAI getAI(PlayerEntity playerEntity) {
        switch (playerEntity.type) {
            default:
                return null;
            case ai_random:
                return aiRandom;
            case ai_balancer:
                return getBalancerAI();
        }
    }


    public AbstractAI getBalancerAI() {
        switch (coreModel.ruleset.getRulesType()) {
            default:
                return null;
            case def:
                return aiBalancerDefault;
            case classic:
                return aiBalancerClassic;
            case experimental:
                return aiBalancerExperimental;
            case duel:
                return aiBalancerDuel;
        }
    }


    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }


    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }


    public void onMatchEnded() {
        active = false;
    }
}
