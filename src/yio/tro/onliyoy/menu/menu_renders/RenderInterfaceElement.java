package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.onliyoy.game.view.GameView;
import yio.tro.onliyoy.menu.MenuViewYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

public abstract class RenderInterfaceElement {

    protected MenuViewYio menuViewYio;
    protected SpriteBatch batch;
    protected Color c;
    public int w, h;
    protected TextureRegion blackPixel;
    protected ShapeRenderer shapeRenderer;
    protected float alpha;


    public RenderInterfaceElement() {
        alpha = 1;
        MenuRenders.list.listIterator().add(this);
    }


    void update(MenuViewYio menuViewYio) {
        this.menuViewYio = menuViewYio;
        batch = menuViewYio.batch;
        c = batch.getColor();
        w = menuViewYio.w;
        h = menuViewYio.h;
        blackPixel = GraphicsYio.loadTextureRegion("pixels/black.png", false);
        shapeRenderer = menuViewYio.shapeRenderer;
        loadTextures();
    }


    public abstract void loadTextures();


    public abstract void render(InterfaceElement element);


    public GameView getGameView() {
        return menuViewYio.yioGdxGame.gameView;
    }


    protected TextureRegion getTextureFromAtlas(String name) {
        return (new Storage3xTexture(menuViewYio.menuAtlas, name)).getNormal();
    }


    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }


    protected void prepareShapeRenderer() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(menuViewYio.orthoCam.combined);
    }


    protected void drawRectShape(RectangleYio pos) {
        shapeRenderer.rect(pos.x, pos.y, pos.width, pos.height);
    }


    protected void drawRoundRectShape(RectangleYio pos, float cr) {
        GraphicsYio.drawRoundRectWithShape(shapeRenderer, pos, cr);
    }


    protected void renderWhiteText(RenderableTextYio renderableTextYio, TextureRegion whitePixel, double alpha) {
        if (renderableTextYio.isMovingFast()) {
            GraphicsYio.setBatchAlpha(batch, 0.25 * alpha);
            GraphicsYio.drawByRectangle(batch, whitePixel, renderableTextYio.bounds);
            GraphicsYio.setBatchAlpha(batch, 1);
        } else {
            renderableTextYio.font.setColor(0.9f, 0.9f, 0.9f, (float) alpha);
            GraphicsYio.renderText(batch, renderableTextYio);
            renderableTextYio.font.setColor(Color.BLACK);
        }
    }


    protected void renderRedText(RenderableTextYio renderableTextYio, TextureRegion redPixel, double alpha) {
        if (renderableTextYio.isMovingFast()) {
            GraphicsYio.setBatchAlpha(batch, 0.25 * alpha);
            GraphicsYio.drawByRectangle(batch, redPixel, renderableTextYio.bounds);
            GraphicsYio.setBatchAlpha(batch, 1);
        } else {
            renderableTextYio.font.setColor(0.92f, 0.38f, 0.47f, (float) alpha);
            GraphicsYio.renderText(batch, renderableTextYio);
            renderableTextYio.font.setColor(Color.BLACK);
        }
    }

}
