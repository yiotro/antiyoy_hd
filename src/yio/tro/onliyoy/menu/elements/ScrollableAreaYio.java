package yio.tro.onliyoy.menu.elements;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.scroll_engine.ScrollEngineYio;

import java.util.ArrayList;

public class ScrollableAreaYio extends InterfaceElement<ScrollableAreaYio> {

    boolean touched, childrenReachable, conflictMode, scrollActive;
    boolean scrollNeeded;
    ScrollEngineYio scrollEngineYio;
    ArrayList<InterfaceElement> children;
    private float lowerOffset, conflictResolveDistance;
    InterfaceElement conflictElement;


    public ScrollableAreaYio(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);

        children = new ArrayList<>();

        touched = false;
        scrollNeeded = false;
        scrollEngineYio = new ScrollEngineYio();
        scrollEngineYio.setFriction(0.05);
        lowerOffset = 0.05f * GraphicsYio.width;
        conflictResolveDistance = 0.02f * GraphicsYio.width;
        setAnimation(AnimationYio.center);
    }


    @Override
    protected ScrollableAreaYio getThis() {
        return this;
    }


    @Override
    protected void onViewPositionUpdated() {
        moveScroll();
        viewPosition.y -= scrollEngineYio.getSlider().a;
        viewPosition.y += position.y;
    }


    @Override
    public void onMove() {

    }


    @Override
    protected void updateViewPosition() {
        viewPosition.setBy(position);
    }


    private void moveScroll() {
        if (!scrollNeeded) return;

        scrollEngineYio.move();
    }


    @Override
    protected void onChildAdded(InterfaceElement child) {
        super.onChildAdded(child);

        children.add(child);
        updateVerticalLimit();
    }


    @Override
    public void onSceneEndCreation() {
        for (InterfaceElement element : sceneOwner.getLocalElementsList()) {
            if (!isSubChildren(element)) continue;
            element.setCaptureTouch(false);
        }
    }


    private boolean isSubChildren(InterfaceElement element) {
        InterfaceElement tempParent = element.parent;

        while (tempParent != null) {
            if (tempParent == this) {
                return true;
            }

            tempParent = tempParent.parent;
        }

        return false;
    }


    private void updateVerticalLimit() {
        InterfaceElement lowestElement = null;

        for (InterfaceElement child : children) {
            if (child.position.y >= position.y) continue;

            if (lowestElement == null || child.position.y < lowestElement.position.y) {
                lowestElement = child;
            }
        }

        if (lowestElement == null) {
            scrollEngineYio.setLimits(position.y, position.y + position.height);
            return;
        }

        // result
        scrollEngineYio.setLimits(position.y + lowestElement.position.y - lowerOffset, position.y + position.height);
        updateScrollNeeded();
    }


    private void updateScrollNeeded() {
        scrollNeeded = (scrollEngineYio.getSlider().getLength() < scrollEngineYio.getLimits().getLength());
    }


    @Override
    protected void onChildPositionChanged(InterfaceElement child) {
        super.onChildPositionChanged(child);

        updateVerticalLimit();
    }


    public void forceToTop() {
        sceneOwner.forceElementToTop(this);
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        resetScrollPosition();
    }


    private void resetScrollPosition() {
        scrollEngineYio.resetToTop();
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        childrenReachable = true;
        scrollActive = true;
        conflictMode = false;

        if (isTouched()) {
            touched = true;
            scrollEngineYio.onTouchDown();
            checkForConflicts();
            return !childrenReachable;
        }

        return false;
    }


    private boolean isTouched() {
        if (currentTouch.x < viewPosition.x) return false;
        if (currentTouch.x > viewPosition.x + viewPosition.width) return false;
        if (currentTouch.y > viewPosition.y + viewPosition.height) return false;
        return true;
    }


    private void checkForConflicts() {
        for (InterfaceElement element : sceneOwner.getLocalElementsList()) {
            if (isElementConflicting(element)) {
                conflictMode = true;
                childrenReachable = false;
                scrollActive = false;
                conflictElement = element;
                break;
            }
        }
    }


    private boolean isElementConflicting(InterfaceElement element) {
        if (element instanceof SliderElement && element.isTouchedBy(currentTouch)) return true;

        return false;
    }


    @Override
    public boolean touchDrag() {
        if (!touched) return false;

        if (conflictMode) {
            return dragInConflictMode();
        }

        if (scrollActive) {
            scrollEngineYio.setSpeed(-1.5 * (currentTouch.y - lastTouch.y));
        }

        return !childrenReachable;
    }


    private boolean dragInConflictMode() {
        if (initialTouch.distanceTo(currentTouch) < conflictResolveDistance) return true;

        resolveScrollConflict();

        return true;
    }


    private void resolveScrollConflict() {
        float hor = Math.abs(currentTouch.x - initialTouch.x);
        float ver = Math.abs(currentTouch.y - initialTouch.y);
        conflictMode = false;

        if (hor > ver) {
            // give control to child, stop area scroll

            childrenReachable = true;
            conflictElement.touchDownElement((int) currentTouch.x, (int) currentTouch.y);
        } else {
            // ignore child, enable area scroll

            scrollActive = true;
        }
    }


    @Override
    public boolean touchUp() {
        if (!touched) return false;

        if (conflictMode && isClicked()) {
            conflictElement.touchDownElement((int) currentTouch.x, (int) currentTouch.y);
            conflictElement.touchUpElement((int) currentTouch.x, (int) currentTouch.y, 0, 0);
        }

        touched = false;
        scrollEngineYio.onTouchUp();
        conflictMode = false;
        return !childrenReachable;
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();

        scrollEngineYio.setSlider(position.y, position.y + position.height);
        scrollEngineYio.setSoftLimitOffset(0.05 * position.height);
    }


    @Override
    public boolean onMouseWheelScrolled(int amount) {
        if (amount == 1) {
            scrollEngineYio.giveImpulse(-0.03 * GraphicsYio.width);
        } else if (amount == -1) {
            scrollEngineYio.giveImpulse(0.03 * GraphicsYio.width);
        }
        scrollEngineYio.hardCorrection();
        return true;
    }


    public ScrollEngineYio getScrollEngineYio() {
        return scrollEngineYio;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderScrollableArea;
    }


    @Override
    public String toString() {
        return "[" + getClass().getSimpleName() +
                " " + scrollEngineYio +
                "]";
    }


}
