package yio.tro.onliyoy.menu.elements;

import yio.tro.onliyoy.*;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

public class ImportantConfirmationButton extends InterfaceElement<ImportantConfirmationButton> {


    public float cornerRadius;
    public SelectionEngineYio selectionEngineYio;
    boolean touched;
    String string;
    public RenderableTextYio title;
    Reaction reaction;
    boolean ready;
    public int counter;
    int defCounterValue;
    RepeatYio<ImportantConfirmationButton> repeatCount;
    float touchOffset;


    public ImportantConfirmationButton(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        selectionEngineYio = new SelectionEngineYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        defCounterValue = 5;
        touchOffset = 0;
        initRepeats();
    }


    private void initRepeats() {
        repeatCount = new RepeatYio<ImportantConfirmationButton>(this, 30) {
            @Override
            public void performAction() {
                parent.decreaseCounter();
            }
        };
        if (YioGdxGame.platformType == PlatformType.pc) {
            repeatCount.setFrequency(15);
        }
    }


    @Override
    protected ImportantConfirmationButton getThis() {
        return this;
    }


    void decreaseCounter() {
        if (counter == 0) return;
        counter--;

        updateTitleText();
    }


    private void updateTitleText() {
        String s = "" + counter;
        if (isCounterAtZero()) {
            s = string;
        }

        title.setString(s);
        title.updateMetrics();
    }


    public ImportantConfirmationButton applyText(String key) {
        this.string = LanguagesManager.getInstance().getString(key);
        return this;
    }


    public ImportantConfirmationButton setCounterValue(int value) {
        defCounterValue = value;
        return this;
    }


    public ImportantConfirmationButton setReaction(Reaction reaction) {
        this.reaction = reaction;
        return this;
    }


    @Override
    public void onMove() {
        moveSelection();
        repeatCount.move();
        updateTitlePosition();
    }


    private void updateTitlePosition() {
        title.centerVertical(viewPosition);
        title.centerHorizontal(viewPosition);
        title.updateBounds();
    }


    private void moveSelection() {
        if (touched) return;
        selectionEngineYio.move();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        appearFactor.setValues(0, 0.001);
        touched = false;
        ready = false;
        counter = defCounterValue;
        updateTitleText();
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();
        updateCornerRadius();
    }


    private void updateCornerRadius() {
        cornerRadius = Math.min(position.height / 2 - 1, GraphicsYio.defCornerRadius);
    }


    @Override
    public boolean checkToPerformAction() {
        if (!ready) return false;

        ready = false;
        if (reaction != null) {
            reaction.perform(menuControllerYio);
        }

        return true;
    }


    @Override
    public boolean touchDown() {
        touched = viewPosition.isPointInside(currentTouch, touchOffset);
        if (touched) {
            selectionEngineYio.applySelection();
            return true;
        }
        return false;
    }


    @Override
    public boolean touchDrag() {
        if (!touched) return false;
        return true;
    }


    @Override
    public boolean touchUp() {
        if (!touched) return false;
        touched = false;
        if (isClicked()) {
            onClick();
        }
        return true;
    }


    private void onClick() {
        SoundManager.playSound(SoundType.button);
        if (!isCounterAtZero()) return;
        ready = true;
    }


    public boolean isCounterAtZero() {
        return counter == 0;
    }


    public ImportantConfirmationButton setTouchOffset(double w) {
        this.touchOffset = (float) (w * GraphicsYio.width);
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderIcButton;
    }
}
