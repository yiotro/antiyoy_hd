package yio.tro.onliyoy.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;

public class RenderBackground extends GameRender{


    private TextureRegion defBackgroundTexture;
    private TextureRegion waterTexture;


    @Override
    protected void loadTextures() {
        defBackgroundTexture = GraphicsYio.loadTextureRegion("game/background/main.png", false);
        waterTexture = GraphicsYio.loadTextureRegion("game/background/water.png", true);
    }


    @Override
    public void render() {
        renderSingleBlock(gameController.cameraController.frame);
    }


    public void renderSingleBlock(RectangleYio position) {
        GraphicsYio.drawByRectangle(batchMovable, gameView.voidTexture, position);
        batchMovable.draw(
                getBackgroundTexture(),
                0,
                0,
                gameController.sizeManager.boundWidth,
                gameController.sizeManager.boundHeight
        );
    }


    private TextureRegion getBackgroundTexture() {
        if (SettingsManager.getInstance().waterTexture) return waterTexture;
        return defBackgroundTexture;
    }


    @Override
    protected void disposeTextures() {
        defBackgroundTexture.getTexture().dispose();
    }
}
