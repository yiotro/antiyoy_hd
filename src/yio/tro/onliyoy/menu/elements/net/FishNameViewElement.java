package yio.tro.onliyoy.menu.elements.net;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.NetUserData;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class FishNameViewElement extends InterfaceElement<FishNameViewElement> {

    public RenderableTextYio name;
    public RenderableTextYio value;
    public CircleYio iconPosition;
    private float offset;


    public FishNameViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        name = new RenderableTextYio();
        name.setFont(Fonts.miniFont);
        value = new RenderableTextYio();
        value.setFont(Fonts.miniFont);
        iconPosition = new CircleYio();
        iconPosition.setRadius(0.011f * GraphicsYio.height);
        offset = 0.02f * GraphicsYio.width;
    }


    public void setName(String string) {
        name.setString(string);
        name.updateMetrics();
    }


    public void setValue(String string) {
        value.setString(string);
        value.updateMetrics();
    }


    @Override
    protected FishNameViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateNamePosition();
        updateValuePosition();
        updateIconPosition();
    }


    private void updateIconPosition() {
        iconPosition.center.x = value.position.x - 0.01f * GraphicsYio.width - iconPosition.radius;
        iconPosition.center.y = value.position.y - value.height / 2;
    }


    private void updateValuePosition() {
        value.position.x = name.position.x - 0.05f * GraphicsYio.width - value.width;
        value.position.y = name.position.y - name.height / 2 + value.height / 2;
        value.updateBounds();
    }


    private void updateNamePosition() {
        name.position.x = viewPosition.x + viewPosition.width - offset - name.width;
        name.position.y = viewPosition.y + viewPosition.height - offset;
        name.updateBounds();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        loadValues();
    }


    public void loadValues() {
        NetRoot netRoot = menuControllerYio.yioGdxGame.netRoot;
        NetUserData userData = netRoot.userData;
        setName(userData.name);
        setValue(Yio.getCompactValueString(userData.fish));
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
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
        return MenuRenders.renderFishNameViewElement;
    }
}
