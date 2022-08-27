package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetPurchasesData;

public class AnirPurchasesData extends AbstractNetInputReaction{

    @Override
    public void apply() {
        NetPurchasesData netPurchasesData = new NetPurchasesData();
        netPurchasesData.decode(value);
        if (Scenes.shop.isCurrentlyVisible()) {
            Scenes.shop.onPurchasesDataReceived(netPurchasesData);
        }
    }
}
