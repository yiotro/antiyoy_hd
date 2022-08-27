package yio.tro.onliyoy.net.postpone;

public class AprOfflineMode extends AbstractPostponedReaction{

    long targetTime;


    public AprOfflineMode(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
        targetTime = 0;
    }


    @Override
    boolean isReady() {
        return System.currentTimeMillis() > targetTime;
    }


    @Override
    protected void onLaunched() {
        targetTime = System.currentTimeMillis() + 1000;
    }


    @Override
    void apply() {
        yioGdxGame.netRoot.enableOfflineMode();
    }
}
