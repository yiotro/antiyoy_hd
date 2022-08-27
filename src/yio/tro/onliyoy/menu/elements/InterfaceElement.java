package yio.tro.onliyoy.menu.elements;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.ClickDetector;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.MenuParams;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public abstract class InterfaceElement<T extends InterfaceElement<T>> {


    public MenuControllerYio menuControllerYio;
    protected RectangleYio position, viewPosition;
    protected FactorYio appearFactor;
    protected boolean visible, touchable, factorMoved, shouldApplyParent;
    protected boolean captureTouch;
    private float hor, ver, cx, cy;
    protected AnimationYio animationType;
    protected MovementType spawnType, destroyType;
    protected double spawnSpeed, destroySpeed;
    protected static InterfaceElement screen = null;
    protected long touchDownTime;
    protected InterfaceElement parent;
    protected SceneYio sceneOwner;
    protected PointYio initialTouch, currentTouch, lastTouch;
    protected PointYio tempPoint;
    protected PointYio animationPoint;
    ClickDetector clickDetector;
    private String key;
    boolean onTopOfGameView;
    public boolean reverseAnimMode;
    boolean fakeDyingStatus;
    private int hotkeyKeycode;
    public ConditionYio conditionAllowedToAppear;
    boolean alphaEnabled;
    boolean resistantToAutoDestroy;
    private FactorYio activationFactor;


    public InterfaceElement(MenuControllerYio menuControllerYio) {
        this.menuControllerYio = menuControllerYio;

        appearFactor = new FactorYio();
        position = new RectangleYio();
        viewPosition = new RectangleYio(-1, -1, -1, -1); // it has to be not zero
        initialTouch = new PointYio();
        currentTouch = new PointYio();
        lastTouch = new PointYio();
        tempPoint = new PointYio();
        clickDetector = new ClickDetector();
        animationPoint = new PointYio();
        visible = false;
        touchable = true;
        shouldApplyParent = true;
        onTopOfGameView = false;
        parent = null; // will be immediately overwritten, see below
        sceneOwner = null;
        captureTouch = true;
        key = null;
        reverseAnimMode = false;
        fakeDyingStatus = false;
        hotkeyKeycode = -1;
        conditionAllowedToAppear = null;
        alphaEnabled = true;
        resistantToAutoDestroy = false;

        setAppearParameters(MenuParams.ANIM_TYPE, MenuParams.ANIM_SPEED);
        setDestroyParameters(MenuParams.ANIM_TYPE, MenuParams.ANIM_SPEED);

        setParent(screen);
        initActivationFactor();
        setAnimation(AnimationYio.center); // after setParent()
    }


    private void initActivationFactor() {
        activationFactor = new FactorYio();
        activationFactor.appear(MovementType.approach, 2.5);
        activationFactor.setValue(1);
    }


    public static void initScreenElement(MenuControllerYio menuControllerYio) {
        screen = new ButtonYio(menuControllerYio);
        screen.getFactor().setValues(1, 0);
        screen.setPosition(0, 0, 1, 1);
    }


    public void moveElement() {
        if (!visible) return;

        factorMoved = appearFactor.move();
        activationFactor.move();
        checkToCancelVisibility();

        updateViewPosition();
        onViewPositionUpdated();

        applyParentViewPosition();
        onMove();
    }


    public float getAlpha() {
        if (!alphaEnabled) return 1;
        if (getFactor().getValue() >= 0.9f) return 1;
        return (1f / 0.9f) * getFactor().getValue();
    }


    public float getShadowAlpha() {
        if (getFactor().getValue() < 0.9f) return 0;
        return 10f * (getFactor().getValue() - 0.9f);
    }


    private void applyParentViewPosition() {
        if (parent == screen || !shouldApplyParent) return;

        if (parent.appearFactor.getValue() < 1 && parent.doesAnimationAffectSize()) {
            viewPosition.x *= appearFactor.getValue();
            viewPosition.y *= appearFactor.getValue();
            viewPosition.width *= appearFactor.getValue();
            viewPosition.height *= appearFactor.getValue();
        }

        viewPosition.x += parent.getHookPositionForChildren().x;
        viewPosition.y += parent.getHookPositionForChildren().y;
    }


    protected boolean doesAnimationAffectSize() {
        switch (animationType) {
            default:
                return false;
            case center:
            case from_touch:
                return true;
        }
    }


    private void checkToCancelVisibility() {
        if (!factorMoved) return;
        if (!appearFactor.isInDestroyState()) return;
        if (!isCompletelyDestroyed()) return;

        visible = false;
        onVisibilityCanceled();
    }


    private boolean isCompletelyDestroyed() {
        if (appearFactor.getValue() == 0) return true;
        if (hasParent() && parent.isCompletelyDestroyed()) return true;
        return false;
    }


    protected void onVisibilityCanceled() {
        //
    }


    protected abstract T getThis();


    public abstract void onMove();


    protected void onViewPositionUpdated() {
        // nothing by default
    }


    protected void updateViewPosition() {
        switch (animationType) {
            case none:
                viewPosition.setBy(position);
                break;
            case def:
                animDefault();
                break;
            case up:
                animUp();
                break;
            case down:
                animDown();
                break;
            case center:
                animFromCenter();
                break;
            case right:
                animRight();
                break;
            case left:
                animLeft();
                break;
            case up_then_fade:
                animUpThenFade();
                break;
            case from_touch:
                animFromTouch();
                break;
        }
    }


    private void animFromTouch() {
        hor = (float) (0.5 * getCurrentAnimValue() * position.width);
        ver = (float) (0.5 * getCurrentAnimValue() * position.height);
        cx = position.x + 0.5f * position.width;
        cy = position.y + 0.5f * position.height;

        tempPoint.setBy(animationPoint);
        tempPoint.y += 0.5f * getCurrentAnimValue() * (0.5f * GraphicsYio.height - tempPoint.y);

        if (appearFactor.isInAppearState()) {
            cx -= (1 - getCurrentAnimValue()) * (cx - tempPoint.x);
            cy -= (1 - getCurrentAnimValue()) * (cy - tempPoint.y);
        } else {
            cx -= (1 - getCurrentAnimValue()) * (cx - 0.5f * GraphicsYio.width);
            cy -= (1 - getCurrentAnimValue()) * (cy - 0.5f * GraphicsYio.height);
        }
        viewPosition.set(cx - hor, cy - ver, 2 * hor, 2 * ver);
    }


    private void animUpThenFade() {
        viewPosition.setBy(position);
        if (appearFactor.isInDestroyState()) return;
        viewPosition.y += ((1 - getCurrentAnimValue()) * (GraphicsYio.height - position.y));
    }


    protected float getCurrentAnimValue() {
        return appearFactor.getValue();
    }


    private void animLeft() {
        viewPosition.setBy(position);
        viewPosition.x -= (1 - getCurrentAnimValue()) * 1.2f * position.width;
    }


    private void animRight() {
        viewPosition.setBy(position);
        viewPosition.x += (1 - getCurrentAnimValue()) * (GraphicsYio.width - position.x);
    }


    protected void animFromCenter() {
        hor = (float) (0.5 * getCurrentAnimValue() * position.width);
        ver = (float) (0.5 * getCurrentAnimValue() * position.height);
        cx = position.x + 0.5f * position.width;
        cy = position.y + 0.5f * position.height;
        cx -= (1 - getCurrentAnimValue()) * (cx - 0.5f * GraphicsYio.width);
        cy -= (1 - getCurrentAnimValue()) * (cy - 0.5f * GraphicsYio.height);
        viewPosition.set(cx - hor, cy - ver, 2 * hor, 2 * ver);
    }


    protected void animDown() {
        viewPosition.setBy(position);
        viewPosition.y -= (1 - getCurrentAnimValue()) * (position.y + position.height);
    }


    protected void animUp() {
        viewPosition.setBy(position);
        viewPosition.y += ((1 - getCurrentAnimValue()) * (GraphicsYio.height - position.y));
    }


    protected void animDefault() {
        hor = 0.5f * getCurrentAnimValue() * position.width;
        ver = 0.5f * getCurrentAnimValue() * position.height;
        cx = position.x + 0.5f * position.width;
        cy = position.y + 0.5f * position.height;
        viewPosition.set(cx - hor, cy - ver, 2 * hor, 2 * ver);
    }


    public T setAnimation(AnimationYio animType) {
        this.animationType = animType;
        return getThis();
    }


    public FactorYio getFactor() {
        return appearFactor;
    }


    public RectangleYio getPosition() {
        return position;
    }


    public RectangleYio getViewPosition() {
        return viewPosition;
    }


    public boolean isVisible() {
        return visible && viewPosition.height != 0 && viewPosition.width != 0;
    }


    public boolean isViewPositionNotUpdatedYet() {
        return hasParent() && getFactor().getValue() < 0.001;
    }


    public boolean isTouchable() {
        return touchable;
    }


    protected void onPositionChanged() {
        if (parent == null) return;
        parent.onChildPositionChanged(this);
    }


    protected void onChildPositionChanged(InterfaceElement child) {
        // nothing by default
    }


    protected void onSizeChanged() {
        // nothing for now
    }


    public T setPosition(double x, double y) {
        position.x = (float) (x * GraphicsYio.width);
        position.y = (float) (y * GraphicsYio.height);
        onPositionChanged();
        return getThis();
    }


    public T setPosition(double x, double y, double width, double height) {
        setPosition(x, y);
        return setSize(width, height);
    }


    public T setSize(double width, double height) {
        position.width = (float) (width * GraphicsYio.width);
        position.height = (float) (height * GraphicsYio.height);
        onSizeChanged();
        return getThis();
    }


    public T setSize(double size) {
        return setSize(size, GraphicsYio.convertToHeight(size));
    }


    public T setTouchable(boolean touchable) {
        this.touchable = touchable;
        return getThis();
    }


    public T setKey(String key) {
        this.key = key;
        return getThis();
    }


    public T setFakeDyingStatusEnabled(boolean value) {
        fakeDyingStatus = value;
        return getThis();
    }


    public T setAppearParameters(MovementType spawnType, double spawnSpeed) {
        this.spawnType = spawnType;
        this.spawnSpeed = spawnSpeed;
        return getThis();
    }


    public T setDestroyParameters(MovementType destroyType, double destroySpeed) {
        this.destroyType = destroyType;
        this.destroySpeed = destroySpeed;
        return getThis();
    }


    protected float getParentCompensationX(InterfaceElement anotherElement) {
        return getParentCompensationX(anotherElement.parent == parent);
    }


    protected float getParentCompensationX(boolean sameParent) {
        if (sameParent) {
            return 0;
        }

        return parent.position.x;
    }


    protected float getParentCompensationY(InterfaceElement anotherElement) {
        return getParentCompensationY(anotherElement.parent == parent);
    }


    protected float getParentCompensationY(boolean sameParent) {
        if (sameParent) {
            return 0;
        }

        return parent.position.y;
    }


    public T clone(InterfaceElement src) {
        setParent(src.parent);
        RectangleYio position = src.position;
        setAnimation(src.animationType);
        setResistantToAutoDestroy(src.resistantToAutoDestroy);

        setPosition(
                position.x / GraphicsYio.width,
                position.y / GraphicsYio.height,
                position.width / GraphicsYio.width,
                position.height / GraphicsYio.height
        );

        return getThis();
    }


    public T alignTop(double offset) {
        position.y = (float) (parent.position.y
                + parent.position.height
                - position.height
                - offset * GraphicsYio.height
                - getParentCompensationY(parent));

        onPositionChanged();
        return getThis();
    }


    public T alignAbove(InterfaceElement element, double offset) {
        position.y = (float) (element.position.y
                + element.position.height
                + offset * GraphicsYio.height
                - getParentCompensationY(element));

        onPositionChanged();
        return getThis();
    }


    public T alignBottom(double offset) {
        position.y = (float) (parent.position.y
                + offset * GraphicsYio.height
                - getParentCompensationY(parent));

        onPositionChanged();
        return getThis();
    }


    public T alignUnder(InterfaceElement element, double offset) {
        position.y = (float) (element.position.y
                - position.height
                - offset * GraphicsYio.height
                - getParentCompensationY(element));

        onPositionChanged();
        return getThis();
    }


    public T alignRight(double offset) {
        return alignRight(parent, offset);
    }


    public T alignRight(InterfaceElement element, double offset) {
        if (element == parent) {
            return alignRight(element, offset, true);
        }

        return alignRight(element, offset, false);
    }


    private T alignRight(InterfaceElement element, double offset, boolean inside) {
        if (inside) {
            position.x = (float) (element.position.x
                    + element.position.width
                    - position.width
                    - offset * GraphicsYio.width
                    - getParentCompensationX(element));
        } else {
            position.x = (float) (element.position.x
                    + element.position.width
                    + offset * GraphicsYio.width
                    - getParentCompensationX(element));
        }

        onPositionChanged();
        return getThis();
    }


    public T alignLeft(double offset) {
        return alignLeft(parent, offset);
    }


    public T alignLeft(InterfaceElement element, double offset) {
        if (element == parent) {
            return alignLeft(element, offset, true);
        }

        return alignLeft(element, offset, false);
    }


    private T alignLeft(InterfaceElement element, double offset, boolean inside) {
        if (inside) {
            position.x = (float) (element.position.x
                    + offset * GraphicsYio.width
                    - getParentCompensationX(element));
        } else {
            position.x = (float) (element.position.x
                    - position.width
                    - offset * GraphicsYio.width
                    - getParentCompensationX(element));
        }

        onPositionChanged();
        return getThis();
    }


    public T centerHorizontal() {
        centerHorizontal(parent);
        position.x -= getParentCompensationX(false);
        return getThis();
    }


    public T centerHorizontal(InterfaceElement element) {
        position.x = element.position.x + (element.position.width - position.width) / 2;
        onPositionChanged();
        return getThis();
    }


    public T centerVertical() {
        centerVertical(parent);
        position.y -= getParentCompensationY(false);
        return getThis();
    }


    public T centerVertical(InterfaceElement element) {
        position.y = element.position.y + (element.position.height - position.height) / 2;
        onPositionChanged();
        return getThis();
    }


    public T setParent(InterfaceElement parent) {
        this.parent = parent;
        setAnimation(AnimationYio.none);

        if (parent != null) {
            parent.onChildAdded(this);
        }

        return getThis();
    }


    public InterfaceElement getParent() {
        return parent;
    }


    protected void onChildAdded(InterfaceElement child) {
        // nothing by default
    }


    public void destroy() {
        appearFactor.destroy(destroyType, destroySpeed);
        reverseAnimMode = false;
        onDestroy();
    }


    public boolean getDyingStatus() {
        if (fakeDyingStatus) {
            return false;
        }

        if (hasParent() && parent.fakeDyingStatus) {
            return false;
        }

        return getFactor().isInDestroyState();
    }


    public abstract void onDestroy();


    public void appear() {
        visible = true;
        appearFactor.appear(spawnType, spawnSpeed);
        reverseAnimMode = false;
        activate();
        checkToUpdateAnimationPoint();
        onAppear();
    }


    private void checkToUpdateAnimationPoint() {
        if (animationType != AnimationYio.from_touch) return;
        animationPoint.setBy(menuControllerYio.currentTouchPoint);
    }


    public abstract void onAppear();


    public abstract boolean checkToPerformAction();


    private void updateCurrentTouch(int screenX, int screenY) {
        currentTouch.x = screenX;
        currentTouch.y = screenY;
    }


    protected boolean isClicked() {
        return clickDetector.isClicked();
    }


    public boolean isTouchedBy(PointYio touchPoint) {
        return isTouchInsideRectangle(touchPoint, viewPosition);
    }


    public abstract boolean touchDown();


    public boolean touchDownElement(int screenX, int screenY) {
        if (appearFactor.isInDestroyState()) return false;

        updateCurrentTouch(screenX, screenY);

        lastTouch.setBy(currentTouch);
        initialTouch.setBy(currentTouch);
        touchDownTime = System.currentTimeMillis();
        clickDetector.onTouchDown(currentTouch);
        return touchDown();
    }


    public abstract boolean touchDrag();


    public void touchDragElement(int screenX, int screenY, int pointer) {
        updateCurrentTouch(screenX, screenY);

        boolean b = touchDrag();
        lastTouch.setBy(currentTouch);
        clickDetector.onTouchDrag(currentTouch);
    }


    public abstract boolean touchUp();


    public boolean touchUpElement(int screenX, int screenY, int pointer, int button) {
        if (appearFactor.isInDestroyState()) return false;
        updateCurrentTouch(screenX, screenY);
        clickDetector.onTouchUp(currentTouch);

        return touchUp();
    }


    public abstract RenderInterfaceElement getRenderSystem();


    public static boolean isTouchInsideRectangle(PointYio touchPoint, RectangleYio touchRectangle) {
        return isTouchInsideRectangle(touchPoint.x, touchPoint.y, touchRectangle);
    }


    public static boolean isTouchInsideRectangle(float touchX, float touchY, RectangleYio rectangleYio) {
        return isTouchInsideRectangle(touchX, touchY, rectangleYio.x, rectangleYio.y, rectangleYio.width, rectangleYio.height);
    }


    public static boolean isTouchInsideRectangle(float touchX, float touchY, float x, float y, float width, float height) {
        return isTouchInsideRectangle(touchX, touchY, x, y, width, height, 0, 0);
    }


    public static boolean isTouchInsideRectangle(float touchX, float touchY, float x, float y, float width, float height, float horOffset, float verOffset) {
        if (touchX < x - horOffset) return false;
        if (touchX > x + width + horOffset) return false;
        if (touchY < y - verOffset) return false;
        if (touchY > y + height + verOffset) return false;
        return true;
    }


    public boolean onMouseWheelScrolled(int amount) {
        return false;
    }


    public void pressArtificially(int keycode) {
        menuControllerYio.currentTouchPoint.set(
                viewPosition.x + viewPosition.width / 2,
                viewPosition.y + viewPosition.height / 2
        );
    }


    public void forceDestroyToEnd() {
        // nothing by default
    }


    public void onSceneEndCreation() {
        // nothing by default
    }


    public boolean compareGvStatus(boolean onTopOfGameView) {
        return this.onTopOfGameView == onTopOfGameView;
    }


    public void onAppPause() {

    }


    public void onAppResume() {

    }


    public void setSceneOwner(SceneYio sceneOwner) {
        this.sceneOwner = sceneOwner;
    }


    public boolean isCaptureTouch() {
        return captureTouch;
    }


    public void setCaptureTouch(boolean captureTouch) {
        this.captureTouch = captureTouch;
    }


    public T setHotkeyKeycode(int hotkeyKeycode) {
        this.hotkeyKeycode = hotkeyKeycode;
        return getThis();
    }


    public boolean acceptsKeycode(int keycode) {
        return hotkeyKeycode != -1 && hotkeyKeycode == keycode;
    }


    public boolean isReturningBackButton() {
        return hotkeyKeycode == Input.Keys.BACK;
    }


    public PointYio getTagPosition(String argument) {
        // default, probably shouldn't be used
        tempPoint.set(
                viewPosition.x + viewPosition.width / 2,
                viewPosition.y + viewPosition.height / 2
        );

        return tempPoint;
    }


    public T setOnTopOfGameView(boolean onTopOfGameView) {
        this.onTopOfGameView = onTopOfGameView;
        return getThis();
    }


    public boolean isTagTouched(String argument, PointYio touchPoint) {
        return getTagPosition(argument).distanceTo(touchPoint) < 0.1f * GraphicsYio.dim;
    }


    public boolean isActive() {
        return activationFactor.isInAppearState();
    }


    public void activate() {
        activationFactor.appear(MovementType.approach, 1.66);
    }


    public void deactivate() {
        activationFactor.destroy(MovementType.lighty, 2.1);
    }


    public float getActivationAlpha() {
        if (activationFactor.getValue() == 1) return 1;
        return 0.2f + 0.8f * activationFactor.getValue();
    }


    public boolean isAllowedToAppear() {
        if (conditionAllowedToAppear != null) {
            return conditionAllowedToAppear.get();
        }
        return true;
    }


    public T setAllowedToAppear(ConditionYio conditionAllowedToAppear) {
        this.conditionAllowedToAppear = conditionAllowedToAppear;
        return getThis();
    }


    public String getKey() {
        return key;
    }


    public boolean hasKey(String key) {
        return this.key != null && this.key.equals(key);
    }


    @Override
    public String toString() {
        if (getKey() != null) {
            return "[" + getClass().getSimpleName() + ", " + getKey() + "]";
        }
        return getClass().getSimpleName();
    }


    public boolean isAlphaEnabled() {
        return alphaEnabled || activationFactor.getValue() < 1;
    }


    public T setAlphaEnabled(boolean alphaEnabled) {
        this.alphaEnabled = alphaEnabled;
        return getThis();
    }


    public boolean hasParent() {
        return parent != null && parent != screen;
    }


    public void enableReverseAnimMode() {
        reverseAnimMode = true;
    }


    public GameController getGameController() {
        return menuControllerYio.yioGdxGame.gameController;
    }


    public ObjectsLayer getObjectsLayer() {
        return getGameController().objectsLayer;
    }


    public ViewableModel getViewableModel() {
        ObjectsLayer objectsLayer = getObjectsLayer();
        if (objectsLayer == null) return null;
        return objectsLayer.viewableModel;
    }


    public NetRoot getNetRoot() {
        return menuControllerYio.yioGdxGame.netRoot;
    }


    public boolean isResistantToAutoDestroy() {
        return resistantToAutoDestroy;
    }


    protected RectangleYio getHookPositionForChildren() {
        return viewPosition;
    }


    public T setResistantToAutoDestroy(boolean resistantToAutoDestroy) {
        this.resistantToAutoDestroy = resistantToAutoDestroy;
        return getThis();
    }
}
