package yio.tro.onliyoy.menu.elements.shop;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.scroll_engine.ScrollEngineYio;

import java.util.ArrayList;

public class InternalShopList {

    AbstractShopPage page;
    float hook;
    public RectangleYio position;
    public ArrayList<AbstractIslItem> items;
    ScrollEngineYio scrollEngineYio;
    boolean readyToPrepareScrollEngineOnMove;
    AbstractIslItem targetItem;
    Reaction clickReaction;


    public InternalShopList(AbstractShopPage page) {
        this.page = page;
        hook = 0;
        position = new RectangleYio();
        items = new ArrayList<>();
        scrollEngineYio = new ScrollEngineYio();
        scrollEngineYio.setFriction(0.05);
        targetItem = null;
        clickReaction = null;
    }


    void prepareScrollEngine() {
        scrollEngineYio.setSlider(0, getScrollSliderLength());
        scrollEngineYio.setSoftLimitOffset(0.05 * GraphicsYio.height);

        updateScrollLimit();
    }


    private float getScrollSliderLength() {
        return page.viewPosition.height;
    }


    private void updateScrollLimit() {
        double sum = 0;
        double minValue = getScrollSliderLength();

        for (AbstractIslItem islItem : items) {
            sum += islItem.viewPosition.height;
        }
        sum = Math.max(sum, minValue);

        scrollEngineYio.setLimits(0, sum);
    }


    private double getViewableHeightValue(double srcValue) {
        return Yio.roundUp(srcValue / GraphicsYio.height, 2);
    }


    void onAppear() {
        readyToPrepareScrollEngineOnMove = true;
        scrollEngineYio.resetToBottom();
        for (AbstractIslItem islItem : items) {
            islItem.onAppear();
        }
    }


    void move() {
        checkToPrepareScrollEngineOnMove();
        updatePosition();
        scrollEngineYio.move();
        updateHook();
        moveItems();
    }


    private void checkToPrepareScrollEngineOnMove() {
        if (!readyToPrepareScrollEngineOnMove) return;
        readyToPrepareScrollEngineOnMove = false;
        prepareScrollEngine();
    }


    private void updateHook() {
        hook = (float) scrollEngineYio.getSlider().a;
    }


    private void moveItems() {
        for (AbstractIslItem item : items) {
            item.move();
        }
    }


    public AbstractIslItem addItem(AbstractIslItem islItem) {
        islItem.setDarken(items.size() % 2 == 0);
        items.add(islItem);
        updateDeltas();
        prepareScrollEngine();
        return islItem;
    }


    void onContentsChangedDynamically() {
        move();
        move();
        readyToPrepareScrollEngineOnMove = true;
        for (AbstractIslItem islItem : items) {
            islItem.onContentsChangedDynamically();
        }
    }


    private void updateDeltas() {
        float dy = 0;
        for (AbstractIslItem islItem : items) {
            dy += islItem.viewPosition.height;
            islItem.setDeltaY(dy);
        }
    }


    public void clear() {
        items.clear();
    }


    private void updatePosition() {
        position.setBy(page.viewPosition);
    }


    private AbstractIslItem getTouchedItem(PointYio touchPoint) {
        for (AbstractIslItem islItem : items) {
            if (!islItem.isTouchedBy(touchPoint)) continue;
            return islItem;
        }
        return null;
    }


    void onTouchDown(PointYio touchPoint) {
        scrollEngineYio.onTouchDown();
        AbstractIslItem touchedItem = getTouchedItem(touchPoint);
        if (touchedItem != null) {
            touchedItem.selectionEngineYio.applySelection();
        }
    }


    void onTouchDrag(PointYio touchPoint) {
        scrollEngineYio.setSpeed(1.5 * (touchPoint.y - page.shopViewElement.getLastTouch().y));
    }


    void onTouchUp(PointYio touchPoint) {
        scrollEngineYio.onTouchUp();
    }


    public boolean onMouseWheelScrolled(int amount) {
        if (amount == 1) {
            scrollEngineYio.giveImpulse(0.03 * GraphicsYio.width);
        } else if (amount == -1) {
            scrollEngineYio.giveImpulse(-0.03 * GraphicsYio.width);
        }
        scrollEngineYio.hardCorrection();
        return true;
    }


    void onClick(PointYio touchPoint) {
        AbstractIslItem touchedItem = getTouchedItem(touchPoint);
        if (touchedItem == null) return;
        targetItem = touchedItem;
        if (clickReaction != null) {
            clickReaction.perform(page.shopViewElement.menuControllerYio);
            SoundManager.playSound(SoundType.button);
        }
    }


    public AbstractIslItem getItem(String key) {
        for (AbstractIslItem islItem : items) {
            if (!islItem.key.equals(key)) continue;
            return islItem;
        }
        return null;
    }


    public AbstractIslItem getTargetItem() {
        return targetItem;
    }


    public void setClickReaction(Reaction clickReaction) {
        this.clickReaction = clickReaction;
    }
}
