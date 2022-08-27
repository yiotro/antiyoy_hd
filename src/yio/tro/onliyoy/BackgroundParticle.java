package yio.tro.onliyoy;

import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class BackgroundParticle implements ReusableYio {

    GeneralBackgroundManager generalBackgroundManager;
    public CircleYio position;
    public PointYio speed;
    private float minRadius;
    private float maxRadius;
    RepeatYio<BackgroundParticle> repeatLoop;
    boolean alive;
    private float minSpeed;
    private float maxSpeed;
    private float waveSpeed;
    private FactorYio waveFactor;


    public BackgroundParticle(GeneralBackgroundManager generalBackgroundManager) {
        this.generalBackgroundManager = generalBackgroundManager;
        position = new CircleYio();
        speed = new PointYio();
        waveFactor = new FactorYio();
        minRadius = 0.01f * GraphicsYio.width;
        maxRadius = 2.5f * minRadius;
        minSpeed = 0.05f * minRadius;
        maxSpeed = 3f * minSpeed;
        waveSpeed = 0.04f + 0.02f * YioGdxGame.random.nextFloat();
        initRepeats();
    }


    private void initRepeats() {
        repeatLoop = new RepeatYio<BackgroundParticle>(this, 5) {
            @Override
            public void performAction() {
                parent.checkToLoop();
            }
        };
    }


    @Override
    public void reset() {
        position.reset();
        speed.reset();
        alive = true;
        randomizeWaveFactor();
    }


    private void randomizeWaveFactor() {
        waveFactor.reset();
        waveFactor.setValue(YioGdxGame.random.nextDouble());
        if (YioGdxGame.random.nextBoolean()) {
            waveFactor.appear(MovementType.simple, waveSpeed);
        } else {
            waveFactor.destroy(MovementType.simple, waveSpeed);
        }
    }


    void spawn(PointYio sPos) {
        position.center.setBy(sPos);
        updateRadius(YioGdxGame.random.nextFloat());
    }


    private void updateRadius(float rValue) {
        position.setRadius(minRadius + rValue * (maxRadius - minRadius));
        speed.y = minSpeed + (1 - rValue) * (maxSpeed - minSpeed);
        speed.y *= RefreshRateDetector.getInstance().multiplier;
    }


    boolean isCurrentlyVisible() {
        return alive && getLoopPosition().isPointInside(position.center);
    }


    void move() {
        moveWave();
        updateHorizontalSpeed();
        applySpeed();
        repeatLoop.move();
    }


    private void updateHorizontalSpeed() {
        speed.x = (2 * waveFactor.getValue() - 1) * minSpeed;
        speed.x *= RefreshRateDetector.getInstance().multiplier;
    }


    private void moveWave() {
        waveFactor.move();
        if (waveFactor.getValue() == 0) {
            waveFactor.appear(MovementType.simple, waveSpeed);
        }
        if (waveFactor.getValue() == 1) {
            waveFactor.destroy(MovementType.simple, waveSpeed);
        }
    }


    private void checkToLoop() {
        if (position.center.y < getLoopPosition().y + getLoopPosition().height) return;
        position.center.y -= getLoopPosition().height;
    }


    private RectangleYio getLoopPosition() {
        return generalBackgroundManager.loopPosition;
    }


    private void applySpeed() {
        position.center.add(speed);
    }
}
