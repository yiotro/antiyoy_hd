package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.net.shared.NetUlTransferData;

public class AprCheckUserLevel extends AbstractPostponedReaction{

    String value;


    public AprCheckUserLevel(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return !areNetWaitScenesMovingCurrently();
    }


    @Override
    void apply() {
        NetUlTransferData netUlTransferData = new NetUlTransferData();
        netUlTransferData.decode(value);
        root.verificationInfo.name = netUlTransferData.name;
        root.verificationInfo.creationTime = netUlTransferData.creationTime;
        (new IwClientInit(yioGdxGame, LoadingType.verification)).perform(netUlTransferData.levelCode);
    }


    public void setValue(String value) {
        this.value = value;
    }
}
