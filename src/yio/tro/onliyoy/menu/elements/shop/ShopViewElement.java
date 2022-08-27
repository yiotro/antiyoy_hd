package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.RefreshRateDetector;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.NetPurchasesData;
import yio.tro.onliyoy.net.shared.NetShopData;
import yio.tro.onliyoy.net.shared.NetUserData;
import yio.tro.onliyoy.stuff.CornerEngineYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.calendar.CveColorYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;

public class ShopViewElement extends InterfaceElement<ShopViewElement> {

    public RectangleYio tabsArea;
    public RectangleYio pageArea;
    public ArrayList<AbstractShopPage> pageList;
    float tabWidth;
    float hook;
    float targetHook;
    float previousHook;
    public FactorYio swipeFactor;
    boolean readyToProcessClick;
    boolean touched;
    PointYio clickPosition;
    int currentTabIndex;
    boolean taTouched; // tab area touched
    public CornerEngineYio cornerEngineYio;
    public RectangleYio accentPosition;
    private float topOffset;
    public CveColorYio currentColor;
    public CveColorYio previousColor;
    NetPurchasesData netPurchasesData;
    private PointYio tempPoint;


    public ShopViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        tabsArea = new RectangleYio();
        pageArea = new RectangleYio();
        tabWidth = GraphicsYio.width;
        swipeFactor = new FactorYio();
        clickPosition = new PointYio();
        cornerEngineYio = new CornerEngineYio();
        accentPosition = new RectangleYio();
        topOffset = 0.11f * GraphicsYio.height;
        currentColor = null;
        previousColor = null;
        netPurchasesData = null;
        tempPoint = new PointYio();
        initPages();
    }


    private void initPages() {
        pageList = new ArrayList<>();
        addPage(new SpExchange(this));
        addPage(new SpSkins(this));
        addPage(new SpPhrases(this));
        addPage(new SpRanks(this));
        addPage(new SpAvatars(this));
    }


    private void addPage(AbstractShopPage page) {
        page.index = pageList.size();
        pageList.add(page);
    }


    @Override
    protected ShopViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateTabsArea();
        updateAccentPosition();
        updatePageArea();
        movePageList();
        updateHook();
        swipeFactor.move();
        cornerEngineYio.move(viewPosition, appearFactor, 0);
    }


    private void updateAccentPosition() {
        accentPosition.setBy(tabsArea);
        accentPosition.height = viewPosition.height - accentPosition.y;
    }


    private void updateHook() {
        if (hook == targetHook) return;
        if (Math.abs(targetHook - hook) < GraphicsYio.borderThickness) {
            hook = targetHook;
            return;
        }
        hook += 0.2 * Math.sqrt(RefreshRateDetector.getInstance().multiplier) * (targetHook - hook);
    }


    public void goTo(int tabIndex) {
        targetHook = -tabIndex * tabWidth;
        previousColor = currentColor;
        currentColor = pageList.get(tabIndex).getAccentColor();
        launchSwipeFactor();
        notifyPagesAboutGoToEvent(tabIndex);
    }


    private void notifyPagesAboutGoToEvent(int tabIndex) {
        for (AbstractShopPage page : pageList) {
            page.onGoToEvent(tabIndex);
        }
    }


    private void launchSwipeFactor() {
        if (swipeFactor.getValue() == 1) {
            swipeFactor.reset();
        } else {
            swipeFactor.setValue(0.5);
        }
        swipeFactor.appear(MovementType.inertia, 2);
    }


    private void movePageList() {
        for (AbstractShopPage abstractShopPage : pageList) {
            abstractShopPage.move();
        }
    }


    void updatePageArea() {
        pageArea.setBy(viewPosition);
        pageArea.height = viewPosition.height - topOffset - tabsArea.height;
    }


    private void updateTabsArea() {
        tabsArea.setBy(viewPosition);
        tabsArea.height = 0.08f * GraphicsYio.height;
        tabsArea.y = viewPosition.y + viewPosition.height - topOffset - tabsArea.height;
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        hook = 0;
        targetHook = 0;
        swipeFactor.reset();
        swipeFactor.setValue(1);
        readyToProcessClick = false;
        touched = false;
        currentTabIndex = 0;
        currentColor = pageList.get(0).getAccentColor();
        taTouched = false;
        notifyPagesAboutAppearEvent();
    }


    private void notifyPagesAboutAppearEvent() {
        for (AbstractShopPage page : pageList) {
            page.onAppear();
        }
    }


    public void onShopDataReceived(NetShopData netShopData) {
        for (AbstractShopPage abstractShopPage : pageList) {
            abstractShopPage.onShopDataReceived(netShopData);
        }
    }


    public void onPurchasesDataReceived(NetPurchasesData netPurchasesData) {
        this.netPurchasesData = netPurchasesData;
        loadStatuses();
    }


    public void onCustomizationDataUpdated() {
        loadStatuses();
    }


    private void loadStatuses() {
        NetRoot netRoot = menuControllerYio.yioGdxGame.netRoot;
        for (AbstractShopPage abstractShopPage : pageList) {
            abstractShopPage.loadStatuses(netPurchasesData, netRoot.customizationData);
        }
    }


    @Override
    public boolean checkToPerformAction() {
        if (readyToProcessClick) {
            readyToProcessClick = false;
            if (tabsArea.isPointInside(clickPosition)) {
                onClickedInTabsArea();
                return true;
            }
            if (pageArea.isPointInside(clickPosition)) {
                onClickedInPageArea();
            }
            return true;
        }
        return false;
    }


    private void onClickedInPageArea() {
        pageList.get(currentTabIndex).onClick(currentTouch);
    }


    private void onClickedInTabsArea() {
        int tabTouchResult = getTabTouchResult(clickPosition.x);
        if (tabTouchResult == 0) return;
        if (tabTouchResult == -1) {
            currentTabIndex = getDecreasedIndex();
        } else {
            currentTabIndex = getIncreasedIndex();
        }
        SoundManager.playSound(SoundType.button);
        goTo(currentTabIndex);
    }


    private int getDecreasedIndex() {
        if (currentTabIndex == 0) return 0;
        return currentTabIndex - 1;
    }


    private int getIncreasedIndex() {
        if (currentTabIndex == pageList.size() - 1) return pageList.size() - 1;
        return currentTabIndex + 1;
    }


    @Override
    public boolean touchDown() {
        touched = viewPosition.isPointInside(currentTouch);
        if (!touched) return false;
        checkToSelectTabName();
        taTouched = pageArea.isPointInside(currentTouch);
        if (taTouched) {
            pageList.get(currentTabIndex).onTouchDown(currentTouch);
        }
        return true;
    }


    private void checkToSelectTabName() {
        if (!tabsArea.isPointInside(currentTouch)) return;
        int tabTouchResult = getTabTouchResult(currentTouch.x);
        if (tabTouchResult == 1) {
            applyTabNameSelection(getIncreasedIndex());
        }
        if (tabTouchResult == -1) {
            applyTabNameSelection(getDecreasedIndex());
        }
    }


    private int getTabTouchResult(float x) {
        if (x < tabsArea.x + 0.25f * tabsArea.width) return -1;
        if (x > tabsArea.x + 0.75f * tabsArea.width) return 1;
        return 0;
    }


    private void applyTabNameSelection(int index) {
        pageList.get(index).selectionEngineYio.applySelection();
    }


    @Override
    public boolean touchDrag() {
        if (!touched) return false;
        if (taTouched) {
            pageList.get(currentTabIndex).onTouchDrag(currentTouch);
        }
        return true;
    }


    @Override
    public boolean touchUp() {
        if (!touched) return false;
        touched = false;
        if (taTouched) {
            pageList.get(currentTabIndex).onTouchUp(currentTouch);
            taTouched = false;
        }
        if (isClicked()) {
            readyToProcessClick = true;
            clickPosition.setBy(currentTouch);
        }
        return true;
    }


    @Override
    public boolean onMouseWheelScrolled(int amount) {
        return pageList.get(currentTabIndex).onMouseWheelScrolled(amount);
    }


    @Override
    public PointYio getTagPosition(String argument) {
        tempPoint.set(
                viewPosition.x + viewPosition.width - 0.02f * GraphicsYio.width,
                tabsArea.y + tabsArea.height / 2
        );
        return tempPoint;
    }


    @Override
    public boolean isTagTouched(String argument, PointYio touchPoint) {
        tempPoint.set(
                viewPosition.x + viewPosition.width - 0.02f * GraphicsYio.width,
                tabsArea.y + tabsArea.height / 2
        );
        return tempPoint.distanceTo(touchPoint) < 0.1f * GraphicsYio.height;
    }


    public PointYio getLastTouch() {
        return lastTouch;
    }


    NetUserData getUserData() {
        return menuControllerYio.yioGdxGame.netRoot.userData;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderShopViewElement;
    }
}
