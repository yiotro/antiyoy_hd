package yio.tro.onliyoy.menu.elements.calendar;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.game.loading.LoadingParameters;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.CornerEngineYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.calendar.CalendarManager;
import yio.tro.onliyoy.stuff.calendar.CmmItem;
import yio.tro.onliyoy.stuff.calendar.CveMonth;
import yio.tro.onliyoy.stuff.tabs_engine.TabsEngineYio;

import java.util.ArrayList;

public class CalendarViewElement extends InterfaceElement<CalendarViewElement> {

    public ArrayList<CveTab> tabsList;
    RectangleYio tempRectangle;
    TabsEngineYio tabsEngineYio;
    boolean touchedCurrently;
    int previousTabIndex;
    int previousTabsQuantity;
    public CornerEngineYio cornerEngineYio;
    public RectangleYio screenPosition;


    public CalendarViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        tabsList = new ArrayList<>();
        tempRectangle = new RectangleYio();
        previousTabIndex = -1;
        previousTabsQuantity = -1;
        cornerEngineYio = new CornerEngineYio();
        screenPosition = new RectangleYio(0, 0, GraphicsYio.width, GraphicsYio.height);
        initTabsEngine();
    }


    private void initTabsEngine() {
        tabsEngineYio = new TabsEngineYio();
        tabsEngineYio.setFriction(0);
        tabsEngineYio.setSoftLimitOffset(0.08 * GraphicsYio.width);
        tabsEngineYio.setMagnetMaxPower(0.01 * GraphicsYio.width);
    }


    @Override
    protected CalendarViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        tabsEngineYio.move();
        cornerEngineYio.move(viewPosition, appearFactor, 0);
        moveTabs();
    }


    private void moveTabs() {
        for (CveTab cveTab : tabsList) {
            cveTab.move();
        }
    }


    @Override
    public void onDestroy() {
        previousTabIndex = tabsEngineYio.getCurrentTabIndex();
        previousTabsQuantity = tabsList.size();
    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        loadValues();
        checkToRestorePreviousTabIndex();
    }


    private void checkToRestorePreviousTabIndex() {
        if (previousTabsQuantity != tabsList.size()) return; // something changed
        if (previousTabIndex == -1) return;
        float x = previousTabIndex * GraphicsYio.width;
        tabsEngineYio.setSlider(x, x + GraphicsYio.width);
    }


    private void updateTabEngineMetrics() {
        tabsEngineYio.setLimits(0, GraphicsYio.width * tabsList.size());
        tabsEngineYio.setSlider(0, GraphicsYio.width);
        tabsEngineYio.setNumberOfTabs(tabsList.size());
    }


    private void loadValues() {
        initTabs();
        loadStates();
        updateDefaultTabPositions();
        updateTabEngineMetrics();
        moveTabs();
    }


    private void loadStates() {
        CalendarManager calendarManager = CalendarManager.getInstance();
        for (CveTab cveTab : tabsList) {
            CveMonth cveMonth = cveTab.month;
            CmmItem cmmItem = calendarManager.getItem(cveMonth.year, cveMonth.monthIndex);
            for (CveDayButton dayButton : cveTab.dayButtons) {
                if (calendarManager.isLocked(cveMonth.year, cveMonth.monthIndex, dayButton.index)) {
                    dayButton.setState(CveDayState.locked);
                    continue;
                }
                if (cmmItem != null && cmmItem.isCompleted(dayButton.index)) {
                    dayButton.setState(CveDayState.completed);
                    continue;
                }
                dayButton.setState(CveDayState.unlocked);
            }
        }
        checkToTagMonthsAsCompleted();
        checkToMergeTabs();
    }


    private void checkToMergeTabs() {
        int bottomYear = findBottomYear();
        int topYear = findTopYear();
        for (int year = bottomYear; year <= topYear; year++) {
            if (!isYearFullyCompleted(year)) continue;
            mergeTabs(year);
        }
    }


    private void mergeTabs(int year) {
        for (int i = tabsList.size() - 1; i >= 0; i--) {
            CveTab cveTab = tabsList.get(i);
            if (cveTab.month.year != year) continue;
            if (cveTab.month.monthIndex == 1) {
                cveTab.setFullYearCompletedMode(true);
            } else {
                tabsList.remove(cveTab);
            }
        }
    }


    private boolean isYearFullyCompleted(int year) {
        int c = 0;
        for (CveTab cveTab : tabsList) {
            if (cveTab.month.year != year) continue;
            if (!cveTab.completed) continue;
            c++;
        }
        return c == 12;
    }


    private int findBottomYear() {
        int minValue = -1;
        for (CveTab cveTab : tabsList) {
            if (minValue == -1 || cveTab.month.year < minValue) {
                minValue = cveTab.month.year;
            }
        }
        return minValue;
    }


    private int findTopYear() {
        int maxValue = -1;
        for (CveTab cveTab : tabsList) {
            if (maxValue == -1 || cveTab.month.year > maxValue) {
                maxValue = cveTab.month.year;
            }
        }
        return maxValue;
    }


    private void checkToTagMonthsAsCompleted() {
        for (CveTab cveTab : tabsList) {
            if (!cveTab.areAllDaysTaggedAsCompleted()) continue;
            cveTab.setCompleted(true);
        }
    }


    private void initTabs() {
        tabsList.clear();
        for (CveMonth month : CalendarManager.getInstance().months) {
            CveTab cveTab = new CveTab(this);
            cveTab.setMonth(month);
            tabsList.add(cveTab);
        }
    }


    private void updateDefaultTabPositions() {
        tempRectangle.set(0, 0, GraphicsYio.width, GraphicsYio.height);
        for (CveTab cveTab : tabsList) {
            cveTab.defaultPosition.setBy(tempRectangle);
            tempRectangle.x -= tempRectangle.width;
        }
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    CveDayButton getCurrentlyTouchedButton() {
        for (CveTab cveTab : tabsList) {
            if (cveTab.completed) continue;
            CveDayButton dayButton = cveTab.getCurrentlyTouchedButton(currentTouch);
            if (dayButton == null) continue;
            return dayButton;
        }
        return null;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = true;
        tabsEngineYio.onTouchDown();
        checkToSelect();
        return true;
    }


    private void checkToSelect() {
        CveDayButton currentlyTouchedButton = getCurrentlyTouchedButton();
        if (currentlyTouchedButton == null) return;
        currentlyTouchedButton.selectionEngineYio.applySelection();
    }


    @Override
    public boolean touchDrag() {
        if (!touchedCurrently) return false;
        tabsEngineYio.setSpeed(1.5f * (currentTouch.x - lastTouch.x));
        return true;
    }


    @Override
    public boolean touchUp() {
        if (!touchedCurrently) return false;
        touchedCurrently = false;
        tabsEngineYio.onTouchUp();
        if (isClicked()) {
            onClick();
        }
        return true;
    }


    @Override
    public boolean onMouseWheelScrolled(int amount) {
        tabsEngineYio.swipeTab(amount);
        return true;
    }


    private void onDayButtonClicked(CveDayButton cveDayButton) {
        if (cveDayButton.state == CveDayState.locked) return;
        CveMonth cveMonth = cveDayButton.tab.month;
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.add("year", cveMonth.year);
        loadingParameters.add("month", cveMonth.monthIndex);
        loadingParameters.add("day", cveDayButton.index);
        loadingParameters.add("color", cveMonth.color);
        menuControllerYio.yioGdxGame.loadingManager.startInstantly(LoadingType.calendar, loadingParameters);
    }


    private void onClick() {
        CveDayButton currentlyTouchedButton = getCurrentlyTouchedButton();
        if (currentlyTouchedButton == null) return;
        SoundManager.playSound(SoundType.button);
        onDayButtonClicked(currentlyTouchedButton);
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderCalendarViewElement;
    }
}
