package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.AvatarType;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetRatingData;
import yio.tro.onliyoy.net.shared.NetRatingType;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.IconTextYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RatingListItem extends AbstractCustomListItem{

    public NetRatingType netRatingType;
    public String rawName;
    public long rawValue;
    public String clientId;
    public IconTextYio iconTextYio;
    public RenderableTextYio valueViewText;
    public AvatarType avatarType;


    @Override
    protected void initialize() {
        netRatingType = null;
        rawName = "-";
        rawValue = 0;
        clientId = "-";
        iconTextYio = new IconTextYio();
        iconTextYio.setFont(Fonts.miniFont);
        iconTextYio.setOffset(0.01f * GraphicsYio.width);
        valueViewText = new RenderableTextYio();
        valueViewText.setFont(Fonts.miniFont);
        avatarType = null;
    }


    @Override
    protected void move() {
        updateIconTextPosition();
        updateValueViewPosition();
        iconTextYio.move();
    }


    private void updateValueViewPosition() {
        valueViewText.centerVertical(viewPosition);
        valueViewText.position.x = viewPosition.x + viewPosition.width - 0.03f * GraphicsYio.width - valueViewText.width;
        valueViewText.updateBounds();
    }


    private void updateIconTextPosition() {
        iconTextYio.centerVertical(viewPosition);
        iconTextYio.position.x = viewPosition.x + 0.03f * GraphicsYio.width;
        iconTextYio.onPositionChanged();
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return 0.055f * GraphicsYio.height;
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    protected void onClicked() {
        Scenes.userDossierByRating.setId(clientId);
        Scenes.userDossierByRating.create();
    }


    @Override
    protected void onLongTapped() {

    }


    public void setNetRatingType(NetRatingType netRatingType) {
        this.netRatingType = netRatingType;
    }


    public void setBy(NetRatingData netRatingData) {
        clientId = netRatingData.clientId;
        rawName = netRatingData.name;
        rawValue = netRatingData.value;
        iconTextYio.setString(CharLocalizerYio.getInstance().apply(rawName));
        iconTextYio.setupByAvatarType(netRatingData.avatarType);
        iconTextYio.updateMetrics();
        avatarType = netRatingData.avatarType;
        valueViewText.setString(getActualValue());
        valueViewText.updateMetrics();
    }


    private String getActualValue() {
        switch (netRatingType) {
            default:
                System.out.println("RatingListItem.setBy: problem");
                return "-";
            case elp:
                return "" + rawValue;
            case money:
                return "$" + Yio.getCompactValueString((int) rawValue);
            case time:
                int frames = Yio.convertMillisIntoFrames(rawValue);
                return Yio.convertTimeToUnderstandableString(frames);
        }
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderRatingListItem;
    }
}
