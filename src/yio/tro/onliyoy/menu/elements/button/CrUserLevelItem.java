package yio.tro.onliyoy.menu.elements.button;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.customizable_list.UserLevelListItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class CrUserLevelItem extends AbstractCacheRender{

    UserLevelListItem ullItem;
    private TextureRegion completionTexture;
    private TextureRegion lineBackgroundTexture;
    private TextureRegion lineForegroundTexture;
    private TextureRegion separatorTexture;
    private TextureRegion blackPixel;
    private CircleYio tempCircle;
    private RenderableTextYio tempRenderableText;
    private RectangleYio tempRectangle;


    public CrUserLevelItem() {
        tempCircle = new CircleYio();
        tempRenderableText = new RenderableTextYio();
        tempRectangle = new RectangleYio();
    }


    @Override
    public void loadTextures() {
        completionTexture = GraphicsYio.loadTextureRegion("menu/user_levels/completed.png", true);
        lineBackgroundTexture = GraphicsYio.loadTextureRegion("pixels/white.png", true);
        lineForegroundTexture = GraphicsYio.loadTextureRegion("pixels/dark.png", true);
        separatorTexture = GraphicsYio.loadTextureRegion("menu/separator.png", true);
        blackPixel = GraphicsYio.loadTextureRegion("pixels/black.png", false);
    }


    @Override
    public void setTarget(Object object) {
        ullItem = (UserLevelListItem) object;
        framePosition.set(0, 0, ullItem.viewPosition.width, ullItem.viewPosition.height);
    }


    @Override
    protected void render(SpriteBatch batch) {
        renderBackground(batch);
        renderCompletion(batch);
        renderNameAndDescription(batch);
        renderRating(batch);
    }


    private void renderRating(SpriteBatch batch) {
        if (ullItem.netUlCacheData.likes == 0 && ullItem.netUlCacheData.dislikes == 0) return;
        renderInternalText(batch, ullItem.ratingViewText, 1);
        renderInternalRectangle(batch, lineBackgroundTexture, ullItem.ratingBackground);
        renderInternalRectangle(batch, lineForegroundTexture, ullItem.ratingForeground);
        renderInternalBorder(batch, separatorTexture, ullItem.ratingBackground);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderInternalBorder(SpriteBatch batch, TextureRegion textureRegion, RectangleYio srcRectangle) {
        tempRectangle.setBy(srcRectangle);
        tempRectangle.x -= ullItem.viewPosition.x;
        tempRectangle.y -= ullItem.viewPosition.y;
        tempRectangle.x += framePosition.x;
        tempRectangle.y += framePosition.y;
        GraphicsYio.renderBorder(batch, textureRegion, tempRectangle);
    }


    private void renderInternalRectangle(SpriteBatch batch, TextureRegion textureRegion, RectangleYio srcRectangle) {
        tempRectangle.setBy(srcRectangle);
        tempRectangle.x -= ullItem.viewPosition.x;
        tempRectangle.y -= ullItem.viewPosition.y;
        tempRectangle.x += framePosition.x;
        tempRectangle.y += framePosition.y;
        GraphicsYio.drawByRectangle(batch, textureRegion, tempRectangle);
    }


    private void renderNameAndDescription(SpriteBatch batch) {
        renderInternalText(batch, ullItem.name, 1);
        renderInternalText(batch, ullItem.desc1, 0.7f);
        renderInternalText(batch, ullItem.desc2, 0.7f);
    }


    private void renderInternalText(SpriteBatch batch, RenderableTextYio srcText, float alpha) {
        tempRenderableText.setBy(srcText);
        tempRenderableText.position.x -= ullItem.viewPosition.x;
        tempRenderableText.position.y -= ullItem.viewPosition.y;
        tempRenderableText.position.x += framePosition.x;
        tempRenderableText.position.y += framePosition.y;
        GraphicsYio.setFontAlpha(tempRenderableText.font, alpha);
        GraphicsYio.renderText(batch, tempRenderableText);
        GraphicsYio.setFontAlpha(tempRenderableText.font, 1);
    }


    private void renderCompletion(SpriteBatch batch) {
        if (!ullItem.completed) return;
        tempCircle.setBy(ullItem.beatIconPosition);
        tempCircle.center.x -= ullItem.viewPosition.x;
        tempCircle.center.y -= ullItem.viewPosition.y;
        tempCircle.center.x += framePosition.x;
        tempCircle.center.y += framePosition.y;
        GraphicsYio.drawByCircle(batch, completionTexture, tempCircle);
    }


    private void renderBackground(SpriteBatch batch) {
        GraphicsYio.drawByRectangle(
                batch,
                MenuRenders.renderMultiButtonElement.mapBackgrounds.get(ullItem.backgroundYio),
                framePosition
        );
    }
}
