package yio.tro.onliyoy.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.viewable_model.UmSelector;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

public class RenderUmSelector extends GameRender{


    private Storage3xTexture selection3xTexture;
    private Storage3xTexture directModeSelection3xTexture;


    @Override
    protected void loadTextures() {
        selection3xTexture = load3xTexture("unit_selection");
        directModeSelection3xTexture = load3xTexture("dr_selection");
    }


    @Override
    public void render() {
        UmSelector selector = getObjectsLayer().viewableModel.unitsManager.selector;
        if (selector.appearFactor.getValue() == 0) return;
        GraphicsYio.setBatchAlpha(batchMovable, selector.appearFactor.getValue());
        GraphicsYio.drawByCircle(
                batchMovable,
                getSelectionTexture(),
                selector.viewPosition
        );
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private TextureRegion getSelectionTexture() {
        if (DebugFlags.directRender) {
            return directModeSelection3xTexture.getTexture(getCurrentZoomQuality());
        }
        return selection3xTexture.getTexture(getCurrentZoomQuality());
    }


    @Override
    protected void disposeTextures() {

    }
}
