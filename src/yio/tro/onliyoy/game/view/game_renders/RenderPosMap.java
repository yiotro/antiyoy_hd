package yio.tro.onliyoy.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.posmap.AbstractPmObjectYio;
import yio.tro.onliyoy.stuff.posmap.PosMapYio;

import java.util.ArrayList;

public class RenderPosMap extends GameRender{


    private TextureRegion xTexture;
    private PosMapYio posMap;
    RectangleYio tempRectangle;
    private RectangleYio pmPos;


    public RenderPosMap() {
        tempRectangle = new RectangleYio();
        pmPos = new RectangleYio();
    }


    @Override
    protected void loadTextures() {
        xTexture = GraphicsYio.loadTextureRegion("game/stuff/x.png", false);
    }


    @Override
    public void render() {
        if (!DebugFlags.showPosMap) return;
        updateReferences();
        renderSectors();
        GraphicsYio.renderBorder(batchMovable, getBlackPixel(), pmPos);
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private void updateReferences() {
        posMap = getObjectsLayer().viewableModel.posMapYio;
        pmPos.setBy(posMap.position);
    }


    private void renderSectors() {
        float alpha;
        float sectorSize = posMap.sectorSize;
        float alphaDelta = 0.05f;

        ArrayList<AbstractPmObjectYio> sector;
        for (int i = 0; i < posMap.width; i++) {
            for (int j = 0; j < posMap.height; j++) {
                sector = posMap.getSector(i, j);
                if (sector.size() == 0) continue;

                alpha = Math.min(1, 0.1f + alphaDelta * sector.size());

                GraphicsYio.setBatchAlpha(batchMovable, alpha);
                tempRectangle.set(
                        pmPos.x + i * sectorSize,
                        pmPos.y + j * sectorSize,
                        sectorSize, sectorSize
                );
                GraphicsYio.drawByRectangle(batchMovable, getBlackPixel(), tempRectangle);
            }
        }
    }


    @Override
    protected void disposeTextures() {
        xTexture.getTexture().dispose();
    }
}