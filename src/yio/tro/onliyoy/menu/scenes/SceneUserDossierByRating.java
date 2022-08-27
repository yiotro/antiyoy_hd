package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.elements.AdvancedLabelElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.gameplay.RankWorker;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.NetExperienceManager;
import yio.tro.onliyoy.net.NetRole;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetUserDossierData;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneUserDossierByRating extends ModalSceneYio {

    String id;
    AdvancedLabelElement advancedLabelElement;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.25);
        createReportButton();
        createAdvancedLabel();
    }


    private void createReportButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.45, 0.05)
                .alignRight(0.05)
                .alignBottom(0.02)
                .setBackground(BackgroundYio.gray)
                .applyText("do_report")
                .setReaction(getReportReaction());
    }


    private Reaction getReportReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onReportButtonPressed();
            }
        };
    }


    private void onReportButtonPressed() {
        destroy();
        Scenes.matchInfoPanel.destroy();
        Scenes.keyboard.create();
        Scenes.keyboard.setHint("do_report");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) return;
                netRoot.sendMessage(NmType.do_report_user, id + "#" + input);
                Scenes.notification.show("report_sent");
            }
        });
    }


    private void createAdvancedLabel() {
        advancedLabelElement = uiFactory.getAdvancedLabelElement()
                .setParent(defaultPanel)
                .setSize(0.92, 0.01)
                .centerHorizontal()
                .alignTop(0.06)
                .setFont(Fonts.miniFont)
                .applyText(" ");
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        netRoot.sendMessage(NmType.get_dossier_by_rating, id);
        advancedLabelElement.applyText("...");
    }


    public void onDossierReceived(NetUserDossierData netUserDossierData) {
        defaultPanel.setTitle(generateGeneralTitle(netUserDossierData));
        String matchesString = generateMatchesString(netUserDossierData);
        String elpString = generateElpString(netUserDossierData);
        String rankString = generateRankString(netUserDossierData);
        if (netUserDossierData.role == NetRole.guest) {
            matchesString = "";
            elpString = "";
            rankString = "";
        }
        advancedLabelElement.applyText(
                rankString + matchesString + elpString
        );
    }


    private String generateRankString(NetUserDossierData netUserDossierData) {
        String string = RankWorker.apply(netUserDossierData.rankType, netUserDossierData.elp);
        return Yio.getCapitalizedString(string) + "#";
    }


    private String generateElpString(NetUserDossierData netUserDossierData) {
        return "ELP: " + netUserDossierData.elp + "#";
    }


    private String generateMatchesString(NetUserDossierData netUserDossierData) {
        String matchesPrefix = languagesManager.getString("matches");
        int matchesWon = netUserDossierData.matchesWon;
        int matchesPlayed = netUserDossierData.matchesPlayed;
        return matchesPrefix + ": " + matchesWon + " / " + matchesPlayed + "#";
    }


    private String generateGeneralTitle(NetUserDossierData netUserDossierData) {
        String localizedName = CharLocalizerYio.getInstance().apply(netUserDossierData.name);
        String levelString = "";
        if (netUserDossierData.role != NetRole.guest) {
            int level = NetExperienceManager.convertExperienceToLevel(netUserDossierData.experience);
            levelString = ", lvl " + level;
        }
        return localizedName + levelString;
    }


    public void setId(String id) {
        this.id = id;
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
