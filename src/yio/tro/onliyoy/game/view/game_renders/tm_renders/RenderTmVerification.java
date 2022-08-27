package yio.tro.onliyoy.game.view.game_renders.tm_renders;

import com.badlogic.gdx.graphics.Color;
import yio.tro.onliyoy.game.touch_modes.TmVerification;
import yio.tro.onliyoy.game.touch_modes.TmvViewItem;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.view.game_renders.GameRender;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RenderTmVerification extends GameRender {

    TmVerification tm;


    @Override
    protected void loadTextures() {

    }


    @Override
    public void render() {
        tm = TouchMode.tmVerification;
        for (TmvViewItem viewItem : tm.viewItems) {
            GraphicsYio.setBatchAlpha(batchMovable, 0.6);
            GraphicsYio.drawByRectangle(batchMovable, getBlackPixel(), viewItem.incBounds);
            GraphicsYio.setBatchAlpha(batchMovable, 1);
            RenderableTextYio title = viewItem.title;
            title.font.setColor(0.9f, 0.9f, 0.9f, 1);
            GraphicsYio.renderText(batchMovable, title);
            title.font.setColor(Color.BLACK);
        }
    }


    @Override
    protected void disposeTextures() {

    }
}
