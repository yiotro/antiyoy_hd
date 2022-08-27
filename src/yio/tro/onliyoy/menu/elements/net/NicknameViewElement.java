package yio.tro.onliyoy.menu.elements.net;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.resizable_element.HorizontalAlignType;
import yio.tro.onliyoy.menu.elements.resizable_element.VerticalAlignType;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.AvatarType;
import yio.tro.onliyoy.stuff.*;

public class NicknameViewElement extends InterfaceElement<NicknameViewElement> {

    public IconTextYio iconTextYio;
    HorizontalAlignType horizontalAlignType;
    VerticalAlignType verticalAlignType;
    Reaction reaction;
    public SelectionEngineYio selectionEngineYio;
    public RectangleYio touchPosition;
    boolean touchedCurrently;
    boolean readyToProcessClick;
    public AvatarType avatarType;
    public RectangleYio backgroundPosition;
    private float bExtValue;


    public NicknameViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        iconTextYio = new IconTextYio();
        iconTextYio.setFont(Fonts.miniFont);
        iconTextYio.setOffset(0.01f * GraphicsYio.width);
        setValues("-", AvatarType.empty);
        horizontalAlignType = HorizontalAlignType.center;
        verticalAlignType = VerticalAlignType.center;
        reaction = null;
        selectionEngineYio = new SelectionEngineYio();
        touchPosition = new RectangleYio();
        avatarType = null;
        backgroundPosition = new RectangleYio();
        bExtValue = 0.005f * GraphicsYio.width;
    }


    @Override
    protected NicknameViewElement getThis() {
        return this;
    }


    public void setValues(String string, AvatarType avatarType) {
        this.avatarType = avatarType;
        iconTextYio.setString(string);
        iconTextYio.updateMetrics();
        iconTextYio.setupByAvatarType(avatarType);
    }


    @Override
    public void onMove() {
        updateIconTextPosition();
        updateTouchPosition();
        updateBackgroundPosition();
        moveSelection();
        iconTextYio.move();
    }


    private void updateBackgroundPosition() {
        backgroundPosition.setBy(iconTextYio.bounds);
        backgroundPosition.increase(0.013f * GraphicsYio.width);
        backgroundPosition.width += bExtValue;
        if (avatarType == null || avatarType == AvatarType.empty) {
            backgroundPosition.x -= bExtValue;
            backgroundPosition.width += bExtValue;
        }
    }


    private void updateTouchPosition() {
        touchPosition.setBy(iconTextYio.bounds);
        touchPosition.increase(0.03f * GraphicsYio.width);
    }


    private void moveSelection() {
        if (touchedCurrently) return;
        selectionEngineYio.move();
    }


    private void updateIconTextPosition() {
        switch (horizontalAlignType) {
            default:
            case center:
                iconTextYio.centerHorizontal(viewPosition);
                break;
            case left:
                iconTextYio.position.x = viewPosition.x;
                break;
            case right:
                iconTextYio.position.x = viewPosition.x + viewPosition.width - iconTextYio.getWidth();
                break;
        }
        switch (verticalAlignType) {
            default:
            case center:
                iconTextYio.centerVertical(viewPosition);
                break;
            case bottom:
                iconTextYio.position.y = viewPosition.y + iconTextYio.renderableTextYio.height;
                break;
            case top:
                iconTextYio.position.y = viewPosition.y + viewPosition.height;
                break;
            case under:
                iconTextYio.position.y = viewPosition.y;
                break;
            case above:
                iconTextYio.position.y = viewPosition.y + viewPosition.height + iconTextYio.renderableTextYio.height;
                break;
        }
        iconTextYio.onPositionChanged();
    }


    @Override
    public void onDestroy() {
        touchedCurrently = false;
    }


    @Override
    public void onAppear() {
        touchedCurrently = false;
        readyToProcessClick = false;
        avatarType = getNetRoot().customizationData.avatarType;
    }


    @Override
    public boolean checkToPerformAction() {
        if (readyToProcessClick) {
            readyToProcessClick = false;
            reaction.perform(menuControllerYio);
            return true;
        }
        return false;
    }


    @Override
    public boolean touchDown() {
        if (reaction == null) return false;
        touchedCurrently = touchPosition.isPointInside(currentTouch, 0.04f * GraphicsYio.height);
        if (!touchedCurrently) return false;
        selectionEngineYio.applySelection();
        return true;
    }


    @Override
    public boolean touchDrag() {
        if (!touchedCurrently) return false;
        return true;
    }


    @Override
    public boolean touchUp() {
        if (reaction == null) return false;
        if (!touchedCurrently) return false;
        touchedCurrently = false;
        if (isClicked()) {
            onClick();
        }
        return true;
    }


    private void onClick() {
        SoundManager.playSound(SoundType.button);
        readyToProcessClick = true;
    }


    @Override
    public NicknameViewElement alignTop(double offset) {
        setVerticalAlignType(VerticalAlignType.top);
        return super.alignTop(offset);
    }


    @Override
    public NicknameViewElement alignAbove(InterfaceElement element, double offset) {
        setVerticalAlignType(VerticalAlignType.above);
        return super.alignAbove(element, offset);
    }


    @Override
    public NicknameViewElement alignBottom(double offset) {
        setVerticalAlignType(VerticalAlignType.bottom);
        return super.alignBottom(offset);
    }


    @Override
    public NicknameViewElement alignUnder(InterfaceElement element, double offset) {
        setVerticalAlignType(VerticalAlignType.under);
        return super.alignUnder(element, offset);
    }


    @Override
    public NicknameViewElement alignRight(double offset) {
        setHorizontalAlignType(HorizontalAlignType.right);
        return super.alignRight(offset);
    }


    @Override
    public NicknameViewElement alignRight(InterfaceElement element, double offset) {
        setHorizontalAlignType(HorizontalAlignType.right);
        return super.alignRight(element, offset);
    }


    @Override
    public NicknameViewElement alignLeft(double offset) {
        setHorizontalAlignType(HorizontalAlignType.left);
        return super.alignLeft(offset);
    }


    @Override
    public NicknameViewElement alignLeft(InterfaceElement element, double offset) {
        setHorizontalAlignType(HorizontalAlignType.left);
        return super.alignLeft(element, offset);
    }


    public NicknameViewElement setHorizontalAlignType(HorizontalAlignType horizontalAlignType) {
        this.horizontalAlignType = horizontalAlignType;
        return this;
    }


    public NicknameViewElement setVerticalAlignType(VerticalAlignType verticalAlignType) {
        this.verticalAlignType = verticalAlignType;
        return this;
    }


    public NicknameViewElement setReaction(Reaction reaction) {
        this.reaction = reaction;
        return this;
    }


    @Override
    public PointYio getTagPosition(String argument) {
        PointYio pointYio = new PointYio();
        RectangleYio bounds = iconTextYio.bounds;
        pointYio.set(
                bounds.x + bounds.width / 2,
                bounds.y + bounds.height / 2
        );
        return pointYio;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderNicknameViewElement;
    }
}
