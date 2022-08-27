package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRoot;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetMatchSpectateData;
import yio.tro.onliyoy.net.shared.NmType;
import yio.tro.onliyoy.stuff.CircleYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;
import yio.tro.onliyoy.stuff.SelfScrollWorkerYio;

import java.util.ArrayList;

public class SpectateMatchItem extends AbstractCustomListItem {

    public NetMatchSpectateData netMatchSpectateData;
    public RenderableTextYio name;
    public RenderableTextYio desc1;
    public RenderableTextYio desc2;
    public CircleYio passwordPosition;
    public SelfScrollWorkerYio selfScroll1;
    public SelfScrollWorkerYio selfScroll2;


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
        selfScroll1 = new SelfScrollWorkerYio();
        selfScroll2 = new SelfScrollWorkerYio();
    }


    public void setNetMatchSpectateData(NetMatchSpectateData netMatchSpectateData) {
        this.netMatchSpectateData = netMatchSpectateData;
        name.setString(CharLocalizerYio.getInstance().apply(netMatchSpectateData.name));
        name.updateMetrics();
        desc1.setString(generateDesc1());
        desc1.updateMetrics();
        desc2.setString(generateDesc2());
        desc2.updateMetrics();
        selfScroll1.launch(desc1.width, getWidth() - 0.05f * GraphicsYio.width);
        selfScroll2.launch(desc2.width, getWidth() - 0.05f * GraphicsYio.width);
    }


    private String generateDesc1() {
        if (netMatchSpectateData.hasPassword) {
            return LanguagesManager.getInstance().getString("secret_information");
        }
        String sizeString = LanguagesManager.getInstance().getString("" + netMatchSpectateData.levelSize);
        String rulesKey;
        switch (netMatchSpectateData.rulesType) {
            default:
                rulesKey = netMatchSpectateData.rulesType + "_rules";
                break;
            case def:
                rulesKey = "normal_rules";
                break;
        }
        String diplomacyKey = "without_diplomacy";
        if (netMatchSpectateData.diplomacy) {
            diplomacyKey = "with_diplomacy";
        }
        String diplomacyString = LanguagesManager.getInstance().getString(diplomacyKey).toLowerCase();
        String rulesString = LanguagesManager.getInstance().getString(rulesKey).toLowerCase();
        return sizeString.toLowerCase() + ", " + rulesString + ", " + diplomacyString;
    }


    private String generateDesc2() {
        if (netMatchSpectateData.hasPassword) return "";
        StringBuilder builder = new StringBuilder();
        ArrayList<String> participants = netMatchSpectateData.participants;
        for (int i = 0; i < participants.size(); i++) {
            builder.append(CharLocalizerYio.getInstance().apply(participants.get(i)));
            if (i < participants.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }


    @Override
    protected void move() {
        selfScroll1.move();
        selfScroll2.move();
        updateNamePosition();
        updateDesc1Position();
        updateDesc2Position();
        updatePasswordPosition();
    }


    private void updatePasswordPosition() {
        passwordPosition.center.x = viewPosition.x + viewPosition.width - 0.03f * GraphicsYio.width - passwordPosition.radius;
        passwordPosition.center.y = viewPosition.y + viewPosition.height / 2;
    }


    private void updateDesc2Position() {
        desc2.position.x = name.position.x + selfScroll2.getDelta();
        desc2.position.y = desc1.position.y - desc1.height - 0.01f * GraphicsYio.height;
        desc2.updateBounds();
    }


    private void updateDesc1Position() {
        desc1.position.x = name.position.x + selfScroll1.getDelta();
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
        if (netMatchSpectateData.hasPassword) {
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
        netRoot.sendMessage(NmType.request_spectate, netMatchSpectateData.matchId + "/" + password);
        Scenes.waitMatchJoining.create();
    }


    @Override
    protected void onLongTapped() {

    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderSpectateMatchItem;
    }
}
