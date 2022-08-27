package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.CheckButtonYio;
import yio.tro.onliyoy.menu.elements.plot_view.PlotColor;
import yio.tro.onliyoy.menu.elements.plot_view.PlotParameters;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneSetupPlotParameters extends ModalSceneYio{

    PlotParameters parameters;
    private CheckButtonYio chkUpwards;
    private double[] speedValues;
    private SliderElement sliderColor;
    private SliderElement sliderMovementType;
    private SliderElement sliderSpeed;


    @Override
    protected void initialize() {
        createDarken();
        createCloseButton();
        createDefaultPanel(0.4);
        createInternals();
    }


    private void createInternals() {
        chkUpwards = uiFactory.getCheckButton()
                .setParent(defaultPanel)
                .centerHorizontal()
                .alignTop(0.01)
                .setName("Upwards");

        sliderColor = uiFactory.getSlider()
                .setParent(defaultPanel)
                .centerHorizontal()
                .alignUnder(previousElement, 0.02)
                .setTitle("Color")
                .setPossibleValues(PlotColor.class);

        sliderMovementType = uiFactory.getSlider()
                .setParent(defaultPanel)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setTitle("Movement type")
                .setPossibleValues(MovementType.class);

        speedValues = new double[]{0.1, 0.25, 0.5, 1, 1.5, 2, 3, 5};
        sliderSpeed = uiFactory.getSlider()
                .setParent(defaultPanel)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setTitle("Speed")
                .setPossibleValues(speedValues);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        applyValues();
        Scenes.researchFactorBehavior.onParamsChanged();
    }


    private void applyValues() {
        parameters.setUpwards(chkUpwards.isChecked());
        parameters.setColor(PlotColor.values()[sliderColor.getValueIndex()]);
        parameters.setMovementType(MovementType.values()[sliderMovementType.getValueIndex()]);
        parameters.setSpeed(speedValues[sliderSpeed.getValueIndex()]);
        parameters.setName(generateName(parameters));
    }


    public String generateName(PlotParameters parameters) {
        return parameters.color + " " + parameters.movementType + " x" + parameters.speed;
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        chkUpwards.setChecked(parameters.upwards);
        sliderColor.setValueIndex(parameters.color.ordinal());
        sliderMovementType.setValueIndex(parameters.movementType.ordinal());
        sliderSpeed.setValueIndex(convertSpeedValueToIndex(parameters.speed));
    }


    private int convertSpeedValueToIndex(double speedValue) {
        for (int i = 0; i < speedValues.length; i++) {
            if (speedValue == speedValues[i]) return i;
        }
        return 3;
    }


    public void setParameters(PlotParameters parameters) {
        this.parameters = parameters;
    }
}
