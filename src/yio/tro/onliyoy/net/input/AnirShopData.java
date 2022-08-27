package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetShopData;

public class AnirShopData extends AbstractNetInputReaction{

    @Override
    public void apply() {
        NetShopData netShopData = new NetShopData();
        netShopData.decode(value);
        if (Scenes.shop.isCurrentlyVisible()) {
            Scenes.shop.onShopDataReceived(netShopData);
        }
    }
}
