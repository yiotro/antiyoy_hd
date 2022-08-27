package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NmType;

public class AprSendDuelRequest extends AbstractPostponedReaction{

    public AprSendDuelRequest(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return Scenes.searchingForDuel.netProcessViewElement.getFactor().getValue() == 1;
    }


    @Override
    void apply() {
        root.sendMessage(NmType.search_for_duel, "");
    }
}
