package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.*;
import yio.tro.onliyoy.stuff.calendar.CveColorYio;

import java.util.Map;

public class SpSkins extends AbstractShopListPage{

    public SpSkins(ShopViewElement shopViewElement) {
        super(shopViewElement);
    }


    @Override
    public ShopPageType getType() {
        return ShopPageType.skins;
    }


    @Override
    public CveColorYio getAccentColor() {
        return CveColorYio.aqua;
    }


    @Override
    void onShopDataReceived(NetShopData netShopData) {
        internalShopList.clear();
        for (Map.Entry<SkinType, Integer> entry : netShopData.mapSkins.entrySet()) {
            IslSkinItem islSkinItem = new IslSkinItem(internalShopList, entry.getKey());
            String string = LanguagesManager.getInstance().getString("" + entry.getKey());
            islSkinItem.setName(Yio.getCapitalizedString(string));
            islSkinItem.setPrice(entry.getValue());
            islSkinItem.setKey("" + entry.getKey());
            internalShopList.addItem(islSkinItem);
        }
        internalShopList.onContentsChangedDynamically();
    }


    @Override
    void loadStatuses(NetPurchasesData netPurchasesData, NetCustomizationData customizationData) {
        for (AbstractIslItem item : internalShopList.items) {
            item.setStatusValue(IsliStatus.available);
        }
        for (SkinType skinType : netPurchasesData.skins) {
            AbstractIslItem item = internalShopList.getItem("" + skinType);
            if (item == null) continue;
            item.setStatusValue(IsliStatus.purchased);
        }
        AbstractIslItem item = internalShopList.getItem("" + customizationData.skinType);
        if (item != null) {
            item.setStatusValue(IsliStatus.activated);
        }
    }


    @Override
    void onClickedOnItem(AbstractIslItem islItem) {
        switch (islItem.statusValue) {
            default:
                System.out.println("SpSkins.onClickedOnItem: problem");
                break;
            case unknown:
                break;
            case available:
                Scenes.confirmPurchase.netShopQueryData.productType = NetSqProductType.skin;
                Scenes.confirmPurchase.netShopQueryData.key = islItem.key;
                Scenes.confirmPurchase.price = islItem.priceValue;
                Scenes.confirmPurchase.create();
                break;
            case purchased:
                sendShopQuery(NetSqProductType.skin, islItem.key, NetSqActionType.activate);
                break;
            case activated:
                sendShopQuery(NetSqProductType.skin, islItem.key, NetSqActionType.deactivate);
                break;
        }
    }
}
