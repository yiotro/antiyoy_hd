package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.*;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetCustomizationData;
import yio.tro.onliyoy.net.shared.NetPurchasesData;
import yio.tro.onliyoy.net.shared.NetShopData;
import yio.tro.onliyoy.net.shared.NetUserData;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.calendar.CveColorYio;

public class SpExchange extends AbstractShopPage{

    public RenderableTextYio title;
    public RenderableTextYio fishViewText;
    public RenderableTextYio coinViewText;
    public CircleYio fishIconPosition;
    public CircleYio coinIconPosition;
    private float tempWidth;
    private float tempValue;
    public SpeLocalButton exchangeButton;
    public SpeLocalButton buyFishButton;
    int price;
    private float iOffset;


    public SpExchange(ShopViewElement shopViewElement) {
        super(shopViewElement);
        initTitle();
        initCoinViewText();
        initFishViewText();
        initFishIconPosition();
        initCoinIconPosition();
        initButtons();
        price = -1;
        iOffset = 0.005f * GraphicsYio.width;
    }


    private void initButtons() {
        exchangeButton = new SpeLocalButton(this);
        exchangeButton.initTitle("do_exchange", Fonts.gameFont);
        exchangeButton.setSize(0.6, 0.055);
        buyFishButton = new SpeLocalButton(this);
        buyFishButton.initTitle("buy_fish", Fonts.miniFont);
        buyFishButton.setSize(0.45, 0.045);
    }


    private void initCoinIconPosition() {
        coinIconPosition = new CircleYio();
        coinIconPosition.setRadius(fishIconPosition.radius);
    }


    private void initFishIconPosition() {
        fishIconPosition = new CircleYio();
        fishIconPosition.setRadius(0.012f * GraphicsYio.height);
    }


    private void initFishViewText() {
        coinViewText = new RenderableTextYio();
        coinViewText.setFont(Fonts.miniFont);
        coinViewText.setString("-");
        coinViewText.updateMetrics();
    }


    private void initCoinViewText() {
        fishViewText = new RenderableTextYio();
        fishViewText.setFont(Fonts.miniFont);
        fishViewText.setString("1 = ");
        fishViewText.updateMetrics();
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        title.setString(LanguagesManager.getInstance().getString("current_exchange_rate"));
        title.updateMetrics();
    }


    @Override
    public ShopPageType getType() {
        return ShopPageType.exchange;
    }


    @Override
    public CveColorYio getAccentColor() {
        return CveColorYio.green;
    }


    @Override
    void onShopDataReceived(NetShopData netShopData) {
        price = netShopData.fishPrice;
        coinViewText.setString(Yio.getCompactValueString(price));
        coinViewText.updateMetrics();
    }


    @Override
    void loadStatuses(NetPurchasesData netPurchasesData, NetCustomizationData customizationData) {

    }


    @Override
    void onMove() {
        updateTitlePosition();
        updateExchangeRatePosition();
        moveButtons();
    }


    private void moveButtons() {
        exchangeButton.viewPosition.x = viewPosition.x + viewPosition.width / 2 - exchangeButton.viewPosition.width / 2;
        exchangeButton.viewPosition.y = coinIconPosition.center.y - 0.1f * GraphicsYio.height;
        exchangeButton.move();
        buyFishButton.viewPosition.x = viewPosition.x + viewPosition.width / 2 - buyFishButton.viewPosition.width / 2;
        buyFishButton.viewPosition.y = viewPosition.y + 0.03f * GraphicsYio.height;
        buyFishButton.move();
    }


    @Override
    void onTouchDown(PointYio touchPoint) {
        if (exchangeButton.isTouchedBy(touchPoint)) {
            exchangeButton.selectionEngineYio.applySelection();
            SoundManager.playSound(SoundType.button);
        }
        if (buyFishButton.isTouchedBy(touchPoint)) {
            buyFishButton.selectionEngineYio.applySelection();
            SoundManager.playSound(SoundType.button);
        }
    }


    @Override
    void onTouchDrag(PointYio touchPoint) {

    }


    @Override
    void onTouchUp(PointYio touchPoint) {

    }


    @Override
    void onClick(PointYio touchPoint) {
        if (exchangeButton.isTouchedBy(touchPoint)) {
            onExchangeButtonClicked();
        }
        if (buyFishButton.isTouchedBy(touchPoint)) {
            onBuyFishButtonClicked();
        }
    }


    private void onBuyFishButtonClicked() {
        if (checkForFishExplanation()) return;
        IBillingManagerYio billingManager = shopViewElement.menuControllerYio.yioGdxGame.billingManager;
        billingManager.launch();
    }


    private boolean checkForFishExplanation() {
        if (OneTimeInfo.getInstance().fishExplanation) return false;
        OneTimeInfo.getInstance().fishExplanation = true;
        OneTimeInfo.getInstance().save();
        Scenes.fishExplanation.create();
        return true;
    }


    private void onExchangeButtonClicked() {
        if (price == -1) return; // haven't received shop data from server yet
        NetUserData userData = shopViewElement.getUserData();
        if (userData.money < price) {
            Scenes.notification.show("not_enough_coins");
            return;
        }
        int maxFish = userData.money / price;
        Scenes.fishExchangeDialog.setMaxFish(maxFish);
        Scenes.fishExchangeDialog.create();
    }


    private void updateExchangeRatePosition() {
        tempWidth = 2 * fishIconPosition.radius + 2 * coinIconPosition.radius + fishViewText.width + coinViewText.width + 2 * iOffset;
        tempValue = viewPosition.x + viewPosition.width / 2 - tempWidth / 2 + fishIconPosition.radius;

        fishIconPosition.center.x = tempValue;
        tempValue += fishIconPosition.radius + iOffset;
        fishViewText.position.x = tempValue;
        tempValue += fishViewText.width + coinIconPosition.radius;
        coinIconPosition.center.x = tempValue;
        tempValue += coinIconPosition.radius + iOffset;
        coinViewText.position.x = tempValue;

        fishIconPosition.center.y = title.position.y - title.height - 0.01f * GraphicsYio.height - fishIconPosition.radius;
        fishViewText.position.y = fishIconPosition.center.y + fishViewText.height / 2;
        coinIconPosition.center.y = fishIconPosition.center.y;
        coinViewText.position.y = fishIconPosition.center.y + coinViewText.height / 2;

        coinViewText.updateBounds();
        fishViewText.updateBounds();
    }


    private void updateTitlePosition() {
        title.centerHorizontal(viewPosition);
        title.centerVertical(viewPosition);
        title.position.y += 0.1f * GraphicsYio.height;
        title.updateBounds();
    }

}
