package yio.tro.onliyoy.menu.elements.resizable_element;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.menu_renders.rve_renders.AbstractRveRender;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.SelectionEngineYio;

public abstract class AbstractRveConditionItem extends AbstractRveClickableItem {

    public RenderableTextYio title;
    public SelectionEngineYio selectionEngineYio;
    public boolean arrowUpMode;
    protected float iconRadius;
    protected boolean readMode;
    public boolean invalid;


    @Override
    protected void initIcons() {
        iconRadius = 0.025f * GraphicsYio.height;
        addIcon(RveIconType.delete);
        addIcon(RveIconType.executor);
        setArrowUpMode(true);
        getIcon(RveIconType.executor).setTouchOffset(0.5f * (getHeight() - 2 * iconRadius));
    }


    @Override
    protected void updateIconPositions() {
        float offset = 0.02f * GraphicsYio.width + iconRadius;

        RveIcon leftIcon = getIcon(RveIconType.executor);
        if (leftIcon == null) {
            leftIcon = getIcon(RveIconType.notification);
        }
        if (leftIcon != null) {
            leftIcon.position.radius = iconRadius;
            leftIcon.position.center.x = position.x + offset;
            leftIcon.position.center.y = position.y + position.height / 2;
        }

        RveIcon deleteIcon = getIcon(RveIconType.delete);
        if (deleteIcon != null) {
            deleteIcon.position.radius = iconRadius;
            deleteIcon.position.center.x = position.x + position.width - offset;
            deleteIcon.position.center.y = position.y + position.height / 2;
        }
    }


    protected void initialize() {
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        setTitle("-");
        selectionEngineYio = new SelectionEngineYio();
        readMode = false;
        invalid = false;
    }


    public void enableReadMode() {
        readMode = true;
        removeIcon(RveIconType.delete);
    }


    @Override
    protected RveIcon getTouchedIcon(PointYio touchPoint) {
        if (readMode) return null;
        return super.getTouchedIcon(touchPoint);
    }


    public void setArrowUpMode(boolean arrowUpMode) {
        this.arrowUpMode = arrowUpMode;
        RveIcon executorIcon = getIcon(RveIconType.executor);
        if (executorIcon == null) return;
        if (arrowUpMode) {
            executorIcon.setTargetAngle(Math.PI / 2);
        } else {
            executorIcon.setTargetAngle(-Math.PI / 2);
        }
    }


    protected void onMove() {
        moveTitle();
        moveSelection();
    }


    @Override
    boolean onTouchDown(PointYio touchPoint) {
        boolean touchedIcon = super.onTouchDown(touchPoint);
        if (!touchedIcon) {
            selectionEngineYio.applySelection();
        }
        return touchedIcon;
    }


    @Override
    boolean onClick(PointYio touchPoint) {
        boolean clickedIcon = super.onClick(touchPoint);
        if (clickedIcon) return true;
        SoundManager.playSound(SoundType.button);
        if (readMode) {
            onClickedInReadMode();
        } else {
            applyClickReaction();
        }
        return true;
    }


    protected abstract void applyClickReaction();


    protected void onClickedInReadMode() {

    }


    private void moveSelection() {
        if (resizableViewElement.touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void moveTitle() {
        title.position.x = position.x + 0.05f * GraphicsYio.height + 0.03f * GraphicsYio.width;
        title.centerVertical(position);
        title.updateBounds();
    }


    @Override
    public boolean isInsideDynamicPosition() {
        if (title.position.x + title.width > position.x + position.width) return false;
        return super.isInsideDynamicPosition();
    }


    public void setTitle(String string) {
        title.setString(string);
        title.updateMetrics();
    }


    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }


    @Override
    protected float getHeight() {
        return 0.0625f * GraphicsYio.height;
    }


    public abstract AbstractRveRender getRender();


    @Override
    protected void applyIconReaction(RveIconType rveIconType) {
        switch (rveIconType) {
            default:
                break;
            case delete:
                resizableViewElement.removeItem(this);
                if (!doesRvElementHaveAdditionItem()) {
                    resizableViewElement.addItem(new RveAddConditionItem(), resizableViewElement.items.size() - 1);
                }
                break;
            case executor:
                setArrowUpMode(!arrowUpMode);
                break;
        }
    }


    private boolean doesRvElementHaveAdditionItem() {
        for (AbstractRveItem rveItem : resizableViewElement.items) {
            if (rveItem instanceof RveAddConditionItem) return true;
        }
        return false;
    }


    @Override
    protected RenderableTextYio getRenderableTextYio() {
        return title;
    }
}
