package yio.tro.onliyoy.stuff;

import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class SelectionEngineYio implements ReusableYio {

    public boolean selected;
    public FactorYio factorYio;


    public SelectionEngineYio() {
        selected = false;
        factorYio = new FactorYio();
    }


    @Override
    public void reset() {
        selected = false;
        factorYio.reset();
    }


    public void move() {
        if (!selected) return;

        factorYio.move();

        if (factorYio.getValue() == 0) {
            selected = false;
        }
    }


    public float getAlpha() {
        return 0.33f * factorYio.getValue();
    }


    public boolean isSelected() {
        return selected;
    }


    public void applySelection() {
        selected = true;
        factorYio.setValues(1, 0);
        factorYio.destroy(MovementType.lighty, 2.6);
    }
}
