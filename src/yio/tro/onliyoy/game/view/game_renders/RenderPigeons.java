package yio.tro.onliyoy.game.view.game_renders;

import yio.tro.onliyoy.game.viewable_model.Pigeon;
import yio.tro.onliyoy.game.viewable_model.PigeonsManager;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

public class RenderPigeons extends GameRender {


    private Storage3xTexture letter3xTexture;


    @Override
    protected void loadTextures() {
        letter3xTexture = load3xTexture("letter");
    }


    @Override
    public void render() {
        PigeonsManager pigeonsManager = getViewableModel().pigeonsManager;
        for (Pigeon pigeon : pigeonsManager.pigeons) {
            if (!pigeon.isCurrentlyVisible()) continue;
            GraphicsYio.setBatchAlpha(batchMovable, pigeonsManager.alphaModifier * pigeon.getAlpha());
            GraphicsYio.drawByCircle(
                    batchMovable,
                    letter3xTexture.getTexture(getCurrentZoomQuality()),
                    pigeon.viewPosition
            );
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    @Override
    protected void disposeTextures() {

    }
}
