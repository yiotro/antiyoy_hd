package yio.tro.onliyoy.stuff;

public abstract class LongTapDetector {

    public static final int DEFAULT_DELAY = 500;

    boolean performedCheck;
    long touchDownTime;
    PointYio initialTouch, currentTouch;
    boolean touched;
    int delay;


    public LongTapDetector() {
        performedCheck = false;
        initialTouch = new PointYio();
        currentTouch = new PointYio();
        touched = false;
        delay = DEFAULT_DELAY;
    }


    public void setDelay(int delay) {
        this.delay = delay;
    }


    public void onTouchDown(PointYio touchPoint) {
        initialTouch.setBy(touchPoint);
        currentTouch.setBy(initialTouch);

        performedCheck = false;
        touched = true;
        touchDownTime = System.currentTimeMillis();
    }


    public void onTouchDrag(PointYio touchPoint) {
        currentTouch.setBy(touchPoint);
    }


    public void onTouchUp(PointYio touchPoint) {
        touched = false;
    }


    public void move() {
        if (!touched) return;
        if (performedCheck) return;
        if (System.currentTimeMillis() - touchDownTime <= delay) return;

        performedCheck = true;

        if (initialTouch.distanceTo(currentTouch) > 0.05f * GraphicsYio.width) return;

        onLongTapDetected();
    }


    public float getProgressValue() {
        if (!touched) return 0;
        if (initialTouch.distanceTo(currentTouch) > 0.05f * GraphicsYio.width) return 0;
        float value = (float) (System.currentTimeMillis() - touchDownTime) / (float) delay;
        if (value < 0) {
            value = 0;
        }
        if (value > 1) {
            value = 1;
        }
        return value;
    }


    public abstract void onLongTapDetected();

}
