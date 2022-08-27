package yio.tro.onliyoy.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.view.GameView;
import yio.tro.onliyoy.game.viewable_model.GeometricalHexData;
import yio.tro.onliyoy.game.viewable_model.GhDataContainer;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.stuff.AtlasLoader;
import yio.tro.onliyoy.stuff.Storage3xTexture;

public abstract class GameRender {

    protected GameView gameView;
    protected GameController gameController;
    protected SpriteBatch batchMovable, batchSolid;
    float w, h;
    protected AtlasLoader atlasLoader;
    protected AtlasLoader roughAtlasLoader;


    public GameRender() {
        GameRendersList.getInstance().gameRenders.listIterator().add(this);
    }


    void update(GameView gameView) {
        this.gameView = gameView;
        gameController = gameView.gameController;
        batchMovable = gameView.batchMovable;
        batchSolid = gameView.batchSolid;
        w = gameView.w;
        h = gameView.h;
        atlasLoader = gameView.atlasLoader;
        roughAtlasLoader = gameView.roughAtlasLoader;

        loadTextures();
    }


    protected Storage3xTexture load3xTexture(String name) {
        return load3xTexture(name, false);
    }


    protected Storage3xTexture load3xTexture(String name, boolean rough) {
        if (rough) {
            return new Storage3xTexture(roughAtlasLoader, name + ".png");
        }
        return new Storage3xTexture(atlasLoader, name + ".png");
    }


    public static void updateAllTextures() {
        for (GameRender gameRender : GameRendersList.getInstance().gameRenders) {
            gameRender.loadTextures();
        }
    }


    public static void disposeAllTextures() {
        for (GameRender gameRender : GameRendersList.getInstance().gameRenders) {
            gameRender.disposeTextures();
        }
    }


    protected void drawGhData(ShapeRenderer shapeRenderer, GhDataContainer ghDataContainer) {
        for (GeometricalHexData geometricalHexData : ghDataContainer.list) {
            shapeRenderer.rect(
                    geometricalHexData.rectangle.x,
                    geometricalHexData.rectangle.y,
                    geometricalHexData.rectangle.width,
                    geometricalHexData.rectangle.height
            );
            shapeRenderer.triangle(
                    geometricalHexData.leftTriangle[0].x, geometricalHexData.leftTriangle[0].y,
                    geometricalHexData.leftTriangle[1].x, geometricalHexData.leftTriangle[1].y,
                    geometricalHexData.leftTriangle[2].x, geometricalHexData.leftTriangle[2].y
            );
            shapeRenderer.triangle(
                    geometricalHexData.rightTriangle[0].x, geometricalHexData.rightTriangle[0].y,
                    geometricalHexData.rightTriangle[1].x, geometricalHexData.rightTriangle[1].y,
                    geometricalHexData.rightTriangle[2].x, geometricalHexData.rightTriangle[2].y
            );
        }
    }


    protected abstract void loadTextures();


    public abstract void render();


    protected abstract void disposeTextures();


    protected int getCurrentZoomQuality() {
        return gameView.currentZoomQuality;
    }


    public ObjectsLayer getObjectsLayer() {
        return gameController.objectsLayer;
    }


    protected ViewableModel getViewableModel() {
        return getObjectsLayer().viewableModel;
    }


    public TextureRegion getBlackPixel() {
        return gameView.blackPixel;
    }
}
