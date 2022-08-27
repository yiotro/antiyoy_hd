package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.menu.elements.CheckButtonYio;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneUlevFilters extends ModalSceneYio{


    private CheckButtonYio chkHideCompleted;
    LevelSize[] possibleSizes;
    String[] sizeStrings;
    private SliderElement sliderSize;
    int[] possibleColorQuantities;
    String[] colorQuantityStrings;
    private SliderElement sliderColorQuantities;
    boolean somethingChanged;


    @Override
    protected void initialize() {
        createDarken();
        createCloseButton();
        createDefaultPanel(0.37);
        initArrays();
        createInternals();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        somethingChanged = false;
        loadValues();
    }


    private void loadValues() {
        Preferences preferences = getPreferences();
        chkHideCompleted.setChecked(preferences.getBoolean("hide_completed", false));
        LevelSize levelSize;
        try {
            levelSize = LevelSize.valueOf(preferences.getString("level_size"));
        } catch (Exception e) {
            levelSize = null;
        }
        sliderSize.setValueIndex(0);
        if (levelSize != null) {
            sliderSize.setValueIndex(levelSize.ordinal() + 1);
        }
        int colorQuantity = preferences.getInteger("colors", 0);
        sliderColorQuantities.setValueIndex(convertColorQuantityIntoValueIndex(colorQuantity));
    }


    private int convertColorQuantityIntoValueIndex(int colorQuantity) {
        for (int i = 0; i < possibleColorQuantities.length; i++) {
            if (possibleColorQuantities[i] == colorQuantity) return i;
        }
        return 0;
    }


    @Override
    protected Reaction getCloseReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onCloseButtonPressed();
            }
        };
    }


    private void onCloseButtonPressed() {
        destroy();
        if (!somethingChanged) return;
        applyValues();
        Scenes.offlineUserLevels.loadValues();
    }


    private void applyValues() {
        Preferences preferences = getPreferences();
        preferences.putBoolean("hide_completed", chkHideCompleted.isChecked());
        preferences.putString("level_size", possibleSizes[sliderSize.getValueIndex()] + "");
        preferences.putInteger("colors", possibleColorQuantities[sliderColorQuantities.getValueIndex()]);
        preferences.flush();
    }


    private void initArrays() {
        possibleSizes = new LevelSize[LevelSize.values().length + 1];
        possibleSizes[0] = null;
        System.arraycopy(LevelSize.values(), 0, possibleSizes, 1, possibleSizes.length - 1);
        sizeStrings = new String[possibleSizes.length];
        sizeStrings[0] = languagesManager.getString("any");
        for (int i = 1; i < sizeStrings.length; i++) {
            sizeStrings[i] = languagesManager.getString("" + possibleSizes[i]);
        }
        possibleColorQuantities = new int[16];
        possibleColorQuantities[0] = 0;
        for (int i = 1; i < possibleColorQuantities.length; i++) {
            possibleColorQuantities[i] = i + 1;
        }
        colorQuantityStrings = new String[possibleColorQuantities.length];
        colorQuantityStrings[0] = languagesManager.getString("any");
        for (int i = 1; i < colorQuantityStrings.length; i++) {
            colorQuantityStrings[i] = "x" + possibleColorQuantities[i];
        }
    }


    private void createInternals() {
        chkHideCompleted = uiFactory.getCheckButton()
                .setParent(defaultPanel)
                .alignTop(0.02)
                .centerHorizontal()
                .setReaction(getSomethingChangedReaction())
                .setName("hide_completed");

        sliderSize = uiFactory.getSlider()
                .setParent(defaultPanel)
                .alignUnder(previousElement, 0.04)
                .centerHorizontal()
                .setTitle("level_size")
                .setValueChangeReaction(getSomethingChangedReaction())
                .setPossibleValues(sizeStrings);

        sliderColorQuantities = uiFactory.getSlider()
                .setParent(defaultPanel)
                .alignUnder(previousElement, 0.01)
                .centerHorizontal()
                .setTitle("players")
                .setValueChangeReaction(getSomethingChangedReaction())
                .setPossibleValues(colorQuantityStrings);
    }


    private Reaction getSomethingChangedReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                somethingChanged = true;
            }
        };
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences("yio.tro.onliyoy.user_level_filters");
    }


}
