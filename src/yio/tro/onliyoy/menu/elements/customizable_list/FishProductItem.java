package yio.tro.onliyoy.menu.elements.customizable_list;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.BillingBuffer;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.SfbProduct;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.IconTextYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class FishProductItem extends AbstractCustomListItem{

    public SfbProduct sfbProduct;
    public IconTextYio iconTextYio;
    public RenderableTextYio priceViewText;
    public boolean darken;
    private String processedPriceString;


    @Override
    protected void initialize() {
        sfbProduct = null;
        darken = false;
        iconTextYio = new IconTextYio();
        iconTextYio.setFont(Fonts.miniFont);
        iconTextYio.setIconEnabled(true);
        iconTextYio.setOffset(0.01f * GraphicsYio.width);
        priceViewText = new RenderableTextYio();
        priceViewText.setFont(Fonts.miniFont);
    }


    public void setValues(SfbProduct sfbProduct) {
        this.sfbProduct = sfbProduct;
        iconTextYio.setString("" + sfbProduct.getFishAmount());
        iconTextYio.updateMetrics();
        updateProcessedPriceString();
        priceViewText.setString(processedPriceString);
        priceViewText.updateMetrics();
    }


    private void updateProcessedPriceString() {
        String localizedPrice = CharLocalizerYio.getInstance().apply(sfbProduct.price);
        processedPriceString = localizedPrice.replaceAll("#", " ");
    }


    public void setDarken(boolean darken) {
        this.darken = darken;
    }


    @Override
    protected void move() {
        updateIconTextPosition();
        updatePriceViewTextPosition();
    }


    private void updatePriceViewTextPosition() {
        priceViewText.centerVertical(viewPosition);
        priceViewText.position.x = viewPosition.x + viewPosition.width - 0.03f * GraphicsYio.width - priceViewText.width;
        priceViewText.updateBounds();
    }


    private void updateIconTextPosition() {
        iconTextYio.centerVertical(viewPosition);
        iconTextYio.position.x = viewPosition.x + 0.03f * GraphicsYio.width;
        iconTextYio.onPositionChanged();
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return 0.07f * GraphicsYio.height;
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    protected void onClicked() {
        Scenes.chooseFishToBuy.destroy();
        YioGdxGame yioGdxGame = customizableListYio.menuControllerYio.yioGdxGame;
        yioGdxGame.billingManager.showPurchaseDialog(sfbProduct.id);
        activateRestorePurchasesOnNextShopEntry();
    }


    private void activateRestorePurchasesOnNextShopEntry() {
        Preferences preferences = Gdx.app.getPreferences(BillingBuffer.PREFS);
        preferences.putBoolean("force", true);
        preferences.flush();
    }


    @Override
    protected void onLongTapped() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderFishProductItem;
    }
}
