package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.*;

public class DiplomaticAiEasy extends DiplomaticAI {


    private EntitiesManager entitiesManager;
    private PlayerEntity currentEntity;


    public DiplomaticAiEasy(CoreModel coreModel) {
        super(coreModel);
    }


    @Override
    public void apply() {
        entitiesManager = coreModel.entitiesManager;
        currentEntity = entitiesManager.getCurrentEntity();
        if (!isAlive(currentEntity)) return;
        askOtherEntitiesForMoney();
        declareWar();
        agreeToEverything();
    }


    private void declareWar() {
        if (random.nextFloat() > 0.1) return;
        PlayerEntity potentialEnemy = getPotentialEnemy();
        if (potentialEnemy == null) return;
        Letter letter = startLetter(currentEntity, potentialEnemy);
        letter.addCondition(getNotificationCondition(RelationType.war));
        sendLetter(letter);
    }


    private PlayerEntity getPotentialEnemy() {
        PlayerEntity[] entities = coreModel.entitiesManager.entities;
        int c = 50;
        while (c > 0) {
            c--;
            int index = random.nextInt(entities.length);
            PlayerEntity enemy = entities[index];
            if (enemy == currentEntity) continue;
            if (!isAlive(enemy)) continue;
            if (currentEntity.getRelation(enemy).type == RelationType.war) continue;
            return enemy;
        }
        return null;
    }


    private void agreeToEverything() {
        updateInboxList();
        for (Letter letter : inboxList) {
            agreeToLetter(letter);
        }
    }


    private void askOtherEntitiesForMoney() {
        for (PlayerEntity entity : entitiesManager.entities) {
            if (entity == currentEntity) continue;
            if (!isAlive(entity)) continue;
            if (random.nextFloat() > 0.15) continue;
            Letter letter = startLetter(currentEntity, entity);
            letter.addCondition(getMoneyCondition(entity, 1));
            sendLetter(letter);
        }
    }
}