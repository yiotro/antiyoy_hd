package yio.tro.onliyoy.menu.elements.forefinger;

import com.badlogic.gdx.graphics.OrthographicCamera;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.general.CameraController;
import yio.tro.onliyoy.game.tutorial.ScriptManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;
import java.util.Iterator;

public class ForefingerElement extends InterfaceElement<ForefingerElement> {

    ForefingerModeType mode;
    public PointYio defTarget, hook;
    public float touchOffset, radius, viewRadius;
    public FactorYio selectionFactor, effectFactor, blackoutFactor;
    boolean touched, readyForEffect, readyForBlackout;
    public float effectRadius;
    RepeatYio<ForefingerElement> repeatStartEffect, repeatStartBlackout;
    public RectangleYio blackoutPosition;
    public ArrayList<RectangleYio> blackoutBorders;
    InterfaceElement uiTarget;
    String tagArgument;
    private float blackoutRadius;
    double angle, cutAngle;
    public double viewAngle;
    public PointYio viewPoint;
    RepeatYio<ForefingerElement> repeatAdjustAngle;
    public Hex hexTarget;


    public ForefingerElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);

        defTarget = new PointYio();
        hook = new PointYio();
        mode = ForefingerModeType.def;
        viewRadius = 0;
        selectionFactor = new FactorYio();
        touched = false;
        effectFactor = new FactorYio();
        effectRadius = 0;
        readyForEffect = false;
        blackoutFactor = new FactorYio();
        readyForBlackout = false;
        blackoutPosition = new RectangleYio();
        blackoutBorders = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            blackoutBorders.add(new RectangleYio());
        }
        uiTarget = null;
        tagArgument = null;
        viewAngle = 0;
        resetAngle();
        cutAngle = 0.01;
        viewPoint = new PointYio();
        hexTarget = null;

        initMetrics();
        initRepeats();
    }


    private void initRepeats() {
        repeatStartEffect = new RepeatYio<ForefingerElement>(this, -1) {
            @Override
            public void performAction() {
                parent.startEffect();
            }
        };

        repeatStartBlackout = new RepeatYio<ForefingerElement>(this, -1) {
            @Override
            public void performAction() {
                parent.startBlackout();
            }
        };

        repeatAdjustAngle = new RepeatYio<ForefingerElement>(this, 12) {
            @Override
            public void performAction() {
                parent.checkToAdjustAngleByScreenBorders();
            }
        };
    }


    private void initMetrics() {
        radius = 0.05f * GraphicsYio.width;
        touchOffset = 2 * radius;
        blackoutRadius = 8 * radius;
    }


    @Override
    protected ForefingerElement getThis() {
        return this;
    }


    private void resetAngle() {
        setAngle(Math.PI / 2);
    }


    public void setAngle(double angle) {
        this.angle = angle;
        adjustViewAngle();
    }


    public void adjustViewAngle() {
        while (viewAngle > angle + Math.PI) {
            viewAngle -= 2 * Math.PI;
        }

        while (viewAngle < angle - Math.PI) {
            viewAngle += 2 * Math.PI;
        }
    }


    @Override
    public void onMove() {
        updateViewRadius();
        updateHook();
        updateViewPosition();
        updateViewPoint();
        moveSelection();
        moveEffect();
        moveBlackout();
        updateViewAngle();
        repeatAdjustAngle.move();
    }


    protected void updateViewAngle() {
        if (angle == viewAngle) return;

        viewAngle += 0.15f * (angle - viewAngle);

        if (Math.abs(angle - viewAngle) < cutAngle) {
            viewAngle = angle;
        }
    }


    private void moveBlackout() {
        if (readyForBlackout) {
            repeatStartBlackout.move();
            return;
        }

        blackoutFactor.move();
        updateBlackoutPosition();
        updateBlackoutBorders();
    }


    private void updateBlackoutBorders() {
        Iterator<RectangleYio> iterator = blackoutBorders.iterator();

        iterator.next().set(
                0, 0,
                GraphicsYio.width, blackoutPosition.y
        );

        iterator.next().set(
                0, blackoutPosition.y,
                blackoutPosition.x, blackoutPosition.height
        );

        iterator.next().set(
                0, blackoutPosition.y + blackoutPosition.height,
                GraphicsYio.width, GraphicsYio.height - (blackoutPosition.y + blackoutPosition.height)
        );

        iterator.next().set(
                blackoutPosition.x + blackoutPosition.width, blackoutPosition.y,
                GraphicsYio.width - (blackoutPosition.x + blackoutPosition.width), blackoutPosition.height
        );
    }


    private void updateBlackoutPosition() {
        blackoutPosition.x = hook.x - blackoutRadius;
        blackoutPosition.y = hook.y - blackoutRadius;
        blackoutPosition.width = 2 * blackoutRadius;
        blackoutPosition.height = 2 * blackoutRadius;
    }


    public void startBlackoutWithDelay() {
        blackoutFactor.setValues(0, 0);
        blackoutFactor.stop();

        readyForBlackout = true;

        repeatStartBlackout.setCountDown(10);
    }


    public void startBlackout() {
        readyForBlackout = false;

        blackoutFactor.setValues(0, 0);
        blackoutFactor.appear(MovementType.approach, 2.5);
    }


    private void moveEffect() {
        if (readyForEffect) {
            repeatStartEffect.move();
            return;
        }

        effectFactor.move();

        effectRadius = 3 * effectFactor.getValue() * radius;
    }


    public void startEffectWithDelay() {
        readyForEffect = true;

        repeatStartEffect.setCountDown(15);
    }


    public void startEffect() {
        readyForEffect = false;

        effectFactor.setValues(0, 0);
        effectFactor.appear(MovementType.approach, 2.5);
    }


    private void moveSelection() {
        if (touched) return;

        selectionFactor.move();
    }


    public void select() {
        selectionFactor.setValues(1, 0);
        selectionFactor.destroy(MovementType.lighty, 4);
    }


    public boolean isSelected() {
        return selectionFactor.getValue() > 0;
    }


    private void updateViewRadius() {
        viewRadius = (appearFactor.getValue() + 0.5f * selectionFactor.getValue()) * radius;
    }


    private void updateViewPoint() {
        viewPoint.setBy(hook);
        viewPoint.relocateRadial(0.25f * radius, viewAngle - Math.PI / 2);
        viewPoint.relocateRadial(1.2f * radius, viewAngle + Math.PI);
    }


    @Override
    protected void updateViewPosition() {
        viewPosition.x = hook.x - viewRadius;
        viewPosition.y = hook.y - viewRadius;
        viewPosition.width = 2 * viewRadius;
        viewPosition.height = 2 * viewRadius;
    }


    private void updateHook() {
        switch (mode) {
            case def:
                updateHookDefault();
                break;
            case ui_element:
                updateHookForUiElement();
                break;
            case ui_tag:
                updateHookForUiTag();
                break;
            case hex:
                updateHookForHex();
                break;
        }
    }


    private void updateHookForHex() {
        CameraController cameraController = getGameController().cameraController;
        OrthographicCamera orthoCam = cameraController.orthoCam;

        hook.x = (hexTarget.position.center.x - orthoCam.position.x) / orthoCam.zoom + 0.5f * GraphicsYio.width;
        hook.y = (hexTarget.position.center.y - orthoCam.position.y) / orthoCam.zoom + 0.5f * GraphicsYio.height;
    }


    private void updateHookForUiTag() {
        hook.setBy(uiTarget.getTagPosition(tagArgument));
    }


    private void updateHookForUiElement() {
        RectangleYio viewPosition = uiTarget.getViewPosition();
        hook.set(
                viewPosition.x + viewPosition.width / 2,
                viewPosition.y + viewPosition.height / 2
        );
    }


    private void updateHookDefault() {
        hook.setBy(defTarget);
    }


    private void resetTargets() {
        uiTarget = null;
        hexTarget = null;
    }


    public void setTarget(double x, double y) {
        resetTargets();
        mode = ForefingerModeType.def;
        defTarget.set(x, y);
        onTargetSet();
    }


    public void setTarget(InterfaceElement target) {
        resetTargets();
        mode = ForefingerModeType.ui_element;
        uiTarget = target;
        onTargetSet();
    }


    public void setTarget(InterfaceElement target, String tag) {
        resetTargets();
        mode = ForefingerModeType.ui_tag;
        uiTarget = target;
        tagArgument = tag;
        onTargetSet();
    }


    public void setTarget(Hex hex) {
        resetTargets();
        mode = ForefingerModeType.hex;
        hexTarget = hex;

        CameraController cameraController = getGameController().cameraController;
        if (!cameraController.isPointInViewFrame(hexTarget.position.center, 0)) {
            cameraController.focusOnPoint(hexTarget.position.center);
        }

        onTargetSet();
    }


    private void onTargetSet() {
        startEffectWithDelay();
        startBlackoutWithDelay();
        updateAngle();
        checkToShowNotification();
        deactivateAllOtherUiElements();
    }


    private void deactivateAllOtherUiElements() {
        if (uiTarget == null && hexTarget == null) return;
        for (InterfaceElement visibleElement : menuControllerYio.getVisibleElements()) {
            if (visibleElement == uiTarget) continue;
            visibleElement.deactivate();
        }
    }


    private void checkToShowNotification() {
        updateHook();

        if (hook.y > 0.9f * GraphicsYio.height) return;

        if (mode == ForefingerModeType.ui_element && uiTarget instanceof ButtonYio) {
            Scenes.notification.show("press_button", false);
        }

        if (mode == ForefingerModeType.hex) {
            Scenes.notification.show("tap_hex", false);
        }
    }


    private void updateAngle() {
        viewAngle = 0.75 * Math.PI;
        resetAngle();
        checkToAdjustAngleByScreenBorders();
    }


    private void checkToAdjustAngleByScreenBorders() {
        updateHook();
        if (!isHookTooCloseToScreenBorders()) return;

        tempPoint.set(GraphicsYio.width / 2, GraphicsYio.height / 2);
        setAngle(tempPoint.angleTo(hook));
    }


    boolean isHookTooCloseToScreenBorders() {
        float offset = 4 * radius;
        if (hook.x < offset) return true;
        if (hook.y < offset) return true;
        if (hook.x > GraphicsYio.width - offset) return true;
        if (hook.y > GraphicsYio.height - offset) return true;

        return false;
    }


    @Override
    public void onDestroy() {
        touched = false;

        blackoutFactor.destroy(MovementType.lighty, 4);
        notifyScriptManager();
        hideNotification();
        activateAllUiElements();
    }


    private void activateAllUiElements() {
        for (InterfaceElement element : menuControllerYio.getInterfaceElements()) {
            if (element.isActive()) continue;
            element.activate();
        }
    }


    private void notifyScriptManager() {
        ScriptManager scriptManager = menuControllerYio.yioGdxGame.gameController.scriptManager;
        scriptManager.prepareToExecuteNextScriptFaster();
    }


    private void hideNotification() {
        Scenes.notification.destroy();
    }


    @Override
    public void deactivate() {
        // can't deactivate forefinger
    }


    @Override
    public void onAppear() {

    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        touched = isTouchValid();

        if (touched) {
            select();
            return false;
        }

        return true;
    }


    @Override
    public boolean touchDrag() {
        if (touched) {

        }

        return false;
    }


    @Override
    public boolean touchUp() {
        if (touched) {
            touched = false;

            if (isClicked()) {
                onClick();
            }

            return false;
        }

        return false;
    }


    private void onClick() {
        if (isTouchValid()) {
            destroy();
        }
    }


    private boolean isTouchValid() {
        if (appearFactor.getValue() < 1) return false;
        getGameController().updateTouchPoints((int) currentTouch.x, (int) currentTouch.y);

        switch (mode) {
            default:
                return false;
            case def:
                return currentTouch.distanceTo(hook) < radius + touchOffset;
            case ui_element:
                return uiTarget.isTouchedBy(currentTouch);
            case ui_tag:
                return uiTarget.isTagTouched(tagArgument, currentTouch);
            case hex:
                PointYio currentTouchConverted = getGameController().currentTouchConverted;
                Hex hex = getViewableModel().getClosestHex(currentTouchConverted);
                if (hex == null) return false;
                if (hexTarget != hex) return false;
                return isTouchInsideHex(hex, currentTouchConverted);
        }
    }


    private boolean isTouchInsideHex(Hex hex, PointYio touchPoint) {
        return hex.position.center.distanceTo(touchPoint) < getViewableModel().getHexRadius();
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderForefinger;
    }

}
