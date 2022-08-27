package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.AdvancedLabelElement;
import yio.tro.onliyoy.menu.elements.ImportantConfirmationButton;
import yio.tro.onliyoy.menu.elements.setup_entities.CondensedEntitiesViewElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetUserLevelData;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneConfirmVerifyLevel extends ModalSceneYio {


    private AdvancedLabelElement advancedLabelElement;
    private CondensedEntitiesViewElement condensedEntitiesViewElement;
    private ImportantConfirmationButton confirmationButton;


    @Override
    protected void initialize() {
        createCloseButton();
        createDefaultPanel(0.4);
        createAdvancedLabel();
        createVerificationButton();
        createEntitiesView();
    }


    private void createEntitiesView() {
        condensedEntitiesViewElement = uiFactory.getCondensedEntitiesViewElement()
                .setParent(defaultPanel)
                .setSize(0.9, 0.15)
                .centerHorizontal()
                .setTouchable(false)
                .alignAbove(confirmationButton, 0.02);
    }


    private void createVerificationButton() {
        confirmationButton = uiFactory.getImportantConfirmationButton()
                .setParent(defaultPanel)
                .setSize(0.4, 0.05)
                .alignRight(0.03)
                .alignBottom(0.015)
                .setTouchOffset(0.05)
                .setCounterValue(10)
                .applyText("verify")
                .setReaction(getVerifyReaction());
    }


    private Reaction getVerifyReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onVerifyButtonPressed();
            }
        };
    }


    private void onVerifyButtonPressed() {
        destroy();
        yioGdxGame.applyFullTransitionToUI();

        NetUserLevelData netUserLevelData = new NetUserLevelData();
        netUserLevelData.levelSize = getGameController().sizeManager.initialLevelSize;
        netUserLevelData.rulesType = getViewableModel().ruleset.getRulesType();
        netUserLevelData.diplomacy = getViewableModel().diplomacyManager.enabled;
        netUserLevelData.colorsQuantity = getViewableModel().entitiesManager.entities.length;
        netRoot.sendMessage(NmType.verify_user_level, netUserLevelData.encode());
        Scenes.moderator.create();
    }


    private void createAdvancedLabel() {
        advancedLabelElement = uiFactory.getAdvancedLabelElement()
                .setParent(defaultPanel)
                .setSize(0.9, 0.01)
                .centerHorizontal()
                .alignTop(0.03)
                .setFont(Fonts.miniFont)
                .applyText("-");
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        long timeMillis = netRoot.verificationInfo.creationTime;
        int timeFrames = Yio.convertMillisIntoFrames(timeMillis);
        String timeString = Yio.convertTimeToUnderstandableString(timeFrames);
        String name = netRoot.verificationInfo.name;
        advancedLabelElement.applyText(
                languagesManager.getString("level_name") + ": " + CharLocalizerYio.getInstance().apply(name) + "#" +
                        languagesManager.getString("creation_time") + ": " + timeString + "#" +
                        generateDescription()
        );
        ViewableModel viewableModel = getObjectsLayer().viewableModel;
        condensedEntitiesViewElement.loadValues(viewableModel.entitiesManager.entities);
    }


    private String generateDescription() {
        RulesType rulesType = getViewableModel().ruleset.getRulesType();
        String rulesKey;
        switch (rulesType) {
            default:
                rulesKey = rulesType + "_rules";
                break;
            case def:
                rulesKey = "normal_rules";
                break;
        }
        String diplomacyKey = "without_diplomacy";
        if (getViewableModel().diplomacyManager.enabled) {
            diplomacyKey = "with_diplomacy";
        }
        String rulesString = LanguagesManager.getInstance().getString(rulesKey);
        String diplomacyString = LanguagesManager.getInstance().getString(diplomacyKey).toLowerCase();
        return rulesString + ", " + diplomacyString;
    }
}
