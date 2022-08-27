package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.save_system.UserLevelsProgressManager;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRole;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetUlCacheData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class UserLevelListItem extends AbstractCustomListItem {

    public NetUlCacheData netUlCacheData;
    public RenderableTextYio name;
    public RenderableTextYio desc1;
    public RenderableTextYio desc2;
    public CircleYio beatIconPosition;
    public boolean completed;
    public BackgroundYio backgroundYio;
    public FactorYio alphaFactor;
    public RenderableTextYio ratingViewText;
    public RectangleYio ratingBackground;
    public RectangleYio ratingForeground;
    private float rh;
    private float defHeight;


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
        ratingBackground = new RectangleYio();
        rh = 0.003f * GraphicsYio.height;
        ratingForeground = new RectangleYio();
        defHeight = 0.12f * GraphicsYio.height;
    }


    public void setNetUlCacheData(NetUlCacheData netUlCacheData) {
        this.netUlCacheData = netUlCacheData;
        desc1.setString(generateDesc1());
        desc1.updateMetrics();
        desc2.setString(generateDesc2());
        desc2.updateMetrics();
        updateRatingString();
        updateCompletion();
    }


    private void updateName() {
        String src = netUlCacheData.name;
        String localized = CharLocalizerYio.getInstance().apply(src);
        name.setString(localized);
        name.updateMetrics();
        int c = 25;
        while (c > 0 && name.width > 0.85 * getWidth()) {
            c--;
            localized = localized.substring(0, localized.length() - 1);
            name.setString(localized);
            name.updateMetrics();
        }
    }


    private void updateRatingString() {
        int difference = netUlCacheData.likes - netUlCacheData.dislikes;
        String string = Yio.getCompactValueString(difference);
        if (difference > 0) {
            string = "+" + string;
        }
        ratingViewText.setString(string);
        ratingViewText.updateMetrics();
    }


    private String generateDesc1() {
        switch (netUlCacheData.rulesType) {
            default:
                String key = netUlCacheData.rulesType + "_rules";
                String string = LanguagesManager.getInstance().getString(key);
                return string.toLowerCase();
            case def:
                return "";
        }
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
        float lineOffset = 3.5f * rh;

        ratingViewText.position.x = viewPosition.x + viewPosition.width - rightOffset - ratingViewText.width;
        ratingViewText.position.y = name.position.y - name.height / 2 + ratingViewText.height / 2;
        ratingViewText.updateBounds();
        if (netUlCacheData.likes + netUlCacheData.dislikes == 0) return;

        ratingBackground.height = rh;
        ratingBackground.width = ratingViewText.width + 2 * GraphicsYio.borderThickness;
        ratingBackground.x = ratingViewText.bounds.x + ratingViewText.bounds.width / 2 - ratingBackground.width / 2;
        ratingBackground.y = ratingViewText.position.y - ratingViewText.height - lineOffset;

        float ratio = (float) netUlCacheData.likes / (netUlCacheData.likes + netUlCacheData.dislikes);
        ratingForeground.setBy(ratingBackground);
        ratingForeground.width *= ratio;
    }


    private void updateBeatIconPosition() {
        if (!completed) return;
        beatIconPosition.center.x = viewPosition.x + 0.02f * GraphicsYio.width + beatIconPosition.radius;
        beatIconPosition.center.y = name.position.y - name.height / 2;
    }


    private void updateDesc2Position() {
        desc2.position.x = desc1.position.x;
        desc2.position.y = desc1.position.y - desc1.height - 0.01f * GraphicsYio.height;
        if (desc1.string.length() == 0) {
            desc2.position.y = desc1.position.y;
        }
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
        updateName();
        BackgroundYio previousBackground = null;
        UserLevelListItem lastUlItem = getLastUlItem();
        if (lastUlItem != null) {
            previousBackground = lastUlItem.backgroundYio;
        }
        backgroundYio = ScrollListItem.getNextBackground(previousBackground);
    }


    private UserLevelListItem getLastUlItem() {
        UserLevelListItem userLevelListItem = null;
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!(item instanceof UserLevelListItem)) continue;
            if (item == this) continue;
            userLevelListItem = (UserLevelListItem) item;
        }
        return userLevelListItem;
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return defHeight;
    }


    @Override
    protected void onPositionChanged() {

    }


    public void applyCompactMode() {
        backgroundYio = null;
        defHeight = 0.09f * GraphicsYio.height;
        desc1.setString("");
        desc1.updateMetrics();
    }


    @Override
    protected void onClicked() {
        NetRoot netRoot = customizableListYio.getNetRoot();
        netRoot.sendMessage(NmType.request_user_level_to_play, "" + netUlCacheData.id);
        Scenes.waitToPlayUserLevel.create();
    }


    @Override
    protected void onLongTapped() {
        final NetRoot netRoot = customizableListYio.getNetRoot();
        NetRole role = netRoot.userData.role;
        if (role != NetRole.admin) return;
        Scenes.keyboard.create();
        Scenes.keyboard.setHint("level_name");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) return;
                netRoot.sendMessage(NmType.rename_user_level, input + "/" + netUlCacheData.id);
            }
        });
    }


    public void updateCompletion() {
        UserLevelsProgressManager instance = UserLevelsProgressManager.getInstance();
        completed = instance.isCompleted(netUlCacheData.id);
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderUserLevelListItem;
    }
}
