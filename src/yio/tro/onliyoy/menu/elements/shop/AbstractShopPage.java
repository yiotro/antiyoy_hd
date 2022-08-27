package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.net.shared.*;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.calendar.CveColorYio;

public abstract class AbstractShopPage {

    ShopViewElement shopViewElement;
    public RectangleYio viewPosition;
    public int index;
    float nameX;
    float namePreviousX;
    float nameTargetX;
    public RenderableTextYio nameViewText;
    public SelectionEngineYio selectionEngineYio;
    public RectangleYio selectionViewBounds;
    private float rightVisualLimit;
    private float leftVisualLimit;
    public RectangleYio maskPosition;


    public AbstractShopPage(ShopViewElement shopViewElement) {
        this.shopViewElement = shopViewElement;
        viewPosition = new RectangleYio();
        index = -1;
        nameViewText = null;
        selectionEngineYio = new SelectionEngineYio();
        selectionViewBounds = new RectangleYio();
        rightVisualLimit = 1.05f * GraphicsYio.width;
        leftVisualLimit = -0.05f * GraphicsYio.width;
        maskPosition = new RectangleYio();
    }


    private void initNameViewText() {
        if (nameViewText != null) return;
        nameViewText = new RenderableTextYio();
        nameViewText.setFont(Fonts.gameFont);
        nameViewText.setString(LanguagesManager.getInstance().getString(getNameKey()));
        nameViewText.updateMetrics();
    }


    void move() {
        updateViewPosition();
        updateMaskPosition();
        moveNameX();
        updateNameViewTextPosition();
        updateSelectionViewBounds();
        selectionEngineYio.move();
        onMove();
    }


    private void updateMaskPosition() {
        maskPosition.setBy(viewPosition);
        maskPosition.height *= 2;
    }


    private void updateSelectionViewBounds() {
        selectionViewBounds.setBy(nameViewText.bounds);
        selectionViewBounds.increase(0.01f * GraphicsYio.width);
    }


    private void moveNameX() {
        nameX = namePreviousX + shopViewElement.swipeFactor.getValue() * (nameTargetX - namePreviousX);
    }


    private void updateNameViewTextPosition() {
        nameViewText.centerVertical(shopViewElement.tabsArea);
        nameViewText.position.x = nameX - nameViewText.width / 2;
        nameViewText.updateBounds();
    }


    void updateViewPosition() {
        viewPosition.setBy(shopViewElement.pageArea);
        viewPosition.x = shopViewElement.getViewPosition().x + index * shopViewElement.pageArea.width + shopViewElement.hook;
    }


    public String getNameKey() {
        return "" + getType();
    }


    public abstract ShopPageType getType();


    public abstract CveColorYio getAccentColor();


    void onAppear() {
        initNameViewText();
        nameX = 0;
        onGoToEvent(0);
        namePreviousX = nameTargetX;
    }


    abstract void onShopDataReceived(NetShopData netShopData);


    abstract void loadStatuses(NetPurchasesData netPurchasesData, NetCustomizationData customizationData);


    abstract void onMove();


    abstract void onTouchDown(PointYio touchPoint);


    abstract void onTouchDrag(PointYio touchPoint);


    abstract void onTouchUp(PointYio touchPoint);


    abstract void onClick(PointYio touchPoint);


    public boolean isMaskingAlwaysRequired() {
        return false;
    }


    public boolean onMouseWheelScrolled(int amount) {
        return false;
    }


    void onGoToEvent(int tabIndex) {
        namePreviousX = nameX;
        float temporaryRelativeIndex = tabIndex - index;
        nameTargetX = GraphicsYio.width / 2 - 0.45f * GraphicsYio.width * temporaryRelativeIndex;
    }


    public boolean isCurrentlyVisible() {
        if (viewPosition.x >= GraphicsYio.width) return false;
        if (viewPosition.x + viewPosition.width <= 0) return false;
        return true;
    }


    public boolean isNameVisible() {
        if (nameViewText.position.x > rightVisualLimit) return false;
        if (nameViewText.position.x + nameViewText.width < leftVisualLimit) return false;
        return true;
    }


    protected void sendShopQuery(NetSqProductType productType, String key, NetSqActionType actionType) {
        NetShopQueryData netShopQueryData = new NetShopQueryData();
        netShopQueryData.productType = productType;
        netShopQueryData.key = key;
        netShopQueryData.actionType = actionType;
        shopViewElement.getNetRoot().sendMessage(NmType.shop_query, netShopQueryData.encode());
    }


    public void setIndex(int index) {
        this.index = index;
    }
}
