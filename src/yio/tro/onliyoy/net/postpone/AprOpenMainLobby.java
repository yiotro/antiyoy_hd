package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.OneTimeInfo;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class AprOpenMainLobby extends AbstractPostponedReaction{

    long targetTime;
    String value;


    public AprOpenMainLobby(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
        value = "";
    }


    @Override
    protected void onLaunched() {
        switch (YioGdxGame.platformType) {
            default:
            case android:
                targetTime = System.currentTimeMillis() + 50;
                break;
            case pc:
                targetTime = System.currentTimeMillis() + 600;
                break;
        }
    }


    @Override
    boolean isReady() {
        if (areNetWaitScenesMovingCurrently()) return false;
        return System.currentTimeMillis() > targetTime;
    }


    @Override
    void apply() {
        Scenes.mainLobby.create();
        if (value.length() > 2) {
            Scenes.notification.show(value);
        }
    }


    public void setValue(String value) {
        this.value = value;
    }
}
