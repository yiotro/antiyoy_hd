package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.Hex;

import java.util.ArrayList;

public class LtSellLands extends AbstractLetterTemplate{

    public LtSellLands(DiplomaticAI diplomaticAI) {
        super(diplomaticAI);
    }


    @Override
    void addConditions() {
        ArrayList<Hex> hexesToSell = getHexesForExchange(currentEntity.color, 2 + random.nextInt(4));
        letter.addCondition(diplomaticAI.getLandsCondition(hexesToSell));
        int estimatedValue = diplomaticAI.appraiser.estimate(hexesToSell);
        int money = Math.abs(estimatedValue - 4 + random.nextInt(10));
        letter.addCondition(diplomaticAI.getMoneyCondition(recipient, money));
    }
}
