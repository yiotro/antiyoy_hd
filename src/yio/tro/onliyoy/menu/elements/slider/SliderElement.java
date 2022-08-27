package yio.tro.onliyoy.menu.elements.slider;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.*;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;
import java.util.EnumSet;

public class SliderElement extends InterfaceElement<SliderElement> {

    public RenderableTextYio title;
    public RectangleYio line;
    public RectangleYio leftSide;
    public RectangleYio rightSide;
    public CircleYio indicatorPosition;
    public RenderableTextYio valueViewText;
    private float vOffset;
    private String[] possibleValues;
    public double hookValue;
    private float internalSectorLength;
    private int valueViewIndex;
    private boolean touchedCurrently;
    public RectangleYio hookLine;
    public CircleYio selectionPosition;
    public SelectionEngineYio selectionEngineYio;
    private FactorYio magnetFactor;
    private double magnetStartValue;
    private double magnetTargetValue;
    public ArrayList<RectangleYio> sectors;
    private float sOffset;
    Reaction valueChangeReaction;


    public SliderElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        initTitle();
        line = new RectangleYio();
        line.height = 2 * GraphicsYio.borderThickness;
        vOffset = 0.012f * GraphicsYio.height;
        sOffset = 0.01f * GraphicsYio.width;
        hookValue = 0.5;
        possibleValues = null;
        internalSectorLength = 0;
        valueViewIndex = -1;
        hookLine = new RectangleYio();
        selectionEngineYio = new SelectionEngineYio();
        magnetFactor = new FactorYio();
        magnetStartValue = 0;
        magnetTargetValue = 0;
        sectors = new ArrayList<>();
        valueChangeReaction = null;
        initLeftSide();
        initRightSide();
        initIndicator();
        initSelectionPosition();
        initValueViewText();
    }


    private void initSelectionPosition() {
        selectionPosition = new CircleYio();
        selectionPosition.setRadius(3 * indicatorPosition.radius);
    }


    private void initValueViewText() {
        valueViewText = new RenderableTextYio();
        valueViewText.setFont(Fonts.miniFont);
        valueViewText.setString("-");
        valueViewText.updateMetrics();
    }


    private void initIndicator() {
        indicatorPosition = new CircleYio();
        indicatorPosition.setRadius(9 * GraphicsYio.borderThickness);
    }


    private void initRightSide() {
        rightSide = new RectangleYio();
        rightSide.width = leftSide.width;
        rightSide.height = leftSide.height;
    }


    private void initLeftSide() {
        leftSide = new RectangleYio();
        leftSide.width = 2 * GraphicsYio.borderThickness;
        leftSide.height = 4 * leftSide.width;
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        title.setString("-");
        title.updateMetrics();
    }


    public SliderElement setTitle(String key) {
        title.setString(LanguagesManager.getInstance().getString(key));
        title.updateMetrics();
        return this;
    }


    public SliderElement setFonts(BitmapFont titleFont, BitmapFont valueFont) {
        title.setFont(titleFont);
        title.updateMetrics();
        valueViewText.setFont(valueFont);
        valueViewText.updateMetrics();
        return this;
    }


    public SliderElement setPossibleValues(String[] possibleValues) {
        this.possibleValues = possibleValues;
        onPossibleValuesSet();
        return this;
    }


    public SliderElement setPossibleValues(int[] srcArray) {
        possibleValues = new String[srcArray.length];
        for (int i = 0; i < srcArray.length; i++) {
            possibleValues[i] = "" + srcArray[i];
        }
        onPossibleValuesSet();
        return this;
    }


    public SliderElement setPossibleValues(double[] srcArray) {
        possibleValues = new String[srcArray.length];
        for (int i = 0; i < srcArray.length; i++) {
            possibleValues[i] = "" + srcArray[i];
        }
        onPossibleValuesSet();
        return this;
    }


    public <E extends Enum<E>> SliderElement setPossibleValues(Class<E> eClass) {
        EnumSet<E> enumSet = EnumSet.allOf(eClass);
        possibleValues = new String[enumSet.size()];
        int i = 0;
        for (E e : enumSet) {
            possibleValues[i] = e.name();
            i++;
        }
        onPossibleValuesSet();
        return this;
    }


    @Override
    public SliderElement setParent(InterfaceElement parent) {
        double iOffset = 0.3 * GraphicsYio.convertToWidth(0.07);
        setSize(parent.getPosition().width / GraphicsYio.width - 2 * iOffset, 0.1);
        return super.setParent(parent);
    }


    public SliderElement setWidth(double width) {
        position.width = (float) (width * GraphicsYio.width);
        onSizeChanged();
        return this;
    }


    private void onPossibleValuesSet() {
        updateInternalSectorLength();
        makeSectorsList();
    }


    private void makeSectorsList() {
        sectors.clear();
        for (int i = 0; i < possibleValues.length; i++) {
            RectangleYio rectangleYio = new RectangleYio();
            rectangleYio.height = line.height;
            sectors.add(rectangleYio);
        }
    }


    private void updateInternalSectorLength() {
        // yes, this very small value is needed to avoid
        // problems when indicator is on right side
        internalSectorLength = 0.0001f + 1f / possibleValues.length;
    }


    @Override
    protected SliderElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        moveTitle();
        moveLine();
        moveHookLine();
        moveLeftSide();
        moveRightSide();
        moveMagnet();
        moveIndicator();
        moveValueViewText();
        moveSelectionPosition();
        moveSelectionEngine();
        moveSectors();
    }


    private void moveSectors() {
        if (!areSectorsCurrentlyVisible()) return;
        float sectorLength = internalSectorLength * hookLine.width;
        float curX = hookLine.x;
        float offset = selectionEngineYio.factorYio.getValue() * sOffset;
        for (RectangleYio rectangleYio : sectors) {
            rectangleYio.y = line.y;
            rectangleYio.x = curX + offset;
            rectangleYio.width = sectorLength - 2 * offset;
            if (rectangleYio.x < line.x) {
                rectangleYio.width = rectangleYio.x + rectangleYio.width - line.x;
                rectangleYio.x = line.x;
            }
            if (rectangleYio.x + rectangleYio.width > line.x + line.width) {
                rectangleYio.width = line.x + line.width - rectangleYio.x;
            }
            curX += sectorLength;
        }
    }


    public boolean areSectorsCurrentlyVisible() {
        return selectionEngineYio.isSelected() && possibleValues.length <= 12;
    }


    private void moveMagnet() {
        if (!magnetFactor.move()) return;
        hookValue = magnetStartValue + magnetFactor.getValue() * (magnetTargetValue - magnetStartValue);
    }


    private void moveSelectionEngine() {
        if (touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void moveSelectionPosition() {
        selectionPosition.center.setBy(indicatorPosition.center);
    }


    private void moveHookLine() {
        hookLine.x = line.x - 0.5f * line.width * internalSectorLength;
        hookLine.width = line.width + line.width * internalSectorLength;
        hookLine.y = line.y;
        hookLine.height = line.height;
    }


    private void moveValueViewText() {
        checkToUpdateValueString();
        valueViewText.position.x = line.x + line.width - valueViewText.width;
        valueViewText.position.y = line.y + line.height + vOffset + valueViewText.height;
        valueViewText.updateBounds();
    }


    private void checkToUpdateValueString() {
        if (valueViewIndex == getValueIndex()) return;
        valueViewIndex = getValueIndex();
        String key = possibleValues[valueViewIndex];
        String string = LanguagesManager.getInstance().getString(key);
        valueViewText.setString(string.toLowerCase());
        valueViewText.updateMetrics();
    }


    private void moveIndicator() {
        indicatorPosition.center.y = hookLine.y + hookLine.height / 2;
        indicatorPosition.center.x = hookLine.x + (float) hookValue * hookLine.width;
        float delta = 1.2f * indicatorPosition.radius;
        if (indicatorPosition.center.x > line.x + line.width - delta) {
            indicatorPosition.center.x = line.x + line.width - delta;
        }
        if (indicatorPosition.center.x < line.x + delta) {
            indicatorPosition.center.x = line.x + delta;
        }
    }


    private void moveRightSide() {
        rightSide.x = line.x + line.width - rightSide.width;
        rightSide.y = leftSide.y;
    }


    private void moveLeftSide() {
        leftSide.x = line.x;
        leftSide.y = line.y + line.height / 2 - leftSide.height / 2;
    }


    public void setValueIndex(int index) {
        if (index > possibleValues.length - 1) {
            index = possibleValues.length - 1;
        }
        hookValue = index * internalSectorLength + internalSectorLength / 2;
        applyMagnetToNearestSectorCenter();
        limitHookValue();
    }


    public SliderElement setValueChangeReaction(Reaction reaction) {
        this.valueChangeReaction = reaction;
        return this;
    }


    private void limitHookValue() {
        if (hookValue > 1) {
            hookValue = 1;
        }
        if (hookValue < 0) {
            hookValue = 0;
        }
    }


    public int getValueIndex() {
        return (int) (hookValue / internalSectorLength);
    }


    private void moveLine() {
        line.x = viewPosition.x;
        line.width = viewPosition.width;
        line.y = viewPosition.y + viewPosition.height / 2 - line.height / 2;
    }


    private void moveTitle() {
        title.position.x = viewPosition.x;
        title.position.y = viewPosition.y + viewPosition.height;
        title.updateBounds();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    private void applyCurrentTouch() {
        int previousValueIndex = getValueIndex();
        hookValue = (currentTouch.x - hookLine.x) / hookLine.width;
        limitHookValue();
        if (previousValueIndex != getValueIndex()) {
            onValueChangedByTouch();
        }
    }


    private void onValueChangedByTouch() {
        SoundManager.playSound(SoundType.slider_change);
        if (valueChangeReaction == null) return;
        valueChangeReaction.perform(menuControllerYio);
    }


    private void applyMagnet(double targetValue) {
        magnetStartValue = hookValue;
        magnetTargetValue = targetValue;
        magnetFactor.reset();
        magnetFactor.appear(MovementType.inertia, 2);
    }


    private void applyMagnetToNearestSectorCenter() {
        int valueIndex = getValueIndex();
        if (valueIndex == 0) {
            applyMagnet(0);
            return;
        }
        if (valueIndex == possibleValues.length - 1) {
            applyMagnet(1);
            return;
        }
        applyMagnet(valueIndex * internalSectorLength + internalSectorLength / 2);
    }


    @Override
    public boolean touchDown() {
        touchedCurrently = isTouchedBy(currentTouch);
        if (!touchedCurrently) return false;
        applyCurrentTouch();
        magnetFactor.stop();
        selectionEngineYio.applySelection();
        SoundManager.playSound(SoundType.slider_touch);
        return true;
    }


    @Override
    public boolean touchDrag() {
        if (!touchedCurrently) return false;
        applyCurrentTouch();
        return true;
    }


    @Override
    public boolean touchUp() {
        if (!touchedCurrently) return false;
        touchedCurrently = false;
        applyCurrentTouch();
        applyMagnetToNearestSectorCenter();
        return true;
    }


    public boolean isTouchedCurrently() {
        return touchedCurrently;
    }


    public String[] getPossibleValues() {
        return possibleValues;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderSlider;
    }

}
