package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;

public class AnirEvent extends AbstractNetInputReaction{

    @Override
    public void apply() {
        PostponedReactionsManager.aprServerEvent.launch();
        PostponedReactionsManager.aprServerEvent.addToBuffer(value);
    }
}
