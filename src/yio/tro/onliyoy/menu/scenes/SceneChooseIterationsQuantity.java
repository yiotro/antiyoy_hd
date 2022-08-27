package yio.tro.onliyoy.menu.scenes;

import com.badlogic.gdx.Input;
import yio.tro.onliyoy.game.tests.AbstractTest;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class SceneChooseIterationsQuantity extends ModalSceneYio{

    AbstractTest test;
    private SliderElement slider;
    private int[] values;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.25);
        initValues();
        createSlider();
        createApplyButton();
    }


    private void initValues() {
        values = new int[]{1, 10, 100, 1000, 2000, 5000, 10000, 25000, 50000};
    }


    private void createApplyButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.3, 0.06)
                .setBackground(BackgroundYio.gray)
                .applyText("Apply")
                .alignRight(0.03)
                .alignUnder(slider, 0)
                .setHotkeyKeycode(Input.Keys.ENTER)
                .setReaction(getApplyReaction());
    }


    private Reaction getApplyReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                test.onQuantityChosen(values[slider.getValueIndex()]);
                test.perform(gameController);
                destroy();
            }
        };
    }


    private void createSlider() {
        slider = uiFactory.getSlider()
                .setParent(defaultPanel)
                .centerHorizontal()
                .alignTop(0.04)
                .setTitle("Quantity")
                .setPossibleValues(values);
        slider.setValueIndex(5);
    }


    public void setTest(AbstractTest test) {
        this.test = test;
    }
}
