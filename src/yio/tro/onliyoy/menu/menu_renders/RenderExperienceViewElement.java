package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.experience.EvePanel;
import yio.tro.onliyoy.menu.elements.experience.EveStrip;
import yio.tro.onliyoy.menu.elements.experience.ExperienceViewElement;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderExperienceViewElement extends RenderInterfaceElement{


    private ExperienceViewElement evElement;
    private TextureRegion greenPixel;
    private TextureRegion fishTexture;


    @Override
    public void loadTextures() {
        greenPixel = GraphicsYio.loadTextureRegion("pixels/green.png", false);
        fishTexture = GraphicsYio.loadTextureRegion("menu/shop/fish.png", true);
    }


    @Override
    public void render(InterfaceElement element) {
        evElement = (ExperienceViewElement) element;

        renderPanel(evElement.panel0);
        renderPanel(evElement.panel1);
    }


    void renderPanel(EvePanel panel) {
        if (!panel.isCurrentlyVisible()) return;
        GraphicsYio.renderText(batch, panel.levelTitle);
        renderStrip(panel.strip);
        renderFish(panel);
    }


    private void renderFish(EvePanel panel) {
        if (!panel.hasToDisplayFish()) return;
        GraphicsYio.drawByCircle(batch, fishTexture, panel.fishPosition);
    }


    void renderStrip(EveStrip strip) {
        GraphicsYio.setBatchAlpha(batch, 0.1);
        GraphicsYio.drawByRectangle(batch, blackPixel, strip.position);
        GraphicsYio.setBatchAlpha(batch, 0.75);
        GraphicsYio.drawByRectangle(batch, blackPixel, strip.solidPartPosition);
        GraphicsYio.drawByRectangle(batch, greenPixel, strip.deltaPartPosition);
        GraphicsYio.setBatchAlpha(batch, 0.75 * evElement.fadeFactor.getValue());
        GraphicsYio.drawByRectangle(batch, blackPixel, strip.deltaPartPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }
}
