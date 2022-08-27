package yio.tro.onliyoy.menu.elements.button;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class CrLabelElement extends AbstractCacheRender{

    LabelElement labelElement;
    PointYio tempPoint;
    private TextureRegion backgroundTexture;
    private RenderableTextYio title;
    private float delta;


    public CrLabelElement() {
        super();
        tempPoint = new PointYio();
    }


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("pixels/dark.png", false);
    }


    @Override
    public void setTarget(Object object) {
        labelElement = (LabelElement) object;
        title = labelElement.title;
        delta = 0.015f * GraphicsYio.dim;
        framePosition.set(0, 0, title.width + 2 * delta, title.height + 2 * delta);
    }


    @Override
    protected void render(SpriteBatch batch) {
        GraphicsYio.drawByRectangle(batch, backgroundTexture, framePosition);
        tempPoint.set(framePosition.x + delta, framePosition.y + framePosition.height - delta);
        BitmapFont font = title.font;
        font.setColor(Color.WHITE);
        GraphicsYio.renderText(
                batch,
                font,
                title.string,
                tempPoint
        );
        font.setColor(Color.BLACK);
    }
}
