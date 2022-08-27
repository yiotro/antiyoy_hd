package yio.tro.onliyoy.menu.elements.calendar;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.stuff.*;

public class CveDayButton {

    CveTab tab;
    public CircleYio position;
    PointYio delta;
    int index;
    int week;
    public RenderableTextYio title;
    public SelectionEngineYio selectionEngineYio;
    public CveDayState state;


    public CveDayButton(CveTab tab) {
        this.tab = tab;
        position = new CircleYio();
        delta = new PointYio();
        index = -1;
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        selectionEngineYio = new SelectionEngineYio();
        state = null;
        week = -1;
    }


    void move() {
        updatePosition();
        moveTitle();
        moveSelection();
    }


    private void moveSelection() {
        if (tab.calendarViewElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    boolean isTouchedBy(PointYio touchPoint) {
        if (Math.abs(position.center.x - touchPoint.x) > position.radius) return false;
        if (Math.abs(position.center.y - touchPoint.y) > position.radius) return false;
        return true;
    }


    private void moveTitle() {
        if (state != CveDayState.unlocked) return;
        title.position.x = position.center.x - title.width / 2;
        title.position.y = position.center.y + title.height / 2;
        title.updateBounds();
    }


    private void updatePosition() {
        position.center.x = tab.position.x + delta.x;
        position.center.y = tab.position.y + delta.y;
        position.center.y -= (1 - tab.calendarViewElement.getFactor().getValue()) * week * 0.1f * GraphicsYio.height;
    }


    public void setIndex(int index) {
        this.index = index;
        title.setString("" + index);
        title.updateMetrics();
    }


    public void setWeek(int week) {
        this.week = week;
    }


    public void setState(CveDayState state) {
        this.state = state;
    }
}
