package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.net.shared.NetCustomizationData;
import yio.tro.onliyoy.net.shared.NetPurchasesData;
import yio.tro.onliyoy.net.shared.NetShopData;
import yio.tro.onliyoy.stuff.calendar.CveColorYio;

public class SpSampleList extends AbstractShopListPage{

    public SpSampleList(ShopViewElement shopViewElement) {
        super(shopViewElement);
    }


    @Override
    public ShopPageType getType() {
        return ShopPageType.sample_list;
    }


    @Override
    public CveColorYio getAccentColor() {
        return CveColorYio.purple;
    }


    @Override
    public String getNameKey() {
        return "Samples";
    }


    @Override
    void onShopDataReceived(NetShopData netShopData) {

    }


    @Override
    void loadStatuses(NetPurchasesData netPurchasesData, NetCustomizationData customizationData) {

    }


    @Override
    void onAppear() {
        super.onAppear();
        internalShopList.clear();
        for (int i = 0; i < 15; i++) {
            IslSampleItem sampleItem = new IslSampleItem(internalShopList);
            sampleItem.setKey("item_" + (i + 1));
            sampleItem.setName("Item " + (i + 1));
            internalShopList.addItem(sampleItem);
        }
        internalShopList.onContentsChangedDynamically();
    }


    @Override
    void onClickedOnItem(AbstractIslItem islItem) {
        System.out.println("SpSampleList.onClickedOnItem");
    }
}
