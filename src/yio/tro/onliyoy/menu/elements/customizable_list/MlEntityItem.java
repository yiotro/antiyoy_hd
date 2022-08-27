package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.AvatarType;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetMlpData;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.IconTextYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class MlEntityItem extends AbstractCustomListItem{

    public String clientId;
    public FactorYio fadeInFactor;
    public HColor color;
    private float cOffset;
    public RectangleYio incBounds;
    public RectangleYio selectionPosition;
    public HColor previousColor;
    public FactorYio colorFactor;
    boolean calmAnimationMode;
    boolean chooseColorAllowed;
    boolean clickAllowed;
    private String matchId;
    public FactorYio messageFactor;
    public RenderableTextYio messageViewText;
    public RectangleYio messageBounds;
    public long messageEndTime;
    public boolean darken;
    public float darkValue;
    public IconTextYio iconTextYio;
    float deltaX;
    public AvatarType avatarType;


    public MlEntityItem() {
        clientId = "";
        darken = false;
        darkValue = 0.04f;
        fadeInFactor = new FactorYio();
        fadeInFactor.appear(MovementType.inertia, 1.1);
        cOffset = 0.015f * GraphicsYio.width;
        color = null;
        incBounds = new RectangleYio();
        selectionPosition = new RectangleYio();
        previousColor = null;
        colorFactor = new FactorYio();
        calmAnimationMode = false;
        chooseColorAllowed = false;
        clickAllowed = true;
        matchId = "-";
        iconTextYio = new IconTextYio();
        iconTextYio.setFont(Fonts.miniFont);
        iconTextYio.setOffset(0.01f * GraphicsYio.width);
        messageFactor = new FactorYio();
        messageViewText = new RenderableTextYio();
        messageViewText.setFont(Fonts.miniFont);
        messageBounds = new RectangleYio();
        messageEndTime = 0;
        deltaX = 0;
        avatarType = AvatarType.empty;
    }


    @Override
    protected void initialize() {

    }


    @Override
    protected double getHeight() {
        return 0.045f * GraphicsYio.height;
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected void move() {
        updateIconTextPosition();
        fadeInFactor.move();
        colorFactor.move();
        updateIncBounds();
        updateSelectionPosition();
        messageFactor.move();
        updateMessagePosition();
        updateMessageBounds();
        checkToRemoveMessage();
        iconTextYio.move();
    }


    private void checkToRemoveMessage() {
        if (messageFactor.getValue() == 0) return;
        if (messageFactor.isInDestroyState()) return;
        if (System.currentTimeMillis() < messageEndTime) return;
        messageFactor.destroy(MovementType.lighty, 3.5);
    }


    private void updateMessageBounds() {
        messageBounds.setBy(messageViewText.bounds);
        messageBounds.increase(cOffset);
    }


    private void updateMessagePosition() {
        if (messageFactor.getValue() == 0) return;
        messageViewText.position.x = viewPosition.x + 0.025f * GraphicsYio.width + cOffset;
        messageViewText.centerVertical(viewPosition);
        if (fadeInFactor.getValue() < 1 && !calmAnimationMode) {
            messageViewText.position.x -= (1 - fadeInFactor.getValue()) * (messageViewText.delta.x + messageViewText.width + 0.02f * GraphicsYio.width);
        }
        messageViewText.updateBounds();
    }


    private void updateIconTextPosition() {
        iconTextYio.position.x = viewPosition.x + deltaX;
        iconTextYio.centerVertical(viewPosition);
        if (fadeInFactor.getValue() < 1 && !calmAnimationMode) {
            iconTextYio.position.x -= (1 - fadeInFactor.getValue()) * (deltaX + iconTextYio.getWidth() + 0.02f * GraphicsYio.width);
        }
        iconTextYio.onPositionChanged();
    }


    private void updateSelectionPosition() {
        if (messageFactor.getValue() > 0) {
            selectionPosition.setBy(messageBounds);
            selectionPosition.increase(cOffset);
            return;
        }
        selectionPosition.setBy(incBounds);
        selectionPosition.increase(cOffset);
    }


    private void updateIncBounds() {
        if (!iconTextYio.iconEnabled) {
            incBounds.setBy(iconTextYio.renderableTextYio.bounds);
            incBounds.increase(cOffset);
            return;
        }
        incBounds.setBy(iconTextYio.bounds);
        incBounds.increase(cOffset);
        RenderableTextYio renderableTextYio = iconTextYio.renderableTextYio;
        incBounds.height = renderableTextYio.height + 2 * cOffset;
        incBounds.y = renderableTextYio.position.y - renderableTextYio.height - cOffset;
    }


    public void showMessage(String value) {
        messageViewText.setString(value);
        messageViewText.updateMetrics();
        messageFactor.reset();
        messageFactor.appear(MovementType.approach, 2.5);
        messageEndTime = System.currentTimeMillis() + 5000;
    }


    @Override
    protected void onPositionChanged() {
        deltaX = 0.025f * GraphicsYio.width + cOffset;
    }


    @Override
    protected void onClicked() {
        if (!clickAllowed) return;
        NetRoot netRoot = customizableListYio.getNetRoot();
        if (chooseColorAllowed && clientId.equals(netRoot.userData.id)) {
            Scenes.chooseColor.create();
            Scenes.chooseColor.loadValues(color);
            Scenes.chooseColor.setListener(Scenes.matchLobby);
            return;
        }
        Scenes.mlUserInfo.setId(clientId);
        Scenes.mlUserInfo.create();
        Scenes.mlUserInfo.setMatchId(matchId);
    }


    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public void setByNetMlpData(NetMlpData netMlpData) {
        setClientId(netMlpData.clientId);
        setColor(netMlpData.color);
        setValues(netMlpData.name, netMlpData.avatarType);
    }


    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }


    public void forceAppearance() {
        fadeInFactor.setValue(1);
        colorFactor.setValue(1);
    }


    public void setValues(String string, AvatarType avatarType) {
        this.avatarType = avatarType;
        String localizedString = CharLocalizerYio.getInstance().apply(string);
        iconTextYio.setString(localizedString);
        iconTextYio.updateMetrics();
        iconTextYio.setupByAvatarType(avatarType);
    }


    @Override
    public String getKey() {
        return clientId;
    }


    public void setColor(HColor color) {
        if (this.color == color) return;
        previousColor = this.color;
        this.color = color;
        onColorChanged();
    }


    public void setCalmAnimationMode(boolean calmAnimationMode) {
        this.calmAnimationMode = calmAnimationMode;
    }


    public void setChooseColorAllowed(boolean chooseColorAllowed) {
        this.chooseColorAllowed = chooseColorAllowed;
    }


    public void setClickAllowed(boolean clickAllowed) {
        this.clickAllowed = clickAllowed;
    }


    @Override
    protected void onLongTapped() {

    }


    private void onColorChanged() {
        colorFactor.reset();
        colorFactor.appear(MovementType.inertia, 1);
    }


    public void setDarken(boolean darken) {
        this.darken = darken;
    }


    public void setDarkValue(float darkValue) {
        this.darkValue = darkValue;
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderMlEntityItem;
    }
}
