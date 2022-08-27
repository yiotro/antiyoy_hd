package yio.tro.onliyoy.game.viewable_model;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.JumpEngineYio;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class Exclamation implements ReusableYio {

    Hex hex;
    public CircleYio viewPosition;
    public FactorYio appearFactor;
    private JumpEngineYio jumpEngineYio;
    RepeatYio<Exclamation> repeatJump;


    public Exclamation() {
        viewPosition = new CircleYio();
        appearFactor = new FactorYio();
        jumpEngineYio = new JumpEngineYio();
        initRepeats();
    }


    private void initRepeats() {
        repeatJump = new RepeatYio<Exclamation>(this, 60) {
            @Override
            public void performAction() {
                parent.doJump();
            }
        };
    }


    @Override
    public void reset() {
        hex = null;
        viewPosition.reset();
        appearFactor.reset();
        appearFactor.appear(MovementType.approach, 5);
        repeatJump.setCountDown(1);
    }


    private void doJump() {
        jumpEngineYio.apply(1, 1.1);
    }


    void move() {
        appearFactor.move();
        jumpEngineYio.move();
        repeatJump.move();
        updateViewPosition();
    }


    private void updateViewPosition() {
        viewPosition.center.setBy(hex.position.center);
        viewPosition.center.x += 0.45f * hex.position.radius;
        viewPosition.center.y += 0.35f * appearFactor.getValue() * hex.position.radius;
        viewPosition.radius = 0.3f * hex.position.radius;
        viewPosition.center.y += jumpEngineYio.getValue() * appearFactor.getValue() * 0.5f * viewPosition.radius;
    }


    void kill() {
        appearFactor.destroy(MovementType.lighty, 5);
    }


    public void setHex(Hex hex) {
        this.hex = hex;
    }
}
