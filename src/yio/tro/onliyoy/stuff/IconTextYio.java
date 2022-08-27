package yio.tro.onliyoy.stuff;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.net.shared.AvatarType;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class IconTextYio implements ReusableYio {

    // this class is designed as a swap for renderable text yio
    // it's the same thing but with icon on the left

    public RenderableTextYio renderableTextYio;
    public PointYio position;
    public CircleYio iconPosition;
    public float offset;
    public boolean iconEnabled;
    public RectangleYio bounds;
    public boolean rotating;
    private double rotationSpeed;
    public boolean blinking;
    public FactorYio blinkFactor;
    public RectangleYio blinkPosition;
    private RepeatYio<IconTextYio> repeatBlink;
    private boolean dicing;
    private int maxDiceIndex;
    private RepeatYio<IconTextYio> repeatDice;
    private FactorYio diceFactor;
    private boolean readyToChangeDiceIndex;
    public int diceIndex;


    public IconTextYio() {
        renderableTextYio = new RenderableTextYio();
        position = new PointYio();
        iconPosition = new CircleYio();
        bounds = new RectangleYio();
        blinkFactor = new FactorYio();
        blinkPosition = new RectangleYio();
        diceFactor = new FactorYio();
        initRepeats();
        reset();
    }


    private void initRepeats() {
        repeatBlink = new RepeatYio<IconTextYio>(this, 200) {
            @Override
            public void performAction() {
                parent.blinkFactor.destroy(MovementType.approach, 2.5);
            }
        };
        repeatDice = new RepeatYio<IconTextYio>(this, 200) {
            @Override
            public void performAction() {
                parent.applyDiceAction();
            }
        };
    }


    @Override
    public void reset() {
        renderableTextYio.reset();
        position.reset();
        iconPosition.reset();
        bounds.reset();
        offset = 0;
        iconEnabled = true;
        rotating = false;
        blinking = false;
        blinkFactor.reset();
        blinkPosition.reset();
        dicing = false;
        diceFactor.reset();
        readyToChangeDiceIndex = false;
        diceIndex = 0;
    }


    public void move() {
        checkToRotate();
        checkToBlink();
        checkToDice();
    }


    private void checkToDice() {
        if (!dicing) return;
        diceFactor.move();
        repeatDice.move();
        if (diceFactor.getValue() < 0.5) {
            iconPosition.angle = -0.5 * diceFactor.getValue() * Math.PI;
        } else if (diceFactor.getValue() < 1) {
            if (readyToChangeDiceIndex) {
                readyToChangeDiceIndex = false;
                randomizeDiceIndex();
            }
            iconPosition.angle = -0.5 * (diceFactor.getValue() - 1) * Math.PI;
        }
    }


    private void randomizeDiceIndex() {
        int previousIndex = diceIndex;
        int c = 25;
        while (c > 0) {
            c--;
            diceIndex = 1 + YioGdxGame.random.nextInt(maxDiceIndex);
            if (diceIndex != previousIndex) break;
        }
    }


    private void applyDiceAction() {
        diceFactor.reset();
        diceFactor.appear(MovementType.inertia, 2);
        readyToChangeDiceIndex = true;
    }


    private void checkToBlink() {
        if (!blinking) return;
        blinkFactor.move();
        repeatBlink.move();
        if (blinkFactor.isInDestroyState() && blinkFactor.getValue() == 0) {
            blinkFactor.appear(MovementType.simple, 0.7);
        }
        blinkPosition.setBy(iconPosition);
        if (blinkFactor.getValue() < 1) {
            blinkPosition.height *= 0.3f + 0.7f * blinkFactor.getValue();
            blinkPosition.y = iconPosition.center.y - blinkPosition.height / 2;
        }
    }


    private void checkToRotate() {
        if (!rotating) return;
        iconPosition.angle -= rotationSpeed;
    }


    public void centerHorizontal(RectangleYio parent) {
        position.x = parent.x + parent.width / 2 - getWidth() / 2;
    }


    public void centerVertical(RectangleYio parent) {
        position.y = parent.y + parent.height / 2 + renderableTextYio.height / 2;
    }


    public void updateMetrics() {
        renderableTextYio.updateMetrics();
        iconPosition.radius = 0.75f * renderableTextYio.height;
    }


    public void setFont(BitmapFont font) {
        renderableTextYio.setFont(font);
    }


    public void setString(String string) {
        renderableTextYio.setString(string);
    }


    public void setupByAvatarType(AvatarType avatarType) {
        if (avatarType == null) return;
        setIconEnabled(avatarType != AvatarType.empty);
        setRotating(false);
        setBlinking(false);
        switch (avatarType) {
            default:
                break;
            case gear:
                enableRotation(0.01);
                break;
            case hypno:
                enableRotation(0.05);
                break;
            case heli:
                enableRotation(0.025);
                break;
            case eyes:
            case heart:
            case finger_up:
                setBlinking(true);
                break;
            case dice:
                enableDicing(6);
                break;
            case face:
                enableDicing(4);
                break;
        }
    }


    public void onPositionChanged() {
        if (!iconEnabled) {
            renderableTextYio.position.setBy(position);
            renderableTextYio.updateBounds();
            bounds.setBy(renderableTextYio.bounds);
            return;
        }
        iconPosition.center.x = position.x + iconPosition.radius;
        iconPosition.center.y = position.y - renderableTextYio.height / 2;
        renderableTextYio.position.x = iconPosition.center.x + iconPosition.radius + offset;
        renderableTextYio.position.y = position.y;
        renderableTextYio.updateBounds();
        bounds.set(
                position.x,
                iconPosition.center.y - getHeight() / 2,
                getWidth(),
                getHeight()
        );
    }


    public boolean isMovingFast() {
        return renderableTextYio.isMovingFast();
    }


    public float getHeight() {
        if (!iconEnabled) {
            return renderableTextYio.height;
        }
        return Math.max(2 * iconPosition.radius, renderableTextYio.height);
    }


    public float getWidth() {
        if (!iconEnabled) {
            return renderableTextYio.width;
        }
        return 2 * iconPosition.radius + offset + renderableTextYio.width;
    }


    public void setOffset(float offset) {
        this.offset = offset;
    }


    private void enableRotation(double speed) {
        setRotating(true);
        rotationSpeed = speed;
        iconPosition.angle = Yio.getRandomAngle();
    }


    public void setRotating(boolean rotating) {
        this.rotating = rotating;
        iconPosition.setAngle(0);
    }


    public void setIconEnabled(boolean iconEnabled) {
        this.iconEnabled = iconEnabled;
    }


    public void setBlinking(boolean blinking) {
        this.blinking = blinking;
    }


    private void enableDicing(int maxDiceIndex) {
        setDicing(true);
        this.maxDiceIndex = maxDiceIndex;
        randomizeDiceIndex();
    }


    public void setDicing(boolean dicing) {
        this.dicing = dicing;
    }


    @Override
    public String toString() {
        return "[IconTextYio: " + renderableTextYio.string + "]";
    }
}
