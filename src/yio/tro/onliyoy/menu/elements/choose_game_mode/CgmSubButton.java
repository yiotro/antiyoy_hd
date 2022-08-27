package yio.tro.onliyoy.menu.elements.choose_game_mode;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class CgmSubButton {

    CgmGroup group;
    public Reaction reaction;
    public RectangleYio viewPosition;
    int index;
    public RenderableTextYio title;
    public FactorYio alphaFactor;
    boolean readyToAppear;
    public SelectionEngineYio selectionEngineYio;


    public CgmSubButton(CgmGroup group) {
        this.group = group;
        reaction = null;
        viewPosition = new RectangleYio();
        index = -1;
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        alphaFactor = new FactorYio();
        selectionEngineYio = new SelectionEngineYio();
    }


    void move() {
        if (group.expansionFactor.getValue() == 0) return;
        moveSelection();
        moveAlphaFactor();
        updateViewPosition();
        updateTitlePosition();
    }


    void onElementAppear() {
        selectionEngineYio.reset();
    }


    private void moveSelection() {
        if (group.cgmElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void moveAlphaFactor() {
        alphaFactor.move();
        if (readyToAppear && !alphaFactor.isInAppearState() && group.expansionFactor.getValue() > 0.8) {
            readyToAppear = false;
            alphaFactor.appear(MovementType.approach, 5);
        }
    }


    private void updateTitlePosition() {
        title.centerVertical(viewPosition);
        title.centerHorizontal(viewPosition);
        title.updateBounds();
    }


    void prepareToAppear() {
        readyToAppear = true;
        alphaFactor.reset();
    }


    void destroy() {
        alphaFactor.destroy(MovementType.lighty, 7);
    }


    void setTitle(String string) {
        title.setString(string);
        title.updateMetrics();
    }


    private void updateViewPosition() {
        RectangleYio gvp = group.viewPosition;
        viewPosition.width = gvp.width - 0.16f * GraphicsYio.width;
        viewPosition.x = gvp.x + gvp.width / 2 - viewPosition.width / 2;
        viewPosition.height = group.sbHeight;
        viewPosition.y = gvp.y + gvp.height - group.sbOffset - group.sbHeight - (group.sbDelta + group.sbHeight) * index;
        if (group.expansionFactor.getValue() < 1) {
            float targetY = gvp.y + gvp.height / 2 - viewPosition.height / 2;
            viewPosition.y += (1 - group.expansionFactor.getValue()) * (targetY - viewPosition.y);
        }
    }


    public boolean isTouchedBy(PointYio touchPoint, float touchOffset) {
        return viewPosition.isPointInside(touchPoint, touchOffset);
    }
}
