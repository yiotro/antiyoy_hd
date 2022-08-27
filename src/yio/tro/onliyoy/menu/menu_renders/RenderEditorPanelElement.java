package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.general.SkinManager;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.editor.EditorPanelElement;
import yio.tro.onliyoy.menu.elements.editor.EpbType;
import yio.tro.onliyoy.menu.elements.editor.EpeButton;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Storage3xTexture;

import java.util.HashMap;

public class RenderEditorPanelElement extends RenderInterfaceElement{


    private TextureRegion sideShadowTexture;
    private TextureRegion backgroundTexture;
    private EditorPanelElement epElement;
    HashMap<EpbType, TextureRegion> mapTextures;


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/editor/panels/background.png", false);
        sideShadowTexture = GraphicsYio.loadTextureRegion("menu/side_shadow.png", false);
        mapTextures = new HashMap<>();
        for (EpbType type : EpbType.values()) {
            mapTextures.put(type, GraphicsYio.loadTextureRegion("menu/editor/panels/" + type + ".png", true));
        }
    }


    @Override
    public void render(InterfaceElement element) {
        epElement = (EditorPanelElement) element;

        renderShadow();
        renderBackground();
        renderButtons();
    }


    private PieceType convertToPieceType(EpbType epbType) {
        switch (epbType) {
            default:
                return null;
            case peasant:
                return PieceType.peasant;
            case spearman:
                return PieceType.spearman;
            case farm:
                return PieceType.farm;
            case pine:
                return PieceType.pine;
            case palm:
                return PieceType.palm;
            case tower:
                return PieceType.tower;
        }
    }


    private void renderButtons() {
        for (EpeButton button : epElement.buttons) {
            PieceType pieceType = convertToPieceType(button.type);
            if (pieceType == null) {
                GraphicsYio.drawByCircle(batch, mapTextures.get(button.type), button.position);
            } else {
                renderPiece(button, pieceType);
            }
            renderSelection(button);
        }
    }


    private void renderPiece(EpeButton button, PieceType pieceType) {
        Storage3xTexture storage3xTexture;
        if (pieceType == PieceType.farm) {
            storage3xTexture = SkinManager.getInstance().getFarm3xTexture(0);
        } else {
            storage3xTexture = SkinManager.getInstance().getPiece3xTexture(pieceType);
        }
        GraphicsYio.drawByCircle(batch, storage3xTexture.getNormal(), button.piecePosition);
    }


    private void renderSelection(EpeButton button) {
        if (!button.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * button.selectionEngineYio.getAlpha());
        GraphicsYio.drawByCircle(batch, blackPixel, button.position);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderShadow() {
        GraphicsYio.setBatchAlpha(batch, epElement.getShadowAlpha());
        GraphicsYio.drawByRectangle(batch, sideShadowTexture, epElement.sideShadowPosition);
    }


    private void renderBackground() {
        GraphicsYio.setBatchAlpha(batch, 1);
        GraphicsYio.drawByRectangle(batch, backgroundTexture, epElement.renderPosition);
    }
}
