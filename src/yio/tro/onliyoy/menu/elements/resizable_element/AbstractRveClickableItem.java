package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;

import java.util.ArrayList;

public abstract class AbstractRveClickableItem extends AbstractRveItem{

    public ArrayList<RveIcon> icons;


    public AbstractRveClickableItem() {
        icons = new ArrayList<>();
        initIcons();
    }


    @Override
    public void move() {
        updatePosition();
        moveFactor();
        moveColorBounds();
        updateIconPositions();
        moveIcons();
        onMove();
    }


    private void moveIcons() {
        for (RveIcon rveIcon : icons) {
            rveIcon.move();
        }
    }


    protected abstract void initIcons();


    protected void addIcon(RveIconType rveIconType) {
        RveIcon rveIcon = new RveIcon(this, rveIconType);
        rveIcon.position.setRadius(0.03f * GraphicsYio.height);
        icons.add(rveIcon);
    }


    protected void removeIcon(RveIconType rveIconType) {
        RveIcon rveIcon = getIcon(rveIconType);
        if (rveIcon == null) return;
        icons.remove(rveIcon);
    }


    protected abstract void updateIconPositions();


    protected RveIcon getTouchedIcon(PointYio touchPoint) {
        for (RveIcon rveIcon : icons) {
            if (rveIcon.isTouchedBy(touchPoint)) return rveIcon;
        }
        return null;
    }


    @Override
    boolean onTouchDown(PointYio touchPoint) {
        RveIcon touchedIcon = getTouchedIcon(touchPoint);
        if (touchedIcon != null) {
            touchedIcon.selectionEngineYio.applySelection();
            return true;
        }
        return false;
    }


    @Override
    boolean onClick(PointYio touchPoint) {
        RveIcon touchedIcon = getTouchedIcon(touchPoint);
        if (touchedIcon != null) {
            SoundManager.playSound(SoundType.button);
            applyIconReaction(touchedIcon.type);
            return true;
        }
        return false;
    }


    protected RveIcon getIcon(RveIconType rveIconType) {
        for (RveIcon rveIcon : icons) {
            if (rveIcon.type == rveIconType) return rveIcon;
        }
        return null;
    }


    protected abstract void applyIconReaction(RveIconType rveIconType);
}
