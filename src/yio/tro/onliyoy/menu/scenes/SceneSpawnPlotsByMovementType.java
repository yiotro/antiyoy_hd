package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.CheckButtonYio;
import yio.tro.onliyoy.menu.elements.plot_view.PlotColor;
import yio.tro.onliyoy.menu.elements.plot_view.PlotParameters;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;

public class SceneSpawnPlotsByMovementType extends ModalSceneYio {

    private CheckButtonYio chkUpwards;
    private SliderElement sliderMovementType;


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
        sliderMovementType.setValueIndex(MovementType.inertia.ordinal());
    }


    private void createInternals() {
        chkUpwards = uiFactory.getCheckButton()
                .setParent(defaultPanel)
                .centerHorizontal()
                .alignTop(0.01)
                .setName("Upwards");

        sliderMovementType = uiFactory.getSlider()
                .setParent(defaultPanel)
                .centerHorizontal()
                .alignUnder(previousElement, 0.02)
                .setTitle("Movement type")
                .setPossibleValues(MovementType.class);

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
        double[] speedValues = new double[]{0.1, 0.5, 1, 2, 3, 5};
        PlotColor[] colors = new PlotColor[]{PlotColor.aqua, PlotColor.blue, PlotColor.green, PlotColor.cyan, PlotColor.yellow, PlotColor.red};
        ArrayList<PlotParameters> paramsList = Scenes.researchFactorBehavior.paramsList;
        paramsList.clear();
        for (int i = 0; i < speedValues.length; i++) {
            PlotParameters parameters = new PlotParameters();
            parameters.upwards = chkUpwards.isChecked();
            parameters.movementType = MovementType.values()[sliderMovementType.getValueIndex()];
            parameters.speed = speedValues[i];
            parameters.color = colors[i];
            parameters.name = Scenes.setupPlotParameters.generateName(parameters);
            paramsList.add(parameters);
        }
        Scenes.researchFactorBehavior.onParamsChanged();
    }
}
