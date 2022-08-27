package yio.tro.onliyoy.menu.elements.net;

import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class QmsItem implements Comparable<QmsItem> {

    QmsElement qmsElement;
    public CircleYio viewPosition;
    float deltaX;
    float targetRadius;
    public FactorYio appearFactor;
    boolean readyToAppear;
    long appearTime;
    public FactorYio indicatorFactor;
    public CircleYio indicatorPosition;
    int sortValue;
    FactorYio tickleFactor;


    public QmsItem(QmsElement qmsElement) {
        this.qmsElement = qmsElement;
        viewPosition = new CircleYio();
        appearFactor = new FactorYio();
        readyToAppear = true;
        indicatorFactor = new FactorYio();
        indicatorPosition = new CircleYio();
        tickleFactor = new FactorYio();
    }


    void move() {
        checkToAppear();
        appearFactor.move();
        updateViewPosition();
        indicatorFactor.move();
        updateIndicatorPosition();
        moveTickleFactor();
    }


    private void moveTickleFactor() {
        tickleFactor.move();
        if (tickleFactor.isInAppearState() && tickleFactor.getValue() == 1) {
            tickleFactor.destroy(MovementType.lighty, 1.4);
        }
    }


    public void tickle() {
        tickleFactor.appear(MovementType.approach, 10);
    }


    private void updateIndicatorPosition() {
        indicatorPosition.center.setBy(viewPosition.center);
        indicatorPosition.radius = indicatorFactor.getValue() * viewPosition.radius;
    }


    private void checkToAppear() {
        if (!readyToAppear) return;
        if (System.currentTimeMillis() < appearTime) return;
        readyToAppear = false;
        appearFactor.appear(MovementType.approach, 2.5);
    }


    private void updateViewPosition() {
        viewPosition.setRadius((appearFactor.getValue() + 0.25f * tickleFactor.getValue()) * targetRadius);
        RectangleYio titleBounds = qmsElement.title.bounds;
        viewPosition.center.x = titleBounds.x + titleBounds.width / 2 + deltaX;
        viewPosition.center.y = titleBounds.y - 0.01f * GraphicsYio.height - targetRadius;
        if (qmsElement.getFactor().getValue() < 1) {
            viewPosition.center.y += (1 - qmsElement.getFactor().getValue()) * 2 * targetRadius;
        }
    }


    public void setTargetRadius(float targetRadius) {
        this.targetRadius = targetRadius;
    }


    public void setAppearTime(long appearTime) {
        this.appearTime = appearTime;
    }


    public void setIndicate(boolean value) {
        if (value == getCurrentIndicatorValue()) return;
        if (value) {
            indicatorFactor.appear(MovementType.approach, 3.5);
        } else {
            indicatorFactor.destroy(MovementType.lighty, 4.5);
        }
    }


    private boolean getCurrentIndicatorValue() {
        return indicatorFactor.isInAppearState();
    }


    @Override
    public int compareTo(QmsItem o) {
        return sortValue - o.sortValue;
    }
}
