package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.net.shared.NetRepTransferData;

public class AprCheckReport extends AbstractPostponedReaction{

    String value;


    public AprCheckReport(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return !areNetWaitScenesMovingCurrently();
    }


    @Override
    void apply() {
        NetRepTransferData netRepTransferData = new NetRepTransferData();
        netRepTransferData.decode(value);
        root.verificationInfo.name = netRepTransferData.name;
        root.verificationInfo.report = netRepTransferData.message;
        root.verificationInfo.levelId = netRepTransferData.levelId;
        (new IwClientInit(yioGdxGame, LoadingType.report)).perform(netRepTransferData.levelCode);
    }


    public void setValue(String value) {
        this.value = value;
    }
}
