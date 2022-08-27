package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetRenamingData;

public class AprCheckRenaming extends AbstractPostponedReaction{

    String value;


    public AprCheckRenaming(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        return !areNetWaitScenesMovingCurrently();
    }


    @Override
    void apply() {
        NetRenamingData netRenamingData = new NetRenamingData();
        netRenamingData.decode(value);
        Scenes.checkRenaming.setNetRenamingData(netRenamingData);
        Scenes.checkRenaming.create();
    }


    public void setValue(String value) {
        this.value = value;
    }
}
