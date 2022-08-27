package yio.tro.onliyoy.menu.elements.setup_entities;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class EseItem implements ReusableYio, Encodeable {

    EntitiesSetupElement esElement;
    public CircleYio position;
    public CircleYio iconPosition;
    protected PointYio delta;
    protected PointYio targetDelta;
    public HColor color;
    public EseType type;
    public RectangleYio selectionPosition;
    public SelectionEngineYio selectionEngineYio;
    public FactorYio appearFactor;


    public EseItem(EntitiesSetupElement esElement) {
        this.esElement = esElement;
        position = new CircleYio();
        iconPosition = new CircleYio();
        delta = new PointYio();
        targetDelta = new PointYio();
        selectionPosition = new RectangleYio();
        selectionEngineYio = new SelectionEngineYio();
        appearFactor = new FactorYio();
    }


    @Override
    public void reset() {
        position.reset();
        iconPosition.reset();
        delta.reset();
        selectionPosition.reset();
        selectionEngineYio.reset();
        targetDelta = new PointYio();
        appearFactor.reset();
        appearFactor.appear(MovementType.approach, 5);
        color = null;
        type = null;
    }


    void move() {
        appearFactor.move();
        updatePosition();
        updateIconPosition();
        updateSelectionPosition();
        moveSelectionEngine();
        moveDelta();
    }


    private void moveDelta() {
        if (delta.x == targetDelta.x && delta.y == targetDelta.y) return;
        if (delta.fastDistanceTo(targetDelta) < GraphicsYio.borderThickness) {
            delta.setBy(targetDelta);
            return;
        }
        delta.x += 0.2 * (targetDelta.x - delta.x);
        delta.y += 0.2 * (targetDelta.y - delta.y);
    }


    private void moveSelectionEngine() {
        if (esElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    void forceDelta() {
        delta.setBy(targetDelta);
    }


    private void updateSelectionPosition() {
        selectionPosition.setBy(position);
        selectionPosition.increase(0.016f * GraphicsYio.width);
    }


    private void updateIconPosition() {
        iconPosition.center.setBy(position.center);
        iconPosition.radius = esElement.getFactor().getValue() * 0.23f * esElement.iSize;
    }


    private void updatePosition() {
        float fValue = esElement.getFactor().getValue();
        if (esElement.calmAnimationMode) {
            fValue = 1;
        }
        position.center.x = esElement.getViewPosition().x + fValue * delta.x;
        position.center.y = esElement.getViewPosition().y + fValue * delta.y;
        position.radius = fValue * appearFactor.getValue() * 0.47f * esElement.iSize;
    }


    boolean isTouchedBy(PointYio touchPoint) {
        if (Math.abs(position.center.x - touchPoint.x) > 0.5f * esElement.iSize) return false;
        if (Math.abs(position.center.y - touchPoint.y) > 0.5f * esElement.iSize) return false;
        return true;
    }


    @Override
    public String encode() {
        return type + " " + color;
    }


    public void setType(EseType type) {
        this.type = type;
    }


    public void setColor(HColor color) {
        this.color = color;
    }
}
