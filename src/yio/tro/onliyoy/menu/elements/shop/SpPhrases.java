package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.*;
import yio.tro.onliyoy.stuff.calendar.CveColorYio;

import java.util.Map;

public class SpPhrases extends AbstractShopListPage{

    public SpPhrases(ShopViewElement shopViewElement) {
        super(shopViewElement);
    }


    @Override
    public ShopPageType getType() {
        return ShopPageType.phrases;
    }


    @Override
    public CveColorYio getAccentColor() {
        return CveColorYio.purple;
    }


    @Override
    void onClickedOnItem(AbstractIslItem islItem) {
        switch (islItem.statusValue) {
            default:
                System.out.println("SpPhrases.onClickedOnItem: problem");
                break;
            case unknown:
                break;
            case available:
                Scenes.confirmPurchase.netShopQueryData.productType = NetSqProductType.phrase;
                Scenes.confirmPurchase.netShopQueryData.key = islItem.key;
                Scenes.confirmPurchase.price = islItem.priceValue;
                Scenes.confirmPurchase.create();
                break;
            case purchased:
                sendShopQuery(NetSqProductType.phrase, islItem.key, NetSqActionType.activate);
                break;
            case activated:
                sendShopQuery(NetSqProductType.phrase, islItem.key, NetSqActionType.deactivate);
                break;
        }
    }


    @Override
    void onShopDataReceived(NetShopData netShopData) {
        internalShopList.clear();
        for (Map.Entry<PhraseType, Integer> entry : netShopData.mapPhrases.entrySet()) {
            IslPhraseItem islPhraseItem = new IslPhraseItem(internalShopList);
            islPhraseItem.setName(LanguagesManager.getInstance().getString("" + entry.getKey()));
            islPhraseItem.setKey("" + entry.getKey());
            islPhraseItem.setPrice(entry.getValue());
            internalShopList.addItem(islPhraseItem);
        }
        internalShopList.onContentsChangedDynamically();
    }


    @Override
    void loadStatuses(NetPurchasesData netPurchasesData, NetCustomizationData customizationData) {
        for (AbstractIslItem item : internalShopList.items) {
            item.setStatusValue(IsliStatus.available);
        }
        for (PhraseType phraseType : netPurchasesData.phrases) {
            AbstractIslItem item = internalShopList.getItem("" + phraseType);
            if (item == null) continue;
            item.setStatusValue(IsliStatus.purchased);
        }
        for (PhraseType phraseType : customizationData.phrases) {
            AbstractIslItem item = internalShopList.getItem("" + phraseType);
            if (item == null) continue;
            item.setStatusValue(IsliStatus.activated);
        }
    }


    @Override
    public boolean isMaskingAlwaysRequired() {
        return true;
    }
}
