package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.OneTimeInfo;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class AprHintTabsInCustomization extends AbstractPostponedReaction{

    public AprHintTabsInCustomization(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        if (OneTimeInfo.getInstance().tabsInCustomization) return false;
        if (!Scenes.shop.isCurrentlyVisible()) return false;
        if (Scenes.shop.shopViewElement.getFactor().getValue() < 1) return false;
        return true;
    }


    @Override
    void apply() {
        Scenes.forefinger.create();
        Scenes.forefinger.forefinger.setTarget(Scenes.shop.shopViewElement, "");
        OneTimeInfo.getInstance().tabsInCustomization = true;
        OneTimeInfo.getInstance().save();
    }
}
