package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;

public class AnirUnableToJoinCustomMatch extends AbstractNetInputReaction{

    @Override
    public void apply() {
        PostponedReactionsManager.aprShowCustomMatches.launch();
    }
}
