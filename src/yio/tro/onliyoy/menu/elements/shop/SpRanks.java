package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.*;
import yio.tro.onliyoy.stuff.calendar.CveColorYio;

import java.util.Map;

public class SpRanks extends AbstractShopListPage{

    public SpRanks(ShopViewElement shopViewElement) {
        super(shopViewElement);
    }


    @Override
    public ShopPageType getType() {
        return ShopPageType.ranks;
    }


    @Override
    public CveColorYio getAccentColor() {
        return CveColorYio.yellow;
    }


    @Override
    void onShopDataReceived(NetShopData netShopData) {
        internalShopList.clear();
        for (Map.Entry<RankType, Integer> entry : netShopData.mapRanks.entrySet()) {
            IslRankItem islRankItem = new IslRankItem(internalShopList);
            islRankItem.setName(getName(entry));
            islRankItem.setKey("" + entry.getKey());
            islRankItem.setPrice(entry.getValue());
            internalShopList.addItem(islRankItem);
        }
        internalShopList.onContentsChangedDynamically();
    }


    private String getName(Map.Entry<RankType, Integer> entry) {
        switch (entry.getKey()) {
            default:
                String string = LanguagesManager.getInstance().getString("" + entry.getKey());
                return Yio.getCapitalizedString(string);
            case elp:
                return "ELP";
        }
    }


    @Override
    void loadStatuses(NetPurchasesData netPurchasesData, NetCustomizationData customizationData) {
        for (AbstractIslItem item : internalShopList.items) {
            item.setStatusValue(IsliStatus.available);
        }
        for (RankType rankType : netPurchasesData.ranks) {
            AbstractIslItem item = internalShopList.getItem("" + rankType);
            if (item == null) continue;
            item.setStatusValue(IsliStatus.purchased);
        }
        AbstractIslItem item = internalShopList.getItem("" + customizationData.rankType);
        if (item != null) {
            item.setStatusValue(IsliStatus.activated);
        }
    }


    @Override
    void onClickedOnItem(AbstractIslItem islItem) {
        switch (islItem.statusValue) {
            default:
                System.out.println("SpRanks.onClickedOnItem: problem");
                break;
            case unknown:
                break;
            case available:
                Scenes.confirmPurchase.netShopQueryData.productType = NetSqProductType.rank;
                Scenes.confirmPurchase.netShopQueryData.key = islItem.key;
                Scenes.confirmPurchase.price = islItem.priceValue;
                Scenes.confirmPurchase.create();
                break;
            case purchased:
                sendShopQuery(NetSqProductType.rank, islItem.key, NetSqActionType.activate);
                break;
            case activated:
                sendShopQuery(NetSqProductType.rank, islItem.key, NetSqActionType.deactivate);
                break;
        }
    }
}
