package yio.tro.onliyoy.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.viewable_model.CacheBlock;
import yio.tro.onliyoy.game.viewable_model.VmCacheManager;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;

public class RenderVmCache extends GameRender{


    private TextureRegion xTexture;
    RectangleYio tempRectangle;
    private TextureRegion redPixel;


    public RenderVmCache() {
        tempRectangle = new RectangleYio();
    }


    @Override
    protected void loadTextures() {
        xTexture = GraphicsYio.loadTextureRegion("game/stuff/x.png", false);
        redPixel = GraphicsYio.loadTextureRegion("pixels/red.png", false);
    }


    @Override
    public void render() {
        for (CacheBlock block : getViewableModel().cacheManager.blocks) {
            GraphicsYio.drawByRectangle(
                    batchMovable,
                    block.textureRegion,
                    block.position
            );
        }
        renderDebugStuff();
    }


    private void renderDebugStuff() {
        if (!DebugFlags.showVmCacheDebug) return;
        GraphicsYio.setBatchAlpha(batchMovable, 0.1);
        VmCacheManager cacheManager = getViewableModel().cacheManager;
        for (CacheBlock block : cacheManager.intersectedBlocks) {
            GraphicsYio.drawByRectangle(batchMovable, xTexture, block.position);
        }
        for (Hex hex : cacheManager.lazyHexes) {
            tempRectangle.setBy(hex.position);
            GraphicsYio.drawByRectangle(batchMovable, redPixel, tempRectangle);
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
        GraphicsYio.renderBorder(batchMovable, getBlackPixel(), cacheManager.optiFrame, 3 * GraphicsYio.borderThickness);
    }


    @Override
    protected void disposeTextures() {
        xTexture.getTexture().dispose();
        redPixel.getTexture().dispose();
    }
}
