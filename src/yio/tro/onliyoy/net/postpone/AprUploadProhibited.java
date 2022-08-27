package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.menu.scenes.Scenes;

public class AprUploadProhibited extends AbstractPostponedReaction{

    public AprUploadProhibited(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return !areNetWaitScenesMovingCurrently();
    }


    @Override
    void apply() {
        Scenes.editorLobby.create();
        Scenes.notification.show("wait_to_upload");
    }
}
