package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.net.shared.NetUlTransferData;

public class AprPlayUserLevel extends AbstractPostponedReaction{

    String value;


    public AprPlayUserLevel(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return !areNetWaitScenesMovingCurrently();
    }


    @Override
    void apply() {
        NetUlTransferData tempUlTransferData = yioGdxGame.netRoot.tempUlTransferData;
        tempUlTransferData.decode(value);
        (new IwClientInit(yioGdxGame, LoadingType.user_level)).perform(tempUlTransferData.levelCode);
    }


    public void setValue(String value) {
        this.value = value;
    }
}
