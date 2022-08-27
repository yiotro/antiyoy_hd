package yio.tro.onliyoy.menu.elements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class CheckButtonYio extends InterfaceElement<CheckButtonYio> {

    RectangleYio activeSquare;
    float internalOffset;
    boolean checked, touched;
    double defaultHeight;
    public FactorYio activeFactor, selectionFactor;
    Reaction reaction;
    PointYio textOffset;
    public RenderableTextYio renderableText;
    public RectangleYio squareView;
    private float targetSvSize;


    public CheckButtonYio(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);

        activeSquare = new RectangleYio();
        activeFactor = new FactorYio();
        selectionFactor = new FactorYio();
        renderableText = new RenderableTextYio();
        renderableText.setFont(Fonts.gameFont);
        textOffset = new PointYio();
        reaction = null;
        squareView = new RectangleYio();
        targetSvSize = 0.034f * GraphicsYio.height;

        // defaults
        internalOffset = 0;
        defaultHeight = 0.07;
        setChecked(false);
        setName("[Check button]");
    }


    @Override
    protected CheckButtonYio getThis() {
        return this;
    }


    private void moveSelection() {
        if (touched) return;

        selectionFactor.move();
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();

        internalOffset = 0.3f * position.height;
    }


    @Override
    public void onMove() {
        activeFactor.move();
        moveSelection();
        updateActiveSquare();
        updateSquareView();
        updateTextPosition();
    }


    private void updateSquareView() {
        squareView.setBy(activeSquare);
        float f = viewPosition.height / position.height;
        f /= 2;
        squareView.increase(f * (targetSvSize - squareView.height));
    }


    private void updateTextPosition() {
        renderableText.position.x = viewPosition.x + textOffset.x;
        renderableText.position.y = viewPosition.y + viewPosition.height - textOffset.y;
        renderableText.updateBounds();
    }


    @Override
    public CheckButtonYio setParent(InterfaceElement parent) {
        setSize(parent.position.width / GraphicsYio.width, defaultHeight);
        return super.setParent(parent);
    }


    @Override
    public float getAlpha() {
        if (parent != null && parent instanceof LightBottomPanelElement) return 1;
        return super.getAlpha();
    }


    public CheckButtonYio setHeight(double height) {
        return setSize(position.width / GraphicsYio.width, height);
    }


    public CheckButtonYio setInternalOffset(double offset) {
        internalOffset = (float) (offset * GraphicsYio.width);
        updateActiveSquare();
        return this;
    }


    private void updateActiveSquare() {
        activeSquare.height = viewPosition.height - 2 * internalOffset;
        activeSquare.width = activeSquare.height;
        activeSquare.x = viewPosition.x + viewPosition.width - internalOffset - activeSquare.width;
        activeSquare.y = viewPosition.y + internalOffset;
    }


    public RectangleYio getActiveSquare() {
        return activeSquare;
    }


    @Override
    public void onDestroy() {
        selectionFactor.destroy(MovementType.approach, 1.9);
    }


    @Override
    public void onAppear() {

    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        if (isTouchInsideRectangle(currentTouch, viewPosition)) {
            touched = true;
            select();
            return true;
        }

        return false;
    }


    public void press() {
        SoundManager.playSound(SoundType.tick);
        setChecked(!checked);
    }


    private void select() {
        selectionFactor.setValues(1, 0);
        selectionFactor.destroy(MovementType.lighty, 4);
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        if (touched) {
            touched = false;
            if (isClicked()) {
                onClick();
            }
            return true;
        }

        return false;
    }


    private void onClick() {
        press();
        applyClickReaction();
    }


    public void applyClickReaction() {
        if (reaction == null) return;
        reaction.perform(menuControllerYio);
    }


    public CheckButtonYio setToast(final String key) {
        return setReaction(new Reaction() {
            @Override
            protected void apply() {
                if (!isChecked()) return;
                Scenes.toast.show(key);
            }
        });
    }


    public CheckButtonYio setReaction(Reaction reaction) {
        this.reaction = reaction;
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderCheckButton;
    }


    public boolean isChecked() {
        return checked;
    }


    public CheckButtonYio setName(String key) {
        renderableText.setString(LanguagesManager.getInstance().getString(key));
        updateTextMetrics();
        return this;
    }


    public CheckButtonYio setFont(BitmapFont font) {
        renderableText.setFont(font);
        return this;
    }


    @Override
    public CheckButtonYio clone(InterfaceElement src) {
        super.clone(src);

        CheckButtonYio srcCheckButton = (CheckButtonYio) src;
        setFont(srcCheckButton.renderableText.font);

        return getThis();
    }


    private void updateTextMetrics() {
        renderableText.updateMetrics();

        textOffset.x = internalOffset;
        textOffset.y = (position.height - renderableText.height) / 2;
    }


    public CheckButtonYio setChecked(boolean checked) {
        if (this.checked == checked) return this;

        this.checked = checked;

        if (!checked) { // uncheck
            activeFactor.setValues(1, 0);
            activeFactor.destroy(MovementType.inertia, 2.3);
        } else { // check
            activeFactor.setValues(0, 0);
            activeFactor.appear(MovementType.inertia, 2.3);
        }

        return this;
    }


    @Override
    public String toString() {
        return "[CheckButton: " +
                renderableText.string +
                "]";
    }
}
