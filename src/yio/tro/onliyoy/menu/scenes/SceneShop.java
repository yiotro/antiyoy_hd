package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.IBillingManagerYio;
import yio.tro.onliyoy.OneTimeInfo;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.net.FishNameViewElement;
import yio.tro.onliyoy.menu.elements.shop.ShopViewElement;
import yio.tro.onliyoy.net.BillingBuffer;
import yio.tro.onliyoy.net.postpone.PostponedReactionsManager;
import yio.tro.onliyoy.net.shared.NetPurchasesData;
import yio.tro.onliyoy.net.shared.NetShopData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneShop extends SceneYio{


    public FishNameViewElement fishNameViewElement;
    public ShopViewElement shopViewElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected void initialize() {
        createShopViewElement();
        createFishNameViewElement();
        spawnBackButton(getOpenSceneReaction(Scenes.mainLobby));
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        netRoot.sendMessage(NmType.get_shop_data, "");
        netRoot.sendMessage(NmType.get_purchases_data, "");
        checkToRestorePurchases();
        tagHintProfileAsCompleted();
        PostponedReactionsManager.aprHintTabsInCustomization.launch();
    }


    private void tagHintProfileAsCompleted() {
        if (OneTimeInfo.getInstance().hintProfile) return; // already tagged
        OneTimeInfo.getInstance().hintProfile = true;
        OneTimeInfo.getInstance().save();
    }


    private void checkToRestorePurchases() {
        Preferences preferences = Gdx.app.getPreferences(BillingBuffer.PREFS);
        if (!preferences.getBoolean("force")) return;
        preferences.putBoolean("force", false);
        preferences.flush();
        IBillingManagerYio billingManager = yioGdxGame.billingManager;
        billingManager.launchAndRestorePurchases();
    }


    public void onShopDataReceived(NetShopData netShopData) {
        if (shopViewElement == null) return;
        shopViewElement.onShopDataReceived(netShopData);
    }


    public void onPurchasesDataReceived(NetPurchasesData netPurchasesData) {
        if (shopViewElement == null) return;
        shopViewElement.onPurchasesDataReceived(netPurchasesData);
    }


    public void onCustomizationDataUpdated() {
        if (shopViewElement == null) return;
        shopViewElement.onCustomizationDataUpdated();
    }


    private void createShopViewElement() {
        shopViewElement = uiFactory.getShopViewElement()
                .setSize(1, 1)
                .setAnimation(AnimationYio.up);
    }


    public void onUserDataUpdated() {
        if (fishNameViewElement == null) return;
        fishNameViewElement.loadValues();
    }


    private void createFishNameViewElement() {
        fishNameViewElement = uiFactory.getFishNameViewElement()
                .setSize(0.06)
                .alignRight(0)
                .alignTop(0)
                .setAnimation(AnimationYio.up);
    }
}
