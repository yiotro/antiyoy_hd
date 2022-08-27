package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.net.shared.NetRenamingData;

public class AnirGoCheckRenaming extends AbstractNetInputReaction{

    @Override
    public void apply() {
        PostponedReactionsManager.aprCheckRenaming.launch();
        PostponedReactionsManager.aprCheckRenaming.setValue(value);
    }
}
