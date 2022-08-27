package yio.tro.onliyoy.game.view.game_renders.tm_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.touch_modes.TmDiplomacy;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.touch_modes.ViewableEntityInfo;
import yio.tro.onliyoy.game.view.game_renders.GameRender;
import yio.tro.onliyoy.game.view.game_renders.GameRendersList;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Masking;
import yio.tro.onliyoy.stuff.RenderableTextYio;

import java.util.HashMap;

public class RenderTmDiplomacy extends GameRender {

    private TmDiplomacy tm;
    private ShapeRenderer shapeRenderer;


    @Override
    protected void loadTextures() {

    }


    @Override
    public void render() {
        tm = getTmDiplomacy();
        renderSelection();
    }


    protected TmDiplomacy getTmDiplomacy() {
        return TouchMode.tmDiplomacy;
    }


    private void renderSelection() {
        if (!tm.selectionEngineYio.isSelected()) return;
        batchMovable.end();
        Masking.begin();
        shapeRenderer = gameController.yioGdxGame.menuViewYio.shapeRenderer;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(gameView.orthoCam.combined);
        drawGhData(shapeRenderer, tm.ghDataContainer);
        shapeRenderer.end();
        batchMovable.begin();
        Masking.continueAfterBatchBegin();
        GraphicsYio.setBatchAlpha(batchMovable, tm.selectionEngineYio.getAlpha());
        GraphicsYio.drawByRectangle(
                batchMovable,
                getBlackPixel(),
                gameController.cameraController.getFrame()
        );
        GraphicsYio.setBatchAlpha(batchMovable, 1);
        Masking.end(batchMovable);
    }


    private void renderItems() {
        // if rendered this way items will become smaller when zoom out
        // see RenderDiplomaticItems.render(), that's what is used currently
        HashMap<HColor, TextureRegion> mapBackgrounds = MenuRenders.renderUiColors.map;
        for (ViewableEntityInfo viewableEntityInfo : tm.items) {
            GraphicsYio.drawByRectangle(
                    batchMovable,
                    mapBackgrounds.get(viewableEntityInfo.playerEntity.color),
                    viewableEntityInfo.incBounds
            );
            for (RenderableTextYio renderableTextYio : viewableEntityInfo.viewTexts) {
                GraphicsYio.renderText(batchMovable, renderableTextYio);
            }
        }
    }


    @Override
    protected void disposeTextures() {

    }
}
