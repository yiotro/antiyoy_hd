package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.gameplay.income_graph.IncomeGraphElement;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;

public class SceneIncomeGraph extends ModalSceneYio {


    public IncomeGraphElement incomeGraphElement;


    public SceneIncomeGraph() {
        incomeGraphElement = null;
    }


    @Override
    protected void initialize() {
        createCloseButton();
        createIncomeGraph();
    }


    private void createIncomeGraph() {
        incomeGraphElement = uiFactory.getIncomeGraphElement()
                .setSize(0.9, 0.36)
                .centerHorizontal()
                .alignBottom(0.25)
                .setAnimation(AnimationYio.from_touch);
    }


    public boolean isInTransitionCurrently() {
        if (incomeGraphElement == null) return false;
        float f = incomeGraphElement.getFactor().getValue();
        return f > 0.01 && f < 0.99;
    }
}
