package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AprShowInGameToast extends AbstractPostponedReaction{

    String key;


    public AprShowInGameToast(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return yioGdxGame.gameView.coversAllScreen();
    }


    @Override
    void apply() {
        Scenes.toast.show(key);
    }


    public void setKey(String key) {
        this.key = key;
    }
}
