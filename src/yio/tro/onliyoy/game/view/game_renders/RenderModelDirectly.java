package yio.tro.onliyoy.game.view.game_renders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.general.SkinManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.stuff.*;

import java.util.HashMap;

public class RenderModelDirectly extends GameRender{

    private RenderableTextYio renderableTextYio;
    HashMap<HColor, Storage3xTexture> mapHexTextures;
    CircleYio circleYio;
    CircleYio indicatorCircle;
    RenderableTextYio title;
    private TextureRegion redPixel;


    public RenderModelDirectly() {
        super();
        renderableTextYio = new RenderableTextYio();
        renderableTextYio.setFont(Fonts.miniFont);
        circleYio = new CircleYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        title.setString("Direct render");
        title.updateMetrics();
        indicatorCircle = new CircleYio();
    }


    @Override
    protected void loadTextures() {
        redPixel = GraphicsYio.loadTextureRegion("pixels/red.png", false);
        mapHexTextures = new HashMap<>();
        for (HColor hColor : HColor.values()) {
            mapHexTextures.put(hColor, load3xTexture("hex_" + hColor, true));
        }
    }


    @Override
    public void render() {
        renderTitle();
        renderHexes();
        renderPieces();
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private void renderTitle() {
        updateTitlePosition();
        title.font.setColor(0.9f, 0.9f, 0.9f, 1);
        GraphicsYio.renderText(batchMovable, title);
        title.font.setColor(Color.BLACK);
    }


    private void updateTitlePosition() {
        RectangleYio pos = gameController.sizeManager.position;
        title.position.x = pos.x;
        title.position.y = pos.y + pos.height + 0.01f * GraphicsYio.height + title.height;
        title.updateBounds();
    }


    private void renderPieces() {
        circleYio.setRadius(0.7f * getObjectsLayer().getHexRadius());
        ViewableModel viewableModel = getViewableModel();
        for (Hex hex : getRenderedModel().hexes) {
            if (!hex.hasPiece()) continue;
            if (!viewableModel.isHexCurrentlyVisible(hex)) continue;
            circleYio.center.setBy(hex.position.center);
            Storage3xTexture piece3xTexture = SkinManager.getInstance().getPiece3xTexture(hex, hex.piece);
            GraphicsYio.drawByCircle(
                    batchMovable,
                    piece3xTexture.getTexture(getCurrentZoomQuality()),
                    circleYio
            );
            checkToIndicateReadiness(hex);
        }
    }


    private void checkToIndicateReadiness(Hex hex) {
        if (!Core.isUnit(hex.piece)) return;
        if (!getRenderedModel().readinessManager.isReady(hex)) return;
        indicatorCircle.center.setBy(hex.position.center);
        indicatorCircle.center.x += 0.6f * circleYio.radius;
        indicatorCircle.center.y += 0.65f * circleYio.radius;
        indicatorCircle.radius = 0.12f * circleYio.radius;
        GraphicsYio.drawByCircle(batchMovable, redPixel, indicatorCircle);
    }


    private void renderHexes() {
        GraphicsYio.setBatchAlpha(batchMovable, 1);
        for (Hex hex : getRenderedModel().hexes) {
            renderSingleHex(hex);
        }
    }


    private CoreModel getRenderedModel() {
        if (DebugFlags.showRefModel) {
            return getViewableModel().refModel;
        }
        return getViewableModel();
    }


    private void renderSingleHex(Hex hex) {
        circleYio.setBy(hex.position);
        circleYio.radius += GraphicsYio.borderThickness;
        GraphicsYio.drawByCircle(
                batchMovable,
                mapHexTextures.get(hex.getColor()).getTexture(getCurrentZoomQuality()),
                circleYio
        );
        renderCoordinates(hex);
    }


    private void renderCoordinates(Hex hex) {
        if (!DebugFlags.showCoordinates) return;
        renderableTextYio.setString(hex.coordinate1 + ", " + hex.coordinate2);
        renderableTextYio.updateMetrics();
        renderableTextYio.position.x = hex.position.center.x - renderableTextYio.width / 2;
        renderableTextYio.position.y = hex.position.center.y + renderableTextYio.height / 2;
        GraphicsYio.renderText(batchMovable, renderableTextYio);
    }


    @Override
    protected void disposeTextures() {

    }
}
