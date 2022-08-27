package yio.tro.onliyoy.game.core_model.ai;

public class LtProposeMoney extends AbstractLetterTemplate{

    public LtProposeMoney(DiplomaticAI diplomaticAI) {
        super(diplomaticAI);
    }


    @Override
    void addConditions() {
        int value = 1 + random.nextInt(3);
        letter.addCondition(diplomaticAI.getMoneyCondition(currentEntity, value));
    }
}
