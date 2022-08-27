package yio.tro.onliyoy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SplashManager {

    YioGdxGame yioGdxGame;
    int counter;
    public boolean visible;
    TextureRegion texture;
    FactorYio factor;
    private SpriteBatch batch;
    float defaultRadius;
    CircleYio logoPosition;
    private TextureRegion blackPixel;
    private RectangleYio screenPosition;


    public SplashManager(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        counter = 0;
        defaultRadius = 0.2f * GraphicsYio.dim;
        logoPosition = new CircleYio();
        screenPosition = new RectangleYio();
        screenPosition.set(0, 0, GraphicsYio.width, GraphicsYio.height);
        factor = new FactorYio();
        loadTextures();
    }


    private void loadTextures() {
        texture = GraphicsYio.loadTextureRegion("menu/splash.png", true);
        blackPixel = GraphicsYio.loadTextureRegion("pixels/black.png", false);
    }


    void init() {
        visible = true;
        factor.setValues(0, 0);
        factor.appear(MovementType.lighty, 4);
    }


    void move() {
        if (!visible) return;

        factor.move();
        updateLogoPosition();

        if (factor.getValue() == 1) {
            visible = false;
        }
    }


    private void updateLogoPosition() {
        logoPosition.center.set(GraphicsYio.width / 2, GraphicsYio.height / 2);
        logoPosition.setRadius(defaultRadius);
        if (factor.getValue() == 0) return;

        logoPosition.radius *= 1 + 0.33 * factor.getValue();
    }


    void increaseSplashCount() {
        if (counter == 2) {
            yioGdxGame.generalInitialization();
        }
        counter++;
    }


    void renderBeforeInitialization() {
        batch = yioGdxGame.batch;
        updateLogoPosition();
        batch.begin();
        GraphicsYio.drawByRectangle(batch, blackPixel, screenPosition);
        GraphicsYio.drawByCircle(batch, texture, logoPosition);
        batch.end();
    }


    void renderAfterInitialization() {
        if (!visible) return;
        batch = yioGdxGame.batch;
        batch.begin();
        Color c = batch.getColor();
        float a = c.a;
        batch.setColor(c.r, c.g, c.b, 1 - factor.getValue());
        GraphicsYio.drawByRectangle(batch, blackPixel, screenPosition);
        GraphicsYio.drawByCircle(batch, texture, logoPosition);
        batch.setColor(c.r, c.g, c.b, a);
        batch.end();
    }


    public boolean areKeyInputsAllowed() {
        return factor.getValue() == 1;
    }
}
