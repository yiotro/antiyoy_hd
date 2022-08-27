package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.general.SkinManager;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.ConstructionViewElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.CveButton;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.CveIndicator;
import yio.tro.onliyoy.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderConstructionViewElement extends RenderInterfaceElement{


    private TextureRegion backgroundTexture;
    private ConstructionViewElement cvElement;
    private TextureRegion selectionTexture;
    private TextureRegion whitePixel;
    private TextureRegion redPixel;


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/province_ui/construction_background.png", false);
        selectionTexture = GraphicsYio.loadTextureRegion("menu/selection.png", true);
        whitePixel = GraphicsYio.loadTextureRegion("pixels/white.png", false);
        redPixel = GraphicsYio.loadTextureRegion("pixels/red.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        cvElement = (ConstructionViewElement) element;
        renderBackground();
        renderButtons();
        renderIndicators();
    }


    private void renderIndicators() {
        for (CveIndicator indicator : cvElement.indicators) {
            if (!indicator.alive) continue;
            if (indicator.isAlphaEnabled()) {
                GraphicsYio.setBatchAlpha(batch, indicator.appearFactor.getValue());
            }
            GraphicsYio.drawByCircle(
                    batch,
                    getPieceTexture(indicator.pieceType),
                    indicator.viewPosition
            );
            if (indicator.isAlphaEnabled()) {
                GraphicsYio.setBatchAlpha(batch, 1);
            }
            renderPrice(indicator);
        }
    }


    private TextureRegion getPieceTexture(PieceType pieceType) {
        if (pieceType == PieceType.farm) {
            return SkinManager.getInstance().getFarm3xTexture(0).getNormal();
        }
        return SkinManager.getInstance().getPiece3xTexture(pieceType).getNormal();
    }


    private void renderPrice(CveIndicator indicator) {
        if (!indicator.isPriceVisible()) return;
        if (indicator.canAfford) {
            renderWhiteText(indicator.priceViewText, whitePixel, 1);
            return;
        }
        renderRedText(indicator.priceViewText, redPixel, 1);
    }


    private void renderButtons() {
        for (CveButton button : cvElement.buttons) {
            TextureRegion textureRegion = getPieceTexture(button.getCurrentViewPieceType());
            if (textureRegion == null) continue;
            GraphicsYio.drawByCircle(
                    batch,
                    textureRegion,
                    button.viewPosition
            );
            renderButtonSelection(button);
        }
    }


    private void renderButtonSelection(CveButton button) {
        if (!button.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, cvElement.getAlpha() * button.selectionEngineYio.getAlpha());
        GraphicsYio.drawByCircle(batch, selectionTexture, button.touchPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderBackground() {
        GraphicsYio.drawByRectangle(batch, backgroundTexture, cvElement.getViewPosition());
    }
}
