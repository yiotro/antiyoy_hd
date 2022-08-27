package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.Hex;

import java.util.ArrayList;

public class LtExchangeLands extends AbstractLetterTemplate{

    public LtExchangeLands(DiplomaticAI diplomaticAI) {
        super(diplomaticAI);
    }


    @Override
    void addConditions() {
        ArrayList<Hex> hexesToSell = getHexesForExchange(currentEntity.color, 2 + random.nextInt(4));
        letter.addCondition(diplomaticAI.getLandsCondition(hexesToSell));
        ArrayList<Hex> hexesToBuy = getHexesForExchange(recipient.color, 2 + random.nextInt(4));
        letter.addCondition(diplomaticAI.getLandsCondition(hexesToBuy));
    }
}
