package yio.tro.onliyoy.menu.elements.keyboard;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class CbTextField {

    CustomKeyboardElement customKeyboardElement;
    public RectangleYio viewPosition;
    public RectangleYio targetPosition;
    public CornerEngineYio cornerEngineYio;
    PointYio animationPoint;
    public RenderableTextYio title;
    public RectangleYio cursorPosition;
    public FactorYio cursorFactor;
    private long timeToBlink;
    private float hOffset;
    public RectangleYio highlightPosition;
    public FactorYio highlightFactor;
    public RenderableTextYio hintViewText;
    public FactorYio hintFactor;


    public CbTextField(CustomKeyboardElement customKeyboardElement) {
        this.customKeyboardElement = customKeyboardElement;
        initTargetPosition();
        viewPosition = new RectangleYio();
        cornerEngineYio = new CornerEngineYio();
        animationPoint = new PointYio();
        cursorFactor = new FactorYio();
        hOffset = 0.03f * GraphicsYio.width;
        highlightPosition = new RectangleYio();
        highlightFactor = new FactorYio();
        hintFactor = new FactorYio();
        initCursorPosition();
        initTitle();
        initHintViewText();
    }


    private void initHintViewText() {
        hintViewText = new RenderableTextYio();
        hintViewText.setFont(Fonts.gameFont);
        hintViewText.setString("");
        hintViewText.updateMetrics();
    }


    private void initCursorPosition() {
        cursorPosition = new RectangleYio();
        cursorPosition.width = 2 * GraphicsYio.borderThickness;
        cursorPosition.height = 0.4f * targetPosition.height;
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
    }


    private void initTargetPosition() {
        targetPosition = new RectangleYio();
        targetPosition.set(
                0.1f * GraphicsYio.width,
                0.42f * GraphicsYio.height,
                0.8f * GraphicsYio.width,
                0.08f * GraphicsYio.height
        );
    }


    void onAppear() {
        cursorFactor.reset();
        updateAnimationPoint();
        setString("");
        timeToBlink = System.currentTimeMillis();
        launchHintFactor();
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
        updateTitlePosition();
        moveCursorFactor();
        updateCursorPosition();
        moveHighlightStuff();
        moveHint();
    }


    private void moveHint() {
        hintFactor.move();
        hintViewText.centerVertical(viewPosition);
        hintViewText.position.x = viewPosition.x + 1.5f * hOffset;
        hintViewText.updateBounds();
    }


    public void launchHintFactor() {
        hintFactor.appear(MovementType.approach, 3.5);
    }


    public void destroyHintFactor() {
        hintFactor.destroy(MovementType.lighty, 15);
    }


    private void moveHighlightStuff() {
        highlightFactor.move();
        if (highlightFactor.getValue() == 0) return;
        updateHighlightPosition();
    }


    private void updateHighlightPosition() {
        highlightPosition.setBy(title.bounds);
        highlightPosition.increase(0.015f * GraphicsYio.width);
    }


    public boolean isHighlighted() {
        return highlightFactor.getValue() > 0;
    }


    void applyHighlight() {
        highlightFactor.setValue(1);
        highlightFactor.stop();
    }


    void hideHighlight() {
        highlightFactor.destroy(MovementType.lighty, 6);
    }


    private void updateCursorPosition() {
        cursorPosition.y = viewPosition.y + viewPosition.height / 2 - cursorPosition.height / 2;
        float tx = title.bounds.x + title.bounds.width + GraphicsYio.borderThickness;
        if (cursorPosition.x == tx) return;
        if (cursorPosition.x < tx || Math.abs(cursorPosition.x - tx) < GraphicsYio.borderThickness) {
            cursorPosition.x = tx;
            return;
        }
        cursorPosition.x += getCursorSpeed() * (tx - cursorPosition.x);
    }


    private double getCursorSpeed() {
        if (customKeyboardElement.deletionMode) return 1;
        return 0.2;
    }


    void addText(String deltaText) {
        if (isHighlighted()) {
            setString("");
            highlightFactor.reset();
        }
        String newString = title.string + deltaText;
        if (customKeyboardElement.capitalizeMode && title.string.length() == 0) {
            newString = Yio.getCapitalizedString(newString);
        }
        setString(newString);
        checkToApplyLimit();
    }


    private void checkToApplyLimit() {
        while (title.width > targetPosition.width - 2 * hOffset) {
            deleteLastCharacter();
        }
    }


    void deleteLastCharacter() {
        String string = title.string;
        if (string.length() == 0) return;
        if (isHighlighted()) {
            hideHighlight();
            setString("");
            return;
        }
        setString(string.substring(0, string.length() - 1));
    }


    private void moveCursorFactor() {
        cursorFactor.move();
        if (cursorFactor.getValue() == 1 && !cursorFactor.isInDestroyState() && System.currentTimeMillis() > timeToBlink) {
            cursorFactor.destroy(MovementType.lighty, 1.4);
        }
        if (cursorFactor.getValue() == 0 && !cursorFactor.isInAppearState()) {
            cursorFactor.appear(MovementType.approach, 2.5);
            timeToBlink = System.currentTimeMillis() + 500;
        }
    }


    private void updateTitlePosition() {
        title.centerVertical(viewPosition);
        title.position.x = viewPosition.x + hOffset;
        title.updateBounds();
    }


    private void moveCornerEngine() {
        cornerEngineYio.move(viewPosition, customKeyboardElement.getFactor());
    }


    void setString(String string) {
        title.setString(string);
        title.updateMetrics();
        if (title.string.length() == 0) {
            launchHintFactor();
        } else {
            destroyHintFactor();
        }
    }


    private void updateViewPosition() {
        viewPosition.set(animationPoint.x, animationPoint.y, 0, 0);
        float value = customKeyboardElement.getFactor().getValue();
        viewPosition.x += value * (targetPosition.x - viewPosition.x);
        viewPosition.y += value * (targetPosition.y - viewPosition.y);
        viewPosition.width += value * (targetPosition.width - viewPosition.width);
        viewPosition.height += value * (targetPosition.height - viewPosition.height);
    }
}
