package yio.tro.onliyoy.menu.elements.net;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class NetProcessViewElement extends InterfaceElement<NetProcessViewElement> {

    public RenderableTextYio title;
    public CircleYio iconPosition;
    private float iOffset;
    private FactorYio rotationFactor;
    public boolean clockMode;
    private String currentString;
    private long appearTime;
    RepeatYio<NetProcessViewElement> repeatUpdateClock;
    public boolean backgroundEnabled;
    public RectangleYio backgroundPosition;


    public NetProcessViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        setTitle("-");
        iconPosition = new CircleYio();
        iOffset = 0.03f * GraphicsYio.width;
        rotationFactor = new FactorYio();
        clockMode = false;
        backgroundEnabled = false;
        backgroundPosition = new RectangleYio();
        initRepeats();
    }


    private void initRepeats() {
        repeatUpdateClock = new RepeatYio<NetProcessViewElement>(this, 30) {
            @Override
            public void performAction() {
                parent.updateClock();
            }
        };
    }


    @Override
    protected NetProcessViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        repeatUpdateClock.move();
        updateTitlePosition();
        updateIconPosition();
        updateBackgroundPosition();
        applyRotation();
    }


    private void updateBackgroundPosition() {
        if (!backgroundEnabled) return;
        backgroundPosition.setBy(title.bounds);
        backgroundPosition.width = iconPosition.center.x + iconPosition.radius - backgroundPosition.x;
        backgroundPosition.increase(0.015f * GraphicsYio.width);
    }


    private void updateClock() {
        if (!clockMode) return;
        long deltaTimeMillis = System.currentTimeMillis() - appearTime;
        int deltaTimeFrames = Yio.convertMillisIntoFrames(deltaTimeMillis);
        String timeString = Yio.convertTime(deltaTimeFrames);
        title.setString(currentString + " " + timeString);
        title.updateMetrics();
    }


    private void applyRotation() {
        if (rotationFactor.getValue() == 1) {
            rotationFactor.reset();
            rotationFactor.appear(MovementType.inertia, 0.5);
            return;
        }
        rotationFactor.move();
    }


    private void updateIconPosition() {
        iconPosition.radius = 0.66f * title.height;
        iconPosition.center.x = title.position.x + title.bounds.width + iOffset + iconPosition.radius;
        if (title.string.length() == 0) {
            iconPosition.center.x = viewPosition.x + viewPosition.width / 2;
        }
        iconPosition.center.y = title.position.y - title.height / 2;
        iconPosition.angle = (1 - rotationFactor.getValue()) * Math.PI;
    }


    private void updateTitlePosition() {
        title.centerVertical(viewPosition);
        title.centerHorizontal(viewPosition);
        if (appearFactor.getValue() < 1) {
            title.position.y -= (1 - appearFactor.getValue()) * 3 * title.height;
        }
        title.updateBounds();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        rotationFactor.reset();
        rotationFactor.setValue(1);
        appearTime = System.currentTimeMillis();
        updateClock();
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    public NetProcessViewElement setTitle(String key) {
        currentString = LanguagesManager.getInstance().getString(key);
        title.setString(currentString);
        title.updateMetrics();
        return this;
    }


    public NetProcessViewElement setBackgroundEnabled(boolean backgroundEnabled) {
        this.backgroundEnabled = backgroundEnabled;
        return this;
    }


    public NetProcessViewElement setClockMode(boolean value) {
        clockMode = value;
        return this;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderNetProcessViewElement;
    }
}
