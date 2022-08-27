package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.stuff.*;

import java.util.HashMap;

public class RenderRoundShape extends RenderInterfaceElement{

    TextureRegion currentBackground;
    TextureRegion currentCorner;
    float cr;
    RectangleYio pos1, pos2, pos3;
    private RectangleYio position;
    CircleYio corners[];
    SpriteBatch currentBatch;
    HashMap<BackgroundYio, TextureRegion> mapCorners;
    HashMap<BackgroundYio, TextureRegion> mapMains;
    boolean onlySidesMode;


    public RenderRoundShape() {
        pos1 = new RectangleYio();
        pos2 = new RectangleYio();
        pos3 = new RectangleYio();
        currentBackground = null;

        corners = new CircleYio[4];
        for (int i = 0; i < corners.length; i++) {
            corners[i] = new CircleYio();
        }
    }


    @Override
    public void loadTextures() {
        mapCorners = new HashMap<>();
        mapMains = new HashMap<>();
        for (BackgroundYio backgroundYio : BackgroundYio.values()) {
            mapCorners.put(backgroundYio, loadCornerTexture("" + backgroundYio));
            mapMains.put(backgroundYio, loadMainBackground("" + backgroundYio));
        }
    }


    TextureRegion getCornerTexture(BackgroundYio backgroundYio) {
        return mapCorners.get(backgroundYio);
    }


    public TextureRegion getBackgroundTexture(BackgroundYio backgroundYio) {
        return mapMains.get(backgroundYio);
    }


    private TextureRegion loadMainBackground(String name) {
        return GraphicsYio.loadTextureRegion("menu/round_shape/" + name + ".png", false);
    }


    private TextureRegion loadCornerTexture(String name) {
        return GraphicsYio.loadTextureRegion("menu/round_shape/" + name + "_corner.png", true);
    }


    public void renderRoundShape(SpriteBatch argBatch, RectangleYio position, BackgroundYio backgroundYio, float cornerRadius, boolean onlySides) {
        currentBatch = argBatch;
        currentBackground = getBackgroundTexture(backgroundYio);
        currentCorner = getCornerTexture(backgroundYio);
        onlySidesMode = onlySides;
        if (cornerRadius > position.height / 2 - 1) {
            cornerRadius = position.height / 2 - 1;
        }
        cr = cornerRadius / 2;
        this.position = position;

        updatePos1();
        updatePos2();
        updatePos3();
        updateCorners();

        GraphicsYio.drawByRectangle(currentBatch, currentBackground, pos1);
        if (!onlySidesMode) {
            GraphicsYio.drawByRectangle(currentBatch, currentBackground, pos2);
        }
        GraphicsYio.drawByRectangle(currentBatch, currentBackground, pos3);
        renderCorners();
    }


    public void renderRoundShape(RectangleYio position, BackgroundYio backgroundYio, float cornerRadius) {
        renderRoundShape(batch, position, backgroundYio, cornerRadius, false);
    }


    private void renderCorners() {
        for (int i = 0; i < corners.length; i++) {
            GraphicsYio.drawByCircle(
                    currentBatch,
                    currentCorner,
                    corners[i]
            );
        }
    }


    private void updateCorners() {
        corners[0]
                .setRadius(cr)
                .setAngle(0)
                .center.set(position.x + cr, position.y + cr);

        corners[1]
                .setRadius(cr)
                .setAngle(-0.5 * Math.PI)
                .center.set(position.x + cr, position.y + position.height - cr);

        corners[2]
                .setRadius(cr)
                .setAngle(-Math.PI)
                .center.set(position.x + position.width - cr, position.y + position.height - cr);

        corners[3]
                .setRadius(cr)
                .setAngle(-1.5 * Math.PI)
                .center.set(position.x + position.width - cr, position.y + cr);

    }


    private void updatePos3() {
        pos3.x = position.x + position.width - 2 * cr;
        pos3.y = position.y + 2 * cr;
        pos3.width = 2 * cr;
        pos3.height = position.height - 4 * cr;
    }


    private void updatePos2() {
        pos2.x = position.x + 2 * cr;
        pos2.y = position.y;
        pos2.width = position.width - 4 * cr;
        pos2.height = position.height;
    }


    private void updatePos1() {
        pos1.x = position.x;
        pos1.y = position.y + 2 * cr;
        pos1.width = 2 * cr;
        pos1.height = position.height - 4 * cr;
    }


    public void setCurrentBatch(SpriteBatch currentBatch) {
        this.currentBatch = currentBatch;
    }


    public void renderRoundShape(RectangleYio position, BackgroundYio backgroundYio) {
        renderRoundShape(position, backgroundYio, GraphicsYio.defCornerRadius);
    }


    @Override
    public void render(InterfaceElement element) {

    }


}
