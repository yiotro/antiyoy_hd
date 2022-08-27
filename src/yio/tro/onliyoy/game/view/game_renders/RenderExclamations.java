package yio.tro.onliyoy.game.view.game_renders;

import yio.tro.onliyoy.game.viewable_model.Exclamation;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

public class RenderExclamations extends GameRender{


    private Storage3xTexture exclamation3xTexture;


    @Override
    protected void loadTextures() {
        exclamation3xTexture = load3xTexture("exclamation_mark");
    }


    @Override
    public void render() {
        for (Exclamation exclamation : getViewableModel().exclamationsManager.exclamations) {
            GraphicsYio.setBatchAlpha(batchMovable, exclamation.appearFactor.getValue());
            GraphicsYio.drawByCircle(
                    batchMovable,
                    exclamation3xTexture.getTexture(getCurrentZoomQuality()),
                    exclamation.viewPosition
            );
            GraphicsYio.setBatchAlpha(batchMovable, 1);
        }
    }


    @Override
    protected void disposeTextures() {

    }
}
