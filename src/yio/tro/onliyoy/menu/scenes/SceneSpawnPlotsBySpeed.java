package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.CheckButtonYio;
import yio.tro.onliyoy.menu.elements.plot_view.PlotColor;
import yio.tro.onliyoy.menu.elements.plot_view.PlotParameters;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;

public class SceneSpawnPlotsBySpeed extends ModalSceneYio{

    private CheckButtonYio chkUpwards;
    private SliderElement sliderSpeed;
    private double[] speedValues;


    @Override
    protected void initialize() {
        createDarken();
        createCloseButton();
        createDefaultPanel(0.3);
        createInternals();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        chkUpwards.setChecked(true);
        sliderSpeed.setValueIndex(3);
    }


    private void createInternals() {
        speedValues = new double[]{0.1, 0.25, 0.5, 1, 1.5, 2, 3, 5};

        chkUpwards = uiFactory.getCheckButton()
                .setParent(defaultPanel)
                .centerHorizontal()
                .alignTop(0.01)
                .setName("Upwards");

        sliderSpeed = uiFactory.getSlider()
                .setParent(defaultPanel)
                .centerHorizontal()
                .alignUnder(previousElement, 0.02)
                .setTitle("Speed")
                .setPossibleValues(speedValues);

        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.3, 0.05)
                .alignRight(0.03)
                .alignUnder(previousElement, 0.01)
                .setTouchOffset(0.03)
                .setBackground(BackgroundYio.gray)
                .applyText("Apply")
                .setReaction(getApplyReaction());
    }


    private Reaction getApplyReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onApplyButtonPressed();
                destroy();
            }
        };
    }


    private void onApplyButtonPressed() {
        ArrayList<MovementType> movementTypes = generateMovementTypesList();
        PlotColor[] colors = new PlotColor[]{PlotColor.aqua, PlotColor.blue, PlotColor.red, PlotColor.cyan, PlotColor.yellow};
        ArrayList<PlotParameters> paramsList = Scenes.researchFactorBehavior.paramsList;
        paramsList.clear();
        for (int i = 0; i < movementTypes.size(); i++) {
            PlotParameters parameters = new PlotParameters();
            parameters.upwards = chkUpwards.isChecked();
            parameters.movementType = movementTypes.get(i);
            parameters.speed = speedValues[sliderSpeed.getValueIndex()];
            parameters.color = colors[i % colors.length];
            parameters.name = Scenes.setupPlotParameters.generateName(parameters);
            paramsList.add(parameters);
        }
        Scenes.researchFactorBehavior.onParamsChanged();
    }


    private ArrayList<MovementType> generateMovementTypesList() {
        ArrayList<MovementType> list = new ArrayList<>();
        for (MovementType value : MovementType.values()) {
            if (value == MovementType.stay) continue;
            list.add(value);
        }
        return list;
    }
}
