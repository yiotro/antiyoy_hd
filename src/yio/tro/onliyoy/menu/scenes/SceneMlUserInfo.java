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
import yio.tro.onliyoy.net.shared.RankType;

public class SceneMlUserInfo extends ModalSceneYio{

    String matchId;
    String id;
    AdvancedLabelElement advancedLabelElement;


    @Override
    protected void initialize() {
        createCloseButton();
        createDefaultPanel(0.3);
        createKickButton();
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


    private void createKickButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.4, 0.05)
                .alignLeft(0.05)
                .alignBottom(0.02)
                .setBackground(BackgroundYio.gray)
                .applyText("kick")
                .setAllowedToAppear(getCaptainCondition())
                .setReaction(getKickReaction());
    }


    private Reaction getKickReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                netRoot.sendMessage(NmType.request_kick_from_match, matchId + "/" + id);
            }
        };
    }


    private ConditionYio getCaptainCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                if (Scenes.matchLobby.isCurrentlyVisible()) {
                    return Scenes.matchLobby.isInCaptainMode();
                }
                if (netRoot.currentMatchData == null) return false;
                if (matchId.equals("-")) return false;
                return netRoot.currentMatchData.getCreatorId().equals(netRoot.userData.id);
            }
        };
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        netRoot.sendMessage(NmType.get_user_dossier, id);
        advancedLabelElement.applyText("...");
        matchId = "-";
    }


    public void onDossierReceived(NetUserDossierData netUserDossierData) {
        defaultPanel.setTitle(generateGeneralTitle(netUserDossierData));
        if (netUserDossierData.hidden && !netRoot.isSpectatorCurrently()) {
            advancedLabelElement.applyText(languagesManager.getString("secret_information"));
            return;
        }
        String roleString = getRoleString(netUserDossierData);
        String timePrefix = languagesManager.getString("time");
        int timeFrames = Yio.convertMillisIntoFrames(netUserDossierData.timeOnline);
        String timeString = timePrefix + ": " + Yio.convertTimeToUnderstandableString(timeFrames) + "#";
        String matchesPrefix = languagesManager.getString("matches");
        int matchesWon = netUserDossierData.matchesWon;
        int matchesPlayed = netUserDossierData.matchesPlayed;
        String matchesString = matchesPrefix + ": " + matchesWon + " / " + matchesPlayed + "#";
        String elpString = "ELP: " + netUserDossierData.elp + "#";
        if (netUserDossierData.role == NetRole.guest) {
            matchesString = "";
            elpString = "";
        }
        advancedLabelElement.applyText(
                roleString + timeString + matchesString + elpString
        );
    }


    private String generateGeneralTitle(NetUserDossierData netUserDossierData) {
        String localizedName = CharLocalizerYio.getInstance().apply(netUserDossierData.name);
        if (netUserDossierData.hidden) return localizedName;
        String levelString = "";
        if (netUserDossierData.role != NetRole.guest) {
            int level = NetExperienceManager.convertExperienceToLevel(netUserDossierData.experience);
            levelString = ", lvl " + level;
        }
        return localizedName + levelString;
    }


    private String getRoleString(NetUserDossierData netUserDossierData) {
        NetRole role = netUserDossierData.role;
        if (role == null) return "";
        String rank = RankWorker.apply(netUserDossierData.rankType, netUserDossierData.elp);
        String postfix = ", " + rank + "#";
        switch (role) {
            default:
                return languagesManager.getString("" + role) + postfix;
            case normal:
                if (netUserDossierData.rankType == RankType.usual_player) {
                    return languagesManager.getString("usual_player") + "#";
                }
                return languagesManager.getString("usual_player") + postfix;
        }
    }


    public void setId(String id) {
        this.id = id;
    }


    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
