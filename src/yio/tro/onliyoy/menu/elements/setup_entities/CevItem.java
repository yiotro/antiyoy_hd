package yio.tro.onliyoy.menu.elements.setup_entities;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.core_model.EntityType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

public class CevItem {

    public CondensedEntitiesViewElement condensedEntitiesViewElement;
    public RenderableTextYio title;
    public RectangleYio incBounds;
    public RectangleYio touchPosition;
    PointYio delta;
    public HColor color;
    public SelectionEngineYio selectionEngineYio;
    public EntityType entityType;


    public CevItem(CondensedEntitiesViewElement condensedEntitiesViewElement) {
        this.condensedEntitiesViewElement = condensedEntitiesViewElement;
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        incBounds = new RectangleYio();
        touchPosition = new RectangleYio();
        delta = new PointYio();
        color = null;
        selectionEngineYio = new SelectionEngineYio();
    }


    void setValues(EntityType entityType) {
        this.entityType = entityType;
        String key = "ai";
        if (entityType == EntityType.human) {
            key = "human";
        }
        title.setString(LanguagesManager.getInstance().getString(key));
        title.updateMetrics();
    }


    void move() {
        moveSelection();
        updateTitlePosition();
        updateIncBounds();
        updateTouchPosition();
    }


    private void updateTouchPosition() {
        touchPosition.setBy(incBounds);
        touchPosition.increase(condensedEntitiesViewElement.incOffset / 2);
    }


    private void moveSelection() {
        if (condensedEntitiesViewElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void updateIncBounds() {
        incBounds.setBy(title.bounds);
        incBounds.increase(condensedEntitiesViewElement.incOffset);
    }


    private void updateTitlePosition() {
        title.position.x = condensedEntitiesViewElement.getViewPosition().x + delta.x;
        title.position.y = condensedEntitiesViewElement.getViewPosition().y + delta.y;
        title.updateBounds();
    }


    boolean isTouchedBy(PointYio touchPoint) {
        return touchPosition.isPointInside(touchPoint);
    }
}
