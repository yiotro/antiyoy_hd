package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.stuff.*;

public abstract class AbstractIslItem {

    InternalShopList internalShopList;
    public RectangleYio viewPosition;
    float deltaY;
    public String key;
    public RenderableTextYio nameViewText;
    public SelectionEngineYio selectionEngineYio;
    public int priceValue;
    public CircleYio fishPosition;
    public RenderableTextYio priceViewText;
    public IsliStatus statusValue;
    public boolean darken;
    public RectangleYio coverPosition;
    public SelfScrollWorkerYio selfScrollWorkerYio;


    public AbstractIslItem(InternalShopList internalShopList) {
        this.internalShopList = internalShopList;
        deltaY = 0;
        viewPosition = new RectangleYio();
        viewPosition.height = getHeight();
        initNameViewText();
        key = "";
        selectionEngineYio = new SelectionEngineYio();
        fishPosition = new CircleYio();
        fishPosition.setRadius(0.012f * GraphicsYio.height);
        statusValue = IsliStatus.unknown;
        darken = false;
        coverPosition = new RectangleYio();
        selfScrollWorkerYio = new SelfScrollWorkerYio();
        initPriceViewText();
    }


    private void initPriceViewText() {
        priceViewText = new RenderableTextYio();
        priceViewText.setFont(Fonts.miniFont);
        setPrice(0);
    }


    private void initNameViewText() {
        nameViewText = new RenderableTextYio();
        nameViewText.setFont(Fonts.gameFont);
        setName("-");
    }


    void move() {
        updateViewPosition();
        updateNameTextPosition();
        moveSelection();
        updatePriceTextPosition();
        updateFishPosition();
        updateCoverPosition();
        selfScrollWorkerYio.move();
    }


    private void updateCoverPosition() {
        coverPosition.setBy(viewPosition);
        coverPosition.width = viewPosition.width - (fishPosition.center.x - fishPosition.radius - 0.03f * GraphicsYio.width);
        coverPosition.x = viewPosition.x + viewPosition.width - coverPosition.width;
    }


    void onAppear() {

    }


    void onContentsChangedDynamically() {
        float nameWidthLimit = 0.85f * GraphicsYio.width;
        selfScrollWorkerYio.launch(nameViewText.width, nameWidthLimit - 0.08f * GraphicsYio.width);
    }


    public boolean isStatusCurrentlyVisible() {
        switch (statusValue) {
            default:
            case available:
            case unknown:
                return false;
            case activated:
            case purchased:
                return true;
        }
    }


    private void updateFishPosition() {
        fishPosition.center.x = priceViewText.position.x - 0.01f * GraphicsYio.width - fishPosition.radius;
        fishPosition.center.y = priceViewText.position.y - priceViewText.height / 2;
    }


    private void updatePriceTextPosition() {
        priceViewText.position.x = viewPosition.x + viewPosition.width - 0.04f * GraphicsYio.width - priceViewText.width;
        priceViewText.position.y = nameViewText.position.y - nameViewText.height / 2 + priceViewText.height / 2;
        priceViewText.updateBounds();
    }


    private void moveSelection() {
        if (internalShopList.page.shopViewElement.touched) return;
        selectionEngineYio.move();
    }


    protected void updateNameTextPosition() {
        nameViewText.position.x = viewPosition.x + 0.04f * GraphicsYio.width + selfScrollWorkerYio.getDelta();
        nameViewText.position.y = viewPosition.y + viewPosition.height / 2 + nameViewText.height / 2;
        nameViewText.updateBounds();
    }


    public boolean isTouchedBy(PointYio touchPoint) {
        return viewPosition.isPointInside(touchPoint);
    }


    private void updateViewPosition() {
        RectangleYio pos = internalShopList.position;
        viewPosition.width = pos.width;
        viewPosition.x = pos.x;
        viewPosition.y = pos.y + pos.height - deltaY + internalShopList.hook;
    }


    void setName(String string) {
        nameViewText.setString(string);
        nameViewText.updateMetrics();
    }


    public void setDeltaY(float deltaY) {
        this.deltaY = deltaY;
    }


    public void setHeight(double h) {
        viewPosition.height = (float) (h * GraphicsYio.height);
    }


    public void setPrice(int priceValue) {
        this.priceValue = priceValue;
        priceViewText.setString("" + priceValue);
        priceViewText.updateMetrics();
    }


    public void setKey(String key) {
        this.key = key;
    }


    public void setStatusValue(IsliStatus statusValue) {
        if (this.statusValue == statusValue) return;
        this.statusValue = statusValue;
        if (statusValue == IsliStatus.purchased || statusValue == IsliStatus.activated) {
            priceViewText.setString(LanguagesManager.getInstance().getString("" + statusValue));
            priceViewText.updateMetrics();
        }
    }


    public boolean isCurrentlyVisible() {
        return viewPosition.intersects(internalShopList.position);
    }


    protected abstract float getHeight();


    public void setDarken(boolean darken) {
        this.darken = darken;
    }
}
