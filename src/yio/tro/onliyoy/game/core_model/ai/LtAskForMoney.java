package yio.tro.onliyoy.game.core_model.ai;

public class LtAskForMoney extends AbstractLetterTemplate{

    public LtAskForMoney(DiplomaticAI diplomaticAI) {
        super(diplomaticAI);
    }


    @Override
    void addConditions() {
        int value = 1 + random.nextInt(14);
        letter.addCondition(diplomaticAI.getMoneyCondition(recipient, value));
    }
}
