package yio.tro.onliyoy.game.viewable_model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import yio.tro.onliyoy.PlatformType;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.general.SizeManager;
import yio.tro.onliyoy.stuff.FakeFbYio;
import yio.tro.onliyoy.stuff.FrameBufferYio;
import yio.tro.onliyoy.stuff.RectangleYio;

public class CacheBlock {

    VmCacheManager vmCacheManager;
    public RectangleYio position;
    public FrameBufferYio frameBufferYio;
    public TextureRegion textureRegion;
    Matrix4 matrix4;
    public float qualityRatio;


    public CacheBlock(VmCacheManager vmCacheManager) {
        this.vmCacheManager = vmCacheManager;
        position = new RectangleYio();
        matrix4 = new Matrix4();
        updateQualityRatio();
        createFrameBuffer();
        textureRegion = null;
    }


    private void createFrameBuffer() {
        frameBufferYio = FrameBufferYio.getInstance(
                Pixmap.Format.RGB565,
                (int) (qualityRatio * vmCacheManager.bWidth),
                (int) (qualityRatio * vmCacheManager.bHeight),
                false
        );
    }


    private void updateQualityRatio() {
        if (YioGdxGame.platformType == PlatformType.pc) {
            qualityRatio = 1;
            return;
        }
        ObjectsLayer objectsLayer = vmCacheManager.viewableModel.objectsLayer;
        SizeManager sizeManager = objectsLayer.gameController.sizeManager;
        switch (sizeManager.initialLevelSize) {
            default:
                qualityRatio = 1;
                break;
            case large:
                qualityRatio = 0.75f;
                break;
            case giant:
            case giant_landscape:
                qualityRatio = 0.6f;
                break;
        }
    }


    void prepareBatch(SpriteBatch batch) {
        batch.setProjectionMatrix(matrix4);
    }


    void checkToCreateTextureRegion() {
        if (textureRegion != null) return;
        textureRegion = new TextureRegion(
                frameBufferYio.getColorBufferTexture(),
                (int) (qualityRatio * vmCacheManager.bWidth),
                (int) (qualityRatio * vmCacheManager.bHeight)
        );
        textureRegion.flip(false, true);
    }


    void onPositionChanged() {
        matrix4.setToOrtho2D(
                (int) position.x,
                (int) position.y,
                vmCacheManager.bWidth,
                vmCacheManager.bHeight
        );
    }
}
