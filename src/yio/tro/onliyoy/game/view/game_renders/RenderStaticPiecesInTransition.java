package yio.tro.onliyoy.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.general.SkinManager;
import yio.tro.onliyoy.game.viewable_model.VhSpawnType;
import yio.tro.onliyoy.game.viewable_model.ViewableHex;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

import java.util.HashMap;

public class RenderStaticPiecesInTransition extends GameRender{

    CircleYio circleYio;


    public RenderStaticPiecesInTransition() {
        circleYio = new CircleYio();
    }


    @Override
    protected void loadTextures() {

    }


    @Override
    public void render() {
        for (ViewableHex viewableHex : getViewableModel().landsManager.currentlyAnimatedHexes) {
            if (!viewableHex.isCurrentlyVisible()) continue;
            if (!shouldBeShown(viewableHex)) continue;
            renderPiece(viewableHex);
        }
        GraphicsYio.setBatchAlpha(batchMovable, 1);
    }


    public static boolean shouldBeShown(ViewableHex viewableHex) {
        PieceType pieceType = viewableHex.pieceType;
        if (pieceType == null) return false;
        if (Core.isUnit(pieceType)) return false;
        if (viewableHex.inTransition) return true;
        if (viewableHex.isPieceFactorInMovementMode()) return true;
        if (viewableHex.isColorFactorInMovementMode()) return true; // animated hex covers cache, so piece has to be also rendered
        return false;
    }


    private void renderPiece(ViewableHex viewableHex) {
        updateCircleForPiece(viewableHex);
        GraphicsYio.setBatchAlpha(batchMovable, viewableHex.pieceFactor.getValue());
        GraphicsYio.drawByCircle(
                batchMovable,
                getPieceTexture(viewableHex),
                circleYio
        );
    }


    private TextureRegion getPieceTexture(ViewableHex viewableHex) {
        PieceType viewedPieceType = getViewableModel().getViewedPieceType(viewableHex.pieceType);
        Storage3xTexture piece3xTexture = SkinManager.getInstance().getPiece3xTexture(viewableHex.hex, viewedPieceType);
        return piece3xTexture.getTexture(getCurrentZoomQuality());
    }


    private void updateCircleForPiece(ViewableHex viewableHex) {
        circleYio.center.setBy(viewableHex.hex.position.center);
        VhSpawnType pieceSpawnType = viewableHex.pieceSpawnType;
        switch (pieceSpawnType) {
            default:
            case normal:
                circleYio.setRadius(0.7f * viewableHex.pieceFactor.getValue() * getObjectsLayer().getHexRadius());
                break;
            case constructed:
                circleYio.setRadius(0.7f * getObjectsLayer().getHexRadius());
                circleYio.center.y -= (1 - viewableHex.pieceFactor.getValue()) * circleYio.radius;
                break;
        }
    }


    @Override
    protected void disposeTextures() {

    }
}
