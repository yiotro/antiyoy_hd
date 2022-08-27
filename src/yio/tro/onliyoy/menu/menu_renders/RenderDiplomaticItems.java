package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.touch_modes.TmDiplomacy;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.touch_modes.ViewableEntityInfo;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

import java.util.HashMap;

public class RenderDiplomaticItems {

    GameController gameController;
    RectangleYio rectangle;
    PointYio tempPoint;
    PointYio delta;
    PointYio hookPoint;
    RectangleYio bounds;
    RenderableTextYio viewText;
    private TmDiplomacy tmDiplomacy;


    public RenderDiplomaticItems() {
        rectangle = new RectangleYio();
        tempPoint = new PointYio();
        hookPoint = new PointYio();
        delta = new PointYio();
        viewText = new RenderableTextYio();
        bounds = new RectangleYio();
    }


    void render(SpriteBatch batch, GameController gameController) {
        this.gameController = gameController;
        tmDiplomacy = getTmDiplomacy();
        if (gameController.touchMode != tmDiplomacy) return;
        float alpha = tmDiplomacy.appearFactor.getValue();
        GraphicsYio.setBatchAlpha(batch, alpha);
        for (ViewableEntityInfo viewableEntityInfo : tmDiplomacy.items) {
            bounds.setBy(viewableEntityInfo.incBounds);
            updateHookPoint(bounds);
            updateTempPoint(hookPoint);
            updateBounds();
            GraphicsYio.drawByRectangle(
                    batch,
                    MenuRenders.renderUiColors.map.get(viewableEntityInfo.playerEntity.color),
                    bounds
            );
            for (RenderableTextYio renderableTextYio : viewableEntityInfo.viewTexts) {
                prepareViewText(renderableTextYio);
                GraphicsYio.setFontAlpha(viewText.font, alpha);
                GraphicsYio.renderText(batch, viewText);
                GraphicsYio.setFontAlpha(viewText.font, 1);
            }
        }
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    protected TmDiplomacy getTmDiplomacy() {
        return TouchMode.tmDiplomacy;
    }


    private void prepareViewText(RenderableTextYio renderableTextYio) {
        viewText.setBy(renderableTextYio);
        delta.x = renderableTextYio.position.x - hookPoint.x;
        delta.y = renderableTextYio.position.y - hookPoint.y;
        viewText.position.setBy(tempPoint);
        viewText.position.add(delta);
    }


    private void updateBounds() {
        bounds.x = tempPoint.x - bounds.width / 2;
        bounds.y = tempPoint.y - bounds.height / 2;
    }


    private void updateHookPoint(RectangleYio incBounds) {
        hookPoint.set(
                incBounds.x + incBounds.width / 2,
                incBounds.y + incBounds.height / 2
        );
    }


    private void updateTempPoint(PointYio inGamePoint) {
        OrthographicCamera orthoCam = gameController.cameraController.orthoCam;
        tempPoint.x = 0.5f * GraphicsYio.width + (inGamePoint.x - orthoCam.position.x) / orthoCam.zoom;
        tempPoint.y = 0.5f * GraphicsYio.height + (inGamePoint.y - orthoCam.position.y) / orthoCam.zoom;
    }
}
