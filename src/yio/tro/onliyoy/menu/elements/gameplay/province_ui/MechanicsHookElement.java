package yio.tro.onliyoy.menu.elements.gameplay.province_ui;

import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class MechanicsHookElement extends InterfaceElement<MechanicsHookElement> {


    private double acpHeight;
    private float elevation;


    public MechanicsHookElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        acpHeight = GraphicsYio.convertToHeight(1d / 8d);
        elevation = (float) ((acpHeight + 0.03) * GraphicsYio.height);
        shouldApplyParent = false;
    }


    @Override
    protected MechanicsHookElement getThis() {
        return this;
    }


    @Override
    public void onMove() {

    }


    @Override
    protected void updateViewPosition() {
        viewPosition.x = 0;
        viewPosition.y = 0;
        IConstructionView constructionView = Scenes.provinceManagement.constructionView;
        if (constructionView == null) return;
        if (!(constructionView instanceof AdvancedConstructionPanelElement)) return;
        AdvancedConstructionPanelElement acpElement = (AdvancedConstructionPanelElement) constructionView;
        viewPosition.y += acpElement.getFactor().getValue() * elevation;
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();
        viewPosition.width = position.width;
        viewPosition.height = position.height;
    }


    @Override
    protected boolean doesAnimationAffectSize() {
        return false;
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
        return MenuRenders.renderMechanicsHookElement;
    }
}
