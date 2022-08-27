package yio.tro.onliyoy.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.onliyoy.*;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.AtlasLoader;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class MenuViewYio {

    public YioGdxGame yioGdxGame;
    MenuControllerYio menuControllerYio;
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public OrthographicCamera orthoCam;
    public int w, h;
    public AtlasLoader menuAtlas;


    public MenuViewYio(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        menuControllerYio = yioGdxGame.menuControllerYio;
        shapeRenderer = new ShapeRenderer();
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        createOrthoCam();
        loadAtlas();
        update();
    }


    private void loadAtlas() {
        menuAtlas = new AtlasLoader("menu/gameplay/", true);
    }


    private void createOrthoCam() {
        orthoCam = new OrthographicCamera(yioGdxGame.w, yioGdxGame.h);
        orthoCam.position.set(orthoCam.viewportWidth / 2f, orthoCam.viewportHeight / 2f, 0);
        orthoCam.update();
    }


    public void update() {
        batch = yioGdxGame.batch;
        MenuRenders.updateRenderSystems(this);
    }


    public void applyRender(boolean onTopOfGameView) {
        batch.begin();

        render(onTopOfGameView, true);
        render(onTopOfGameView, false);

        renderDebug();

        GraphicsYio.setBatchAlpha(batch, 1);
        batch.end();
    }


    private void render(boolean onTopOfGameView, boolean dyingStatus) {
        for (InterfaceElement element : menuControllerYio.visibleElements) {
            if (!element.isVisible()) continue;
            if (!element.compareGvStatus(onTopOfGameView)) continue;
            if (element.getDyingStatus() != dyingStatus) continue;
            if (element.isViewPositionNotUpdatedYet()) continue;
            renderSingleElement(element);
        }
    }


    private void renderSingleElement(InterfaceElement element) {
        RenderInterfaceElement renderSystem = element.getRenderSystem();
        if (element.isAlphaEnabled()) {
            renderSystem.setAlpha(element.getAlpha() * element.getActivationAlpha());
        }
        renderSystem.render(element);
        if (element.isAlphaEnabled()) {
            renderSystem.setAlpha(1);
        }
    }


    private void renderDebug() {

    }
}
