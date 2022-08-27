package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.FishProductItem;
import yio.tro.onliyoy.menu.elements.customizable_list.TitleListItem;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.SfbProduct;

import java.util.ArrayList;

public class SceneChooseFishToBuy extends ModalSceneYio{


    private CustomizableListYio customizableListYio;
    private double h;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        h = 0.4;
        createDefaultPanel(h);
        createList();
        createRestorePurchasesButton();
    }


    private void createRestorePurchasesButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.8, 0.05)
                .centerHorizontal()
                .alignBottom(0.01)
                .setBackground(BackgroundYio.gray)
                .setFont(Fonts.miniFont)
                .applyText("restore_purchases")
                .setReaction(getRestorePurchasesReaction());
    }


    private Reaction getRestorePurchasesReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.billingManager.restorePurchases();
            }
        };
    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setParent(defaultPanel)
                .setCornerRadius(0)
                .setSize(0.95, h - 0.07)
                .centerHorizontal()
                .alignTop(0.01);
    }


    public void loadValues(ArrayList<SfbProduct> products) {
        customizableListYio.clearItems();
        boolean darken = true;
        addTitleListItem();
        for (SfbProduct sfbProduct : products) {
            FishProductItem fishProductItem = new FishProductItem();
            fishProductItem.setValues(sfbProduct);
            fishProductItem.setDarken(darken);
            darken = !darken;
            customizableListYio.addItem(fishProductItem);
        }
    }


    private void addTitleListItem() {
        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle(languagesManager.getString("buy_fish"));
        customizableListYio.addItem(titleListItem);
    }


    @Override
    protected Reaction getCloseReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                yioGdxGame.billingManager.finish(); // close connection
            }
        };
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
