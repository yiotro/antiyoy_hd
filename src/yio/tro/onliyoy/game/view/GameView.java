package yio.tro.onliyoy.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.view.game_renders.GameRender;
import yio.tro.onliyoy.game.view.game_renders.GameRendersList;
import yio.tro.onliyoy.menu.MenuParams;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;

public class GameView {

    YioGdxGame yioGdxGame;
    public GameController gameController;

    public int w, h;

    public FactorYio appearFactor;
    FrameBuffer frameBuffer;
    public SpriteBatch batchMovable, batchSolid;
    public OrthographicCamera orthoCam;

    TextureRegion transitionFrameTexture;
    public TextureRegion blackPixel;
    public AtlasLoader atlasLoader, roughAtlasLoader;
    double zoomLevelOne, zoomLevelTwo;
    public int currentZoomQuality;
    public RectangleYio transitionFramePosition;
    public RectangleYio maskPosition;
    private RectangleYio screenPosition;
    public TextureRegion darkPixel;
    private ShapeRenderer shapeRenderer;
    private PointYio animationPoint;
    private CornerEngineYio cornerEngineYio;
    private PointYio tempPoint;
    public TextureRegion voidTexture;


    public GameView(YioGdxGame yioGdxGame) { //must be called after creation of GameController and MenuView
        this.yioGdxGame = yioGdxGame;
        gameController = yioGdxGame.gameController;
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        appearFactor = new FactorYio();
        frameBuffer = FrameBufferYio.getInstance(Pixmap.Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Texture texture = frameBuffer.getColorBufferTexture();
        transitionFrameTexture = new TextureRegion(texture);
        transitionFrameTexture.flip(false, true);
        batchMovable = new SpriteBatch();
        batchSolid = yioGdxGame.batch;
        createOrthoCam();
        loadTextures();
        loadAtlas();
        GameRendersList.getInstance().updateGameRenders(this);
        transitionFramePosition = new RectangleYio();
        maskPosition = new RectangleYio();
        screenPosition = new RectangleYio(0, 0, GraphicsYio.width, GraphicsYio.height);
        animationPoint = new PointYio();
        cornerEngineYio = new CornerEngineYio();
        tempPoint = new PointYio();
    }


    private void loadAtlas() {
        atlasLoader = new AtlasLoader("game/atlas/", true);
        roughAtlasLoader = new AtlasLoader("game/rough_atlas/", false);
    }


    public void createOrthoCam() {
        orthoCam = new OrthographicCamera(yioGdxGame.w, yioGdxGame.h);
        orthoCam.position.set(orthoCam.viewportWidth / 2f, orthoCam.viewportHeight / 2f, 0);
        updateCam();
    }


    public void loadTextures() {
        blackPixel = GraphicsYio.loadTextureRegion("pixels/black.png", false);
        darkPixel = GraphicsYio.loadTextureRegion("pixels/dark.png", false);
        if (SettingsManager.getInstance().grayVoid) {
//            voidTexture = GraphicsYio.loadTextureRegion("game/background/gray_void.png", false);
            voidTexture = GraphicsYio.loadTextureRegion("game/background/main.png", false); // yes, it looks bad on PC but it's perfect on android
        } else {
            voidTexture = blackPixel;
        }
    }


    public void updateCam() {
        orthoCam.update();
        batchMovable.setProjectionMatrix(orthoCam.combined);
    }


    public void defaultValues() {
        currentZoomQuality = GraphicsYio.QUALITY_NORMAL;
    }


    public void appear() {
        appearFactor.setValues(0.02, 0);
        appearFactor.appear(MenuParams.ANIM_TYPE, MenuParams.ANIM_SPEED);
        onAppear();
    }


    private void onAppear() {
        updateAnimationPoint();
        updateAnimationTexture();
        prepareScenes();
    }


    private void updateAnimationPoint() {
        animationPoint.setBy(yioGdxGame.menuControllerYio.currentTouchPoint);
    }


    private void prepareScenes() {
        for (InterfaceElement interfaceElement : yioGdxGame.menuControllerYio.getInterfaceElements()) {
            if (!interfaceElement.isVisible()) continue;
            if (interfaceElement.isResistantToAutoDestroy()) continue;
            interfaceElement.destroy();
        }
    }


    public void destroy() {
        if (appearFactor.getValue() == 0) return;
        appearFactor.destroy(MenuParams.ANIM_TYPE, 0.8 * MenuParams.ANIM_SPEED);
        onDestroy();
    }


    private void onDestroy() {
        updateAnimationPoint();
        updateAnimationTexture();
    }


    public void updateAnimationTexture() {
        frameBuffer.begin();
        batchSolid.begin();
        batchSolid.draw(voidTexture, 0, 0, w, h);
        batchSolid.end();
        currentZoomQuality = GraphicsYio.QUALITY_NORMAL; // zoom quality will be updated immediately after
        renderInternals();
        frameBuffer.end();
        updateZoomQuality();
    }


    public void onPause() {
        atlasLoader.dispose();
        roughAtlasLoader.dispose();
        GameRender.disposeAllTextures();
    }


    public void onResume() {
        loadAtlas();
        GameRendersList.getInstance().updateGameRenders(this);
        GameRender.updateAllTextures();
    }


    public void renderInternals() {
        if (checkForDirectRender()) return;

        batchMovable.begin();
        GameRendersList instance = GameRendersList.getInstance();
        instance.renderVmCache.render();
        instance.renderHexesInTransition.render();
        instance.renderStaticPiecesInTransition.render();
        instance.renderProvinceSelection.render();
        instance.renderExclamations.render();
        instance.renderViewableRelations.render();
        instance.renderUmSelector.render();
        instance.renderUnits.render();
        instance.renderPigeons.render();
        instance.renderMoveZone.render();
        instance.renderDefenseIndicators.render();
        instance.renderQuickInfo.render();
        instance.renderEditorStuff.render();
        instance.renderFogOfWar.render();

        renderDyingTouchModes();
        renderCurrentTouchMode();
        renderDebug();
        batchMovable.end();
    }


    private boolean checkForDirectRender() {
        if (!DebugFlags.directRender) return false;
        batchMovable.begin();
        GameRendersList instance = GameRendersList.getInstance();
        instance.renderBackground.render();
        instance.renderModelDirectly.render();
        instance.renderProvinceSelection.render();
        instance.renderViewableRelations.render();
        instance.renderUmSelector.render();
        instance.renderMoveZone.render();
        instance.renderDefenseIndicators.render();
        renderDyingTouchModes();
        renderCurrentTouchMode();
        renderDebug();
        batchMovable.end();
        return true;
    }


    private void renderDyingTouchModes() {
        for (TouchMode dyingTm : gameController.dyingTms) {
            GameRender render = dyingTm.getRender();
            if (render == null) return;

            render.render();
        }
    }


    private void renderCurrentTouchMode() {
        GameRender render = gameController.touchMode.getRender();
        if (render == null) return;

        render.render();
    }


    private void renderDebug() {
        if (!DebugFlags.debugEnabled) return;

        GameRendersList instance = GameRendersList.getInstance();
        instance.renderPosMap.render();
    }


    public void render() {
        if (appearFactor.getValue() < 0.01) return;

        if (appearFactor.getValue() < 1) {
            renderTransitionFrame();
            return;
        }

        checkForSolidBlackBackground();
        renderInternals();
    }


    private void checkForSolidBlackBackground() {
        if (!gameController.backgroundVisible) return;

        batchSolid.begin();
        batchSolid.draw(voidTexture, 0, 0, w, h);
        batchSolid.end();
    }


    void renderTransitionFrame() {
        updateTransitionFramePosition();

        Masking.begin();
        shapeRenderer = yioGdxGame.menuViewYio.shapeRenderer;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(yioGdxGame.menuViewYio.orthoCam.combined);
        GraphicsYio.drawRoundRectWithShape(shapeRenderer, maskPosition, cornerEngineYio.getCurrentRadius());
        shapeRenderer.end();

        batchSolid.begin();
        Masking.continueAfterBatchBegin();

        Color c = batchSolid.getColor();
        float a = c.a;
        if (appearFactor.isInAppearState()) {
            batchSolid.setColor(c.r, c.g, c.b, Math.min(appearFactor.getValue() * 2, 1));
            GraphicsYio.drawByRectangle(batchSolid, darkPixel, screenPosition);
            batchSolid.setColor(c.r, c.g, c.b, a);
            GraphicsYio.drawByRectangle(batchSolid, transitionFrameTexture, transitionFramePosition);
        } else {
            batchSolid.setColor(c.r, c.g, c.b, Math.max(0, 1 - 1.25f * (1 - appearFactor.getValue())));
            GraphicsYio.drawByRectangle(batchSolid, transitionFrameTexture, transitionFramePosition);
        }
        batchSolid.setColor(c.r, c.g, c.b, c.a);

        Masking.end(batchSolid);
        batchSolid.end();
    }


    private void updateTransitionFramePosition() {
        float value = appearFactor.getValue();

        if (appearFactor.isInAppearState()) {
            tempPoint.setBy(animationPoint);
            tempPoint.x += 0.2f * value * (0.5f * GraphicsYio.width - tempPoint.x);
            tempPoint.y += 0.2f * value * (0.5f * GraphicsYio.height - tempPoint.y);
        } else {
            tempPoint.set(GraphicsYio.width / 2, GraphicsYio.height / 2);
        }

        maskPosition.set(
                (1 - value) * tempPoint.x,
                (1 - value) * tempPoint.y,
                value * GraphicsYio.width,
                value * GraphicsYio.height
        );
        checkToMakeMaskPositionMoreRectangular();
        cornerEngineYio.move(maskPosition, appearFactor, 0);

        value = Math.min(1, 0.4f + 0.605f * value);
        transitionFramePosition.set(
                (1 - value) * 0.5f * GraphicsYio.width,
                (1 - value) * 0.26f * GraphicsYio.height,
                value * GraphicsYio.width,
                value * GraphicsYio.height
        );
    }


    private void checkToMakeMaskPositionMoreRectangular() {
        float value = appearFactor.getValue();
        if (value > 0.5) return;
        float cx = maskPosition.x + maskPosition.width / 2;
        float cy = maskPosition.y + maskPosition.height / 2;
        float difference = maskPosition.height - maskPosition.width;
        float delta = (1 - 2 * value) * 0.5f * difference;
        float w = maskPosition.width + delta;
        float h = maskPosition.height - delta;
        maskPosition.set(cx - w / 2, cy - h / 2, w, h);
    }


    public void move() {
        appearFactor.move();
    }


    public boolean coversAllScreen() {
        return appearFactor.getValue() == 1;
    }


    public boolean isInMotion() {
        return appearFactor.getValue() > 0 && appearFactor.getValue() < 1;
    }


    public void updateZoomQuality() {
        if (gameController.getViewZoomLevel() < zoomLevelOne) {
            currentZoomQuality = GraphicsYio.QUALITY_HIGH;
        } else if (gameController.getViewZoomLevel() < zoomLevelTwo) {
            currentZoomQuality = GraphicsYio.QUALITY_NORMAL;
        } else {
            currentZoomQuality = GraphicsYio.QUALITY_LOW;
        }
    }


    public void setZoomLevels(double zoomValues[][], LevelSize levelsSize) {
        int lsIndex = levelsSize.ordinal();

        if (lsIndex >= zoomValues.length) {
            lsIndex = zoomValues.length - 1;
        }

        zoomLevelOne = zoomValues[lsIndex][0];
        zoomLevelTwo = zoomValues[lsIndex][1];
    }


    public void onBasicStuffCreated() {
        defaultValues();
        setZoomLevels(
                gameController.cameraController.getZoomValues(),
                gameController.sizeManager.initialLevelSize
        );
        appear();
    }


}
