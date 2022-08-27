package yio.tro.onliyoy.menu.scenes.editor;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.game.core_model.generators.LgParameters;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.CheckButtonYio;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneRandomizePanel extends ModalSceneYio {


    private int[] possibleLandsDensities;
    private SliderElement landsDensitySlider;
    private SliderElement treesDensitySlider;
    private String[] possibleTressDensities;
    private SliderElement nodesValueSlider;
    private String[] possibleNodeValues;
    private CheckButtonYio chkLoop;
    private CheckButtonYio chkNeutralTowers;
    private CheckButtonYio chkNeutralCities;
    private CheckButtonYio chkGraves;


    @Override
    protected void initialize() {
        createCloseButton();
        createDefaultPanel(0.72);
        defaultPanel.setDestroyParameters(MovementType.inertia, 1.5);
        createTopLabel();
        createInternals();
        createApplyButton();
    }


    private void createApplyButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.5, 0.055)
                .setBackground(BackgroundYio.gray)
                .alignRight(0.09)
                .alignBottom(0.02)
                .applyText("generate")
                .setReaction(onGenerateButtonPressed());
    }


    private Reaction onGenerateButtonPressed() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                applyGeneration();
            }
        };
    }


    private void applyGeneration() {
        LgParameters parameters = new LgParameters();
        parameters.setEntities(getObjectsLayer().viewableModel.entitiesManager.entities);
        parameters.setLoop(chkLoop.isChecked());
        parameters.setNeutralTowers(chkNeutralTowers.isChecked());
        parameters.setNeutralCities(chkNeutralCities.isChecked());
        parameters.setGraves(chkGraves.isChecked());
        parameters.setLandsDensity(possibleLandsDensities[landsDensitySlider.getValueIndex()]);
        parameters.setTreesDensity(Double.valueOf(possibleTressDensities[treesDensitySlider.getValueIndex()]));
        parameters.setNodesValue(Float.valueOf(possibleNodeValues[nodesValueSlider.getValueIndex()]));
        getObjectsLayer().editorManager.onGenerateButtonPressed(parameters);
        getGameController().cameraController.flyUp(false);
    }


    private void createInternals() {
        possibleLandsDensities = new int[]{2, 3, 5, 7, 9};
        possibleTressDensities = new String[]{"0", "0.05", "0.1", "0.15", "0.2", "0.25", "0.33", "0.5", "0.75", "1"};
        possibleNodeValues = new String[]{"0.1", "0.15", "0.2", "0.25", "0.3", "0.35", "0.4", "0.45", "0.5"};

        landsDensitySlider = uiFactory.getSlider()
                .setParent(defaultPanel)
                .setWidth(0.8)
                .centerHorizontal()
                .alignUnder(previousElement, 0.025)
                .setTitle("lands_density")
                .setPossibleValues(possibleLandsDensities);

        treesDensitySlider = uiFactory.getSlider()
                .setParent(defaultPanel)
                .setWidth(0.8)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setTitle("trees_density")
                .setPossibleValues(possibleTressDensities)
                .setValueChangeReaction(getTreesSliderReaction());

        nodesValueSlider = uiFactory.getSlider()
                .setParent(defaultPanel)
                .setWidth(0.8)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setTitle("nodes_value")
                .setPossibleValues(possibleNodeValues);

        chkLoop = uiFactory.getCheckButton()
                .setParent(defaultPanel)
                .setSize(0.86, 0.055)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setFont(Fonts.miniFont)
                .setName("loop");

        chkNeutralTowers = uiFactory.getCheckButton()
                .clone(previousElement)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setFont(Fonts.miniFont)
                .setName("neutral_towers");

        chkNeutralCities = uiFactory.getCheckButton()
                .clone(previousElement)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setFont(Fonts.miniFont)
                .setName("neutral_cities");

        chkGraves = uiFactory.getCheckButton()
                .clone(previousElement)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setFont(Fonts.miniFont)
                .setName("graves")
                .setReaction(getChkGravesReaction());

        loadDefaultValues();
    }


    private Reaction getChkGravesReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                if (!chkGraves.isChecked()) return;
                if (treesDensitySlider.getValueIndex() > 0) return;
                treesDensitySlider.setValueIndex(1);
            }
        };
    }


    private Reaction getTreesSliderReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                if (treesDensitySlider.getValueIndex() > 0) return;
                if (!chkGraves.isChecked()) return;
                chkGraves.setChecked(false);
            }
        };
    }


    private void loadDefaultValues() {
        chkGraves.setChecked(true);
        chkLoop.setChecked(true);
        chkNeutralCities.setChecked(true);
        chkNeutralTowers.setChecked(true);
        landsDensitySlider.setValueIndex(0);
        treesDensitySlider.setValueIndex(3);
        nodesValueSlider.setValueIndex(2);
    }


    private void createTopLabel() {
        uiFactory.getLabelElement()
                .setParent(defaultPanel)
                .setSize(0.01, 0.06)
                .alignTop(0.01)
                .centerHorizontal()
                .setFont(Fonts.buttonFont)
                .setTitle(languagesManager.getString("random_generation"));
    }
}
