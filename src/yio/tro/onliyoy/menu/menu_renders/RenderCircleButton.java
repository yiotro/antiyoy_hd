package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.CircleButtonYio;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderCircleButton extends RenderInterfaceElement{

    private CircleButtonYio circleButton;
    private TextureRegion selectEffectTexture;


    @Override
    public void loadTextures() {
        selectEffectTexture = GraphicsYio.loadTextureRegion("menu/selection.png", true);
    }


    @Override
    public void render(InterfaceElement element) {
        circleButton = (CircleButtonYio) element;

        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByCircle(batch, circleButton.textureRegion, circleButton.renderPosition);
        renderSelection();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderSelection() {
        if (!circleButton.isSelected()) return;
        if (circleButton.getFactor().getValue() < 0.01) return;

        GraphicsYio.setBatchAlpha(batch, alpha * circleButton.selectionFactor.getValue());
        GraphicsYio.drawByCircle(batch, selectEffectTexture, circleButton.effectPosition);
    }

}
