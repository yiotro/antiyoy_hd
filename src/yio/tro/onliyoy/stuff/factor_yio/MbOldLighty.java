package yio.tro.onliyoy.stuff.factor_yio;

/**
 * Created by ivan on 23.04.2015.
 */
public class MbOldLighty extends AbstractMoveBehavior {


    public static final int CURVE = 10;


    public MbOldLighty() {
    }


    @Override
    void move(FactorYio f) {
        f.value += f.speedMultiplier * f.dy;
        f.dy += f.gravity;
        applyStrictBounds(f);
    }


    @Override
    void onAppear(FactorYio f) {
        super.onAppear(f);
        f.speedMultiplier /= CURVE;
        f.gravity *= CURVE;
    }
}
