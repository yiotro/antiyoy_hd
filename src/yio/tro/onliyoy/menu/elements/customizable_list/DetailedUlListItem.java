package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.save_system.UserLevelsProgressManager;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetUlCacheData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class DetailedUlListItem extends AbstractCustomListItem {

    public NetUlCacheData netUlCacheData;
    public RenderableTextYio name;
    public RenderableTextYio desc1;
    public RenderableTextYio desc2;
    public CircleYio beatIconPosition;
    public boolean completed;
    public BackgroundYio backgroundYio;
    public FactorYio alphaFactor;
    public RenderableTextYio ratingViewText;


    @Override
    protected void initialize() {
        name = new RenderableTextYio();
        name.setFont(Fonts.gameFont);
        desc1 = new RenderableTextYio();
        desc1.setFont(Fonts.miniFont);
        desc2 = new RenderableTextYio();
        desc2.setFont(Fonts.miniFont);
        beatIconPosition = new CircleYio();
        beatIconPosition.setRadius(0.015f * GraphicsYio.height);
        backgroundYio = null;
        alphaFactor = new FactorYio();
        alphaFactor.appear(MovementType.approach, 2.5);
        completed = false;
        ratingViewText = new RenderableTextYio();
        ratingViewText.setFont(Fonts.miniFont);
    }


    public void setNetUlCacheData(NetUlCacheData netUlCacheData) {
        this.netUlCacheData = netUlCacheData;
        name.setString(CharLocalizerYio.getInstance().apply(netUlCacheData.name));
        name.updateMetrics();
        desc1.setString(generateDesc1());
        desc1.updateMetrics();
        desc2.setString(generateDesc2());
        desc2.updateMetrics();
        updateRatingString();
        updateCompletion();
    }


    private void updateRatingString() {
        String likesString = "+" + Yio.getCompactValueString(netUlCacheData.likes);
        String dislikesString = "-" + Yio.getCompactValueString(netUlCacheData.dislikes);
        ratingViewText.setString(likesString + "   " + dislikesString);
        ratingViewText.updateMetrics();
    }


    private String generateDesc1() {
        String rulesKey = "";
        switch (netUlCacheData.rulesType) {
            default:
                rulesKey = netUlCacheData.rulesType + "_rules";
                break;
            case def:
                rulesKey = "normal_rules";
                break;
        }
        String rulesString = LanguagesManager.getInstance().getString(rulesKey).toLowerCase();
        String diplomacyKey = "without_diplomacy";
        if (netUlCacheData.diplomacy) {
            diplomacyKey = "with_diplomacy";
        }
        String diplomacyString = LanguagesManager.getInstance().getString(diplomacyKey).toLowerCase();
        return rulesString + ", " + diplomacyString;
    }


    private String generateDesc2() {
        String sizeString = LanguagesManager.getInstance().getString("" + netUlCacheData.levelSize);
        return sizeString.toLowerCase() + ", x" + netUlCacheData.colorsQuantity;
    }


    @Override
    protected void move() {
        updateNamePosition();
        updateBeatIconPosition();
        updateDesc1Position();
        updateDesc2Position();
        moveRating();
        alphaFactor.move();
    }


    private void moveRating() {
        float rightOffset = 0.03f * GraphicsYio.width;
        ratingViewText.position.x = viewPosition.x + viewPosition.width - rightOffset - ratingViewText.width;
        ratingViewText.position.y = desc2.position.y - desc2.height / 2 + ratingViewText.height / 2;
        ratingViewText.updateBounds();
    }


    private void updateBeatIconPosition() {
        if (!completed) return;
        beatIconPosition.center.x = viewPosition.x + 0.02f * GraphicsYio.width + beatIconPosition.radius;
        beatIconPosition.center.y = name.position.y - name.height / 2;
    }


    private void updateDesc2Position() {
        desc2.position.x = desc1.position.x;
        desc2.position.y = desc1.position.y - desc1.height - 0.01f * GraphicsYio.height;
        desc2.updateBounds();
    }


    private void updateDesc1Position() {
        desc1.position.x = viewPosition.x + 0.02f * GraphicsYio.width;
        desc1.position.y = name.position.y - name.height - 0.01f * GraphicsYio.height;
        desc1.updateBounds();
    }


    private void updateNamePosition() {
        name.position.x = viewPosition.x + 0.02f * GraphicsYio.width;
        name.position.y = viewPosition.y + viewPosition.height - 0.022f * GraphicsYio.height;
        if (completed) {
            name.position.x = beatIconPosition.center.x + beatIconPosition.radius + 0.01f * GraphicsYio.width;
        }
        name.updateBounds();
    }


    @Override
    protected void onAddedToList() {
        super.onAddedToList();
        BackgroundYio previousBackground = null;
        DetailedUlListItem lastUlItem = getLastDulItem();
        if (lastUlItem != null) {
            previousBackground = lastUlItem.backgroundYio;
        }
        backgroundYio = ScrollListItem.getNextBackground(previousBackground);
    }


    private DetailedUlListItem getLastDulItem() {
        DetailedUlListItem detailedUlListItem = null;
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!(item instanceof DetailedUlListItem)) continue;
            if (item == this) continue;
            detailedUlListItem = (DetailedUlListItem) item;
        }
        return detailedUlListItem;
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return 0.12f * GraphicsYio.height;
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    protected void onClicked() {
        NetRoot netRoot = customizableListYio.getNetRoot();
        netRoot.sendMessage(NmType.request_user_level_to_play, "" + netUlCacheData.id);
        Scenes.waitToPlayUserLevel.create();
    }


    @Override
    protected void onLongTapped() {

    }


    public void updateCompletion() {
        UserLevelsProgressManager instance = UserLevelsProgressManager.getInstance();
        completed = instance.isCompleted(netUlCacheData.id);
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderDetailedUlListItem;
    }
}
