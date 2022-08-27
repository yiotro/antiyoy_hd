package yio.tro.onliyoy.menu.menu_renders.render_custom_list;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.elements.customizable_list.AbstractCustomListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.MlEntityItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderMlEntityItem extends AbstractRenderCustomListItem {


    private MlEntityItem mlEntityItem;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractCustomListItem item) {
        mlEntityItem = (MlEntityItem) item;
        renderBackground();
        renderIconText();
        renderMessage();
        renderSelection();
    }


    private void renderMessage() {
        float messageFactorValue = mlEntityItem.messageFactor.getValue();
        if (messageFactorValue == 0) return;
        float fadeInFactorValue = mlEntityItem.fadeInFactor.getValue();
        float a = this.alpha * fadeInFactorValue * messageFactorValue;
        GraphicsYio.setBatchAlpha(batch, a);
        GraphicsYio.drawByRectangle(
                batch,
                getMessageBackgroundTexture(),
                mlEntityItem.messageBounds
        );
        GraphicsYio.renderTextOptimized(
                batch,
                blackPixel,
                mlEntityItem.messageViewText,
                a
        );
    }


    private TextureRegion getMessageBackgroundTexture() {
        if (mlEntityItem.color == null) {
            return MenuRenders.renderUiColors.map.get(HColor.gray);
        }
        return MenuRenders.renderUiColors.map.get(mlEntityItem.color);
    }


    private void renderSelection() {
        if (!mlEntityItem.selectionEngineYio.isSelected()) return;
        GraphicsYio.setBatchAlpha(batch, mlEntityItem.selectionEngineYio.getAlpha() * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, mlEntityItem.selectionPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderBackground() {
        if (mlEntityItem.color == null && mlEntityItem.previousColor == null) return;
        float f = mlEntityItem.colorFactor.getValue();
        if (f == 1) {
            renderColoredBackground(mlEntityItem.color, 1);
            return;
        }
        renderColoredBackground(mlEntityItem.previousColor, 1 - f);
        renderColoredBackground(mlEntityItem.color, f);
    }


    private void renderColoredBackground(HColor color, double f) {
        if (color == null) return;
        GraphicsYio.setBatchAlpha(batch, getNicknameAlpha() * f);
        GraphicsYio.drawByRectangle(
                batch,
                MenuRenders.renderUiColors.map.get(color),
                mlEntityItem.incBounds
        );
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderIconText() {
        GraphicsYio.renderItyOptimized(
                batch,
                blackPixel,
                MenuRenders.renderAvatars.getTexture(mlEntityItem.avatarType, mlEntityItem.iconTextYio),
                mlEntityItem.iconTextYio,
                getNicknameAlpha()
        );
    }


    private float getNicknameAlpha() {
        return alpha * mlEntityItem.fadeInFactor.getValue() * (1 - mlEntityItem.messageFactor.getValue());
    }

}
