package yio.tro.onliyoy.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.SkinManager;
import yio.tro.onliyoy.game.viewable_model.DirectionsManager;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

import java.util.HashMap;

public class RenderCacheableStuff extends GameRender {

    public HashMap<HColor, Storage3xTexture> mapHexTextures;
    Storage3xTexture shadowHex3xTexture;
    CircleYio circleYio;
    private CoreModel model;
    DirectionsManager directionsManager;
    Storage3xTexture outline3xTexture;
    private final double cosPi6;
    PointYio point1;
    PointYio point2;
    PointYio tempPoint;
    private Storage3xTexture gradient3xTexture;
    float outlineRadius;


    public RenderCacheableStuff() {
        super();
        cosPi6 = Math.cos(Math.PI / 6);
        circleYio = new CircleYio();
        directionsManager = new DirectionsManager(null);
        point1 = new PointYio();
        point2 = new PointYio();
        tempPoint = new PointYio();
    }


    @Override
    protected void loadTextures() {
        mapHexTextures = new HashMap<>();
        for (HColor hColor : HColor.values()) {
            mapHexTextures.put(hColor, load3xTexture("hex_" + hColor, true));
        }
        shadowHex3xTexture = load3xTexture("hex_shadow", true);
        outline3xTexture = load3xTexture("outline", true);
        gradient3xTexture = load3xTexture("gradient", true);
    }


    @Override
    public void render() {
        updateModelReference();
        directionsManager.setCoreModel(model);
        outlineRadius = 0.518f * model.getHexRadius();
        renderShadows();
        renderHexesAndStaticPieces();
        renderOutlinesAndGradients();
        renderGrid();
    }


    private void renderGrid() {
        if (!SettingsManager.getInstance().grid) return;
        GraphicsYio.setBatchAlpha(batchMovable, 0.2);
        for (Hex hex : model.hexes) {
            if (hex.counter == -1) continue;
            for (int dir = 0; dir < 6; dir++) {
                Hex adjacentHex = directionsManager.getAdjacentHex(hex, dir);
                if (adjacentHex == null) continue;
                if (adjacentHex.color != hex.color) continue;
                renderSingleGridLine(hex, dir);
            }
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    private void renderSingleGridLine(Hex hex, int dir) {
        double angle = directionsManager.getAngle(dir);
        float hexRadius = getViewableModel().getHexRadius();
        double distance = hexRadius * cosPi6;
        point1.setBy(hex.position.center);
        point1.relocateRadial(distance, angle);
        point2.setBy(point1);
        point1.relocateRadial(outlineRadius, angle + Math.PI / 2);
        point2.relocateRadial(outlineRadius, angle - Math.PI / 2);
        GraphicsYio.drawLine(
                batchMovable,
                outline3xTexture.getTexture(GraphicsYio.QUALITY_HIGH),
                point1,
                point2,
                1.5f * GraphicsYio.borderThickness
        );
    }


    private void updateModelReference() {
        model = getViewableModel().refModel;
        if (gameController.gameMode == GameMode.editor) {
            model = getViewableModel();
        }
    }


    private void renderOutlinesAndGradients() {
        for (Hex hex : model.hexes) {
            if (hex.counter == -1) continue;
            for (int dir = 0; dir < 6; dir++) {
                Hex adjacentHex = directionsManager.getAdjacentHex(hex, dir);
                if (adjacentHex == null) {
                    renderSingleOutlineAndGradient(hex, dir);
                    continue;
                }
                if (adjacentHex.color != hex.color && isDownwards(dir)) {
                    renderSingleOutlineAndGradient(hex, dir);
                }
            }
        }
    }


    private boolean isDownwards(int dir) {
        return dir == 2 || dir == 3 || dir == 4;
    }


    private void renderSingleOutlineAndGradient(Hex hex, int dir) {
        double angle = directionsManager.getAngle(dir);
        float hexRadius = getViewableModel().getHexRadius();
        double distance = hexRadius * cosPi6;
        point1.setBy(hex.position.center);
        point1.relocateRadial(distance, angle);
        if (isDownwards(dir)) {
            tempPoint.setBy(point1);
            tempPoint.relocateRadial(-0.1 * hexRadius, angle);
            GraphicsYio.drawRectangleRotatedByCenter(
                    batchMovable,
                    gradient3xTexture.getTexture(GraphicsYio.QUALITY_HIGH),
                    tempPoint.x,
                    tempPoint.y,
                    0.2 * outlineRadius,
                    outlineRadius,
                    angle + Math.PI
            );
        }
        point2.setBy(point1);
        point1.relocateRadial(outlineRadius, angle + Math.PI / 2);
        point2.relocateRadial(outlineRadius, angle - Math.PI / 2);
        GraphicsYio.drawLine(
                batchMovable,
                outline3xTexture.getTexture(GraphicsYio.QUALITY_HIGH),
                point1,
                point2,
                4 * GraphicsYio.borderThickness
        );
    }


    private void renderShadows() {
        for (Hex hex : model.hexes) {
            if (hex.counter == -1) continue;
            if (!doesDropShadow(hex)) continue;
            circleYio.setBy(hex.position);
            circleYio.radius += GraphicsYio.borderThickness;
            alignCircleCenterToIntegers();
            circleYio.center.x += 0.1f * circleYio.radius;
            circleYio.center.y -= 0.2f * circleYio.radius;
            GraphicsYio.drawByCircle(
                    batchMovable,
                    shadowHex3xTexture.getTexture(GraphicsYio.QUALITY_HIGH),
                    circleYio
            );
        }
    }


    private void alignCircleCenterToIntegers() {
        circleYio.center.x = (int) circleYio.center.x;
        circleYio.center.y = (int) circleYio.center.y;
    }


    private boolean doesDropShadow(Hex hex) {
        if (directionsManager.getAdjacentHex(hex, 2) == null) return true;
        if (directionsManager.getAdjacentHex(hex, 3) == null) return true;
        return false;
    }


    private void renderHexesAndStaticPieces() {
        for (Hex hex : model.hexes) {
            if (hex.counter == -1) continue;
            renderHex(hex);
            renderStaticPiece(hex);
        }
    }


    private void renderStaticPiece(Hex hex) {
        if (!hex.hasPiece()) return;
        if (hex.counter == 2) return;
        if (Core.isUnit(hex.piece) && gameController.gameMode != GameMode.editor) return;
        circleYio.setRadius(0.7f * getObjectsLayer().getHexRadius());
        circleYio.center.setBy(hex.position.center);
        GraphicsYio.drawByCircle(
                batchMovable,
                getPieceTexture(hex),
                circleYio
        );
    }


    private TextureRegion getPieceTexture(Hex hex) {
        PieceType viewedPieceType = getViewableModel().getViewedPieceType(hex.piece);
        return SkinManager.getInstance().getPiece3xTexture(hex, viewedPieceType).getNormal();
    }


    private void renderHex(Hex hex) {
        circleYio.setBy(hex.position);
        circleYio.radius += GraphicsYio.borderThickness;
        GraphicsYio.drawByCircle(
                batchMovable,
                mapHexTextures.get(hex.color).getTexture(GraphicsYio.QUALITY_HIGH),
                circleYio
        );
    }


    @Override
    protected void disposeTextures() {

    }
}
