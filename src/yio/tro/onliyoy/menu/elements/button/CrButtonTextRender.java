package yio.tro.onliyoy.menu.elements.button;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.VisualTextContainer;

public class CrButtonTextRender extends AbstractCacheRender{

    private ButtonYio buttonYio;
    RenderableTextYio tempRenderableText;


    public CrButtonTextRender() {
        super();
        tempRenderableText = new RenderableTextYio();
    }


    @Override
    public void loadTextures() {

    }


    @Override
    public void setTarget(Object object) {
        buttonYio = (ButtonYio) object;
        framePosition.setBy(buttonYio.framePosition);
        framePosition.x = 0;
        framePosition.y = 0;
    }


    @Override
    protected void render(SpriteBatch batch) {
        GraphicsYio.setBatchAlpha(batch, 1);
        renderBackground(batch);
        for (VisualTextContainer textContainer : buttonYio.textContainers) {
            float vDelta = textContainer.position.y;
            for (RenderableTextYio renderableTextYio : textContainer.textList) {
                tempRenderableText.setBy(renderableTextYio);
                tempRenderableText.position.x += framePosition.x;
                tempRenderableText.position.y += framePosition.y + vDelta;
                tempRenderableText.font.setColor(Color.BLACK);
                GraphicsYio.renderText(batch, tempRenderableText);
            }
        }
    }


    private void renderBackground(SpriteBatch batch) {
        TextureRegion backgroundTexture = MenuRenders.renderRoundShape.getBackgroundTexture(buttonYio.background);
        GraphicsYio.drawByRectangle(
                batch,
                backgroundTexture,
                framePosition
        );
    }
}
