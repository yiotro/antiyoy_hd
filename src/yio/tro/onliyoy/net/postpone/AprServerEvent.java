package yio.tro.onliyoy.net.postpone;

import java.util.ArrayList;

public class AprServerEvent extends AbstractPostponedReaction{


    public AprServerEvent(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return !yioGdxGame.gamePaused;
    }


    @Override
    void apply() {
        while (inputBuffer.size() > 0) {
            String value = inputBuffer.get(0);
            viewableModel.onReceivedEventFromServer(value);
            inputBuffer.remove(0);
        }
    }

}
