package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.LightBottomPanelElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.scroll_engine.ScrollEngineYio;

import java.util.ArrayList;

public class CustomizableListYio extends InterfaceElement<CustomizableListYio> {

    public ArrayList<AbstractCustomListItem> items;
    public boolean touchedCurrently;
    float hook;
    ScrollEngineYio scrollEngineYio;
    LongTapDetector longTapDetector;
    AbstractCustomListItem targetItem;
    boolean readyToProcessItemClick;
    boolean readyToProcessItemLongTap;
    public RectangleYio maskPosition;
    public float cornerRadius;
    boolean shadowEnabled;
    boolean backgroundEnabled;
    boolean showBordersEnabled;
    public boolean scrollingEnabled;
    public boolean ltActive;
    public AbstractCustomListItem ltTarget;
    double ltDelta;
    PointYio previousTouch;
    Reaction rbItemRelocated;
    boolean ltChangesDetected;
    public boolean horizontalMode;
    public CornerEngineYio cornerEngineYio;
    private float internalOffset;
    public BackgroundYio backgroundColor;
    public RectangleYio screenPosition;
    public boolean darken;
    private boolean staticCornersMode;
    private FactorYio fakeFactor;


    public CustomizableListYio(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);

        items = new ArrayList<>();
        touchedCurrently = false;
        hook = 0;
        scrollEngineYio = new ScrollEngineYio();
        scrollEngineYio.setFriction(0.05);
        targetItem = null;
        readyToProcessItemClick = false;
        readyToProcessItemLongTap = false;
        maskPosition = new RectangleYio();
        shadowEnabled = true;
        updateCornerRadius();
        backgroundEnabled = true;
        showBordersEnabled = false;
        scrollingEnabled = true;
        ltActive = false;
        previousTouch = new PointYio();
        rbItemRelocated = null;
        horizontalMode = false;
        cornerEngineYio = new CornerEngineYio();
        internalOffset = 0;
        backgroundColor = BackgroundYio.white;
        darken = false;
        screenPosition = new RectangleYio(0, 0, GraphicsYio.width, GraphicsYio.height);
        staticCornersMode = false;
        fakeFactor = new FactorYio();
        fakeFactor.setValue(1);

        initLongTapDetector();
    }


    private void initLongTapDetector() {
        longTapDetector = new LongTapDetector() {
            @Override
            public void onLongTapDetected() {
                CustomizableListYio.this.onLongTapDetected();
            }
        };
    }


    private void updateCornerRadius() {
        cornerRadius = GraphicsYio.defCornerRadius;
    }


    public void addItem(AbstractCustomListItem newItem) {
        addItem(items.size(), newItem);
    }


    public void addItem(int index, AbstractCustomListItem newItem) {
        newItem.setCustomizableListYio(this);
        items.add(index, newItem);
        updateItemDeltas();
        newItem.onAddedToList();
    }


    public void removeItem(AbstractCustomListItem item) {
        items.remove(item);
        updateItemDeltas();
    }


    public void updateItemDeltas() {
        updateMaskPosition(position);
        if (horizontalMode) {
            setDeltasForHorizontalMode();
        } else {
            setDeltasForVerticalMode();
        }
        updateScrollLimit();
    }


    private void setDeltasForHorizontalMode() {
        float currentX = 0;
        for (int i = 0; i < items.size(); i++) {
            AbstractCustomListItem item = items.get(i);
            item.viewPosition.width = (float) item.getWidth();
            item.viewPosition.height = (float) item.getHeight();
            item.positionDelta.set(
                    currentX,
                    (maskPosition.height - item.getHeight()) / 2
            );
            item.onPositionChanged();
            currentX += item.getWidth();
        }
    }


    private void setDeltasForVerticalMode() {
        float currentY = maskPosition.height;
        for (int i = 0; i < items.size(); i++) {
            AbstractCustomListItem item = items.get(i);
            item.viewPosition.width = (float) item.getWidth();
            item.viewPosition.height = (float) item.getHeight();
            item.positionDelta.set(
                    (maskPosition.width - item.getWidth()) / 2,
                    currentY - item.getHeight()
            );
            item.onPositionChanged();
            currentY -= item.getHeight();
        }
    }


    public void clearItems() {
        items.clear();
    }


    private void onLongTapDetected() {
        for (AbstractCustomListItem item : items) {
            if (!item.isTouched(currentTouch)) continue;

            targetItem = item;
            readyToProcessItemLongTap = true;
            SoundManager.playSound(SoundType.merge);
            break;
        }
    }


    public void activateLongTapEditMode(AbstractCustomListItem target) {
        ltActive = true;
        ltDelta = 0;
        ltTarget = target;
        ltChangesDetected = false;
    }


    @Override
    protected CustomizableListYio getThis() {
        return this;
    }


    private void updateHook() {
        hook = (float) scrollEngineYio.getSlider().a;
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();
        updateItemDeltas();

        scrollEngineYio.setSlider(0, getSliderTargetLength());
        scrollEngineYio.setSoftLimitOffset(0.05 * maskPosition.height);

        updateScrollLimit();
    }


    private float getSliderTargetLength() {
        if (horizontalMode) {
            return maskPosition.width;
        }
        return maskPosition.height;
    }


    private void updateScrollLimit() {
        double sum = 0;
        double minValue = getSliderTargetLength();

        for (int i = 0; i < items.size(); i++) {
            AbstractCustomListItem item = items.get(i);
            if (horizontalMode) {
                sum += item.getWidth();
            } else {
                sum += item.getHeight();
            }
        }
        sum = Math.max(sum, minValue);

        scrollEngineYio.setLimits(0, sum);
    }


    @Override
    public CustomizableListYio setSize(double width, double height) {
        super.setSize(width, height);
        if (width == 1 && height == 1) {
            setShadowEnabled(false);
            darken = true;
        }
        return this;
    }


    @Override
    protected void onPositionChanged() {
        super.onPositionChanged();
        updateItemDeltas();
    }


    @Override
    public void onMove() {
        scrollEngineYio.move();
        updateHook();
        longTapDetector.move();
        updateMaskPosition(viewPosition);
        moveCornerEngine();
        moveItems();
    }


    private void moveCornerEngine() {
        if (staticCornersMode) {
            cornerEngineYio.move(viewPosition, fakeFactor, cornerRadius);
            return;
        }
        cornerEngineYio.move(viewPosition, appearFactor, cornerRadius);
    }


    @Override
    public CustomizableListYio setParent(InterfaceElement parent) {
        CustomizableListYio customizableListYio = super.setParent(parent);

        if (parent != null && parent instanceof LightBottomPanelElement) {
            enableEmbeddedMode();
        }

        return customizableListYio;
    }


    public CustomizableListYio enableEmbeddedMode() {
        setBackgroundEnabled(false);
        setShadowEnabled(false);
        setAlphaEnabled(false);
        setAppearParameters(MovementType.approach, 30);
        return this;
    }


    private void updateMaskPosition(RectangleYio src) {
        maskPosition.setBy(src);
        if (internalOffset > 0) {
            maskPosition.increase(-internalOffset);
        }
    }


    private void moveItems() {
        for (AbstractCustomListItem item : items) {
            item.moveItem();
            if (!touchedCurrently) {
                item.selectionEngineYio.move();
            }
        }
    }


    @Override
    public void onDestroy() {
        ltActive = false;
    }


    @Override
    public void onAppear() {
        readyToProcessItemClick = false;
        readyToProcessItemLongTap = false;
        scrollEngineYio.resetToBottom();
        ltActive = false;

        for (AbstractCustomListItem listItem : items) {
            listItem.onAppear();
        }
        onMove(); // to avoid blinking (render before position update)
    }


    public void resetScroll() {
        scrollEngineYio.resetToBottom();
    }


    public void scrollToItem(AbstractCustomListItem item) {
        updateMaskPosition(position);
        while (scrollEngineYio.getSlider().b  < scrollEngineYio.getLimits().b - 1) {
            updateHook();
            item.moveItem();
            if (isItemInGoodEnoughPosition(item)) break;
            scrollEngineYio.relocate(0.1f * GraphicsYio.height);
            scrollEngineYio.hardCorrection();
        }
    }


    private boolean isItemInGoodEnoughPosition(AbstractCustomListItem item) {
        if (item.viewPosition.y < maskPosition.y + 0.3f * maskPosition.height) return false;
        if (item.viewPosition.y > maskPosition.y + maskPosition.height) return false;
        return true;
    }


    @Override
    public boolean checkToPerformAction() {
        if (readyToProcessItemClick) {
            readyToProcessItemClick = false;

            targetItem.onClicked();

            return true;
        }

        if (readyToProcessItemLongTap) {
            readyToProcessItemLongTap = false;

            targetItem.onLongTapped();

            return true;
        }

        return false;
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = isTouchedBy(currentTouch);
        ltActive = false;
        if (!touchedCurrently) return false;

        scrollEngineYio.onTouchDown();
        checkToSelectItem();
        longTapDetector.onTouchDown(currentTouch);
        previousTouch.setBy(currentTouch);

        return true;
    }


    private void checkToSelectItem() {
        for (AbstractCustomListItem item : items) {
            if (!item.isTouched(currentTouch)) continue;

            item.selectionEngineYio.applySelection();
            item.onTouchDown(currentTouch);
            break;
        }
    }


    @Override
    public boolean touchDrag() {
        if (!touchedCurrently) return false;

        if (ltActive) {
            ltDelta += -(currentTouch.y - previousTouch.y);
            checkToApplyLtDelta();
            previousTouch.setBy(currentTouch);
            return true;
        }

        if (scrollingEnabled) {
            if (horizontalMode) {
                scrollEngineYio.setSpeed(-1.5 * (currentTouch.x - lastTouch.x));
            } else {
                scrollEngineYio.setSpeed(1.5 * (currentTouch.y - lastTouch.y));
            }
        }
        longTapDetector.onTouchDrag(currentTouch);

        return true;
    }


    private void checkToApplyLtDelta() {
        if (Math.abs(ltDelta) < ltTarget.getHeight()) return;

        while (ltDelta > ltTarget.getHeight()) {
            ltDelta -= ltTarget.getHeight();
            relocateLtTarget(1);
        }

        while (ltDelta < -ltTarget.getHeight()) {
            ltDelta += ltTarget.getHeight();
            relocateLtTarget(-1);
        }
    }


    private void relocateLtTarget(int indexDelta) {
        int currentIndex = items.indexOf(ltTarget);
        int desiredIndex = currentIndex + indexDelta;
        if (desiredIndex < 0) {
            desiredIndex = 0;
        }
        if (desiredIndex > items.size() - 1) {
            desiredIndex = items.size() - 1;
        }
        if (desiredIndex == currentIndex) return;

        items.remove(ltTarget);
        items.add(desiredIndex, ltTarget);
        ltChangesDetected = true;
        updateItemDeltas();
        ensureThatItemIsVisible(ltTarget);
    }


    private void ensureThatItemIsVisible(AbstractCustomListItem customListItem) {
        RectangleYio tPos = customListItem.viewPosition;

        while (tPos.y + tPos.height / 2 < maskPosition.y + 0.45 * tPos.height) {
            scrollEngineYio.relocate(tPos.height / 2);
            scrollEngineYio.hardCorrection();
            updateHook();
            moveItems();
        }

        while (tPos.y + tPos.height / 2 > maskPosition.y + maskPosition.height - 0.45 * tPos.height) {
            scrollEngineYio.relocate(-tPos.height / 2);
            scrollEngineYio.hardCorrection();
            updateHook();
            moveItems();
        }
    }


    @Override
    public boolean touchUp() {
        if (!touchedCurrently) return false;

        scrollEngineYio.onTouchUp();
        longTapDetector.onTouchUp(currentTouch);
        deactivateLtOnTouchUp();

        if (isClicked()) {
            onClick();
        }

        touchedCurrently = false;
        return true;
    }


    private void deactivateLtOnTouchUp() {
        if (!ltActive) return;
        ltActive = false;

        if (ltChangesDetected && rbItemRelocated != null) {
            rbItemRelocated.perform(menuControllerYio);
        }
    }


    private void onClick() {
        for (AbstractCustomListItem item : items) {
            if (!item.isTouched(currentTouch)) continue;

            targetItem = item;
            readyToProcessItemClick = true;
            SoundManager.playSound(SoundType.button);
            break;
        }
    }


    @Override
    public boolean onMouseWheelScrolled(int amount) {
        if (!scrollingEnabled) return true;
        if (amount == 1) {
            scrollEngineYio.giveImpulse(0.03 * GraphicsYio.width);
        } else if (amount == -1) {
            scrollEngineYio.giveImpulse(-0.03 * GraphicsYio.width);
        }
        scrollEngineYio.hardCorrection();
        return true;
    }


    public CustomizableListYio setInternalOffset(float internalOffset) {
        this.internalOffset = internalOffset;
        return this;
    }


    public CustomizableListYio setCornerRadius(double r) {
        cornerRadius = (float) (r * GraphicsYio.width);
        return this;
    }


    public CustomizableListYio setShadowEnabled(boolean shadowEnabled) {
        this.shadowEnabled = shadowEnabled;
        return this;
    }


    public CustomizableListYio setBackgroundEnabled(boolean backgroundEnabled) {
        this.backgroundEnabled = backgroundEnabled;
        return this;
    }


    public boolean isBackgroundEnabled() {
        return backgroundEnabled;
    }


    public boolean isShadowEnabled() {
        return shadowEnabled;
    }


    public CustomizableListYio setHorizontalMode(boolean horizontalMode) {
        this.horizontalMode = horizontalMode;
        scrollEngineYio.setSlider(0, getSliderTargetLength());
        return this;
    }


    public void applyScrollToBottom() {
        double distanceToTop = scrollEngineYio.getDistanceToTop();
        if (distanceToTop < 2 * GraphicsYio.borderThickness) return;
        if (distanceToTop > GraphicsYio.height / 2) return;
        scrollEngineYio.giveImpulse(0.1 * distanceToTop);
    }


    public AbstractCustomListItem getItem(String key) {
        for (AbstractCustomListItem item : items) {
            if (!key.equals(item.getKey())) continue;
            return item;
        }
        return null;
    }


    @Override
    public PointYio getTagPosition(String argument) {
        AbstractCustomListItem item = getItem(argument);
        if (item != null) {
            if (item instanceof AbstractSingleLineItem) {
                RectangleYio bounds = ((AbstractSingleLineItem) item).title.bounds;
                tempPoint.set(
                        bounds.x + bounds.width / 2,
                        bounds.y + bounds.height / 2
                );
            } else {
                tempPoint.set(
                        item.viewPosition.x + item.viewPosition.width / 2,
                        item.viewPosition.y + item.viewPosition.height / 2
                );
            }
            ensureThatItemIsVisible(item);
            return tempPoint;
        }
        return super.getTagPosition(argument);
    }


    @Override
    public boolean isTagTouched(String argument, PointYio touchPoint) {
        AbstractCustomListItem item = getItem(argument);
        if (item != null) {
            return item.viewPosition.isPointInside(touchPoint);
        }
        return super.isTagTouched(argument, touchPoint);
    }


    public CustomizableListYio setShowBordersEnabled(boolean showBordersEnabled) {
        this.showBordersEnabled = showBordersEnabled;
        return this;
    }


    public boolean areShowBordersEnabled() {
        return showBordersEnabled;
    }


    public CustomizableListYio setRbItemRelocated(Reaction rbItemRelocated) {
        this.rbItemRelocated = rbItemRelocated;
        return this;
    }


    public CustomizableListYio setScrollingEnabled(boolean scrollingEnabled) {
        this.scrollingEnabled = scrollingEnabled;
        return this;
    }


    public ScrollEngineYio getScrollEngineYio() {
        return scrollEngineYio;
    }


    public CustomizableListYio setBackgroundColor(BackgroundYio backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }


    public CustomizableListYio enableStaticCornersMode() {
        staticCornersMode = true;
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderCustomizableList;
    }
}
