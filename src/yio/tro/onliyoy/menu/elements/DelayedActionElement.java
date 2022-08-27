package yio.tro.onliyoy.menu.elements;

import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class DelayedActionElement extends InterfaceElement<DelayedActionElement> {

    private long appearTime;
    long delay;
    Reaction reaction;
    boolean ready;


    public DelayedActionElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        delay = 3000;
    }


    @Override
    protected DelayedActionElement getThis() {
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
        appearTime = System.currentTimeMillis();
        ready = true;
    }


    @Override
    public boolean checkToPerformAction() {
        if (ready && System.currentTimeMillis() > appearTime + delay) {
            ready = false;
            reaction.perform(menuControllerYio);
            return true;
        }
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


    public DelayedActionElement setDelay(long delay) {
        this.delay = delay;
        return this;
    }


    public DelayedActionElement setReaction(Reaction reaction) {
        this.reaction = reaction;
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderDelayedActionElement;
    }
}
