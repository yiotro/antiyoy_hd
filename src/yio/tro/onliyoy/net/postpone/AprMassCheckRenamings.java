package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetMassRenamingData;

public class AprMassCheckRenamings extends AbstractPostponedReaction {

    String value;


    public AprMassCheckRenamings(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return !areNetWaitScenesMovingCurrently();
    }


    @Override
    void apply() {
        NetMassRenamingData netMassRenamingData = new NetMassRenamingData();
        netMassRenamingData.decode(value);
        Scenes.massCheckRenamings.setNetMassRenamingData(netMassRenamingData);
        Scenes.massCheckRenamings.create();
    }


    public void setValue(String value) {
        this.value = value;
    }
}
