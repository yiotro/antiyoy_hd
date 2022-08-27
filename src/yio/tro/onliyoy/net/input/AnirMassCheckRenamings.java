package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.net.shared.NetMassRenamingData;

public class AnirMassCheckRenamings extends AbstractNetInputReaction{

    @Override
    public void apply() {
        PostponedReactionsManager.aprMassCheckRenamings.launch();
        PostponedReactionsManager.aprMassCheckRenamings.setValue(value);
    }
}
