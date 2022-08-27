package yio.tro.onliyoy.game.view.game_renders;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.onliyoy.game.viewable_model.BorderIndicator;
import yio.tro.onliyoy.game.viewable_model.GeometricalHexData;
import yio.tro.onliyoy.game.viewable_model.MoveZoneViewer;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Masking;
import yio.tro.onliyoy.stuff.Storage3xTexture;

public class RenderMoveZone extends GameRender {

    private Storage3xTexture border3xTexture;
    private MoveZoneViewer moveZoneViewer;
    private ShapeRenderer shapeRenderer;


    @Override
    protected void loadTextures() {
        border3xTexture = load3xTexture("move_zone");
    }


    @Override
    public void render() {
        moveZoneViewer = getViewableModel().moveZoneViewer;
        renderDarken();
        renderIndicators();
    }


    private void renderDarken() {
        if (moveZoneViewer.darkenFactor.getValue() == 0) return;
        batchMovable.end();
        Masking.begin();
        shapeRenderer = gameController.yioGdxGame.menuViewYio.shapeRenderer;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(gameView.orthoCam.combined);
        drawGhData(shapeRenderer, moveZoneViewer.ghDataContainer);
        shapeRenderer.end();
        batchMovable.begin();
        Masking.continueAfterBatchBegin();
        Masking.applyInvertedMode();
        GraphicsYio.setBatchAlpha(batchMovable, 0.25 * moveZoneViewer.darkenFactor.getValue());
        GraphicsYio.drawByRectangle(
                batchMovable,
                getBlackPixel(),
                gameController.cameraController.getFrame()
        );
        GraphicsYio.setBatchAlpha(batchMovable, 1);
        Masking.end(batchMovable);
    }


    private void renderIndicators() {
        for (BorderIndicator indicator : moveZoneViewer.indicators) {
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


    @Override
    protected void disposeTextures() {

    }

}
