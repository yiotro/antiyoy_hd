package yio.tro.onliyoy;

public interface IBillingManagerYio {


    void launch();


    void finish();


    void showPurchaseDialog(String productId);


    void onProductConsumed(String token);


    void restorePurchases();


    void launchAndRestorePurchases();

}
