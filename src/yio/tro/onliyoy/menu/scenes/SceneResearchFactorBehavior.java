package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.customizable_list.PlotParamsListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.ScrollListItem;
import yio.tro.onliyoy.menu.elements.customizable_list.SliReaction;
import yio.tro.onliyoy.menu.elements.plot_view.PlotColor;
import yio.tro.onliyoy.menu.elements.plot_view.PlotParameters;
import yio.tro.onliyoy.menu.elements.plot_view.PlotViewElement;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;

public class SceneResearchFactorBehavior extends SceneYio {


    private ButtonYio mainLabel;
    private PlotViewElement plotViewElement;
    private CustomizableListYio customizableListYio;
    private double plotOffset;
    public ArrayList<PlotParameters> paramsList;
    private PlotColor[] goodColors;
    private float itemHeight;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        paramsList = new ArrayList<>();
        initGoodColors();
        createMainLabel();
        createPlotView();
        createList();
        spawnBackButton(getOpenSceneReaction(Scenes.mainLobby));
    }


    private void initGoodColors() {
        goodColors = new PlotColor[]{
                PlotColor.aqua,
                PlotColor.blue,
                PlotColor.brown,
                PlotColor.cyan,
                PlotColor.green,
                PlotColor.purple,
                PlotColor.red,
                PlotColor.yellow,
        };
    }


    private void createList() {
        double bottomOffset = 0.01;
        double h = 1 - plotOffset - GraphicsYio.convertToHeight(0.98) - 2 * bottomOffset;
        customizableListYio = uiFactory.getCustomizableListYio()
                .setParent(mainLabel)
                .enableEmbeddedMode()
                .setCornerRadius(0)
                .setSize(0.98, h)
                .centerHorizontal()
                .alignBottom(bottomOffset);
    }


    private void createPlotView() {
        plotOffset = 0.13;
        plotViewElement = uiFactory.getPlotViewElement()
                .setParent(mainLabel)
                .setSize(0.98)
                .centerHorizontal()
                .alignTop(plotOffset);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        resetParamsList();
        onParamsChanged();
    }


    public void onParamsChanged() {
        updateCustomizableList();
        applyParamsList();
        plotViewElement.moveElement();
        plotViewElement.moveElement();
    }


    private void updateCustomizableList() {
        itemHeight = 0.05f * GraphicsYio.height;
        customizableListYio.clearItems();

        addScrollItem("[clear]", getClearReaction());
        addScrollItem("[spawn by movement type]", getSpawnByMovementTypeReaction());
        addScrollItem("[spawn by speed]", getSpawnBySpeedReaction());

        for (PlotParameters parameters : paramsList) {
            PlotParamsListItem ppListItem = new PlotParamsListItem();
            ppListItem.setHeight(itemHeight);
            ppListItem.setParameters(parameters);
            ppListItem.setClickReaction(getEditReaction());
            customizableListYio.addItem(ppListItem);
        }

        addScrollItem("+", getPlusReaction());
    }


    private void addScrollItem(String name, SliReaction reaction) {
        ScrollListItem scrollListItem = new ScrollListItem();
        scrollListItem.setHeight(itemHeight);
        scrollListItem.setCentered(true);
        scrollListItem.setColored(false);
        scrollListItem.setTitle(name);
        scrollListItem.setClickReaction(reaction);
        customizableListYio.addItem(scrollListItem);
    }


    private SliReaction getSpawnBySpeedReaction() {
        return new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                Scenes.spawnPlotsBySpeed.create();
            }
        };
    }


    private SliReaction getSpawnByMovementTypeReaction() {
        return new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                Scenes.spawnPlotsByMovementType.create();
            }
        };
    }


    private SliReaction getEditReaction() {
        return new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                PlotParamsListItem plotParamsListItem = (PlotParamsListItem) item;
                PlotParameters parameters = plotParamsListItem.parameters;
                Scenes.setupPlotParameters.setParameters(parameters);
                Scenes.setupPlotParameters.create();
            }
        };
    }


    private SliReaction getPlusReaction() {
        return new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                PlotParameters parameters = new PlotParameters();
                PlotParameters lastParameter = getLastParameter();
                if (lastParameter != null) {
                    parameters.movementType = lastParameter.movementType;
                    parameters.upwards = lastParameter.upwards;
                    parameters.color = getNextGoodColor(lastParameter.color);
                }
                paramsList.add(parameters);
                Scenes.setupPlotParameters.setParameters(parameters);
                Scenes.setupPlotParameters.create();
            }
        };
    }


    private PlotColor getNextGoodColor(PlotColor currentColor) {
        if (currentColor == PlotColor.gray) return PlotColor.gray;
        int index = indexOfGoodColor(currentColor);
        if (index == -1) return goodColors[0];
        if (index == goodColors.length - 1) return PlotColor.gray;
        return goodColors[index + 1];
    }


    private int indexOfGoodColor(PlotColor color) {
        for (int i = 0; i < goodColors.length; i++) {
            if (goodColors[i] == color) return i;
        }
        return -1;
    }


    private PlotParameters getLastParameter() {
        if (paramsList.size() == 0) return null;
        return paramsList.get(paramsList.size() - 1);
    }


    private SliReaction getClearReaction() {
        return new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                paramsList.clear();
                onParamsChanged();
            }
        };
    }


    private void applyParamsList() {
        plotViewElement.clear();
        for (PlotParameters parameters : paramsList) {
            plotViewElement.addPlotItem(parameters);
        }
    }


    private void resetParamsList() {
        paramsList.clear();
        PlotParameters parameters = new PlotParameters();
        parameters.setMovementType(MovementType.inertia);
        parameters.setSpeed(1);
        parameters.setUpwards(true);
        parameters.setName(Scenes.setupPlotParameters.generateName(parameters));
        paramsList.add(parameters);
    }


    private void createMainLabel() {
        mainLabel = uiFactory.getButton()
                .setSize(1, 1)
                .centerHorizontal()
                .centerVertical()
                .setAnimation(AnimationYio.down)
                .setBackground(BackgroundYio.white)
                .applyText(" ")
                .setCornerRadius(0)
                .setShadow(false)
                .setAlphaEnabled(false)
                .setTouchable(false);
    }
}
