package yio.tro.onliyoy.menu.elements.highlight_area;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.ConstructionViewElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.EconomicsViewElement;
import yio.tro.onliyoy.menu.elements.resizable_element.HorizontalAlignType;
import yio.tro.onliyoy.menu.elements.resizable_element.VerticalAlignType;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class HighlightAreaElement extends InterfaceElement<HighlightAreaElement> {

    public RectangleYio highlightPosition;
    boolean ready;
    public RenderableTextYio title;
    public RectangleYio incBounds;
    VerticalAlignType verticalAlignType;
    HorizontalAlignType horizontalAlignType;


    public HighlightAreaElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        highlightPosition = new RectangleYio();
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        incBounds = new RectangleYio();
    }


    public void launch(InterfaceElement interfaceElement, String key) {
        if (interfaceElement instanceof ConstructionViewElement) {
            RectangleYio tempRectangle = new RectangleYio();
            tempRectangle.width = 0.5f * GraphicsYio.width;
            tempRectangle.x = GraphicsYio.width / 2 - tempRectangle.width / 2;
            tempRectangle.y = 0;
            tempRectangle.height = 0.09f * GraphicsYio.height;
            launch(tempRectangle, key);
            return;
        }
        if (interfaceElement instanceof EconomicsViewElement) {
            RectangleYio tempRectangle = new RectangleYio();
            tempRectangle.width = 0.15f * GraphicsYio.width;
            tempRectangle.x = GraphicsYio.width / 2 - tempRectangle.width / 2;
            tempRectangle.height = 0.06f * GraphicsYio.height;
            tempRectangle.y = GraphicsYio.height - tempRectangle.height;
            launch(tempRectangle, key);
            return;
        }
        launch(interfaceElement.getViewPosition(), key);
    }


    public void launch(RectangleYio src, String key) {
        highlightPosition.setBy(src);
        highlightPosition.increase(0.03f * GraphicsYio.width);
        title.setString(LanguagesManager.getInstance().getString(key));
        title.updateMetrics();
        updateAlign();
    }


    private void updateAlign() {
        float cy = highlightPosition.y + highlightPosition.height / 2;
        float cx = highlightPosition.x + highlightPosition.width / 2;
        if (cy < 0.5f * GraphicsYio.width) {
            verticalAlignType = VerticalAlignType.above;
        } else {
            verticalAlignType = VerticalAlignType.under;
        }
        if (cx < 0.33f * GraphicsYio.width) {
            horizontalAlignType = HorizontalAlignType.left;
        } else if (cx < 0.66f * GraphicsYio.width) {
            horizontalAlignType = HorizontalAlignType.center;
        } else {
            horizontalAlignType = HorizontalAlignType.right;
        }
    }


    @Override
    protected HighlightAreaElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateTitlePosition();
        updateIncBounds();
    }


    private void updateTitlePosition() {
        float delta = 0.02f * GraphicsYio.width;
        switch (horizontalAlignType) {
            default:
            case center:
                title.position.x = GraphicsYio.width / 2 - title.width / 2;
                break;
            case left:
                title.position.x = delta;
                break;
            case right:
                title.position.x = GraphicsYio.width - delta - title.width;
                break;
        }
        switch (verticalAlignType) {
            default:
            case above:
                title.position.y = highlightPosition.y + highlightPosition.height + delta + title.height;
                title.position.y -= (1 - appearFactor.getValue()) * delta;
                break;
            case under:
                title.position.y = highlightPosition.y - delta;
                title.position.y += (1 - appearFactor.getValue()) * delta;
                break;
        }
        title.updateBounds();
    }


    private void updateIncBounds() {
        incBounds.setBy(title.bounds);
        incBounds.increase(0.015f * GraphicsYio.width);
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        ready = false;
    }


    @Override
    public boolean checkToPerformAction() {
        if (ready) {
            ready = false;
            Scenes.highlightArea.destroy();
            return true;
        }
        return false;
    }


    @Override
    public boolean touchDown() {
        return true;
    }


    @Override
    public boolean touchDrag() {
        return true;
    }


    @Override
    public boolean touchUp() {
        if (isClicked()) {
            SoundManager.playSound(SoundType.button);
            ready = true;
        }
        return true;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderHighlightAreaElement;
    }
}
