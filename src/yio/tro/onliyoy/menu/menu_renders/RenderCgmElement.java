package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.choose_game_mode.CgmElement;
import yio.tro.onliyoy.menu.elements.choose_game_mode.CgmGroup;
import yio.tro.onliyoy.menu.elements.choose_game_mode.CgmSubButton;
import yio.tro.onliyoy.menu.elements.multi_button.MbLocalButton;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Masking;

import java.util.HashMap;

public class RenderCgmElement extends RenderInterfaceElement{

    private CgmElement cgmElement;
    public HashMap<BackgroundYio, TextureRegion> mapBackgrounds;


    @Override
    public void loadTextures() {
        mapBackgrounds = new HashMap<>();
        for (BackgroundYio backgroundYio : BackgroundYio.values()) {
            mapBackgrounds.put(backgroundYio, loadBackgroundTexture(backgroundYio));
        }
    }


    private TextureRegion loadBackgroundTexture(BackgroundYio backgroundYio) {
        return GraphicsYio.loadTextureRegion("menu/multi_button/" + backgroundYio + ".png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        cgmElement = (CgmElement) element;

        renderShadow();

        batch.end();
        Masking.begin();

        prepareShapeRenderer();
        drawRoundRectShape(cgmElement.dynamicPosition, cgmElement.cornerEngineYio.getCurrentRadius());
        shapeRenderer.end();

        batch.begin();
        Masking.continueAfterBatchBegin();
        renderInternals();
        Masking.end(batch);

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderInternals() {
        for (CgmGroup group : cgmElement.groups) {
            renderDefaultGroupBackground(group);
            renderGroupTitle(group);
            renderGroupSelection(group);
            renderExpandedGroupBackground(group);
            renderSubButtons(group);
        }
    }


    private void renderSubButtons(CgmGroup group) {
        if (group.expansionFactor.getValue() == 0) return;
        for (CgmSubButton subButton : group.subButtons) {
            float sbAlpha = alpha * group.expansionFactor.getValue() * subButton.alphaFactor.getValue();
            GraphicsYio.setBatchAlpha(batch, sbAlpha);
            MenuRenders.renderRoundShape.renderRoundShape(
                    subButton.viewPosition,
                    BackgroundYio.gray
            );
            GraphicsYio.setFontAlpha(subButton.title.font, sbAlpha);
            GraphicsYio.renderText(batch, subButton.title);
            GraphicsYio.setFontAlpha(subButton.title.font, 1);
            renderSubButtonSelection(subButton, sbAlpha);
        }
    }


    private void renderSubButtonSelection(CgmSubButton subButton, float sbAlpha) {
        if (!subButton.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, sbAlpha * subButton.selectionEngineYio.getAlpha());
        MenuRenders.renderRoundShape.renderRoundShape(
                subButton.viewPosition,
                BackgroundYio.black
        );
    }


    private void renderGroupSelection(CgmGroup group) {
        if (!group.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, alpha * group.selectionEngineYio.getAlpha());
        GraphicsYio.drawByRectangle(batch, blackPixel, group.viewPosition);
    }


    private void renderGroupTitle(CgmGroup group) {
        if (group.expansionFactor.getValue() == 1) return;
        GraphicsYio.renderTextOptimized(
                batch,
                blackPixel,
                group.title,
                alpha * (1 - group.expansionFactor.getValue())
        );
    }


    private void renderExpandedGroupBackground(CgmGroup group) {
        if (!(group.expansionFactor.getValue() > 0)) return;
        GraphicsYio.setBatchAlpha(batch, group.expansionFactor.getValue() * alpha);
        GraphicsYio.drawByRectangle(
                batch,
                mapBackgrounds.get(BackgroundYio.white),
                group.viewPosition
        );
    }


    private void renderDefaultGroupBackground(CgmGroup group) {
        if (!(group.expansionFactor.getValue() < 1)) return;
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(
                batch,
                mapBackgrounds.get(group.backgroundYio),
                group.viewPosition
        );
    }


    private void renderShadow() {
        GraphicsYio.setBatchAlpha(batch, cgmElement.getShadowAlpha() * cgmElement.getActivationAlpha());
        MenuRenders.renderShadow.renderShadow(
                cgmElement.dynamicPosition,
                cgmElement.cornerEngineYio.getShadowRadius()
        );
    }
}
