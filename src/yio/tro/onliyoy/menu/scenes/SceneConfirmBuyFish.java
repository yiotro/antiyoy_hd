package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.SfbProduct;

public class SceneConfirmBuyFish extends AbstractConfirmationScene{

    // this scene is not used currently
    SfbProduct sfbProduct;
    String price;


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
                yioGdxGame.billingManager.showPurchaseDialog(sfbProduct.id);
            }
        };
    }


    @Override
    protected String getQuestionKey() {
        String buy = languagesManager.getString("buy");
        int fishAmount = sfbProduct.getFishAmount();
        return buy + " " + fishAmount + "f (" + price + ")?";
    }


    public void setSfbProduct(SfbProduct sfbProduct) {
        this.sfbProduct = sfbProduct;
    }


    public void setPrice(String price) {
        this.price = price;
    }
}
