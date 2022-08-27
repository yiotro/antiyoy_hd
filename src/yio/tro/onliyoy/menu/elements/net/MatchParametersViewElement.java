package yio.tro.onliyoy.menu.elements.net;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.net.shared.NetMatchLobbyData;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class MatchParametersViewElement extends InterfaceElement<MatchParametersViewElement> {

    NetMatchLobbyData netMatchLobbyData;
    public RenderableTextYio title;
    public RenderableTextYio desc1;
    public RenderableTextYio desc2;


    public MatchParametersViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        initTitle();
        initDesc1();
        initDesc2();
    }


    private void initDesc2() {
        desc2 = new RenderableTextYio();
        desc2.setFont(Fonts.miniFont);
        desc2.setString("-");
        desc2.updateMetrics();
    }


    private void initDesc1() {
        desc1 = new RenderableTextYio();
        desc1.setFont(Fonts.miniFont);
        desc1.setString("-");
        desc1.updateMetrics();
    }


    private void initTitle() {
        title = new RenderableTextYio();
        title.setFont(Fonts.gameFont);
        title.setString("-");
        title.updateMetrics();
    }


    @Override
    protected MatchParametersViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        updateTitlePosition();
        moveDesc1();
        moveDesc2();
    }


    private void moveDesc2() {
        desc2.position.x = viewPosition.x;
        desc2.position.y = desc1.position.y - desc1.height - 0.01f * GraphicsYio.height;
        desc2.updateBounds();
    }


    private void moveDesc1() {
        desc1.position.x = viewPosition.x;
        desc1.position.y = title.position.y - title.height - 0.015f * GraphicsYio.height;
        desc1.updateBounds();
    }


    private void updateTitlePosition() {
        title.centerHorizontal(viewPosition);
        title.position.y = viewPosition.y + viewPosition.height;
        title.updateBounds();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {

    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    private String generateDesc1() {
        String sizeString = LanguagesManager.getInstance().getString("" + netMatchLobbyData.levelSize) + ", ";
        if (isQuick()) {
            sizeString = "";
        }
        int quantity = netMatchLobbyData.entities.size();
        String turnDurationString = Yio.convertTimeToTurnDurationString(60 * netMatchLobbyData.turnSeconds);
        String customMapString = "";
        if (!isQuick() && netMatchLobbyData.levelCode.length() > 5) {
            customMapString = ", " + LanguagesManager.getInstance().getString("custom_map").toLowerCase();
        }
        return sizeString.toLowerCase() + "x" + quantity + ", " + turnDurationString + customMapString;
    }


    private String generateDesc2() {
        String rulesKey;
        switch (netMatchLobbyData.rulesType) {
            default:
                rulesKey = netMatchLobbyData.rulesType + "_rules";
                break;
            case def:
                rulesKey = "normal_rules";
                break;
        }
        String diplomacyKey = "without_diplomacy";
        if (netMatchLobbyData.diplomacy) {
            diplomacyKey = "with_diplomacy";
        }
        String rulesString = LanguagesManager.getInstance().getString(rulesKey).toLowerCase();
        String diplomacyString = ", " + LanguagesManager.getInstance().getString(diplomacyKey).toLowerCase();
        if (isQuick()) {
            diplomacyString = "";
        }
        String fogString = "";
        if (netMatchLobbyData.fog) {
            fogString = ", " + LanguagesManager.getInstance().getString("fog").toLowerCase();
        }
        return rulesString + diplomacyString + fogString;
    }


    private boolean isQuick() {
        return !netMatchLobbyData.hasCreator;
    }


    public void setNetMatchParameters(NetMatchLobbyData netMatchLobbyData) {
        this.netMatchLobbyData = netMatchLobbyData;
        desc1.setString(generateDesc1());
        desc1.updateMetrics();
        updateDesc2();
        updateTitle();
    }


    private void updateDesc2() {
        String string = generateDesc2();
        int c = 50;
        while (c > 0) {
            c--;
            desc2.setString(string);
            desc2.updateMetrics();
            if (desc2.width < getPosition().width - 0.015f * GraphicsYio.height) break;
            if (string.length() == 0) break;
            string = string.substring(0, string.length() - 1);
        }
    }


    private void updateTitle() {
        String key = "match";
        if (isQuick()) {
            key = "quick_match";
        }
        title.setString(LanguagesManager.getInstance().getString(key));
        title.updateMetrics();
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderMatchParametersViewElement;
    }
}
