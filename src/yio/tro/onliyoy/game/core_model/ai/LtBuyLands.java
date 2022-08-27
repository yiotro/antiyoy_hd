package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.Hex;

import java.util.ArrayList;

public class LtBuyLands extends AbstractLetterTemplate{

    public LtBuyLands(DiplomaticAI diplomaticAI) {
        super(diplomaticAI);
    }


    @Override
    void addConditions() {
        ArrayList<Hex> hexesToBuy = getHexesForExchange(recipient.color, 2 + random.nextInt(4));
        int estimatedValue = diplomaticAI.appraiser.estimate(hexesToBuy);
        int money = Math.abs(estimatedValue - 4 + random.nextInt(10));
        letter.addCondition(diplomaticAI.getLandsCondition(hexesToBuy));
        letter.addCondition(diplomaticAI.getMoneyCondition(currentEntity, money));
    }
}
