package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class DefenseIndicator implements ReusableYio {

    DefenseViewer defenseViewer;
    public boolean alive;
    Hex parentHex;
    Hex targetHex;
    public int defenseValue;
    public CircleYio viewPosition;
    public FactorYio appearFactor;
    public FactorYio lifeFactor;
    private float defRadius;
    boolean readyToAppear;
    long timeToAppear;


    public DefenseIndicator(DefenseViewer defenseViewer) {
        this.defenseViewer = defenseViewer;
        viewPosition = new CircleYio();
        appearFactor = new FactorYio();
        lifeFactor = new FactorYio();
    }


    @Override
    public void reset() {
        alive = true;
        parentHex = null;
        targetHex = null;
        defenseValue = -1;
        readyToAppear = false;
        timeToAppear = 0;
        viewPosition.reset();
        appearFactor.reset();
        lifeFactor.reset();
    }


    void move() {
        if (!alive) return;
        appearFactor.move();
        lifeFactor.move();
        checkToAppear();
        checkToDie();
        checkToUpdateAliveStatus();
        updateViewPosition();
    }


    private void checkToAppear() {
        if (!readyToAppear) return;
        if (System.currentTimeMillis() < timeToAppear) return;
        readyToAppear = false;
        appearFactor.appear(MovementType.approach, 6);
    }


    private void checkToDie() {
        if (lifeFactor.getValue() < 1) return;
        if (appearFactor.isInDestroyState()) return;
        kill();
    }


    public void kill() {
        appearFactor.destroy(MovementType.lighty, 4);
    }


    private void updateViewPosition() {
        if (appearFactor.isInDestroyState()) {
            viewPosition.center.setBy(targetHex.position.center);
            viewPosition.radius = (1 + 0.25f * (1 - appearFactor.getValue())) * defRadius;
            return;
        }
        viewPosition.center.setBy(parentHex.position.center);
        viewPosition.center.x += appearFactor.getValue() * (targetHex.position.center.x - parentHex.position.center.x);
        viewPosition.center.y += appearFactor.getValue() * (targetHex.position.center.y - parentHex.position.center.y);
    }


    private void checkToUpdateAliveStatus() {
        if (!appearFactor.isInDestroyState()) return;
        if (appearFactor.getValue() > 0) return;
        alive = true;
    }


    void spawn(Hex parentHex, Hex targetHex, int defenseValue, long delay) {
        this.parentHex = parentHex;
        this.targetHex = targetHex;
        this.defenseValue = defenseValue;
        defRadius = 0.55f * parentHex.position.radius;
        viewPosition.setRadius(defRadius);
        appearFactor.reset();
        lifeFactor.reset();
        lifeFactor.appear(MovementType.simple, 0.27);
        readyToAppear = true;
        timeToAppear = System.currentTimeMillis() + delay;
        updateViewPosition();
    }


    public boolean isCurrentlyVisible() {
        if (!alive) return false;
        if (appearFactor.getValue() == 0) return false;
        ObjectsLayer objectsLayer = defenseViewer.viewableModel.objectsLayer;
        GameController gameController = objectsLayer.gameController;
        return gameController.cameraController.isCircleInViewFrame(viewPosition);
    }
}
