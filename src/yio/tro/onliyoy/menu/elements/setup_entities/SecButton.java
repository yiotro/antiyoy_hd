package yio.tro.onliyoy.menu.elements.setup_entities;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SecButton {

    SingleEntityConfigureElement secElement;
    public CircleYio position;
    public CircleYio iconPosition;
    protected PointYio delta;
    public EseType eseType;
    public RectangleYio selectionPosition;
    public SelectionEngineYio selectionEngineYio;
    public FactorYio appearFactor;
    public SecType secType;
    boolean specialAnimMode;
    public HColor color;
    public FactorYio borderFactor;


    public SecButton(SingleEntityConfigureElement secElement) {
        this.secElement = secElement;
        position = new CircleYio();
        iconPosition = new CircleYio();
        delta = new PointYio();
        selectionPosition = new RectangleYio();
        selectionEngineYio = new SelectionEngineYio();
        appearFactor = new FactorYio();
        secType = null;
        eseType = null;
        color = null;
        borderFactor = new FactorYio();
    }


    void move() {
        appearFactor.move();
        moveBorderFactor();
        updatePosition();
        updateIconPosition();
        updateSelectionPosition();
        moveSelectionEngine();
    }


    private void moveBorderFactor() {
        borderFactor.move();
        if (!borderFactor.isInAppearState() && appearFactor.getValue() > 0.8) {
            borderFactor.appear(MovementType.approach, 1.8);
        }
        if (!borderFactor.isInDestroyState() && appearFactor.isInDestroyState()) {
            borderFactor.destroy(MovementType.lighty, 40);
        }
    }


    private void moveSelectionEngine() {
        if (secElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void updateSelectionPosition() {
        selectionPosition.setBy(position);
        selectionPosition.increase(0.016f * GraphicsYio.width);
    }


    public float getIconAlpha() {
        if (specialAnimMode) return 1;
        return appearFactor.getValue();
    }


    private void updateIconPosition() {
        iconPosition.center.setBy(position.center);
        iconPosition.radius = secElement.getFactor().getValue() * 0.35f * secElement.iSize;
        if (specialAnimMode) {
            CircleYio target = secElement.parentItem.iconPosition;
            float fValue = 1 - secElement.getFactor().getValue();
            iconPosition.center.x += fValue * (target.center.x - iconPosition.center.x);
            iconPosition.center.y += fValue * (target.center.y - iconPosition.center.y);
            iconPosition.radius += fValue * (target.radius - iconPosition.radius);
            iconPosition.radius = Math.max(target.radius, iconPosition.radius);
        }
    }


    private void updatePosition() {
        position.center.x = secElement.getPosition().x + delta.x;
        position.center.y = secElement.getViewPosition().y + delta.y;
        position.radius = 0.47f * secElement.iSize;
    }


    boolean isTouchedBy(PointYio touchPoint) {
        if (Math.abs(position.center.x - touchPoint.x) > 0.5f * secElement.iSize) return false;
        if (Math.abs(position.center.y - touchPoint.y) > 0.5f * secElement.iSize) return false;
        return true;
    }


    public void onAppear() {
        specialAnimMode = false;
    }


    public void setColor(HColor color) {
        this.color = color;
    }


    public void setEseType(EseType eseType) {
        this.eseType = eseType;
    }


    public void setSecType(SecType secType) {
        this.secType = secType;
    }
}
