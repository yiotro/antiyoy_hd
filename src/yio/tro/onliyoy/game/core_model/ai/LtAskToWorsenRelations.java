package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.*;

import java.util.ArrayList;

public class LtAskToWorsenRelations extends AbstractLetterTemplate{

    public LtAskToWorsenRelations(DiplomaticAI diplomaticAI) {
        super(diplomaticAI);
    }


    @Override
    void addConditions() {
        diplomaticAI.updateAdjacentColors();
        ArrayList<HColor> adjacentColors = diplomaticAI.adjacentColors;
        if (adjacentColors.size() == 0) return;
        HColor targetColor = getTargetColor();
        if (targetColor == null) return;
        PlayerEntity targetEntity = coreModel.entitiesManager.getEntity(targetColor);
        if (targetEntity == null) return;
        RelationType targetRelationType = getTargetRelationType(targetEntity);
        if (targetRelationType == null) return;
        addProposition();
        int lock = random.nextInt(8);
        if (random.nextFloat() < 0.25) {
            lock = 0;
        }
        letter.addCondition(diplomaticAI.getAdvancedRelationsCondition(
                recipient,
                targetEntity,
                targetRelationType,
                lock
        ));
    }


    private RelationType getTargetRelationType(PlayerEntity targetEntity) {
        Relation relation = targetEntity.getRelation(recipient);
        if (relation.isLocked()) return null;
        switch (relation.type) {
            default:
            case war:
                return null;
            case neutral:
                return RelationType.war;
            case friend:
            case alliance:
                if (random.nextFloat() < 0.5) {
                    return RelationType.neutral;
                }
                return RelationType.war;
        }
    }


    private void addProposition() {
        switch (random.nextInt(4)) {
            default:
            case 0:
            case 1:
                // propose nothing
                break;
            case 2:
                int money = 20 + random.nextInt(19);
                letter.addCondition(diplomaticAI.getMoneyCondition(currentEntity, money));
                break;
            case 3:
                ArrayList<Hex> hexes = getHexesForExchange(currentEntity.color, 2 + random.nextInt(4));
                letter.addCondition(diplomaticAI.getLandsCondition(hexes));
                break;
        }
    }


    private HColor getTargetColor() {
        ArrayList<HColor> adjacentColors = diplomaticAI.adjacentColors;
        if (adjacentColors.size() == 1 && adjacentColors.get(0) == recipient.color) return null;
        while (true) {
            HColor color = adjacentColors.get(random.nextInt(adjacentColors.size()));
            if (color == recipient.color) continue;
            return color;
        }
    }
}
