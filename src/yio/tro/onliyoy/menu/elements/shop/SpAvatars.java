package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.*;
import yio.tro.onliyoy.stuff.calendar.CveColorYio;

import java.util.Map;

public class SpAvatars extends AbstractShopListPage {

    public SpAvatars(ShopViewElement shopViewElement) {
        super(shopViewElement);
    }


    @Override
    public ShopPageType getType() {
        return ShopPageType.avatars;
    }


    @Override
    public CveColorYio getAccentColor() {
        return CveColorYio.cyan;
    }


    @Override
    void onShopDataReceived(NetShopData netShopData) {
        internalShopList.clear();
        for (Map.Entry<AvatarType, Integer> entry : netShopData.mapAvatars.entrySet()) {
            IslAvatarItem islAvatarItem = new IslAvatarItem(internalShopList);
            islAvatarItem.setName("");
            islAvatarItem.setAvatarType(entry.getKey());
            islAvatarItem.setKey("" + entry.getKey());
            islAvatarItem.setPrice(entry.getValue());
            internalShopList.addItem(islAvatarItem);
        }
        internalShopList.onContentsChangedDynamically();
    }


    @Override
    void loadStatuses(NetPurchasesData netPurchasesData, NetCustomizationData customizationData) {
        for (AbstractIslItem item : internalShopList.items) {
            item.setStatusValue(IsliStatus.available);
        }
        for (AvatarType avatarType : netPurchasesData.avatars) {
            AbstractIslItem item = internalShopList.getItem("" + avatarType);
            if (item == null) continue;
            item.setStatusValue(IsliStatus.purchased);
        }
        AbstractIslItem item = internalShopList.getItem("" + customizationData.avatarType);
        if (item != null) {
            item.setStatusValue(IsliStatus.activated);
        }
    }


    @Override
    void onClickedOnItem(AbstractIslItem islItem) {
        switch (islItem.statusValue) {
            default:
                System.out.println("SpAvatars.onClickedOnItem: problem");
                break;
            case unknown:
                break;
            case available:
                Scenes.confirmPurchase.netShopQueryData.productType = NetSqProductType.avatar;
                Scenes.confirmPurchase.netShopQueryData.key = islItem.key;
                Scenes.confirmPurchase.price = islItem.priceValue;
                Scenes.confirmPurchase.create();
                break;
            case purchased:
                sendShopQuery(NetSqProductType.avatar, islItem.key, NetSqActionType.activate);
                break;
            case activated:
                sendShopQuery(NetSqProductType.avatar, islItem.key, NetSqActionType.deactivate);
                break;
        }
    }
}
