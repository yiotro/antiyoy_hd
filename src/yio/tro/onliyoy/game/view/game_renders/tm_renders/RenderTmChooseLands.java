package yio.tro.onliyoy.game.view.game_renders.tm_renders;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.onliyoy.game.touch_modes.TmChooseLands;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.view.game_renders.GameRender;
import yio.tro.onliyoy.game.viewable_model.BorderIndicator;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Masking;
import yio.tro.onliyoy.stuff.Storage3xTexture;

public class RenderTmChooseLands extends GameRender {

    TmChooseLands tm;
    private ShapeRenderer shapeRenderer;
    private Storage3xTexture border3xTexture;


    @Override
    protected void loadTextures() {
        border3xTexture = load3xTexture("move_zone");
    }


    @Override
    public void render() {
        tm = TouchMode.tmChooseLands;
        renderDarken();
        renderIndicators();
    }


    private void renderIndicators() {
        for (BorderIndicator indicator : tm.indicators) {
            if (!indicator.isCurrentlyVisible()) continue;
            GraphicsYio.drawRectangleRotatedByCenter(
                    batchMovable,
                    border3xTexture.getTexture(getCurrentZoomQuality()),
                    indicator.viewPosition.center.x,
                    indicator.viewPosition.center.y,
                    indicator.viewSize.x,
                    indicator.viewSize.y,
                    indicator.viewPosition.angle
            );
        }
    }


    private void renderDarken() {
        if (tm.darkenFactor.getValue() == 0) return;
        batchMovable.end();
        Masking.begin();
        shapeRenderer = gameController.yioGdxGame.menuViewYio.shapeRenderer;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(gameView.orthoCam.combined);
        drawGhData(shapeRenderer, tm.ghDataContainer);
        shapeRenderer.end();
        batchMovable.begin();
        Masking.continueAfterBatchBegin();
        Masking.applyInvertedMode();
        GraphicsYio.setBatchAlpha(batchMovable, 0.25 * tm.darkenFactor.getValue());
        GraphicsYio.drawByRectangle(
                batchMovable,
                getBlackPixel(),
                gameController.cameraController.getFrame()
        );
        GraphicsYio.setBatchAlpha(batchMovable, 1);
        Masking.end(batchMovable);
    }


    @Override
    protected void disposeTextures() {

    }
}
