package yio.tro.onliyoy.menu.elements;

import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;

public class DarkenElement extends InterfaceElement<DarkenElement> {

    public DarkenElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        setAnimation(AnimationYio.none);
    }


    @Override
    protected DarkenElement getThis() {
        return this;
    }


    @Override
    public void onMove() {

    }


    @Override
    public void onDestroy() {

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
        return MenuRenders.renderDarkenElement;
    }
}
