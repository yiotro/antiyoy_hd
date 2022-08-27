package yio.tro.onliyoy.menu.elements.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;

import java.util.ArrayList;

public class ButtonYio extends InterfaceElement<ButtonYio> {

    public static final String NO_NAME = "NoName";
    public boolean shadowEnabled;
    public boolean hidden;
    protected boolean currentlyTouched, needToPerformAction, borderEnabled;
    boolean ignoreResumePause;
    int touchDelay;
    protected long lastTimeTouched;
    long timeToPerformAction;
    public float touchOffset;
    public float cornerRadius;
    String debugName;
    public TextureRegion customTexture, selectionTexture;
    public SelectionEngineYio selectionEngineYio;
    Reaction reaction;
    public BackgroundYio background;
    private boolean silentReactionMode;
    String customTexturePath;
    boolean transparencyEnabled;
    public boolean rectangularSelectionEnabled;
    public ArrayList<VisualTextContainer> textContainers;
    BitmapFont font;
    boolean renderRequired;
    public TextureRegion internalTextTexture;
    public RectangleYio framePosition;
    public RectangleYio internalRenderPosition;
    public RectangleYio roundShapePosition;
    public float viewCornerRadius;
    public RectangleYio touchAreaPosition;
    public CornerEngineYio cornerEngineYio;
    private SoundType clickSound;
    private Reaction longTapReaction;
    private LongTapDetector longTapDetector;
    boolean readyToProcessLongTap;


    public ButtonYio(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        borderEnabled = false;
        hidden = false;
        debugName = NO_NAME;
        touchDelay = 1000;
        selectionEngineYio = new SelectionEngineYio();
        textContainers = new ArrayList<>();
        font = Fonts.buttonFont;
        shadowEnabled = true;
        ignoreResumePause = false;
        customTexture = null;
        framePosition = new RectangleYio();
        selectionTexture = null;
        customTexturePath = null;
        reaction = Reaction.rbNothing;
        background = BackgroundYio.white;
        silentReactionMode = false;
        cornerRadius = 0;
        transparencyEnabled = true;
        internalTextTexture = null;
        renderRequired = false;
        internalRenderPosition = new RectangleYio();
        roundShapePosition = new RectangleYio();
        rectangularSelectionEnabled = false;
        touchAreaPosition = new RectangleYio();
        cornerEngineYio = new CornerEngineYio();
        clickSound = SoundType.button;
        longTapReaction = null;
        initLongTapDetector();
    }


    private void initLongTapDetector() {
        longTapDetector = new LongTapDetector() {
            @Override
            public void onLongTapDetected() {
                ButtonYio.this.onLongTapDetected();
            }
        };
    }


    public ButtonYio setFont(BitmapFont font) {
        this.font = font;
        return this;
    }


    @Override
    protected ButtonYio getThis() {
        return this;
    }


    @Override
    public void onMove() {
        moveSelection();
        updateFramePosition(viewPosition);
        updateInternalRenderPosition();
        updateRoundShapePosition();
        updateViewCornerRadius();
        moveCornerEngine();
        updateTouchArea();
        longTapDetector.move();
    }


    private void moveCornerEngine() {
        cornerEngineYio.move(roundShapePosition, appearFactor, viewCornerRadius);
    }


    private void moveSelection() {
        if (currentlyTouched) return;
        selectionEngineYio.move();
    }


    private void updateTouchArea() {
        touchAreaPosition.setBy(viewPosition);
        touchAreaPosition.increase(touchOffset);
    }


    private void updateViewCornerRadius() {
        viewCornerRadius = Math.min(cornerRadius, getViewPosition().height / 2 - GraphicsYio.borderThickness);
    }


    private void updateRoundShapePosition() {
        roundShapePosition.setBy(viewPosition);
    }


    private void updateInternalRenderPosition() {
        internalRenderPosition.setBy(framePosition);
        internalRenderPosition.x = (int) internalRenderPosition.x;
        internalRenderPosition.y = (int) (internalRenderPosition.y + 0.5);
        internalRenderPosition.width = (int) internalRenderPosition.width;
        internalRenderPosition.height = (int) (internalRenderPosition.height - 0.5);
        if (getFactor().getValue() < 1) {
            internalRenderPosition.width += 1; // to avoid graphic bugs
        }
    }


    @Override
    public boolean checkToPerformAction() {
        if (needToPerformAction && System.currentTimeMillis() > timeToPerformAction) {
            needToPerformAction = false;
            if (touchable) { // touchable state can be changed after touch down and before reaction
                reaction.perform(menuControllerYio);
            }
            if (isReturningBackButton()) {
                menuControllerYio.onReturningBackButtonPressed();
            }
            return true;
        }
        if (readyToProcessLongTap) {
            readyToProcessLongTap = false;
            longTapReaction.perform(menuControllerYio);
            return true;
        }
        return false;
    }


    @Override
    public boolean touchDown() {
        if (!touchable) return false;
        if (appearFactor.getValue() < 0.25) return false;

        if (isTouchedBy(currentTouch)) {
            currentlyTouched = true;
            longTapDetector.onTouchDown(currentTouch);
            select();
            return true;
        }

        return false;
    }


    @Override
    public boolean touchDrag() {
        if (!currentlyTouched) return false;
        longTapDetector.onTouchDrag(currentTouch);
        return true;
    }


    @Override
    public boolean touchUp() {
        if (currentlyTouched) {
            currentlyTouched = false;
            longTapDetector.onTouchUp(currentTouch);
            if (isClicked()) {
                onClick();
            }
            return true;
        }

        return false;
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();

        if (position.height < 0.061 * GraphicsYio.height) {
            setFont(Fonts.miniFont);
        }

        if (position.height > 0.12f * GraphicsYio.height) {
            if (sceneOwner != null) {
                setBackground(sceneOwner.getButtonBackground());
            }
        }

        cornerRadius = Math.min(GraphicsYio.defCornerRadius, position.height / 2 - 1);
    }


    private void updateFramePosition(RectangleYio srcPosition) {
        framePosition.x = srcPosition.x + cornerRadius;
        framePosition.width = srcPosition.width - 2 * cornerRadius + 2 * GraphicsYio.borderThickness;
        framePosition.y = srcPosition.y;
        framePosition.height = srcPosition.height;
    }


    @Override
    public void pressArtificially(int keycode) {
        super.pressArtificially(keycode);
        select();
        onClick();
    }


    void onClick() {
        if (reaction == null) return;
        if (appearFactor.isInDestroyState()) return;

        lastTimeTouched = System.currentTimeMillis();
        currentlyTouched = false;
        playClickSound();

        menuControllerYio.yioGdxGame.render();
        needToPerformAction = true;
        timeToPerformAction = System.currentTimeMillis() + 100;
    }


    private void onLongTapDetected() {
        if (longTapReaction == null) return;
        SoundManager.playSound(SoundType.merge);
        readyToProcessLongTap = true;
    }


    private void select() {
        if (!hidden) {
            selectionEngineYio.applySelection();
        }
    }


    private void playClickSound() {
        if (silentReactionMode) return;
        SoundManager.playSound(clickSound);
    }


    @Override
    public ButtonYio setHotkeyKeycode(int hotkeyKeycode) {
        ButtonYio result = super.setHotkeyKeycode(hotkeyKeycode);
        if (isReturningBackButton()) {
            setClickSound(SoundType.back);
        }
        return result;
    }


    public TextureRegion getCustomTexture() {
        return customTexture;
    }


    public ButtonYio setCustomTexture(TextureRegion customTexture) {
        this.customTexture = customTexture;

        setShadow(false);
        setIgnoreResumePause(true);

        return getThis();
    }


    public ButtonYio loadCustomTexture(String path) {
        Texture texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        setDebugNameByPath(path);
        customTexturePath = path;
        setRectangularSelectionEnabled(true);

        return setCustomTexture(new TextureRegion(texture));
    }


    public ButtonYio setSelectionTexture(TextureRegion selectionTexture) {
        this.selectionTexture = selectionTexture;
        return getThis();
    }


    private void setDebugNameByPath(String path) {
        int indexOfSlash = path.lastIndexOf("/");
        int indexOfDot = path.lastIndexOf(".");
        setDebugName(path.substring(indexOfSlash + 1, indexOfDot));
    }


    @Override
    public void onDestroy() {
        readyToProcessLongTap = false;
    }


    @Override
    public void onAppear() {
        appearFactor.setValues(0, 0.001);
        currentlyTouched = false;
        readyToProcessLongTap = false;
        checkToApplyEmptyText();
        checkToRenderText(); // after pause/resume
    }


    private void checkToApplyEmptyText() {
        if (textContainers.size() > 0) return;
        if (hasCustomTexture()) return;
        applyText(" ");
    }


    private ButtonYio clearText() {
        textContainers.clear();
        return getThis();
    }


    public ButtonYio setBorder(boolean hasBorder) {
        this.borderEnabled = hasBorder;
        return getThis();
    }


    public boolean isBorderEnabled() {
        return borderEnabled;
    }


    public boolean isInSilentReactionMode() {
        return silentReactionMode;
    }


    public ButtonYio setSilentReactionMode(boolean silentReactionMode) {
        this.silentReactionMode = silentReactionMode;
        return getThis();
    }


    public ButtonYio setReaction(Reaction reaction) {
        this.reaction = reaction;
        return getThis();
    }


    public ButtonYio setLongTapReaction(Reaction longTapReaction) {
        this.longTapReaction = longTapReaction;
        return this;
    }


    public Reaction getReaction() {
        return reaction;
    }


    public boolean isHidden() {
        return hidden;
    }


    public ButtonYio setHidden(boolean hidden) {
        this.hidden = hidden;
        return getThis();
    }


    public ButtonYio setTouchOffset(double touchOffset) {
        this.touchOffset = (float) (touchOffset * GraphicsYio.dim);
        return getThis();
    }


    public boolean hasCustomTexture() {
        return customTexture != null;
    }


    public boolean isSelected() {
        return selectionEngineYio.isSelected();
    }


    public ButtonYio applyText(String key) {
        clearText();
        VisualTextContainer visualTextContainer = getStandardFreshTextContainer(0);
        String string = LanguagesManager.getInstance().getString(key);
        setDebugName(key);
        visualTextContainer.applySingleTextLine(font, string);
        renderRequired = true;
        checkToRenderText();
        return this;
    }


    public ButtonYio applyManyTextLines(ArrayList<String> lines) {
        clearText();
        if (lines.size() > 0) {
            setDebugName(lines.get(0));
        }
        VisualTextContainer visualTextContainer = getStandardFreshTextContainer(0.04f * GraphicsYio.height);
        visualTextContainer.applyManyTextLines(font, lines);
        renderRequired = true;
        checkToRenderText();
        return this;
    }


    public ButtonYio applyManyTextLines(String key) {
        clearText();
        setDebugName(key);
        VisualTextContainer visualTextContainer = getStandardFreshTextContainer(0.04f * GraphicsYio.height);
        String string = LanguagesManager.getInstance().getString(key);
        visualTextContainer.applyManyTextLines(font, string);
        renderRequired = true;
        checkToRenderText();
        return this;
    }


    private VisualTextContainer getStandardFreshTextContainer(float verticalOffset) {
        VisualTextContainer visualTextContainer = new VisualTextContainer();
        visualTextContainer.setSize(position.width - 2 * cornerRadius, position.height - 2 * verticalOffset);
        visualTextContainer.centerHorizontal(position);
        visualTextContainer.centerVertical(position);
        textContainers.add(visualTextContainer);
        return visualTextContainer;
    }


    public void checkToRenderText() {
        if (!renderRequired) return;
        renderRequired = false;
        updateFramePosition(position);
        internalTextTexture = menuControllerYio.cacheTexturesManager.perform(this);
        updateFramePosition(viewPosition);
    }


    public ButtonYio setShadow(boolean shadowEnabled) {
        this.shadowEnabled = shadowEnabled;

        if (this.shadowEnabled && position.height > 0.2f * GraphicsYio.height - 1) {
            setTransparencyEnabled(false);
        }

        return getThis();
    }


    @Override
    public ButtonYio setParent(InterfaceElement parent) {
        setShadow(false);
        super.setParent(parent);
        if (parent instanceof ButtonYio && ((ButtonYio) parent).background == BackgroundYio.white) {
            setBackground(BackgroundYio.gray);
        }
        if (parent instanceof AnnounceViewElement) {
            setBackground(BackgroundYio.gray);
        }
        return getThis();
    }


    @Override
    public ButtonYio clone(InterfaceElement src) {
        super.clone(src);

        ButtonYio srcButton = (ButtonYio) src;
        setBorder(srcButton.borderEnabled);
        setShadow(srcButton.shadowEnabled);

        touchOffset = srcButton.touchOffset;

        setSelectionTexture(srcButton.selectionTexture);

        cornerRadius = srcButton.cornerRadius;
        font = srcButton.font;
        setBackground(srcButton.background);

        return getThis();
    }


    @Override
    public void forceDestroyToEnd() {
        appearFactor.setValues(0, 0);
    }


    public boolean isTouchedBy(PointYio touchPoint) {
        return isTouchInsideRectangle(touchPoint.x, touchPoint.y, touchAreaPosition);
    }


    @Override
    public void onAppPause() {
        if (ignoreResumePause) return;

        if (customTexture != null) {
            customTexture.getTexture().dispose();
        }
        if (internalTextTexture != null) {
            internalTextTexture.getTexture().dispose();
            internalTextTexture = null;
        }
        renderRequired = true;
    }


    public void reloadCustomTexture() {
        resetCustomTexture();
        boolean shadow = shadowEnabled;
        loadCustomTexture(customTexturePath);
        setShadow(shadow);
    }


    private void resetCustomTexture() {
        customTexture = null;
    }


    public ButtonYio setIgnoreResumePause(boolean ignoreResumePause) {
        this.ignoreResumePause = ignoreResumePause;
        return getThis();
    }


    @Override
    public void onAppResume() {
        if (ignoreResumePause) return;

        if (customTexturePath == null) {
            if (getFactor().getValue() > 0) {
                checkToRenderText();
            }
        } else {
            reloadCustomTexture();
        }
    }


    public ButtonYio setDebugName(String debugName) {
        if (this.debugName.equals(NO_NAME)) {
            this.debugName = debugName;
        }
        return this;
    }


    @Override
    public float getAlpha() {
        if (!transparencyEnabled) {
            return 1;
        }
        return super.getAlpha();
    }


    public ButtonYio setBackground(BackgroundYio background) {
        if (background == null) return this;
        this.background = background;
        return this;
    }


    public ButtonYio setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }


    public ButtonYio setTransparencyEnabled(boolean transparencyEnabled) {
        this.transparencyEnabled = transparencyEnabled;
        return this;
    }


    public ButtonYio setRectangularSelectionEnabled(boolean rectangularSelectionEnabled) {
        this.rectangularSelectionEnabled = rectangularSelectionEnabled;
        return this;
    }


    @Override
    public ButtonYio setKey(String key) {
        setDebugName(key);
        return super.setKey(key);
    }


    public ButtonYio setClickSound(SoundType clickSound) {
        this.clickSound = clickSound;
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderButton;
    }


    @Override
    public String toString() {
        return "Button '" + debugName + "'";
    }
}
