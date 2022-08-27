package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.net.shared.NetUlTransferData;

public class AnirUserLevelToCheck extends AbstractNetInputReaction{

    @Override
    public void apply() {
        PostponedReactionsManager.aprCheckUserLevel.launch();
        PostponedReactionsManager.aprCheckUserLevel.setValue(value);
    }
}
