package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.Condition;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.Relation;
import yio.tro.onliyoy.game.core_model.RelationType;

import java.util.ArrayList;

public class LtImproveRelations extends AbstractLetterTemplate{

    public LtImproveRelations(DiplomaticAI diplomaticAI) {
        super(diplomaticAI);
    }


    @Override
    void addConditions() {
        RelationType currentRelationType = currentEntity.getRelation(recipient).type;
        if (currentRelationType == RelationType.alliance) return;
        int currentRelationOrdinal = currentRelationType.ordinal();
        int delta = RelationType.values().length - currentRelationOrdinal - 1;
        int targetRelationOrdinal = currentRelationOrdinal + 1 + random.nextInt(delta);
        RelationType targetRelationType = RelationType.values()[targetRelationOrdinal];
        int lock = random.nextInt(8);
        if (random.nextFloat() < 0.25) {
            lock = 0;
        }
        Condition simpleRelationsCondition = diplomaticAI.getSimpleRelationsCondition(
                recipient,
                targetRelationType,
                lock
        );
        letter.addCondition(simpleRelationsCondition);
        addProposition(simpleRelationsCondition);
    }


    private void addProposition(Condition simpleRelationsCondition) {
        switch (random.nextInt(7)) {
            default:
            case 0:
            case 1:
            case 2:
            case 3:
                // propose nothing
                break;
            case 4:
            case 5:
                int estimatedValue = diplomaticAI.appraiser.estimate(currentEntity, simpleRelationsCondition);
                int money = Math.abs(estimatedValue - 4 + random.nextInt(10));
                letter.addCondition(diplomaticAI.getMoneyCondition(currentEntity, money));
                break;
            case 6:
                ArrayList<Hex> hexes = getHexesForExchange(currentEntity.color, 2 + random.nextInt(4));
                letter.addCondition(diplomaticAI.getLandsCondition(hexes));
                break;
        }
    }
}
