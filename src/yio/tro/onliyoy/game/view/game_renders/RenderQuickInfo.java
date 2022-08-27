package yio.tro.onliyoy.game.view.game_renders;

import com.badlogic.gdx.graphics.Color;
import yio.tro.onliyoy.game.viewable_model.QimItem;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RenderQuickInfo extends GameRender{

    @Override
    protected void loadTextures() {

    }


    @Override
    public void render() {
        for (QimItem item : getObjectsLayer().viewableModel.quickInfoManager.items) {
            GraphicsYio.setBatchAlpha(batchMovable, 0.6 * item.appearFactor.getValue());
            GraphicsYio.drawByRectangle(batchMovable, getBlackPixel(), item.incBounds);
            GraphicsYio.setBatchAlpha(batchMovable, 1);
            RenderableTextYio title = item.title;
            title.font.setColor(0.9f, 0.9f, 0.9f, item.appearFactor.getValue());
            GraphicsYio.renderText(batchMovable, title);
            title.font.setColor(Color.BLACK);
        }
    }


    @Override
    protected void disposeTextures() {

    }
}
