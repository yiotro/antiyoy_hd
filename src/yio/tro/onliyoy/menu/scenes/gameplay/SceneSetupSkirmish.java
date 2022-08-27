package yio.tro.onliyoy.menu.scenes.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.loading.LoadingParameters;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.menu.MenuSwitcher;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.CheckButtonYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.rules_picker.RulesPickerElement;
import yio.tro.onliyoy.menu.elements.setup_entities.EntitiesSetupElement;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.TimeMeasureYio;

public class SceneSetupSkirmish extends SceneYio {


    public ButtonYio startButton;
    private AnnounceViewElement mainLabel;
    private SliderElement levelSizeSlider;
    public EntitiesSetupElement entitiesSetupElement;
    private CheckButtonYio chkDiplomacy;
    private RulesPickerElement rulesPickerElement;
    private CheckButtonYio chkFogOfWar;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.yellow;
    }


    @Override
    public BackgroundYio getButtonBackground() {
        return BackgroundYio.white;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getBackButtonReaction());

        createStartButton();
        createMainLabel();
        createSliders();
        createRulesPicker();
        createCheckButtons();
        createEntitiesSetupElement();

        loadValues();
    }


    private void createCheckButtons() {
        chkDiplomacy = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setName("diplomacy");

        chkFogOfWar = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setName("fog_of_war");
    }


    private void createRulesPicker() {
        rulesPickerElement = uiFactory.getRulesPickerElement()
                .setParent(mainLabel)
                .centerHorizontal()
                .alignUnder(previousElement, 0);
    }


    private void createEntitiesSetupElement() {
        entitiesSetupElement = uiFactory.getEntitiesSetupElement()
                .setParent(mainLabel)
                .setSize(0.72, 0.2)
                .centerHorizontal()
                .alignUnder(previousElement, 0.04);
    }


    private Reaction getBackButtonReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                saveValues();
                MenuSwitcher.getInstance().createChooseGameModeMenu();
            }
        };
    }


    private void createMainLabel() {
        double h = 0.63;
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
                .setPossibleValues(LevelSize.class);
    }


    private void loadValues() {
        Preferences prefs = getPreferences();

        levelSizeSlider.setValueIndex(prefs.getInteger("level_size", 0));
        entitiesSetupElement.decode(prefs.getString("entities", ""), false);
        rulesPickerElement.setRules(prefs.getString("rules"));
        chkDiplomacy.setChecked(prefs.getBoolean("diplomacy", false));
        chkFogOfWar.setChecked(prefs.getBoolean("fog_of_war", false));
    }


    private void saveValues() {
        Preferences prefs = getPreferences();

        prefs.putInteger("level_size", levelSizeSlider.getValueIndex());
        prefs.putString("entities", entitiesSetupElement.encode());
        prefs.putString("rules", "" + rulesPickerElement.getRules());
        prefs.putBoolean("diplomacy", chkDiplomacy.isChecked());
        prefs.putBoolean("fog_of_war", chkFogOfWar.isChecked());

        prefs.flush();
    }


    public Preferences getPreferences() {
        return Gdx.app.getPreferences("onliyoy.training");
    }


    private void createStartButton() {
        startButton = uiFactory.getButton()
                .setSize(0.4, 0.07)
                .alignRight(0.05)
                .alignTop(0.03)
                .setBackground(BackgroundYio.green)
                .applyText("start")
                .setReaction(getStartReaction())
                .setHotkeyKeycode(Input.Keys.ENTER)
                .setAnimation(AnimationYio.up);
    }


    private Reaction getStartReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onStartButtonPressed();
            }
        };
    }


    private void checkToFixEntities() {
        if (entitiesSetupElement.countColoredItems() == 0) {
            entitiesSetupElement.addDefaultItems();
            return;
        }
        if (entitiesSetupElement.countColoredItems() == 1) {
            entitiesSetupElement.onPlusClicked();
        }
    }


    private void onStartButtonPressed() {
        checkToFixEntities();
        saveValues();
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.add("level_size", "" + LevelSize.values()[levelSizeSlider.getValueIndex()]);
        loadingParameters.add("entities", entitiesSetupElement.encode());
        loadingParameters.add("rules_type", "" + rulesPickerElement.getRules());
        loadingParameters.add("diplomacy", "" + chkDiplomacy.isChecked());
        loadingParameters.add("fog_of_war", "" + chkFogOfWar.isChecked());
        yioGdxGame.loadingManager.startInstantly(LoadingType.training_create, loadingParameters);
    }

}
