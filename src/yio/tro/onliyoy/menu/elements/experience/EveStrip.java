package yio.tro.onliyoy.menu.elements.experience;

import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;

public class EveStrip {

    EvePanel evePanel;
    public RectangleYio position;
    public RectangleYio solidPartPosition;
    public RectangleYio deltaPartPosition;
    double startRatio;
    double endRatio;


    public EveStrip(EvePanel evePanel) {
        this.evePanel = evePanel;
        position = new RectangleYio();
        solidPartPosition = new RectangleYio();
        deltaPartPosition = new RectangleYio();
    }


    public void move() {
        updatePosition();
        updateSolidPartPosition();
        updateDeltaPartPosition();
    }


    private void updateDeltaPartPosition() {
        if (evePanel.index != 0) return;
        deltaPartPosition.setBy(position);
        deltaPartPosition.x = solidPartPosition.x + solidPartPosition.width;
        float fillFactorValue = evePanel.experienceViewElement.fillFactor.getValue();
        deltaPartPosition.width = (float) ((endRatio - startRatio) * position.width * fillFactorValue);
    }


    void setValues(double startRatio, double endRatio) {
        this.startRatio = startRatio;
        this.endRatio = endRatio;
    }


    private void updateSolidPartPosition() {
        solidPartPosition.setBy(position);
        solidPartPosition.width = (float) (startRatio * position.width);
    }


    private void updatePosition() {
        position.setBy(evePanel.viewPosition);
        position.increase(-evePanel.horOffset);
        position.height = 0.012f * GraphicsYio.height;
        position.y = evePanel.viewPosition.y + 0.03f * GraphicsYio.height;
    }
}
