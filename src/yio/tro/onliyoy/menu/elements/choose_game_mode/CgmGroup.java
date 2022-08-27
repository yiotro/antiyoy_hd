package yio.tro.onliyoy.menu.elements.choose_game_mode;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;

public class CgmGroup {

    CgmElement cgmElement;
    public String key;
    public RenderableTextYio title;
    public BackgroundYio backgroundYio;
    public RectangleYio viewPosition;
    Reaction reaction;
    private int index;
    private float defaultHeight;
    public SelectionEngineYio selectionEngineYio;
    public boolean simple;
    public ArrayList<CgmSubButton> subButtons;
    float targetHeight;
    public FactorYio expansionFactor;
    private float expandedHeight;
    public FactorYio shrinkFactor;
    float sbOffset;
    float sbHeight;
    float sbDelta;


    public CgmGroup(CgmElement cgmElement) {
        this.cgmElement = cgmElement;
        key = "";
        title = new RenderableTextYio();
        viewPosition = new RectangleYio();
        title.setFont(Fonts.buttonFont);
        selectionEngineYio = new SelectionEngineYio();
        backgroundYio = null;
        reaction = null;
        simple = true;
        subButtons = new ArrayList<>();
        targetHeight = 0;
        expansionFactor = new FactorYio();
        shrinkFactor = new FactorYio();
        sbOffset = 0.03f * GraphicsYio.height;
        sbHeight = 0.05f * GraphicsYio.height;
        sbDelta = 0.015f * GraphicsYio.height;
    }


    void move() {
        updateViewPosition();
        updateTitlePosition();
        moveSubButtons();
        moveSelection();
    }


    void moveExpansion() {
        expansionFactor.move();
        shrinkFactor.move();
        updateTargetHeight();
    }


    void onElementAppear() {
        for (CgmSubButton cgmSubButton : subButtons) {
            cgmSubButton.onElementAppear();
        }
    }


    private void updateTargetHeight() {
        targetHeight = defaultHeight;
        if (!simple) {
            targetHeight += expansionFactor.getValue() * (expandedHeight - defaultHeight);
        }
        if (shrinkFactor.getValue() > 0) {
            targetHeight *= 1 - 0.05f * shrinkFactor.getValue();
        }
    }


    private void moveSubButtons() {
        if (simple) return;
        for (CgmSubButton cgmSubButton : subButtons) {
            cgmSubButton.move();
        }
    }


    private void moveSelection() {
        if (cgmElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void updateTitlePosition() {
        title.centerVertical(viewPosition);
        title.centerHorizontal(viewPosition);
        title.updateBounds();
    }


    private void updateViewPosition() {
        RectangleYio srcPos = cgmElement.dynamicPosition;
        CgmGroup previousGroup = getPreviousGroup();
        viewPosition.x = srcPos.x;
        viewPosition.width = srcPos.width;
        viewPosition.height = targetHeight + (1 - cgmElement.getFactor().getValue()) * 0.9f * targetHeight;
        if (previousGroup != null) {
            viewPosition.y = previousGroup.viewPosition.y - viewPosition.height;
        } else {
            viewPosition.y = srcPos.y + srcPos.height - viewPosition.height;
        }
    }


    private CgmGroup getPreviousGroup() {
        if (index == 0) return null;
        return cgmElement.groups.get(index - 1);
    }


    boolean isTouchedBy(PointYio touchPoint, float offset) {
        return viewPosition.isPointInside(touchPoint, offset);
    }


    public CgmGroup addSubButton(String key, Reaction reaction) {
        CgmSubButton cgmSubButton = new CgmSubButton(this);
        cgmSubButton.reaction = reaction;
        cgmSubButton.index = subButtons.size();
        cgmSubButton.setTitle(LanguagesManager.getInstance().getString(key));
        subButtons.add(cgmSubButton);
        updateExpandedHeight();
        return this;
    }


    void doCancelExpansion() {
        expansionFactor.destroy(MovementType.inertia, 4);
        for (CgmSubButton cgmSubButton : subButtons) {
            cgmSubButton.destroy();
        }
    }


    void doExpand() {
        expansionFactor.appear(MovementType.inertia, 3.5);
        for (CgmSubButton cgmSubButton : subButtons) {
            cgmSubButton.prepareToAppear();
        }
    }


    private void updateExpandedHeight() {
        expandedHeight = 0;
        expandedHeight += 2 * sbOffset;
        expandedHeight += subButtons.size() * sbHeight;
        expandedHeight += (subButtons.size() - 1) * sbDelta;
    }


    void select(PointYio touchPoint) {
        if (!expansionFactor.isInAppearState()) {
            selectionEngineYio.applySelection();
            return;
        }
        CgmSubButton touchedButton = getTouchedButton(touchPoint);
        if (touchedButton == null) return;
        touchedButton.selectionEngineYio.applySelection();
    }


    CgmSubButton getTouchedButton(PointYio touchPoint) {
        CgmSubButton touchedButton = getTouchedButton(touchPoint, 0);
        if (touchedButton != null) return touchedButton;
        return getTouchedButton(touchPoint, 0.05f * GraphicsYio.width);
    }


    CgmSubButton getTouchedButton(PointYio touchPoint, float tOffset) {
        for (CgmSubButton cgmSubButton : subButtons) {
            if (cgmSubButton.isTouchedBy(touchPoint, tOffset)) return cgmSubButton;
        }
        return null;
    }


    public void setTitle(String key) {
        this.key = key;
        title.setString(LanguagesManager.getInstance().getString(key));
        title.updateMetrics();
    }


    public void setBackgroundYio(BackgroundYio backgroundYio) {
        this.backgroundYio = backgroundYio;
    }


    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }


    public void setIndex(int index) {
        this.index = index;
    }


    public void setDefaultHeight(float defaultHeight) {
        this.defaultHeight = defaultHeight;
        updateTargetHeight();
    }


    public void setSimple(boolean simple) {
        this.simple = simple;
    }
}
