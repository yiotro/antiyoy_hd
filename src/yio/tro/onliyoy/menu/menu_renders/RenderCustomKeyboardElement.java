package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.keyboard.*;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

import java.util.HashMap;

public class RenderCustomKeyboardElement extends RenderInterfaceElement{


    private TextureRegion backgroundTexture;
    private CustomKeyboardElement cbElement;
    private TextureRegion sideShadowTexture;
    HashMap<CbType, TextureRegion> mapIcons;
    private TextureRegion selectionTexture;
    private RectangleYio tempRectangle;


    public RenderCustomKeyboardElement() {
        tempRectangle = new RectangleYio();
    }


    @Override
    public void loadTextures() {
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/round_shape/white.png", false);
        sideShadowTexture = GraphicsYio.loadTextureRegion("menu/side_shadow.png", false);
        selectionTexture = GraphicsYio.loadTextureRegion("menu/keyboard/kb_selection.png", true);
        mapIcons = new HashMap<>();
        for (CbType cbType : CbType.values()) {
            if (cbType == CbType.normal) continue;
            mapIcons.put(cbType, loadIconTexture(cbType));
        }
    }


    private TextureRegion loadIconTexture(CbType cbType) {
        return GraphicsYio.loadTextureRegion("menu/keyboard/" + cbType + "_icon.png", true);
    }


    @Override
    public void render(InterfaceElement element) {
        cbElement = (CustomKeyboardElement) element;

        renderBlackout();
        renderShadow();
        renderBottomEmptyZone();
        renderPages();
        renderDarkenOnSwipe();
        renderButtons();
        renderTextField();
    }


    private void renderBlackout() {
        if (!cbElement.blackoutEnabled) return;
        GraphicsYio.setBatchAlpha(batch, 0.33f * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, cbElement.blackoutPosition);
    }


    private void renderPages() {
        for (CbPage cbPage : cbElement.pages) {
            if (cbPage.cacheTexture == null) continue;
            GraphicsYio.drawByRectangleUpsideDown(batch, cbPage.cacheTexture, cbPage.position);
        }
    }


    private void renderDarkenOnSwipe() {
        if (cbElement.simpleTabsEngineYio.swipeFactor.getValue() == 0) return;
        if (cbElement.pages.size() < 2) return;
        GraphicsYio.setBatchAlpha(batch, 0.33 * cbElement.simpleTabsEngineYio.swipeFactor.getValue());
        int otherTabIndex = cbElement.getOtherTabIndex(cbElement.simpleTabsEngineYio.getCurrentTabIndex());
        CbPage cbPage = cbElement.pages.get(otherTabIndex);
        GraphicsYio.drawByRectangle(batch, blackPixel, cbPage.position);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderTextField() {
        if (cbElement.getFactor().getValue() < 0.01) return;
        CbTextField textField = cbElement.textField;
        GraphicsYio.setBatchAlpha(batch, cbElement.getShadowAlpha());
        MenuRenders.renderShadow.renderShadow(textField.viewPosition, textField.cornerEngineYio.getShadowRadius());
        GraphicsYio.setBatchAlpha(batch, alpha);
        MenuRenders.renderRoundShape.renderRoundShape(textField.viewPosition, BackgroundYio.white, textField.cornerEngineYio.getCurrentRadius());
        GraphicsYio.setBatchAlpha(batch, 1);
        renderTextFieldInternals();
    }


    private void renderTextFieldInternals() {
        if (cbElement.getFactor().getValue() < 0.5) return;
        renderHint();
        CbTextField textField = cbElement.textField;
        RenderableTextYio title = textField.title;
        if (title.bounds.x + title.bounds.width > textField.viewPosition.x + textField.viewPosition.width) return;
        GraphicsYio.renderTextOptimized(batch, blackPixel, title, 1);
        GraphicsYio.setBatchAlpha(batch, alpha * textField.cursorFactor.getValue());
        GraphicsYio.drawByRectangle(batch, blackPixel, textField.cursorPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
        renderHighlight(textField);
    }


    private void renderHint() {
        CbTextField textField = cbElement.textField;
        float hintValue = textField.hintFactor.getValue();
        if (hintValue == 0) return;
        RenderableTextYio hintViewText = textField.hintViewText;
        if (hintViewText.bounds.x + hintViewText.bounds.width > textField.viewPosition.x + textField.viewPosition.width) return;
        GraphicsYio.renderTextOptimized(batch, blackPixel, hintViewText, 0.4f * hintValue * alpha);
    }


    private void renderHighlight(CbTextField textField) {
        if (!textField.isHighlighted()) return;
        GraphicsYio.setBatchAlpha(batch, 0.15 * textField.highlightFactor.getValue());
        GraphicsYio.drawByRectangle(batch, blackPixel, textField.highlightPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderButtons() {
        for (CbPage page : cbElement.pages) {
            for (CbButton button : page.buttons) {
                if (!button.isCurrentlyVisible()) continue;
                renderSelection(button);
            }
        }
    }


    private void renderSelection(CbButton button) {
        if (!button.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, button.selectionEngineYio.getAlpha());
        if (button.type == CbType.space) {
            tempRectangle.setBy(button.position);
            tempRectangle.increase(0.014f * GraphicsYio.width);
            MenuRenders.renderRoundShape.renderRoundShape(tempRectangle, BackgroundYio.black);
        } else {
            GraphicsYio.drawByCircle(batch, selectionTexture, button.selectionPosition);
        }
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderBottomEmptyZone() {
        tempRectangle.setBy(cbElement.bottomEmptyZone);
        tempRectangle.height += 0.01f * GraphicsYio.height;
        GraphicsYio.drawByRectangle(batch, backgroundTexture, tempRectangle);
    }


    private void renderShadow() {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(batch, sideShadowTexture, cbElement.sideShadowPosition);
    }
}
