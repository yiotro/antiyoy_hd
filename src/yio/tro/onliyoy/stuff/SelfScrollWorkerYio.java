package yio.tro.onliyoy.stuff;

import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SelfScrollWorkerYio {

    private float path;
    private FactorYio movementFactor;
    private FactorYio alphaFactor;
    private FactorYio delayFactor;
    private double speed;
    private int step;
    private boolean active;


    public SelfScrollWorkerYio() {
        step = 0;
        path = 0;
        movementFactor = new FactorYio();
        alphaFactor = new FactorYio();
        delayFactor = new FactorYio();
    }


    public void launch(double slider, double limit) {
        active = false;
        if (slider <= limit) return;
        active = true;
        step = 0;
        path = (float) (slider - limit);
        speed = 0.02f * GraphicsYio.width / path;
        movementFactor.reset();
        alphaFactor.reset();
        alphaFactor.setValue(1);
    }


    public void move() {
        movementFactor.move();
        alphaFactor.move();
        delayFactor.move();
        switch (step) {
            case 0:
                if (alphaFactor.getValue() < 1) break;
                delayFactor.reset();
                delayFactor.appear(MovementType.simple, 0.1);
                step++;
                break;
            case 1:
                if (delayFactor.getValue() < 1) break;
                movementFactor.appear(MovementType.simple, speed);
                step++;
                break;
            case 2:
                if (movementFactor.getValue() < 1) break;
                delayFactor.reset();
                delayFactor.appear(MovementType.simple, 0.1);
                step++;
                break;
            case 3:
                if (delayFactor.getValue() < 1) break;
                alphaFactor.destroy(MovementType.lighty, 2);
                step++;
                break;
            case 4:
                if (alphaFactor.getValue() > 0) break;
                movementFactor.reset();
                alphaFactor.appear(MovementType.approach, 1.25);
                step = 0;
                break;
        }
    }


    public float getDelta() {
        if (!active) return 0;
        return -movementFactor.getValue() * path;
    }


    public float getAlpha() {
        if (!active) return 1;
        return alphaFactor.getValue();
    }

}
