package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;

public class AnirMatchFinished extends AbstractNetInputReaction{

    @Override
    public void apply() {
        PostponedReactionsManager.aprFinishNetMatch.setCode(value);
        PostponedReactionsManager.aprFinishNetMatch.launch();
    }
}
