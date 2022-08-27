package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NmType;

public class AprSendQuickMatchRequest extends AbstractPostponedReaction{

    public AprSendQuickMatchRequest(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return Scenes.quickMatchSearching.netProcessViewElement.getFactor().getValue() == 1;
    }


    @Override
    void apply() {
        root.sendMessage(NmType.search_quick_match, "");
    }
}
