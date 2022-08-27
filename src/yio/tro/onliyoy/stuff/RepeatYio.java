package yio.tro.onliyoy.stuff;


import yio.tro.onliyoy.RefreshRateDetector;
import yio.tro.onliyoy.YioGdxGame;

public abstract class RepeatYio<ProviderType> {

    protected ProviderType parent;
    int frequency, countDown;


    public RepeatYio(ProviderType parent, int frequency) {
        this(parent, frequency, YioGdxGame.random.nextInt(Math.max(1, frequency)));
    }


    public RepeatYio(ProviderType parent, int frequency, int defCount) {
        this.parent = parent;
        setFrequency(frequency);
        countDown = applyRefreshRate(defCount);
    }


    public void move() {
        if (countDown == 0) {
            resetCountDown();
            performAction();
        } else countDown--;
    }


    public void resetCountDown() {
        countDown = frequency;
    }


    public abstract void performAction();


    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }


    private int applyRefreshRate(int value) {
        if (value <= 1) return value;
        return (int) (value / RefreshRateDetector.getInstance().multiplier);
    }


    public void setFrequency(int frequency) {
        this.frequency = applyRefreshRate(frequency);
    }
}
