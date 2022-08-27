package yio.tro.onliyoy.game.view.game_renders;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.onliyoy.game.viewable_model.FogOfWarManager;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Masking;

public class RenderFogOfWar extends GameRender{


    private FogOfWarManager fogOfWarManager;
    private ShapeRenderer shapeRenderer;


    @Override
    protected void loadTextures() {

    }


    @Override
    public void render() {
        fogOfWarManager = getViewableModel().fogOfWarManager;
        if (!fogOfWarManager.isVisible()) return;
        if (!gameController.doesCurrentGameModeAllowGameplay()) return;
        batchMovable.end();
        Masking.begin();
        shapeRenderer = gameController.yioGdxGame.menuViewYio.shapeRenderer;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(gameView.orthoCam.combined);
        drawGhData(shapeRenderer, fogOfWarManager.ghDataContainer);
        shapeRenderer.end();
        batchMovable.begin();
        Masking.continueAfterBatchBegin();
        Masking.applyInvertedMode();
        GraphicsYio.drawByRectangle(
                batchMovable,
                getBlackPixel(),
                gameController.cameraController.getFrame()
        );
        Masking.end(batchMovable);
    }


    @Override
    protected void disposeTextures() {

    }
}
