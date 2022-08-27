package yio.tro.onliyoy.menu.elements.keyboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.stuff.*;

public class CbButton {

    CbPage page;
    public RectangleYio position;
    public RenderableTextYio title;
    public PointYio delta;
    public CbType type;
    public SelectionEngineYio selectionEngineYio;
    public CircleYio selectionPosition;
    long selectionTime;


    public CbButton(CbPage page) {
        this.page = page;
        position = new RectangleYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        delta = new PointYio();
        type = null;
        selectionEngineYio = new SelectionEngineYio();
        selectionPosition = new CircleYio();
        selectionTime = 0;
    }


    void setTitle(String string) {
        title.setString(string);
        title.updateMetrics();
    }


    void move() {
        updatePosition();
        updateTitlePosition();
        moveSelection();
        checkToUpdateSelectionPosition();
    }


    private void checkToUpdateSelectionPosition() {
        if (!selectionEngineYio.isSelected()) return;
        updateSelectionPosition();
    }


    public boolean isCurrentlyVisible() {
        if (position.x + position.width < GraphicsYio.borderThickness) return false;
        if (position.x > GraphicsYio.width - GraphicsYio.borderThickness) return false;
        return true;
    }


    private void moveSelection() {
        if (page.customKeyboardElement.touchedCurrently) return;
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) return;
        selectionEngineYio.move();
    }


    public void applySelection() {
        selectionTime = System.currentTimeMillis();
        selectionEngineYio.applySelection();
        updateSelectionPosition();
    }


    private void updateSelectionPosition() {
        selectionPosition.center.x = position.x + position.width / 2;
        selectionPosition.center.y = position.y + position.height / 2;
//        selectionPosition.radius = (0.7f + 0.15f * (1 - selectionEngineYio.factorYio.getValue())) * position.width;
        selectionPosition.radius = 0.8f * position.width;
    }


    private void updateTitlePosition() {
        title.centerHorizontal(position);
        title.centerVertical(position);
        title.updateBounds();
    }


    private void updatePosition() {
        position.x = page.position.x + delta.x - position.width / 2;
        position.y = page.position.y + delta.y - position.height / 2;
    }


    boolean isTouchedBy(PointYio touchPoint, float offset) {
        return position.isPointInside(touchPoint, offset);
    }
}
