package yio.tro.onliyoy.game.view.game_renders;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.viewable_model.ViewableHex;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

import java.util.HashMap;

public class RenderHexesInTransition extends GameRender{

    HashMap<HColor, Storage3xTexture> mapHexTextures;
    CircleYio circleYio;


    public RenderHexesInTransition() {
        circleYio = new CircleYio();
    }


    @Override
    protected void loadTextures() {
        mapHexTextures = new HashMap<>();
        for (HColor hColor : HColor.values()) {
            mapHexTextures.put(hColor, load3xTexture("hex_" + hColor, true));
        }
    }


    @Override
    public void render() {
        for (ViewableHex viewableHex : getViewableModel().cacheManager.ctViewableHexes) {
            if (!viewableHex.isCurrentlyVisible()) continue;
            if (!viewableHex.isColorFactorInMovementMode()) continue;
            GraphicsYio.setBatchAlpha(batchMovable, 1 - viewableHex.colorFactor.getValue());
            renderHex(viewableHex.hex, viewableHex.previousColor);
        }
    }


    private void renderHex(Hex hex, HColor color) {
        circleYio.setBy(hex.position);
        circleYio.radius += GraphicsYio.borderThickness;
        GraphicsYio.drawByCircle(
                batchMovable,
                mapHexTextures.get(color).getTexture(getCurrentZoomQuality()),
                circleYio
        );
    }


    @Override
    protected void disposeTextures() {

    }
}
