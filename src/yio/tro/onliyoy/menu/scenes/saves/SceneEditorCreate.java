package yio.tro.onliyoy.menu.scenes.saves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.editor.EditorManager;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.loading.LoadingParameters;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.rules_picker.RulesPickerElement;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class SceneEditorCreate extends SceneYio {

    public ButtonYio creationButton;
    private AnnounceViewElement mainLabel;
    private SliderElement levelSizeSlider;
    private RulesPickerElement rulesPickerElement;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.yellow;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getBackButtonReaction());

        createCreationButton();
        createMainLabel();
        createSlider();
        createRulesPicker();

        loadValues();
    }


    private void createRulesPicker() {
        rulesPickerElement = uiFactory.getRulesPickerElement()
                .setParent(mainLabel)
                .centerHorizontal()
                .alignUnder(previousElement, 0);
    }


    private void createMainLabel() {
        double h = 0.225;
        mainLabel = uiFactory.getAnnounceViewElement()
                .setSize(0.8, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setAnimation(AnimationYio.from_touch)
                .setText(" ");
    }


    private void createSlider() {
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
        rulesPickerElement.setRules(prefs.getString("rules"));
    }


    private void createCreationButton() {
        creationButton = uiFactory.getButton()
                .setSize(0.4, 0.07)
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
                onCreationButtonPressed();
            }
        };
    }


    private void saveValues() {
        Preferences prefs = getPreferences();
        prefs.putInteger("level_size", levelSizeSlider.getValueIndex());
        prefs.putString("rules", "" + rulesPickerElement.getRules());
        prefs.flush();
    }


    public Preferences getPreferences() {
        return Gdx.app.getPreferences("onliyoy.editor_create");
    }


    void onCreationButtonPressed() {
        saveValues();
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.add("level_size", "" + LevelSize.values()[levelSizeSlider.getValueIndex()]);
        loadingParameters.add("rules_type", "" + rulesPickerElement.getRules());
        yioGdxGame.loadingManager.startInstantly(LoadingType.editor_create, loadingParameters);
        EditorManager.prepareNewSaveSlot(getGameController().savesManager);
    }


    private Reaction getBackButtonReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                saveValues();
                Scenes.editorLobby.create();
            }
        };
    }
}
