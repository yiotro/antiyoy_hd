package yio.tro.onliyoy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import yio.tro.onliyoy.menu.MenuParams;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;
import java.util.HashMap;

public class GeneralBackgroundManager {

    YioGdxGame yioGdxGame;
    BackgroundYio currentValue;
    BackgroundYio previousValue;
    FactorYio transitionFactor;
    private SpriteBatch batch;
    HashMap<BackgroundYio, TextureRegion> mapTextures;
    boolean reverseMode;
    RectangleYio pos;
    ObjectPoolYio<BackgroundParticle> poolParticles;
    ArrayList<BackgroundParticle> particles;
    private TextureRegion splatTexture;
    RepeatYio<GeneralBackgroundManager> repeatRemoveDeadParticles;
    RectangleYio loopPosition;
    PointYio tempPoint;
    FactorYio appearFactor; // only appear once, at launch
    RectangleYio maskPosition;
    CornerEngineYio cornerEngineYio;
    public FactorYio pAlphaFactor;
    boolean readyToSpawnParticles;


    public GeneralBackgroundManager(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        loadTextures();
        currentValue = BackgroundYio.black;
        previousValue = BackgroundYio.black;
        transitionFactor = new FactorYio();
        pos = new RectangleYio();
        particles = new ArrayList<>();
        tempPoint = new PointYio();
        appearFactor = new FactorYio();
        launchAppearFactor();
        maskPosition = new RectangleYio();
        cornerEngineYio = new CornerEngineYio();
        pAlphaFactor = new FactorYio();
        readyToSpawnParticles = true;
        initPools();
        initRepeats();
        initLoopPosition();
    }


    private void initLoopPosition() {
        float lDelta = 0.125f * GraphicsYio.width;
        loopPosition = new RectangleYio(-lDelta, -lDelta, GraphicsYio.width + 2 * lDelta, GraphicsYio.height + 2 * lDelta);
    }


    public void spawnParticles() {
        if (!readyToSpawnParticles) return;
        readyToSpawnParticles = false;
        pAlphaFactor.appear(MovementType.approach, 1);
        for (int i = 0; i < 50; i++) {
            tempPoint.set(
                    loopPosition.x + YioGdxGame.random.nextDouble() * loopPosition.width,
                    loopPosition.y + YioGdxGame.random.nextDouble() * loopPosition.height
            );
            BackgroundParticle freshObject = poolParticles.getFreshObject();
            freshObject.spawn(tempPoint);
        }
    }


    private void initRepeats() {
        repeatRemoveDeadParticles = new RepeatYio<GeneralBackgroundManager>(this, 60) {
            @Override
            public void performAction() {
                parent.removeDeadParticles();
            }
        };
    }


    private void initPools() {
        poolParticles = new ObjectPoolYio<BackgroundParticle>(particles) {
            @Override
            public BackgroundParticle makeNewObject() {
                return new BackgroundParticle(GeneralBackgroundManager.this);
            }
        };
    }


    private void loadTextures() {
        mapTextures = new HashMap<>();
        for (BackgroundYio backgroundYio : BackgroundYio.values()) {
            mapTextures.put(backgroundYio, GraphicsYio.loadTextureRegion("menu/background/" + backgroundYio + ".png", false));
        }
        splatTexture = GraphicsYio.loadTextureRegion("menu/background/splat.png", false);
    }


    public void move() {
        if (yioGdxGame.gameView != null && yioGdxGame.gameView.coversAllScreen()) return;
        transitionFactor.move();
        pAlphaFactor.move();
        moveParticles();
        repeatRemoveDeadParticles.move();
        moveInitialStuff();
    }


    private void moveInitialStuff() {
        if (!appearFactor.move()) return;
        updateMaskPosition();
        checkToMakeMaskPositionMoreRectangular();
        cornerEngineYio.move(maskPosition, appearFactor, 0);
    }


    private void checkToMakeMaskPositionMoreRectangular() {
        float value = appearFactor.getValue();
        if (value > 0.5) return;
        float cx = maskPosition.x + maskPosition.width / 2;
        float cy = maskPosition.y + maskPosition.height / 2;
        float difference = maskPosition.height - maskPosition.width;
        float delta = (1 - 2 * value) * 0.5f *  difference;
        float w = maskPosition.width + delta;
        float h = maskPosition.height - delta;
        maskPosition.set(cx - w / 2, cy - h / 2, w, h);
    }


    private void updateMaskPosition() {
        float value = appearFactor.getValue();
        float cx = GraphicsYio.width / 2;
        float cy = GraphicsYio.height / 2;
        float w = value * GraphicsYio.width;
        float h = value * GraphicsYio.height;
        maskPosition.set(cx - w / 2, cy - h / 2, w, h);
    }


    private void moveParticles() {
        for (BackgroundParticle particle : particles) {
            particle.move();
        }
    }


    private void removeDeadParticles() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            BackgroundParticle particle = particles.get(i);
            if (particle.alive) continue;
            particles.remove(i);
        }
    }


    private void drawBackground(BackgroundYio backgroundYio) {
        pos.set(0, 0, GraphicsYio.width, GraphicsYio.height);
        GraphicsYio.drawByRectangle(batch, mapTextures.get(backgroundYio), pos);
    }


    private void renderOnlyCurrentBackground() { // when transitionFactor.get() == 1
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, 1);
        drawBackground(currentValue);
    }


    private void renderInTransitionState() {
        float f = transitionFactor.getValue();

        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, 1);
        drawBackground(previousValue);

        batch.setColor(c.r, c.g, c.b, f);
        drawBackground(currentValue);
        batch.setColor(c.r, c.g, c.b, 1);
    }


    void render() {
        if (yioGdxGame.gameView != null && yioGdxGame.gameView.coversAllScreen()) return;
        batch = yioGdxGame.batch;
        if (appearFactor.getValue() == 1) {
            batch.begin();
            renderInternals();
            batch.end();
            return;
        }

        Masking.begin();
        ShapeRenderer shapeRenderer = yioGdxGame.menuViewYio.shapeRenderer;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(yioGdxGame.menuViewYio.orthoCam.combined);
        GraphicsYio.drawRoundRectWithShape(shapeRenderer, maskPosition, cornerEngineYio.getCurrentRadius());
        shapeRenderer.end();


        batch.begin();
        Masking.continueAfterBatchBegin();

        renderInternals();

        Masking.end(batch);
        batch.end();
    }


    private void renderInternals() {
        renderBackgrounds();
        renderParticles();
    }


    private void renderBackgrounds() {
        if (transitionFactor.getValue() == 1) {
            renderOnlyCurrentBackground();
            return;
        }
        renderInTransitionState();
    }


    private void renderParticles() {
        if (pAlphaFactor.getValue() == 0) return;
        if (!isCurrentlyVisible()) return;
        if (pAlphaFactor.getValue() < 1) {
            GraphicsYio.setBatchAlpha(batch, pAlphaFactor.getValue());
        }
        for (BackgroundParticle particle : particles) {
            if (!particle.isCurrentlyVisible()) continue;
            GraphicsYio.drawByCircle(batch, splatTexture, particle.position);
        }
        if (pAlphaFactor.getValue() < 1) {
            GraphicsYio.setBatchAlpha(batch, 1);
        }
    }


    private boolean isCurrentlyVisible() {
        if (Scenes.campaign.isCurrentlyVisible() && Scenes.campaign.customizableListYio.getFactor().getValue() > 0.95) return false;
        if (Scenes.calendar.isCurrentlyVisible() && Scenes.calendar.calendarViewElement.getFactor().getValue() > 0.95) return false;
        return true;
    }


    public void launchAppearFactor() {
        appearFactor.reset();
        appearFactor.appear(MovementType.inertia, 1.2);
    }


    public void enableReverseMode() {
        reverseMode = true;
    }


    public boolean isMovingCurrently() {
        if (appearFactor.isInAppearState() && appearFactor.getValue() < 1) return true;
        if (appearFactor.isInDestroyState() && appearFactor.getValue() > 0) return true;
        return false;
    }


    public void applyBackground(BackgroundYio value) {
        if (currentValue == value) return;
        reverseMode = false;

        previousValue = currentValue;
        currentValue = value;

        transitionFactor.setValues(0.02, 0.01);
        transitionFactor.appear(MenuParams.ANIM_TYPE, MenuParams.ANIM_SPEED - 0.05);
    }

}
