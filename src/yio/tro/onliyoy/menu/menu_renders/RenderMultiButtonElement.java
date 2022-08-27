package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.multi_button.MbLocalButton;
import yio.tro.onliyoy.menu.elements.multi_button.MultiButtonElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Masking;

import java.util.HashMap;

public class RenderMultiButtonElement extends RenderInterfaceElement{


    private MultiButtonElement mbElement;
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
        mbElement = (MultiButtonElement) element;

        renderShadow();

        batch.end();
        Masking.begin();

        prepareShapeRenderer();
        drawRoundRectShape(mbElement.getViewPosition(), mbElement.cornerEngineYio.getCurrentRadius());
        shapeRenderer.end();

        batch.begin();
        Masking.continueAfterBatchBegin();
        renderInternals();
        Masking.end(batch);

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderInternals() {
        for (MbLocalButton localButton : mbElement.localButtons) {
            renderSingleLocalButton(localButton);
        }
    }


    private void renderSingleLocalButton(MbLocalButton localButton) {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(
                batch,
                mapBackgrounds.get(localButton.backgroundYio),
                localButton.viewPosition
        );
        GraphicsYio.renderTextOptimized(batch, blackPixel, localButton.title, alpha);
        if (localButton.selectionEngineYio.isSelected()) {
            GraphicsYio.setBatchAlpha(batch, alpha * localButton.selectionEngineYio.getAlpha());
            GraphicsYio.drawByRectangle(batch, blackPixel, localButton.viewPosition);
        }
    }


    private void renderShadow() {
        GraphicsYio.setBatchAlpha(batch, mbElement.getShadowAlpha());
        MenuRenders.renderShadow.renderShadow(
                mbElement.getViewPosition(),
                mbElement.cornerEngineYio.getShadowRadius()
        );
    }
}
