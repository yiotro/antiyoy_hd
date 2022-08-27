package yio.tro.onliyoy.game.view.game_renders;

import yio.tro.onliyoy.game.viewable_model.DefenseIndicator;
import yio.tro.onliyoy.game.viewable_model.DefenseViewer;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

public class RenderDefenseIndicators extends GameRender{

    Storage3xTexture[] textures;
    private DefenseViewer defenseViewer;


    @Override
    protected void loadTextures() {
        textures = new Storage3xTexture[3];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = load3xTexture("defense_indicator_" + (i + 1));
        }
    }


    @Override
    public void render() {
        defenseViewer = getObjectsLayer().viewableModel.defenseViewer;
        for (DefenseIndicator indicator : defenseViewer.indicators) {
            if (!indicator.isCurrentlyVisible()) continue;
            GraphicsYio.setBatchAlpha(batchMovable, indicator.appearFactor.getValue());
            GraphicsYio.drawByCircle(
                    batchMovable,
                    textures[indicator.defenseValue - 1].getTexture(getCurrentZoomQuality()),
                    indicator.viewPosition
            );
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    @Override
    protected void disposeTextures() {

    }
}
