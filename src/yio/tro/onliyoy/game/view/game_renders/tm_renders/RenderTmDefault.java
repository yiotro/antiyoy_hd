package yio.tro.onliyoy.game.view.game_renders.tm_renders;

import yio.tro.onliyoy.game.touch_modes.TmDefault;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.view.game_renders.GameRender;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

public class RenderTmDefault extends GameRender{

    private TmDefault tm;
    CircleYio circleYio;
    private Storage3xTexture highlight3xTexture;


    public RenderTmDefault() {
        circleYio = new CircleYio();
    }


    @Override
    protected void loadTextures() {
        highlight3xTexture = load3xTexture("hex_black");
    }


    @Override
    public void render() {
        tm = TouchMode.tmDefault;
        renderHighlight();
    }


    private void renderHighlight() {
        if (tm.highlightFactor.getValue() == 0) return;
        GraphicsYio.setBatchAlpha(batchMovable, 0.75 * tm.highlightFactor.getValue());
        GraphicsYio.drawByCircle(
                batchMovable,
                highlight3xTexture.getTexture(getCurrentZoomQuality()),
                tm.highlightHex.position
        );
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    @Override
    protected void disposeTextures() {

    }
}
