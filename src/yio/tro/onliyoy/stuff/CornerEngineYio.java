package yio.tro.onliyoy.stuff;

import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;

public class CornerEngineYio {

    private float currentRadius;
    private float shadowRadius;
    private float stRadius; // shadow target radius
    private float transitionPoint;
    private float transitionMultiplier;


    public CornerEngineYio() {
        stRadius = MenuRenders.renderShadow.getDefCornerRadius();
        transitionPoint = 0.66f;
        transitionMultiplier = 1 / (1 - transitionPoint);
    }


    public void move(RectangleYio position, FactorYio factorYio) {
        update(position, factorYio, GraphicsYio.defCornerRadius, true);
    }


    public void move(RectangleYio position, FactorYio factorYio, float targetRadius) {
        update(position, factorYio, targetRadius, true);
    }


    public void update(RectangleYio position, FactorYio factorYio, float targetRadius, boolean applyToShadow) {
        if (applyToShadow) {
            update(position, factorYio, stRadius, false);
            shadowRadius = currentRadius;
        }
        float value = factorYio.getValue();
        if (value == 1) {
            currentRadius = targetRadius;
            return;
        }
        if (value == 0) {
            currentRadius = 0;
            return;
        }

        currentRadius = Math.min(position.width / 2, position.height / 2) - 1;
        if (value < transitionPoint) return;
        float transValue = transitionMultiplier * (value - transitionPoint);
        if (transValue > 1) {
            transValue = 1;
        }
        currentRadius += transValue * (targetRadius - currentRadius);
    }


    public float getCurrentRadius() {
        return currentRadius;
    }


    public float getShadowRadius() {
        return shadowRadius;
    }
}
