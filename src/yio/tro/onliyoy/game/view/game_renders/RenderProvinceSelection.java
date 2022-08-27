package yio.tro.onliyoy.game.view.game_renders;

import java.util.ArrayList;

import yio.tro.onliyoy.game.viewable_model.BorderIndicator;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

public class RenderProvinceSelection extends GameRender {


    private Storage3xTexture border3xTexture;


    @Override
    protected void loadTextures() {
        border3xTexture = load3xTexture("selection_border");
    }


    @Override
    public void render() {
        ArrayList<BorderIndicator> indicators = getViewableModel().provinceSelectionManager.indicators;
        if (indicators.size() == 0) return;
        for (BorderIndicator indicator : indicators) {
            if (!indicator.isCurrentlyVisible()) continue;
            GraphicsYio.drawRectangleRotatedByCenter(
                    batchMovable,
                    border3xTexture.getTexture(getCurrentZoomQuality()),
                    indicator.viewPosition.center.x,
                    indicator.viewPosition.center.y,
                    indicator.viewSize.x,
                    indicator.viewSize.y,
                    indicator.viewPosition.angle
            );
        }
    }


    @Override
    protected void disposeTextures() {

    }
}
