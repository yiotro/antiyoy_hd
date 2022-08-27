package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.highlight_area.HighlightAreaElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Masking;
import yio.tro.onliyoy.stuff.RectangleYio;

public class RenderHighlightAreaElement extends RenderInterfaceElement{


    private HighlightAreaElement haElement;
    private RectangleYio screenPosition;
    private TextureRegion whitePixel;


    public RenderHighlightAreaElement() {
        screenPosition = new RectangleYio(0, 0, GraphicsYio.width, GraphicsYio.height);
    }


    @Override
    public void loadTextures() {
        whitePixel = GraphicsYio.loadTextureRegion("pixels/white.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        haElement = (HighlightAreaElement) element;

        renderDarken();
        renderBorder();
        renderTitle();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderTitle() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, haElement.incBounds);
        renderWhiteText(haElement.title, whitePixel, alpha);
    }


    private void renderBorder() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.renderBorder(batch, blackPixel, haElement.highlightPosition);
    }


    private void renderDarken() {
        batch.end();
        Masking.begin();
        shapeRenderer = menuViewYio.shapeRenderer;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(menuViewYio.orthoCam.combined);
        drawRectShape(haElement.highlightPosition);
        shapeRenderer.end();
        batch.begin();
        Masking.continueAfterBatchBegin();
        Masking.applyInvertedMode();
        GraphicsYio.setBatchAlpha(batch, 0.6f * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, screenPosition);
        Masking.end(batch);
    }
}
