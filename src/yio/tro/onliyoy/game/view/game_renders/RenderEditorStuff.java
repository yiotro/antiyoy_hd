package yio.tro.onliyoy.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.editor.EditorManager;
import yio.tro.onliyoy.game.editor.ViewableChange;
import yio.tro.onliyoy.game.general.SkinManager;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

import java.util.HashMap;

public class RenderEditorStuff extends GameRender {


    private EditorManager editorManager;
    private HashMap<HColor, Storage3xTexture> mapHexTextures;
    CircleYio tempHexCircle;
    CircleYio tempPieceCircle;
    private Storage3xTexture hexBackground3xTexture;
    private Storage3xTexture hexWater3xTexture;


    public RenderEditorStuff() {
        tempHexCircle = new CircleYio();
        tempPieceCircle = new CircleYio();
    }


    @Override
    protected void loadTextures() {
        hexBackground3xTexture = load3xTexture("hex_background", true);
        hexWater3xTexture = load3xTexture("hex_water", true);
    }


    @Override
    public void render() {
        updateReferences();
        renderChanges();
    }


    private void updateReferences() {
        editorManager = getObjectsLayer().editorManager;
        GameRendersList instance = GameRendersList.getInstance();
        RenderHexesInTransition renderHexesInTransition = instance.renderHexesInTransition;
        mapHexTextures = renderHexesInTransition.mapHexTextures;
        tempHexCircle.setRadius(1.03 * getViewableModel().getHexRadius());
        tempPieceCircle.setRadius(0.7 * getViewableModel().getHexRadius());
    }


    private void renderChanges() {
        for (ViewableChange viewableChange : editorManager.viewableChanges) {
            if (!viewableChange.isCurrentlyVisible()) continue;
            renderChangingHexColor(viewableChange);
            GraphicsYio.setBatchAlpha(batchMovable, 1);
        }
    }


    private void renderChangingHexColor(ViewableChange viewableChange) {
        float alpha = viewableChange.colorFactor.getValue();
        HColor color = viewableChange.currentColor;
        Storage3xTexture storage3xTexture;
        if (color == null) {
            if (viewableChange.previousColor == null) return;
            storage3xTexture = getBlankHexTexture();
        } else {
            storage3xTexture = mapHexTextures.get(color);
        }
        GraphicsYio.setBatchAlpha(batchMovable, alpha);
        tempHexCircle.center.setBy(viewableChange.hex.position.center);
        GraphicsYio.drawByCircle(
                batchMovable,
                storage3xTexture.getTexture(getHexZoomQuality()),
                tempHexCircle
        );
        if (viewableChange.hex.hasPiece() && storage3xTexture != getBlankHexTexture()) {
            tempPieceCircle.center.setBy(tempHexCircle.center);
            GraphicsYio.drawByCircle(
                    batchMovable,
                    getPieceTexture(viewableChange.hex),
                    tempPieceCircle
            );
        }
    }


    private Storage3xTexture getBlankHexTexture() {
        if (SettingsManager.getInstance().waterTexture) {
            return hexWater3xTexture;
        }
        return hexBackground3xTexture;
    }


    private int getHexZoomQuality() {
        return Math.min(GraphicsYio.QUALITY_HIGH, getCurrentZoomQuality() + 1);
    }


    private TextureRegion getPieceTexture(Hex hex) {
        PieceType viewedPieceType = getViewableModel().getViewedPieceType(hex.piece);
        return SkinManager.getInstance().getPiece3xTexture(hex, viewedPieceType).getTexture(getCurrentZoomQuality());
    }


    @Override
    protected void disposeTextures() {

    }
}
