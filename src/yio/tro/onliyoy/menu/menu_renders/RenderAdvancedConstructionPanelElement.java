package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.PieceType;
import yio.tro.onliyoy.game.general.SkinManager;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.AcpButton;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.AdvancedConstructionPanelElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.CveIndicator;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderAdvancedConstructionPanelElement extends RenderInterfaceElement{


    private TextureRegion sideShadowTexture;
    private TextureRegion backgroundTexture;
    private AdvancedConstructionPanelElement acpElement;
    private TextureRegion whitePixel;
    private TextureRegion redPixel;


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/construction_panel/background.png", false);
        sideShadowTexture = GraphicsYio.loadTextureRegion("menu/side_shadow.png", false);
        whitePixel = GraphicsYio.loadTextureRegion("pixels/white.png", false);
        redPixel = GraphicsYio.loadTextureRegion("pixels/red.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        acpElement = (AdvancedConstructionPanelElement) element;

        renderShadow();
        renderIndicators();
        renderBackground();
        renderButtons();
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderIndicators() {
        for (CveIndicator indicator : acpElement.indicators) {
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


    private void renderPrice(CveIndicator indicator) {
        if (!indicator.isPriceVisible()) return;
        if (indicator.canAfford) {
            renderWhiteText(indicator.priceViewText, whitePixel, 1);
            return;
        }
        renderRedText(indicator.priceViewText, redPixel, 1);
    }


    private void renderButtons() {
        for (AcpButton button : acpElement.buttons) {
            GraphicsYio.setBatchAlpha(batch, alpha);
            GraphicsYio.drawByCircle(
                    batch,
                    getPieceTexture(button.pieceType),
                    button.viewPosition
            );
            renderSelection(button);
        }
    }


    private void renderSelection(AcpButton button) {
        if (!button.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, button.selectionEngineYio.getAlpha() * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, button.touchPosition);
    }


    private void renderBackground() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(batch, backgroundTexture, acpElement.getViewPosition());
    }


    private void renderShadow() {
        if (alpha < 0.98) return;
        GraphicsYio.setBatchAlpha(batch, acpElement.getShadowAlpha() * alpha);
        GraphicsYio.drawByRectangle(batch, sideShadowTexture, acpElement.sideShadowPosition);
    }


    private TextureRegion getPieceTexture(PieceType pieceType) {
        if (pieceType == PieceType.farm) {
            return SkinManager.getInstance().getFarm3xTexture(0).getNormal();
        }
        return SkinManager.getInstance().getPiece3xTexture(pieceType).getNormal();
    }
}
