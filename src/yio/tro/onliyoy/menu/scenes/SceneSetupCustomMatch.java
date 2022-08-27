package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.menu.elements.*;
import yio.tro.onliyoy.menu.elements.keyboard.AbstractKbReaction;
import yio.tro.onliyoy.menu.elements.rules_picker.RulesPickerElement;
import yio.tro.onliyoy.menu.elements.setup_entities.EntitiesSetupElement;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.NetExperienceManager;
import yio.tro.onliyoy.net.NetRole;
import yio.tro.onliyoy.net.shared.NetMatchLobbyData;
import yio.tro.onliyoy.net.shared.NetMatchType;
import yio.tro.onliyoy.net.shared.NmType;

import java.util.ArrayList;

public class SceneSetupCustomMatch extends SceneYio {


    private AnnounceViewElement mainLabel;
    private CheckButtonYio chkDiplomacy;
    private EntitiesSetupElement entitiesSetupElement;
    private SliderElement levelSizeSlider;
    private SliderElement turnDurationSlider;
    private int[] turnDurationValues;
    private CheckButtonYio chkPassword;
    private String passwordString;
    private CheckButtonYio chkCustomMap;
    private String customLevelCode;
    private ArrayList<InterfaceElement> customMapDependantElements;
    private RulesPickerElement rulesPickerElement;
    private CheckButtonYio chkFogOfWar;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getBackButtonReaction());

        customMapDependantElements = new ArrayList<>();
        createCreationButton();
        createMainLabel();
        createSliders();
        createRulesPicker();
        createCheckButtons();
        createEntitiesSetupElement();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        updateCustomMapDependantElements();
        loadValues();
    }


    private void updateCustomMapDependantElements() {
        customMapDependantElements.clear();
        customMapDependantElements.add(levelSizeSlider);
        customMapDependantElements.add(rulesPickerElement);
        customMapDependantElements.add(chkDiplomacy);
        customMapDependantElements.add(entitiesSetupElement);
        customMapDependantElements.add(chkFogOfWar);
    }


    private void createCreationButton() {
        uiFactory.getButton()
                .setSize(0.5, 0.07)
                .alignRight(0.05)
                .alignTop(0.03)
                .setBackground(BackgroundYio.green)
                .applyText("create")
                .setReaction(getCreationReaction())
                .setHotkeyKeycode(Input.Keys.ENTER)
                .setAnimation(AnimationYio.up);
    }


    private Reaction getCreationReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onCreateButtonPressed();
            }
        };
    }


    private void createCheckButtons() {
        chkDiplomacy = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .centerHorizontal()
                .setHeight(0.065)
                .alignUnder(previousElement, 0)
                .setName("diplomacy");

        chkPassword = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .centerHorizontal()
                .setHeight(0.065)
                .alignUnder(previousElement, 0)
                .setName("password")
                .setReaction(getChkPasswordReaction());

        chkCustomMap = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .centerHorizontal()
                .setHeight(0.065)
                .alignUnder(previousElement, 0)
                .setName("custom_map")
                .setReaction(getChkCustomMapReaction());

        chkFogOfWar = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .centerHorizontal()
                .setHeight(0.065)
                .alignUnder(previousElement, 0)
                .setName("fog_of_war");
    }


    private void createRulesPicker() {
        rulesPickerElement = uiFactory.getRulesPickerElement()
                .setParent(mainLabel)
                .centerHorizontal()
                .alignUnder(previousElement, 0);
    }


    private Reaction getChkCustomMapReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onChkCustomMapPressed();
            }
        };
    }


    private void onChkCustomMapPressed() {
        if (!chkCustomMap.isChecked()) {
            for (InterfaceElement element : customMapDependantElements) {
                element.activate();
            }
            return;
        }
        Scenes.chooseCustomMap.create();
    }


    public void onCustomMapChosen(String levelCode) {
        customLevelCode = levelCode;
        if (customLevelCode.length() < 5) {
            chkCustomMap.setChecked(false);
            return;
        }
        if (chkPassword.isChecked()) {
            applyCustomMapInfluence();
        } else {
            Scenes.keyboard.create();
            Scenes.keyboard.setHint("password");
            Scenes.keyboard.setReaction(new AbstractKbReaction() {
                @Override
                public void onInputFromKeyboardReceived(String input) {
                    if (input.length() == 0) {
                        onInputCancelled();
                        return;
                    }
                    chkPassword.setChecked(true);
                    applyPassword(input);
                    applyCustomMapInfluence();
                }


                @Override
                public void onInputCancelled() {
                    Scenes.toast.show("use_password_for_custom_map");
                    chkCustomMap.setChecked(false);
                }
            });
        }
    }


    private void applyCustomMapInfluence() {
        Scenes.toast.show("toast_custom_map_parameters");
        for (InterfaceElement element : customMapDependantElements) {
            element.deactivate();
        }
    }


    private Reaction getChkPasswordReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onChkPasswordPressed();
            }
        };
    }


    private void onChkPasswordPressed() {
        chkPassword.setName("password");
        if (!chkPassword.isChecked()) {
            onPasswordRemoved();
            return;
        }
        Scenes.keyboard.create();
        Scenes.keyboard.setHint("password");
        Scenes.keyboard.setReaction(new AbstractKbReaction() {
            @Override
            public void onInputFromKeyboardReceived(String input) {
                if (input.length() == 0) {
                    chkPassword.setChecked(false);
                    return;
                }
                applyPassword(input);
            }


            @Override
            public void onInputCancelled() {
                chkPassword.setChecked(false);
            }
        });
    }


    private void applyPassword(String input) {
        if (input.length() > 10) {
            input = input.substring(0, 10);
        }
        String prefix = languagesManager.getString("password");
        passwordString = input;
        chkPassword.setName(prefix + " '" + input + "'");
    }


    private void onPasswordRemoved() {
        if (!chkCustomMap.isChecked()) return;
        chkCustomMap.setChecked(false);
        chkCustomMap.applyClickReaction();
    }


    private void createEntitiesSetupElement() {
        entitiesSetupElement = uiFactory.getEntitiesSetupElement()
                .setParent(mainLabel)
                .setSize(0.72, 0.2)
                .centerHorizontal()
                .alignUnder(previousElement, 0.04);
    }


    private void createMainLabel() {
        double h = 0.825;
        mainLabel = uiFactory.getAnnounceViewElement()
                .setSize(0.9, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAnimation(AnimationYio.from_touch)
                .setText(" ");
    }


    private void createSliders() {
        levelSizeSlider = uiFactory.getSlider()
                .setParent(mainLabel)
                .centerHorizontal()
                .alignTop(0.04)
                .setTitle("level_size")
                .setPossibleValues(getPossibleLevelSizeValues());

        turnDurationValues = generateTurnDurationValues();
        String[] turnDurationStrings = new String[turnDurationValues.length];
        for (int i = 0; i < turnDurationValues.length; i++) {
            turnDurationStrings[i] = Yio.convertTimeToTurnDurationString(60 * turnDurationValues[i]);
        }

        turnDurationSlider = uiFactory.getSlider()
                .setParent(mainLabel)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setTitle("turn_duration")
                .setPossibleValues(turnDurationStrings);
    }


    private String[] getPossibleLevelSizeValues() {
        return new String[]{
                "" + LevelSize.tiny,
                "" + LevelSize.small,
                "" + LevelSize.normal,
                "" + LevelSize.big,
                "" + LevelSize.large,
        };
    }


    private int[] generateTurnDurationValues() {
        NetRole role = netRoot.userData.role;
        if (netRoot.isInLocalMode()) {
            role = NetRole.normal; // pretend like guest is not guest in local mode
        }
        long experience = netRoot.userData.experience;
        int level = NetExperienceManager.convertExperienceToLevel(experience);
        switch (role) {
            default:
                if (level < 5) {
                    return generateTurnDurationValues(11);
                }
                return generateTurnDurationValues(14);
            case guest:
                return generateTurnDurationValues(8);
        }
    }


    private int[] generateTurnDurationValues(int quantity) {
        int[] src = getSourceTurnDurationsArray();
        int[] result = new int[quantity];
        System.arraycopy(src, 0, result, 0, result.length);
        return result;
    }


    private int[] getSourceTurnDurationsArray() {
        int m = 60; // minutes
        int h = 60 * m; // hours
        return new int[]{10, 20, 30, 45, 60, 90, 2 * m, 5 * m, 15 * m, h, 3 * h, 6 * h, 12 * h, 24 * h};
    }


    private void loadValues() {
        Preferences prefs = getPreferences();

        levelSizeSlider.setValueIndex(prefs.getInteger("level_size", 0));
        entitiesSetupElement.decode(prefs.getString("entities", ""), true);
        rulesPickerElement.setRules(prefs.getString("rules"));
        chkDiplomacy.setChecked(prefs.getBoolean("diplomacy", false));
        turnDurationSlider.setValueIndex(prefs.getInteger("turn_duration", 2));
        chkPassword.setChecked(false);
        chkCustomMap.setChecked(false);
        chkFogOfWar.setChecked(prefs.getBoolean("fog_of_war", false));
    }


    private void saveValues() {
        Preferences prefs = getPreferences();

        prefs.putInteger("level_size", levelSizeSlider.getValueIndex());
        prefs.putString("entities", entitiesSetupElement.encode());
        prefs.putString("rules", "" + rulesPickerElement.getRules());
        prefs.putBoolean("diplomacy", chkDiplomacy.isChecked());
        prefs.putInteger("turn_duration", turnDurationSlider.getValueIndex());
        prefs.putBoolean("fog_of_war", chkFogOfWar.isChecked());

        prefs.flush();
    }


    public Preferences getPreferences() {
        return Gdx.app.getPreferences("onliyoy.custom_match");
    }


    private void checkToFixEntities() {
        if (entitiesSetupElement.countColoredItems() == 0) {
            entitiesSetupElement.addItemsForOnlineMatch();
            return;
        }
        if (entitiesSetupElement.countColoredItems() == 1) {
            entitiesSetupElement.onPlusClicked();
        }
    }


    private void onCreateButtonPressed() {
        checkToFixEntities();
        saveValues();
        NetMatchLobbyData netMatchLobbyData = new NetMatchLobbyData();
        netMatchLobbyData.levelSize = LevelSize.values()[levelSizeSlider.getValueIndex()];
        netMatchLobbyData.setEntitiesFromUI(entitiesSetupElement.encode());
        netMatchLobbyData.rulesType = rulesPickerElement.getRules();
        netMatchLobbyData.diplomacy = chkDiplomacy.isChecked();
        netMatchLobbyData.turnSeconds = turnDurationValues[turnDurationSlider.getValueIndex()];
        netMatchLobbyData.setPassword(getPassword());
        netMatchLobbyData.hasCreator = true;
        netMatchLobbyData.levelCode = getLevelCode();
        netMatchLobbyData.fog = chkFogOfWar.isChecked();
        netMatchLobbyData.matchType = NetMatchType.custom;
        netRoot.sendMessage(NmType.custom_match_create, netMatchLobbyData.encode());
        Scenes.waitMatchCreating.create();
    }


    private String getLevelCode() {
        if (!chkCustomMap.isChecked()) return "-";
        return customLevelCode;
    }


    private String getPassword() {
        if (!chkPassword.isChecked()) return "";
        return passwordString;
    }


    private Reaction getBackButtonReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                saveValues();
                Scenes.customMatches.create();
            }
        };
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
