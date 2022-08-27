package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.OneTimeInfo;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NetShopQueryData;
import yio.tro.onliyoy.net.shared.NetSqActionType;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneConfirmPurchase extends AbstractConfirmationScene{

    public NetShopQueryData netShopQueryData;
    public int price;


    public SceneConfirmPurchase() {
        netShopQueryData = new NetShopQueryData();
    }


    @Override
    protected Reaction getNoReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
            }
        };
    }


    @Override
    protected Reaction getYesReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                sendPurchaseMessage();
                sendActivationMessage();
                checkToShowHintActivate();
            }
        };
    }


    private void sendPurchaseMessage() {
        netShopQueryData.actionType = NetSqActionType.purchase;
        netRoot.sendMessage(NmType.shop_query, netShopQueryData.encode());
    }


    private void sendActivationMessage() {
        // toast doesn't help
        // so it would be better to just automatically activate items
        netShopQueryData.actionType = NetSqActionType.activate;
        netRoot.sendMessage(NmType.shop_query, netShopQueryData.encode());
    }


    private void checkToShowHintActivate() {
        if (OneTimeInfo.getInstance().hintActivateItem) return;
        OneTimeInfo.getInstance().hintActivateItem = true;
        OneTimeInfo.getInstance().save();
        Scenes.toast.show("hint_activate");
    }


    @Override
    protected String getQuestionKey() {
        String string = languagesManager.getString("confirm_purchase");
        String item = languagesManager.getString(netShopQueryData.key);
        if (item.length() > 14) {
            item = item.substring(0, 13) + "..";
        }
        string = string.replace("[item]", "'" + item + "'");
        string = string.replace("[price]", price + "f");
        return string;
    }
}
