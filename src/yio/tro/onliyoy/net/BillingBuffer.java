package yio.tro.onliyoy.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.net.shared.SfbProduct;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class BillingBuffer {

    public static String PREFS = "yio.tro.onliyoy.restore_purchases";;
    YioGdxGame yioGdxGame;
    private ArrayList<BbaItem> items;
    private ObjectPoolYio<BbaItem> poolItems;


    public BillingBuffer(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        items = new ArrayList<>();
        initPools();
    }


    private void initPools() {
        poolItems = new ObjectPoolYio<BbaItem>(items) {
            @Override
            public BbaItem makeNewObject() {
                return new BbaItem();
            }
        };
    }


    public synchronized void addItem(BbaType type, Object object) {
        BbaItem bbaItem = poolItems.getFreshObject();
        bbaItem.setType(type);
        bbaItem.setObject(object);
        items.add(bbaItem);
    }


    public synchronized void move() {
        if (items.size() == 0) return;
        while (items.size() > 0) {
            BbaItem bbaItem = items.get(0);
            poolItems.removeFromExternalList(bbaItem);
            apply(bbaItem);
        }
    }


    private void apply(BbaItem bbaItem) {
        switch (bbaItem.type) {
            default:
                System.out.println("BillingBuffer.apply: action not specified");
                break;
            case notification:
                String key = (String) bbaItem.object;
                Scenes.notification.show(key);
                break;
            case choose_fish_to_buy:
                ArrayList<SfbProduct> list = (ArrayList<SfbProduct>) bbaItem.object;
                Scenes.chooseFishToBuy.create();
                Scenes.chooseFishToBuy.loadValues(list);
                break;
            case send_message_on_purchases_updated:
                String message = (String) bbaItem.object;
                yioGdxGame.netRoot.sendMessage(NmType.billing_on_purchases_updated, message);
                break;
            case notify_about_pending:
                Scenes.notification.show("purchase_pending");
                activateRestorePurchasesOnNextShopEntry();
                break;
        }
    }


    private void activateRestorePurchasesOnNextShopEntry() {
        Preferences preferences = Gdx.app.getPreferences(PREFS);
        preferences.putBoolean("force", true);
        preferences.flush();
    }
}
