package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.game.save_system.UserLevelsProgressManager;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetUserLevelData;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class OfflineUlevItem extends AbstractCustomListItem{

    NetUserLevelData netUserLevelData;
    public RenderableTextYio name;
    public RenderableTextYio description;
    public CircleYio beatIconPosition;
    public boolean completed;
    public BackgroundYio backgroundYio;


    @Override
    protected void initialize() {
        name = new RenderableTextYio();
        name.setFont(Fonts.gameFont);
        description = new RenderableTextYio();
        description.setFont(Fonts.miniFont);
        beatIconPosition = new CircleYio();
        beatIconPosition.setRadius(0.015f * GraphicsYio.height);
        backgroundYio = null;
        completed = false;
    }


    public void setNetUserLevelData(NetUserLevelData netUserLevelData) {
        this.netUserLevelData = netUserLevelData;
        description.setString(generateDescription());
        description.updateMetrics();
        updateCompletion();
    }


    private void updateName() {
        String src = netUserLevelData.name;
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


    private String generateDescription() {
        String sizeString = LanguagesManager.getInstance().getString("" + netUserLevelData.levelSize);
        return sizeString.toLowerCase() + ", x" + netUserLevelData.colorsQuantity;
    }


    @Override
    protected void move() {
        updateNamePosition();
        updateBeatIconPosition();
        updateDescriptionPosition();
    }


    private void updateBeatIconPosition() {
        if (!completed) return;
        beatIconPosition.center.x = viewPosition.x + 0.02f * GraphicsYio.width + beatIconPosition.radius;
        beatIconPosition.center.y = name.position.y - name.height / 2;
    }


    private void updateDescriptionPosition() {
        description.position.x = viewPosition.x + 0.02f * GraphicsYio.width;
        description.position.y = name.position.y - name.height - 0.01f * GraphicsYio.height;
        description.updateBounds();
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
        OfflineUlevItem lastUlItem = getLastUlItem();
        if (lastUlItem != null) {
            previousBackground = lastUlItem.backgroundYio;
        }
        backgroundYio = ScrollListItem.getNextBackground(previousBackground);
    }


    private OfflineUlevItem getLastUlItem() {
        OfflineUlevItem userLevelListItem = null;
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!(item instanceof OfflineUlevItem)) continue;
            if (item == this) continue;
            userLevelListItem = (OfflineUlevItem) item;
        }
        return userLevelListItem;
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return 0.09f * GraphicsYio.height;
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    protected void onClicked() {
        YioGdxGame yioGdxGame = getYioGdxGame();
        yioGdxGame.netRoot.tempUlTransferData.id = netUserLevelData.id;
        (new IwClientInit(yioGdxGame, LoadingType.user_level)).perform(netUserLevelData.levelCode);
    }


    @Override
    protected void onLongTapped() {

    }


    public void updateCompletion() {
        UserLevelsProgressManager instance = UserLevelsProgressManager.getInstance();
        completed = instance.isCompleted(netUserLevelData.id);
    }


    private YioGdxGame getYioGdxGame() {
        return customizableListYio.menuControllerYio.yioGdxGame;
    }


    public void setBackgroundYio(BackgroundYio backgroundYio) {
        this.backgroundYio = backgroundYio;
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderOfflineUlevItem;
    }
}
