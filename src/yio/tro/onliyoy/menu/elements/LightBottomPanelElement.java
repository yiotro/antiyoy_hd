package yio.tro.onliyoy.menu.elements;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.VisualTextContainer;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class LightBottomPanelElement extends InterfaceElement<LightBottomPanelElement> {

    public VisualTextContainer visualTextContainer;
    public RectangleYio renderPosition;
    private RectangleYio tempRectangle;
    private ArrayList<String> tempStringList;
    public BackgroundYio color;
    public RectangleYio sideShadowPosition;


    public LightBottomPanelElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        visualTextContainer = new VisualTextContainer();
        renderPosition = new RectangleYio();
        tempRectangle = new RectangleYio();
        tempStringList = new ArrayList<>();
        sideShadowPosition = new RectangleYio();
        color = BackgroundYio.white;
    }


    @Override
    protected LightBottomPanelElement getThis() {
        return this;
    }


    public LightBottomPanelElement setTitle(String key) {
        visualTextContainer.applySingleTextLine(Fonts.gameFont, LanguagesManager.getInstance().getString(key));
        return this;
    }


    public LightBottomPanelElement applyManyLines(String sourceKey) {
        tempStringList.clear();
        tempStringList.add(" ");
        String source = LanguagesManager.getInstance().getString(sourceKey);
        StringTokenizer tokenizer = new StringTokenizer(source, "#");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            tempStringList.add(token);
        }
        return applyManyLines(tempStringList);
    }


    public LightBottomPanelElement applyManyLines(ArrayList<String> strings) {
        visualTextContainer.applyManyTextLines(Fonts.gameFont, strings);
        return this;
    }


    @Override
    public void onMove() {
        moveRenderPosition();
        moveVisualTextContainer();
        updateSideShadowPosition();
    }


    private void updateSideShadowPosition() {
        sideShadowPosition.set(
                0, viewPosition.y + viewPosition.height - 0.03f * GraphicsYio.height,
                GraphicsYio.width, 0.045f * GraphicsYio.height
        );
    }


    public LightBottomPanelElement setColor(BackgroundYio color) {
        this.color = color;
        return this;
    }


    private void moveVisualTextContainer() {
        tempRectangle.x = viewPosition.x + viewPosition.width / 2 - visualTextContainer.position.width / 2;
        tempRectangle.height = 0;
        tempRectangle.y = viewPosition.y + viewPosition.height - 0.03f * GraphicsYio.height;

        visualTextContainer.move(tempRectangle);
    }


    private void moveRenderPosition() {
        renderPosition.setBy(viewPosition);
        renderPosition.increase(GraphicsYio.borderThickness);
        renderPosition.height -= GraphicsYio.borderThickness;

        if (renderPosition.y > 0) {
            renderPosition.height += renderPosition.y;
            renderPosition.y = 0;
        }
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {

    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();
        visualTextContainer.setSize(0.95f * position.width, 0);
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        return isTouchedBy(currentTouch);
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
        return MenuRenders.renderLightBottomPanel;
    }
}
