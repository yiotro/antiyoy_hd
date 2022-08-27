package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class UmSelector {

    UnitsManager unitsManager;
    public ViewableUnit targetUnit;
    public CircleYio viewPosition;
    public FactorYio appearFactor;


    public UmSelector(UnitsManager unitsManager) {
        this.unitsManager = unitsManager;
        targetUnit = null;
        viewPosition = new CircleYio();
        appearFactor = new FactorYio();
    }


    public void move() {
        appearFactor.move();
        updateViewPosition();
    }


    private void updateViewPosition() {
        if (targetUnit == null) return;
        Hex hex = targetUnit.hex;
        viewPosition.center.setBy(hex.position.center);
        viewPosition.radius = getRadiusValue() * 1.1f * hex.position.radius;
    }


    private float getRadiusValue() {
        if (appearFactor.isInAppearState()) {
            return 1.2f - 0.2f * appearFactor.getValue();
        }
        return appearFactor.getValue();
    }


    public void onUnitSelected(ViewableUnit viewableUnit) {
        targetUnit = viewableUnit;
        appearFactor.reset();
        appearFactor.appear(MovementType.approach, 8);
    }


    public void onUnitDeselected(ViewableUnit viewableUnit) {
        if (viewableUnit != targetUnit) {
            System.out.println("UmSelector.onUnitDeselected: problem detected");
        }
        targetUnit = null;
        appearFactor.destroy(MovementType.lighty, 7);
    }
}
