package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.JumpEngineYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class ViewableUnit {

    UnitsManager unitsManager;
    public FactorYio appearFactor;
    public PieceType pieceType;
    public CircleYio viewPosition;
    public PointYio targetPosition;
    public PointYio previousPosition;
    public FactorYio relocationFactor;
    private float targetRadius;
    public Hex hex;
    public boolean alive;
    public FactorYio selectionFactor;
    private JumpEngineYio jumpEngineYio;
    public VuSpawnType spawnType;
    public VuDestroyType destroyType;
    private Hex moHex; // merge out hex
    FactorYio enlargeFactor;
    long enlargeEndTime;


    public ViewableUnit(UnitsManager unitsManager) {
        this.unitsManager = unitsManager;
        targetRadius = 0.7f * unitsManager.viewableModel.objectsLayer.getHexRadius();
        pieceType = null;
        viewPosition = new CircleYio();
        targetPosition = new PointYio();
        previousPosition = new PointYio();
        relocationFactor = new FactorYio();
        appearFactor = new FactorYio();
        relocationFactor.setValue(1);
        selectionFactor = new FactorYio();
        jumpEngineYio = new JumpEngineYio();
        hex = null;
        alive = true;
        spawnType = VuSpawnType.normal;
        destroyType = VuDestroyType.normal;
        moHex = null;
        enlargeFactor = new FactorYio();
        enlargeEndTime = 0;
    }


    void move() {
        moveAppearFactor();
        updateViewRadius();
        relocationFactor.move();
        updateViewPosition();
        moveJumping();
        moveSelection();
        moveEnlarge();
    }


    private void moveEnlarge() {
        enlargeFactor.move();
        if (enlargeFactor.getValue() == 0) return;
        viewPosition.radius += 0.33f * enlargeFactor.getValue() * viewPosition.radius;
        if (!enlargeFactor.isInDestroyState() && System.currentTimeMillis() > enlargeEndTime) {
            enlargeFactor.destroy(MovementType.lighty, 7);
        }
    }


    private void moveJumping() {
        jumpEngineYio.move();
        if (jumpEngineYio.getValue() == 0) return;
        viewPosition.center.y += 0.2f * jumpEngineYio.getValue() * (1 - selectionFactor.getValue()) * targetRadius;
    }


    public void doJump() {
        if (unitsManager.viewableModel.getGameMode() == GameMode.replay) return;
        jumpEngineYio.apply(1, 1.1);
    }


    private void moveSelection() {
        selectionFactor.move();
        if (selectionFactor.getValue() == 0) return;
        if (destroyType != VuDestroyType.normal) return;
        viewPosition.radius = (appearFactor.getValue() + 0.25f * selectionFactor.getValue()) * targetRadius;
    }


    private void moveAppearFactor() {
        if (!appearFactor.move()) return;
        if (appearFactor.isInDestroyState() && appearFactor.getValue() == 0) {
            alive = false;
        }
    }


    private void updateViewRadius() {
        if (isCurrentlyTransparent()) {
            viewPosition.radius = targetRadius;
            return;
        }
        if (destroyType == VuDestroyType.merge_out) {
            if (appearFactor.getValue() > 0.1) {
                viewPosition.radius = targetRadius;
            } else {
                viewPosition.radius = 10f * appearFactor.getValue() * targetRadius;
            }
            return;
        }
        viewPosition.radius = appearFactor.getValue() * targetRadius;
    }


    public boolean isConstructionAnimationActive() {
        return spawnType == VuSpawnType.constructed && appearFactor.isInAppearState() && appearFactor.getValue() < 1;
    }


    public boolean isCurrentlyTransparent() {
        if (destroyType == VuDestroyType.merge_out) return false;
        if (isConstructionAnimationActive()) return true;
        if (appearFactor.isInDestroyState()) return true;
        return false;
    }


    private void updateViewPosition() {
        viewPosition.center.setBy(previousPosition);
        applyRelocationToViewPosition();
        checkForSpawnAnimation();
        checkForDestroyAnimation();
    }


    private void checkForDestroyAnimation() {
        if (!appearFactor.isInDestroyState()) return;
        switch (destroyType) {
            default:
            case normal:
                viewPosition.center.y += (1 - appearFactor.getValue()) * 0.7f * viewPosition.radius;
                break;
            case merge_out:
                viewPosition.center.relocateRadial(
                        (1 - appearFactor.getValue()) * viewPosition.center.distanceTo(moHex.position.center),
                        viewPosition.center.angleTo(moHex.position.center)
                );
                break;
            case merge_in:
                break;
            case quick:
                break;
        }
    }


    private void checkForSpawnAnimation() {
        if (!appearFactor.isInAppearState()) return;
        switch (spawnType) {
            default:
            case normal:
                break;
            case constructed:
                if (appearFactor.getValue() == 1) break;
                viewPosition.center.y -= (1 - appearFactor.getValue()) * viewPosition.radius;
                break;
            case merge:
                break;
        }
    }


    private void applyRelocationToViewPosition() {
        if (relocationFactor.getValue() == 0) return;
        if (relocationFactor.getValue() == 1) {
            viewPosition.center.setBy(targetPosition);
            return;
        }
        viewPosition.center.x += relocationFactor.getValue() * (targetPosition.x - viewPosition.center.x);
        viewPosition.center.y += relocationFactor.getValue() * (targetPosition.y - viewPosition.center.y);
    }


    public void spawn(VuSpawnType spawnType, Hex hex, PieceType pieceType) {
        this.spawnType = spawnType;
        this.hex = hex;
        this.pieceType = pieceType;
        targetPosition.setBy(hex.position.center);
        updateViewPosition();
        launchAppearFactor();
    }


    private void launchAppearFactor() {
        switch (spawnType) {
            default:
            case normal:
                appearFactor.appear(MovementType.approach, 8);
                break;
            case constructed:
                appearFactor.appear(MovementType.approach, 6.6);
                break;
            case merge:
                appearFactor.appear(MovementType.approach, 12);
                break;
        }
    }


    public void relocate(Hex targetHex, boolean quick) {
        hex = targetHex;
        previousPosition.setBy(viewPosition.center);
        targetPosition.setBy(targetHex.position.center);
        relocationFactor.reset();
        jumpEngineYio.reset();
        launchRelocationFactor(quick);
    }


    private void launchRelocationFactor(boolean quick) {
        if (quick) {
            relocationFactor.appear(MovementType.approach, 8);
        } else {
            relocationFactor.appear(MovementType.inertia, 3);
        }
    }


    public void kill() {
        appearFactor.destroy(MovementType.lighty, 9);
    }


    public void enableMergeOutAnimation(Hex targetHex) {
        moHex = targetHex;
        setDestroyType(VuDestroyType.merge_out);
        appearFactor.destroy(MovementType.approach, 6);
    }


    public void enableMergeInAnimation() {
        setDestroyType(VuDestroyType.merge_in);
        appearFactor.destroy(MovementType.approach, 6);
    }


    public void setDestroyType(VuDestroyType destroyType) {
        this.destroyType = destroyType;
    }


    public void applyEnlarge(long delay) {
        enlargeFactor.appear(MovementType.approach, 12);
        enlargeEndTime = System.currentTimeMillis() + delay;
    }


    public boolean isRelocating() {
        return relocationFactor.getValue() < 1;
    }


    public boolean isCurrentlyVisible() {
        return alive && getFrame().intersects(viewPosition);
    }


    private RectangleYio getFrame() {
        return getObjectsLayer().gameController.cameraController.frame;
    }


    public void select() {
        if (isSelected()) return;
        if (!unitsManager.viewableModel.readinessManager.isReady(hex)) return;
        selectionFactor.appear(MovementType.approach, 8);
        unitsManager.viewableModel.onUnitSelected(this);

    }


    public void deselect() {
        if (!isSelected()) return;
        unitsManager.selector.onUnitDeselected(this);
        unitsManager.viewableModel.moveZoneViewer.onUnitDeselected(this);
        selectionFactor.destroy(MovementType.lighty, 7);
    }


    public boolean isSelected() {
        return selectionFactor.isInAppearState();
    }


    void forceAppearance() {
        appearFactor.setValue(1);
        move();
    }


    private ObjectsLayer getObjectsLayer() {
        return unitsManager.viewableModel.objectsLayer;
    }


    @Override
    public String toString() {
        return "[ViewableUnit: " +
                pieceType + " on " + hex +
                "]";
    }
}
