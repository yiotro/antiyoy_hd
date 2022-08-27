package yio.tro.onliyoy.menu.elements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class NotificationElement extends InterfaceElement<NotificationElement> {

    public static final int AUTO_HIDE_DELAY = 1500;

    boolean autoHide;
    public PointYio textPosition, textDelta;
    public String message;
    public BitmapFont font;
    private long timeToHide;
    float textOffset;
    public RectangleYio shadowPosition;


    public NotificationElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);

        autoHide = false;
        message = "";
        timeToHide = 0;
        font = Fonts.gameFont;
        if (GraphicsYio.screenRatio > 1.8f) {
            font = Fonts.miniFont;
        }
        textOffset = 0.03f * GraphicsYio.width;
        textPosition = new PointYio();
        textDelta = new PointYio();
        shadowPosition = new RectangleYio();

        setAnimation(AnimationYio.up);
    }


    @Override
    protected NotificationElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateTextPosition();
        updateShadowPosition();
        checkToDie();
    }


    private void updateShadowPosition() {
        if (!factorMoved) return;

        shadowPosition.setBy(viewPosition);
        shadowPosition.x -= 0.1f * GraphicsYio.width;
        shadowPosition.width += 0.2f * GraphicsYio.width;
        shadowPosition.height = GraphicsYio.height / 2;
    }


    @Override
    protected void updateViewPosition() {
        if (!factorMoved) return;

        viewPosition.setBy(position);

        viewPosition.y += (1 - appearFactor.getValue()) * 1.5f * position.height;
    }


    private void checkToDie() {
        if (autoHide && System.currentTimeMillis() > timeToHide) {
            destroy();
        }
    }


    private void updateTextPosition() {
        if (!factorMoved) return;

        textPosition.x = viewPosition.x + textDelta.x;
        textPosition.y = viewPosition.y + textDelta.y;
    }


    @Override
    public void onDestroy() {
        appearFactor.destroy(MovementType.lighty, 4);

        autoHide = false;
    }


    @Override
    public void onAppear() {
        appearFactor.appear(MovementType.approach, 2.5);
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
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


    public void enableAutoHide() {
        autoHide = true;

        timeToHide = System.currentTimeMillis() + AUTO_HIDE_DELAY;
    }


    public void setMessage(String key) {
        this.message = LanguagesManager.getInstance().getString(key);

        updateTextDelta();
    }


    private void updateTextDelta() {
        textDelta.x = textOffset;
        float textHeight = GraphicsYio.getTextHeight(font, message);
        textDelta.y = position.height / 2 + textHeight / 2;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderNotification;
    }
}
