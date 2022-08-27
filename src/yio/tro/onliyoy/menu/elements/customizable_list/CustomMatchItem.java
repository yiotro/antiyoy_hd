package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.NetMatchListData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.SelfScrollWorkerYio;

public class CustomMatchItem extends AbstractCustomListItem{

    public NetMatchListData netMatchListData;
    public RenderableTextYio name;
    public RenderableTextYio desc1;
    public RenderableTextYio desc2;
    public CircleYio passwordPosition;
    public RenderableTextYio quantityViewText;
    public SelfScrollWorkerYio selfScrollWorkerYio;


    @Override
    protected void initialize() {
        name = new RenderableTextYio();
        name.setFont(Fonts.gameFont);
        desc1 = new RenderableTextYio();
        desc1.setFont(Fonts.miniFont);
        desc2 = new RenderableTextYio();
        desc2.setFont(Fonts.miniFont);
        passwordPosition = new CircleYio();
        passwordPosition.setRadius(0.017f * GraphicsYio.height);
        quantityViewText = new RenderableTextYio();
        quantityViewText.setFont(Fonts.miniFont);
        selfScrollWorkerYio = new SelfScrollWorkerYio();
    }


    public void setNetCustomMatchData(NetMatchListData netMatchListData) {
        this.netMatchListData = netMatchListData;
        name.setString(netMatchListData.name);
        name.updateMetrics();
        desc1.setString(generateDesc1());
        desc1.updateMetrics();
        desc2.setString(generateDesc2());
        desc2.updateMetrics();
        quantityViewText.setString(generateQuantityString());
        quantityViewText.updateMetrics();
        selfScrollWorkerYio.launch(desc1.width, getWidth() - 0.05f * GraphicsYio.width);
    }


    private String generateQuantityString() {
        if (netMatchListData.hasPassword) return "";
        return netMatchListData.currentQuantity + "/" + netMatchListData.maxHumanQuantity;
    }


    private String generateDesc1() {
        if (netMatchListData.hasPassword) {
            return LanguagesManager.getInstance().getString("secret_information");
        }
        String sizeString = LanguagesManager.getInstance().getString("" + netMatchListData.levelSize);
        String turnDurationString = Yio.convertTimeToTurnDurationString(60 * netMatchListData.turnSeconds);
        String diplomacyKey = "without_diplomacy";
        if (netMatchListData.diplomacy) {
            diplomacyKey = "with_diplomacy";
        }
        String diplomacyString = LanguagesManager.getInstance().getString(diplomacyKey).toLowerCase();
        return sizeString.toLowerCase() + ", " + turnDurationString + ", " + diplomacyString;
    }


    private String generateDesc2() {
        if (netMatchListData.hasPassword) return "";
        String rulesKey;
        switch (netMatchListData.rulesType) {
            default:
                rulesKey = netMatchListData.rulesType + "_rules";
                break;
            case def:
                rulesKey = "normal_rules";
                break;
        }
        String rulesString = LanguagesManager.getInstance().getString(rulesKey).toLowerCase();
        String fogString = "";
        if (netMatchListData.fog) {
            fogString = ", " + LanguagesManager.getInstance().getString("fog").toLowerCase();
        }
        return rulesString + fogString;
    }


    @Override
    protected void move() {
        selfScrollWorkerYio.move();
        updateNamePosition();
        updateDesc1Position();
        updateDesc2Position();
        updatePasswordPosition();
        updateQuantityViewTextPosition();
    }


    private void updateQuantityViewTextPosition() {
        quantityViewText.position.x = viewPosition.x + viewPosition.width - 0.02f * GraphicsYio.width - quantityViewText.width;
        quantityViewText.position.y = name.position.y - name.height / 2 + quantityViewText.height / 2;
        quantityViewText.updateBounds();
    }


    private void updatePasswordPosition() {
        passwordPosition.center.x = viewPosition.x + viewPosition.width - 0.03f * GraphicsYio.width - passwordPosition.radius;
        passwordPosition.center.y = viewPosition.y + viewPosition.height / 2;
    }


    private void updateDesc2Position() {
        desc2.position.x = name.position.x;
        desc2.position.y = desc1.position.y - desc1.height - 0.01f * GraphicsYio.height;
        desc2.updateBounds();
    }


    private void updateDesc1Position() {
        desc1.position.x = name.position.x + selfScrollWorkerYio.getDelta();
        desc1.position.y = name.position.y - name.height - 0.01f * GraphicsYio.height;
        desc1.updateBounds();
    }


    private void updateNamePosition() {
        name.position.x = viewPosition.x + 0.02f * GraphicsYio.width;
        name.position.y = viewPosition.y + viewPosition.height - 0.022f * GraphicsYio.height;
        name.updateBounds();
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
        if (netMatchListData.hasPassword) {
            Scenes.keyboard.create();
            Scenes.keyboard.setHint("password");
            Scenes.keyboard.setReaction(new AbstractKbReaction() {
                @Override
                public void onInputFromKeyboardReceived(String input) {
                    applyJoin(input);
                }
            });
            return;
        }
        applyJoin("");
    }


    private void applyJoin(String password) {
        if (password.length() == 0) {
            password = "-";
        }
        NetRoot netRoot = customizableListYio.getNetRoot();
        netRoot.sendMessage(NmType.request_join_custom_match, netMatchListData.matchId + "/" + password);
        Scenes.waitMatchJoining.create();
    }


    @Override
    protected void onLongTapped() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderCustomMatchItem;
    }
}
