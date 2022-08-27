package yio.tro.onliyoy.stuff;

import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SimpleTabsEngineYio {

    float tabWidth;
    float hook;
    float targetHook;
    float previousHook;
    public FactorYio swipeFactor;
    int currentTabIndex;
    int limit;


    public SimpleTabsEngineYio() {
        swipeFactor = new FactorYio();
        tabWidth = GraphicsYio.width;
    }


    public void onAppear() {
        hook = 0;
        targetHook = 0;
        previousHook = 0;
        swipeFactor.reset();
        swipeFactor.setValue(1);
        currentTabIndex = 0;
    }


    public void move() {
        updateHook();
    }


    private void updateHook() {
        if (!swipeFactor.move()) return;
        hook = previousHook + swipeFactor.getValue() * (targetHook - previousHook);
    }


    public void goTo(int tabIndex) {
        if (tabIndex >= limit) {
            tabIndex = limit - 1;
        }
        currentTabIndex = tabIndex;
        previousHook = hook;
        targetHook = -tabIndex * tabWidth;
        launchSwipeFactor();
    }


    private void launchSwipeFactor() {
        if (swipeFactor.getValue() == 1) {
            swipeFactor.reset();
        } else {
            swipeFactor.setValue(0.5);
        }
        swipeFactor.appear(MovementType.inertia, 2);
    }


    public void setTabWidth(float tabWidth) {
        this.tabWidth = tabWidth;
    }


    public float getHook() {
        return hook;
    }


    public int getCurrentTabIndex() {
        return currentTabIndex;
    }


    public void setLimit(int limit) {
        this.limit = limit;
    }
}
