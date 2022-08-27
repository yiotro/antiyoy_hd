package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.forefinger.ForefingerElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;

public class RenderForefinger extends RenderInterfaceElement{


    private TextureRegion main;
    private ForefingerElement forefinger;
    private RectangleYio viewPosition;
    private TextureRegion selection;
    private TextureRegion debugPixel;
    private TextureRegion effect;
    private float ef;
    private TextureRegion blackout;


    @Override
    public void loadTextures() {
        main = GraphicsYio.loadTextureRegion("game/forefinger/forefinger.png", true);
        selection = GraphicsYio.loadTextureRegion("game/forefinger/selection.png", true);
        debugPixel = GraphicsYio.loadTextureRegion("pixels/red.png", false);
        effect = GraphicsYio.loadTextureRegion("game/forefinger/effect.png", false);
        blackout = GraphicsYio.loadTextureRegion("game/forefinger/forefinger_blackout.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        forefinger = (ForefingerElement) element;
        viewPosition = forefinger.getViewPosition();

        renderEffect();
        renderMain();
        renderSelection();
        renderBlackout();

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderBlackout() {
        if (forefinger.blackoutFactor.getValue() == 0) return;

        GraphicsYio.setBatchAlpha(batch, 0.4f * forefinger.blackoutFactor.getValue());

        GraphicsYio.drawByRectangle(
                batch,
                blackout,
                forefinger.blackoutPosition
        );

        for (RectangleYio blackoutBorder : forefinger.blackoutBorders) {
            GraphicsYio.drawByRectangle(
                    batch,
                    blackPixel,
                    blackoutBorder
            );
        }
    }


    private void renderEffect() {
        ef = forefinger.effectFactor.getValue();
        if (ef == 0) return;

        GraphicsYio.setBatchAlpha(batch, 0.45f * (1 - ef));

        GraphicsYio.drawFromCenter(
                batch,
                effect,
                forefinger.hook.x,
                forefinger.hook.y,
                forefinger.effectRadius
        );
    }


    private void renderSelection() {
        if (!forefinger.isSelected()) return;

        GraphicsYio.setBatchAlpha(batch, forefinger.selectionFactor.getValue());

        GraphicsYio.drawFromCenterRotated(
                batch,
                selection,
                forefinger.viewPoint.x,
                forefinger.viewPoint.y,
                forefinger.viewRadius,
                forefinger.viewAngle - Math.PI / 2
        );

        GraphicsYio.setBatchAlpha(batch, alpha);
    }


    private void renderHook() {
        GraphicsYio.setBatchAlpha(batch, 0.4 * alpha);

        GraphicsYio.drawFromCenter(
                batch,
                debugPixel,
                forefinger.hook.x,
                forefinger.hook.y,
                0.005f * GraphicsYio.width
        );

        GraphicsYio.setBatchAlpha(batch, alpha);
    }


    private void renderMain() {
        GraphicsYio.setBatchAlpha(batch, alpha);

        GraphicsYio.drawFromCenterRotated(
                batch,
                main,
                forefinger.viewPoint.x,
                forefinger.viewPoint.y,
                forefinger.viewRadius,
                forefinger.viewAngle - Math.PI / 2
        );
    }


}
