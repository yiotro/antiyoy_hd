package yio.tro.onliyoy.game.touch_modes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.Relation;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class ViewableEntityInfo implements ReusableYio {

    public PlayerEntity playerEntity;
    public ArrayList<RenderableTextYio> viewTexts;
    public RectangleYio incBounds;
    public PointYio hookPoint;
    ObjectPoolYio<RenderableTextYio> poolViewTexts;
    float sumHeight;


    public ViewableEntityInfo() {
        viewTexts = new ArrayList<>();
        incBounds = new RectangleYio();
        hookPoint = new PointYio();
        initPools();
    }


    private void initPools() {
        poolViewTexts = new ObjectPoolYio<RenderableTextYio>(viewTexts) {
            @Override
            public RenderableTextYio makeNewObject() {
                return new RenderableTextYio();
            }
        };
    }


    @Override
    public void reset() {
        playerEntity = null;
        hookPoint.reset();
        incBounds.reset();
        poolViewTexts.clearExternalList();
    }


    void move() {

    }


    private void updateIncBounds() {
        if (viewTexts.size() == 0) return;
        incBounds.height = sumHeight;
        incBounds.y = hookPoint.y - sumHeight / 2;
        incBounds.x = viewTexts.get(0).bounds.x;
        incBounds.width = viewTexts.get(0).bounds.width;
        for (RenderableTextYio renderableTextYio : viewTexts) {
            if (renderableTextYio.bounds.x < incBounds.x) {
                incBounds.x = renderableTextYio.bounds.x;
            }
            if (renderableTextYio.bounds.width > incBounds.width) {
                incBounds.width = renderableTextYio.bounds.width;
            }
        }
        incBounds.increase(0.015f * GraphicsYio.width);
    }


    private void moveViewTexts() {
        float y = hookPoint.y + sumHeight / 2;
        for (RenderableTextYio renderableTextYio : viewTexts) {
            renderableTextYio.position.x = hookPoint.x - renderableTextYio.width / 2;
            renderableTextYio.position.y = y;
            renderableTextYio.updateBounds();
            y -= renderableTextYio.height;
        }
    }


    public void setValues(PlayerEntity playerEntity, PointYio hookPoint) {
        setValues(playerEntity, playerEntity.entitiesManager.getCurrentEntity(), hookPoint);
    }


    public void setValues(PlayerEntity playerEntity, PlayerEntity otherEntity, PointYio hookPoint) {
        this.playerEntity = playerEntity;
        this.hookPoint.setBy(hookPoint);
        addViewText(playerEntity.name);
        Relation relation = otherEntity.getRelation(playerEntity);
        String lockString = "";
        if (relation.lock > 0) {
            lockString = " " + relation.lock + "+";
        }
        addViewText(LanguagesManager.getInstance().getString("" + relation.type) + lockString);
        updateSumHeight();
        moveViewTexts();
        updateIncBounds();
    }


    private void addViewText(String string) {
        RenderableTextYio freshObject = poolViewTexts.getFreshObject();
        freshObject.setFont(Fonts.miniFont);
        freshObject.setString(string);
        freshObject.updateMetrics();
        freshObject.height += 3 * GraphicsYio.borderThickness;
    }


    private void updateSumHeight() {
        sumHeight = 0;
        for (RenderableTextYio renderableTextYio : viewTexts) {
            sumHeight += renderableTextYio.height;
        }
    }
}
