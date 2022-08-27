package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.*;

import java.util.ArrayList;

public class DiplomaticAiNormal extends DiplomaticAI{


    EntitiesManager entitiesManager;
    PlayerEntity currentEntity;
    ArrayList<AbstractLetterTemplate> templates;


    public DiplomaticAiNormal(CoreModel coreModel) {
        super(coreModel);
        initTemplates();
    }


    private void initTemplates() {
        templates = new ArrayList<>();
        templates.add(new LtAskForMoney(this));
        templates.add(new LtImproveRelations(this));
        templates.add(new LtExchangeLands(this));
        templates.add(new LtSellLands(this));
        templates.add(new LtBuyLands(this));
        templates.add(new LtAskToWorsenRelations(this));
        templates.add(new LtProposeMoney(this));
    }


    @Override
    public void apply() {
        entitiesManager = coreModel.entitiesManager;
        currentEntity = entitiesManager.getCurrentEntity();
        if (!isAlive(currentEntity)) return;
        sendLetters();
        answerToLetters();
        checkToShowAggression();
    }


    private void checkToShowAggression() {
        checkToStartWar();
        checkToLeaveAlliance();
    }


    private void checkToLeaveAlliance() {
        if (random.nextInt(4) != 0) return;
        for (PlayerEntity entity : entitiesManager.entities) {
            if (entity == currentEntity) continue;
            Relation relation = currentEntity.getRelation(entity);
            if (relation.type != RelationType.alliance) continue;
            if (relation.isLocked()) continue;
            checkToLeaveAlliance(entity);
        }
    }


    private void checkToLeaveAlliance(PlayerEntity entity) {
        int currentWealth = appraiser.estimate(currentEntity);
        int targetWealth = appraiser.estimate(entity);
        if (targetWealth > 0.66f * currentWealth) return;
        RelationType targetRelationType = RelationType.neutral;
        if (random.nextFloat() < 0.33f) {
            targetRelationType = RelationType.friend;
        }
        Letter letter = startLetter(currentEntity, entity);
        letter.addCondition(getNotificationCondition(targetRelationType));
        sendLetter(letter);
    }


    private void checkToStartWar() {
        if (coreModel.turnsManager.lap < 4) return;
        updateAdjacentColors();
        if (isAtWarWithAdjacentColor()) return;
        PlayerEntity enemy = getEnemyForWar();
        if (enemy == null) return;
        if (isEntityProtectedDiplomatically(enemy)) return;
        if (currentEntity.getRelation(enemy).type == RelationType.war) return;
        if (currentEntity.getRelation(enemy).isLocked()) return;
        Letter letter = startLetter(currentEntity, enemy);
        letter.addCondition(getNotificationCondition(RelationType.war));
        sendLetter(letter);
    }


    private PlayerEntity getEnemyForWar() {
        PlayerEntity weakestNeighbour = findWeakestNeighbour();
        if (weakestNeighbour == null) return null;
        int necessaryMoney = estimateNecessaryMoneyForWar(weakestNeighbour);
        int sumMoney = coreModel.provincesManager.getSumMoney(currentEntity.color);
        if (sumMoney < necessaryMoney) return null;
        return weakestNeighbour;
    }


    private int estimateNecessaryMoneyForWar(PlayerEntity targetEntity) {
        if (random.nextFloat() < 0.05f) return 15;
        int currentWealth = appraiser.estimate(currentEntity);
        int targetWealth = appraiser.estimate(targetEntity);
        return (int) Math.min(0.5f * currentWealth, targetWealth);
    }


    private PlayerEntity findWeakestNeighbour() {
        // important: adjacent colors must be updated at this point
        PlayerEntity weakestNeighbour = null;
        int smallestWealth = 0;
        for (HColor adjacentColor : adjacentColors) {
            PlayerEntity adjacentEntity = entitiesManager.getEntity(adjacentColor);
            if (adjacentEntity == null) continue;
            int wealth = appraiser.estimate(adjacentEntity);
            if (weakestNeighbour == null || wealth < smallestWealth) {
                weakestNeighbour = adjacentEntity;
                smallestWealth = wealth;
            }
        }
        return weakestNeighbour;
    }


    private boolean isEntityProtectedDiplomatically(PlayerEntity targetEntity) {
        for (PlayerEntity entity : entitiesManager.entities) {
            if (entity == targetEntity) continue;
            if (entity == currentEntity) continue;
            if (targetEntity.getRelation(entity).type != RelationType.alliance) continue;
            Relation relation = currentEntity.getRelation(entity);
            if (relation.isLocked()) continue;
            if (relation.type == RelationType.war) continue;
            return true;
        }
        return false;
    }


    private boolean isAtWarWithAdjacentColor() {
        for (HColor color : adjacentColors) {
            PlayerEntity adjacentEntity = entitiesManager.getEntity(color);
            Relation relation = currentEntity.getRelation(adjacentEntity);
            if (relation == null) continue;
            if (relation.type == RelationType.war) return true;
        }
        return false;
    }


    private void answerToLetters() {
        updateInboxList();
        for (Letter letter : inboxList) {
            if (appraiser.isAbuseDetected(currentEntity, letter)) continue;
            if (appraiser.estimate(currentEntity, letter) < 0) continue;
            agreeToLetter(letter);
        }
    }


    private void sendLetters() {
        int quantity = getLettersQuantity();
        for (int i = 0; i < quantity; i++) {
            PlayerEntity recipient = getRandomRecipient();
            if (recipient == null) break;
            AbstractLetterTemplate randomTemplate = getRandomTemplate();
            Letter letter = randomTemplate.make(recipient);
            if (checkToDecreaseLandExchange(recipient, letter)) continue;
            checkToAddSmileys(letter);
            if (letter.conditions.size() == 0) continue;
            if (appraiser.estimate(currentEntity, letter) < -3) continue;
            sendLetter(letter);
        }
    }


    private boolean checkToDecreaseLandExchange(PlayerEntity recipient, Letter letter) {
        if (!recipient.isArtificialIntelligence()) return false;
        if (!letter.contains(ConditionType.give_lands)) return false;
        return random.nextInt(50) != 0;
    }


    private void checkToAddSmileys(Letter letter) {
        if (letter.conditions.size() > 2) return;
        double chance = 0.5;
        if (letter.conditions.size() == 2) {
            chance = 0.1;
        }
        if (random.nextDouble() > chance) return;
        Condition condition = new Condition();
        condition.executorColor = currentEntity.color;
        condition.type = ConditionType.smileys;
        condition.setSmileys(smileysGenerator.apply());
        letter.addCondition(condition);
    }


    private AbstractLetterTemplate getRandomTemplate() {
        int index = random.nextInt(templates.size());
        return templates.get(index);
    }


    private int getLettersQuantity() {
        float randomValue = random.nextFloat();
        if (randomValue < 0.5f) return 1;
        if (randomValue < 0.75f) return 0;
        if (randomValue < 0.9f) return 2;
        return 3;
    }


    private boolean areThereAnyAliveRecipients() {
        for (PlayerEntity entity : coreModel.entitiesManager.entities) {
            if (entity == currentEntity) continue;
            if (!isAlive(entity)) continue;
            return true;
        }
        return false;
    }


    private PlayerEntity getRandomRecipient() {
        if (!areThereAnyAliveRecipients()) return null;
        PlayerEntity[] entities = coreModel.entitiesManager.entities;
        while (true) {
            PlayerEntity playerEntity = entities[random.nextInt(entities.length)];
            if (playerEntity == currentEntity) continue;
            if (!isAlive(playerEntity)) continue;
            return playerEntity;
        }
    }
}
