package yio.tro.onliyoy.menu.elements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.VisualTextContainer;

import java.util.ArrayList;

public class AdvancedLabelElement extends InterfaceElement<AdvancedLabelElement> {

    public VisualTextContainer visualTextContainer;
    public boolean backgroundEnabled;
    BitmapFont font;
    int lifeCounter;
    int lifeDuration;
    public RectangleYio viewBounds;
    boolean centered;
    boolean wasTouched;
    public double backgroundOpacity;
    public float textOpacity;


    public AdvancedLabelElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        visualTextContainer = new VisualTextContainer();
        viewBounds = new RectangleYio();
        backgroundEnabled = false;
        font = Fonts.buttonFont;
        lifeDuration = -1;
        wasTouched = false;
        centered = false;
        backgroundOpacity = 1;
        textOpacity = 0.8f;
    }


    public AdvancedLabelElement applyText(String key) {
        visualTextContainer.clear();
        visualTextContainer.position.setBy(position);
        visualTextContainer.position.height = position.height;
        visualTextContainer.applyManyTextLines(font, LanguagesManager.getInstance().getString(key));
        for (RenderableTextYio renderableTextYio : visualTextContainer.textList) {
            renderableTextYio.centered = centered;
        }
        visualTextContainer.updateTextPosition();
        return this;
    }


    public AdvancedLabelElement setFont(BitmapFont font) {
        this.font = font;
        return this;
    }


    public AdvancedLabelElement setBackgroundEnabled(boolean backgroundEnabled) {
        this.backgroundEnabled = backgroundEnabled;
        return this;
    }


    public AdvancedLabelElement setBackgroundOpacity(double backgroundOpacity) {
        this.backgroundOpacity = backgroundOpacity;
        return this;
    }


    public AdvancedLabelElement setTextOpacity(double textOpacity) {
        this.textOpacity = (float) textOpacity;
        return this;
    }


    public AdvancedLabelElement setLifeDuration(int lifeDuration) {
        this.lifeDuration = lifeDuration;
        lifeCounter = lifeDuration;
        return this;
    }


    @Override
    protected AdvancedLabelElement getThis() {
        return this;
    }


    private void updateViewBounds() {
        viewBounds.setBy(viewPosition);

        ArrayList<RenderableTextYio> textList = visualTextContainer.textList;
        if (textList.size() == 0) return;

        RenderableTextYio topText = textList.get(0);
        RenderableTextYio bottomText = textList.get(textList.size() - 1);

        if (textList.size() == 2 && bottomText.string.equals(" ")) {
            bottomText = topText;
        }

        viewBounds.y = viewPosition.y + bottomText.bounds.y - 0.02f * GraphicsYio.height;
        viewBounds.height = topText.bounds.y + topText.bounds.height - bottomText.bounds.y + 0.03f * GraphicsYio.height;
    }


    private void checkToDie() {
        if (appearFactor.isInDestroyState()) return;
        if (!isReadyToDie()) return;
        destroy();
    }


    private boolean isReadyToDie() {
        if (lifeDuration == -1) return false;

        if (lifeCounter > 0) {
            lifeCounter--;
            return false;
        }

        return true;
    }


    @Override
    public void onMove() {
        moveVisualContainer();
        updateViewBounds();
        checkToDie();
    }


    private void moveVisualContainer() {
        visualTextContainer.position.setBy(viewPosition);
        visualTextContainer.updateTextPosition();
    }


    @Override
    public void onDestroy() {
        wasTouched = false;
    }


    @Override
    public void onAppear() {
        lifeCounter = lifeDuration;
        wasTouched = false;
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    public AdvancedLabelElement setCentered(boolean centered) {
        this.centered = centered;
        return this;
    }


    @Override
    public boolean touchDown() {
        wasTouched = true;
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderAdvancedLabelElement;
    }

}
