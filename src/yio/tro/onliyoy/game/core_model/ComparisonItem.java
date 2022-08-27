package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class ComparisonItem implements ReusableYio {

    public Hex currentHex;
    public Hex targetHex;


    public ComparisonItem() {
        reset();
    }


    @Override
    public void reset() {
        currentHex = null;
        targetHex = null;
    }


    public void setCurrentHex(Hex currentHex) {
        this.currentHex = currentHex;
    }


    public void setTargetHex(Hex targetHex) {
        this.targetHex = targetHex;
    }


    @Override
    public String toString() {
        return "[" +
                getClass().getSimpleName() + " " +
                currentHex + " -> " +
                targetHex +
                "]";
    }
}
