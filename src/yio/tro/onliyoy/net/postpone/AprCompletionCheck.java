package yio.tro.onliyoy.net.postpone;

public class AprCompletionCheck extends AbstractPostponedReaction{

    public AprCompletionCheck(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return !areNetWaitScenesMovingCurrently();
    }


    @Override
    void apply() {
        objectsLayer.editorManager.onCompletionCheckRequested();
    }
}
