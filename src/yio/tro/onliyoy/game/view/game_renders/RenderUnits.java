package yio.tro.onliyoy.game.view.game_renders;

import yio.tro.onliyoy.game.core_model.Core;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.SkinManager;
import yio.tro.onliyoy.game.viewable_model.ViewableUnit;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

import java.util.HashMap;

public class RenderUnits extends GameRender{


    @Override
    protected void loadTextures() {

    }


    @Override
    public void render() {
        if (gameController.gameMode == GameMode.editor) return;
        for (ViewableUnit unit : getViewableModel().unitsManager.units) {
            if (!unit.isCurrentlyVisible()) continue;
            if (unit.isCurrentlyTransparent()) {
                GraphicsYio.setBatchAlpha(batchMovable, unit.appearFactor.getValue());
            }
            Storage3xTexture piece3xTexture = SkinManager.getInstance().getPiece3xTexture(unit.pieceType);
            GraphicsYio.drawByCircle(
                    batchMovable,
                    piece3xTexture.getTexture(getCurrentZoomQuality()),
                    unit.viewPosition
            );
            if (unit.isCurrentlyTransparent()) {
                GraphicsYio.setBatchAlpha(batchMovable, 1);
            }
        }
    }


    @Override
    protected void disposeTextures() {

    }
}
