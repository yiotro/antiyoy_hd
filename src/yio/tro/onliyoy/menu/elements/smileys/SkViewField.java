package yio.tro.onliyoy.menu.elements.smileys;

import yio.tro.onliyoy.game.core_model.SmileyType;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;

public class SkViewField {

    SmileysKeyboardElement smileysKeyboardElement;
    public RectangleYio viewPosition;
    public RectangleYio targetPosition;
    public CornerEngineYio cornerEngineYio;
    PointYio animationPoint;
    public RectangleYio cursorPosition;
    public FactorYio cursorFactor;
    private long timeToBlink;
    private float hOffset;
    public ArrayList<SkItem> items;


    public SkViewField(SmileysKeyboardElement smileysKeyboardElement) {
        this.smileysKeyboardElement = smileysKeyboardElement;
        initTargetPosition();
        viewPosition = new RectangleYio();
        cornerEngineYio = new CornerEngineYio();
        animationPoint = new PointYio();
        cursorFactor = new FactorYio();
        hOffset = 0.03f * GraphicsYio.width;
        items = new ArrayList<>();
        initCursorPosition();
    }


    private void initCursorPosition() {
        cursorPosition = new RectangleYio();
        cursorPosition.width = 2 * GraphicsYio.borderThickness;
        cursorPosition.height = 0.4f * targetPosition.height;
    }


    private void initTargetPosition() {
        targetPosition = new RectangleYio();
        targetPosition.set(
                0.14f * GraphicsYio.width,
                0.42f * GraphicsYio.height,
                0.72f * GraphicsYio.width,
                0.12f * GraphicsYio.width
        );
    }


    void onAppear() {
        cursorFactor.reset();
        updateAnimationPoint();
        timeToBlink = System.currentTimeMillis();
        items.clear();
    }


    private void updateAnimationPoint() {
        animationPoint.set(
                targetPosition.x + targetPosition.width / 2,
                targetPosition.y + targetPosition.height / 2
        );
    }


    void move() {
        updateViewPosition();
        moveCornerEngine();
        moveCursorFactor();
        updateCursorPosition();
        moveItems();
    }


    private void moveItems() {
        for (SkItem skItem : items) {
            skItem.move(viewPosition, false);
        }
    }


    public void setItems(ArrayList<SmileyType> list) {
        items.clear();
        for (SmileyType smileyType : list) {
            addItem(smileyType);
        }
    }


    void addItem(SmileyType smileyType) {
        if (items.size() >= 10) return;
        SkItem skItem = new SkItem();
        skItem.smileyType = smileyType;
        skItem.setRadius(0.2 * targetPosition.height);
        checkToSyncHypno(skItem);
        items.add(skItem);
        updateDeltas();
        move();
        updateCursorPosition();
    }


    private void checkToSyncHypno(SkItem skItem) {
        if (skItem.smileyType != SmileyType.hypno) return;
        SkItem previousHypnoItem = getItem(SmileyType.hypno);
        if (previousHypnoItem == null) return;
        skItem.angle = previousHypnoItem.angle;
    }


    private SkItem getItem(SmileyType smileyType) {
        for (SkItem skItem : items) {
            if (skItem.smileyType == smileyType) return skItem;
        }
        return null;
    }


    private void updateDeltas() {
        float x = hOffset;
        float y = targetPosition.height / 2;
        for (SkItem skItem : items) {
            float r = skItem.position.radius;
            if (isCondensed(skItem.smileyType)) {
                r *= 0.66f;
            }
            x += r;
            skItem.delta.set(x, y);
            x += r;
        }
    }


    public static boolean isCondensed(SmileyType smileyType) {
        if (smileyType == null) return false;
        switch (smileyType) {
            default:
                return false;
            case zero:
            case one:
            case two:
            case three:
            case four:
            case five:
            case six:
            case seven:
            case eight:
            case nine:
            case exclamation:
            case question:
            case dollar:
                return true;
        }
    }


    private SkItem getLastItem() {
        if (items.size() == 0) return null;
        return items.get(items.size() - 1);
    }


    void removeLastItem() {
        SkItem lastItem = getLastItem();
        if (lastItem == null) return;
        items.remove(lastItem);
    }


    private void updateCursorPosition() {
        cursorPosition.y = viewPosition.y + viewPosition.height / 2 - cursorPosition.height / 2;
        float tx = targetPosition.x + hOffset;
        if (items.size() > 0) {
            SkItem lastItem = getLastItem();
            tx = lastItem.position.center.x + lastItem.viewPosition.width + GraphicsYio.borderThickness;
        }
        if (cursorPosition.x == tx) return;
        if (cursorPosition.x < tx || Math.abs(cursorPosition.x - tx) < GraphicsYio.borderThickness) {
            cursorPosition.x = tx;
            return;
        }
        cursorPosition.x += getCursorSpeed() * (tx - cursorPosition.x);
    }


    private double getCursorSpeed() {
        if (smileysKeyboardElement.deletionMode) return 1;
        return 0.2;
    }


    private void moveCursorFactor() {
        cursorFactor.move();
        if (cursorFactor.getValue() == 1 && !cursorFactor.isInDestroyState() && System.currentTimeMillis() > timeToBlink) {
            cursorFactor.destroy(MovementType.lighty, 1.1);
        }
        if (cursorFactor.getValue() == 0 && !cursorFactor.isInAppearState()) {
            cursorFactor.appear(MovementType.approach, 2.5);
            timeToBlink = System.currentTimeMillis() + 500;
        }
    }


    private void moveCornerEngine() {
        cornerEngineYio.move(viewPosition, smileysKeyboardElement.getFactor());
    }


    private void updateViewPosition() {
        viewPosition.set(animationPoint.x, animationPoint.y, 0, 0);
        float value = smileysKeyboardElement.getFactor().getValue();
        viewPosition.x += value * (targetPosition.x - viewPosition.x);
        viewPosition.y += value * (targetPosition.y - viewPosition.y);
        viewPosition.width += value * (targetPosition.width - viewPosition.width);
        viewPosition.height += value * (targetPosition.height - viewPosition.height);
    }
}
