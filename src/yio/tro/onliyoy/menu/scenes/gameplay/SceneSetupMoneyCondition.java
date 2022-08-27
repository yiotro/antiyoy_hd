package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.menu.elements.resizable_element.RveChooseConditionTypeItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveMoneyItem;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneSetupMoneyCondition extends ModalSceneYio {

    RveChooseConditionTypeItem rveChooseConditionTypeItem;
    private int[] moneyValues;
    private SliderElement sliderElement;


    @Override
    protected void initialize() {
        createCloseButton();
        createDefaultPanel(0.15);
        createSlider();
    }


    private void createSlider() {
        moneyValues = new int[]{1, 10, 25, 50, 100, 250, 500, 1000};
        String[] stringValues = new String[moneyValues.length];
        for (int i = 0; i < moneyValues.length; i++) {
            stringValues[i] = "$" + moneyValues[i];
        }
        sliderElement = uiFactory.getSlider()
                .setParent(defaultPanel)
                .centerHorizontal()
                .alignTop(0.04)
                .setTitle("money")
                .setPossibleValues(stringValues);
    }


    @Override
    protected void onDestroy() {
        RveMoneyItem rveMoneyItem = new RveMoneyItem();
        rveMoneyItem.setValue(moneyValues[sliderElement.getValueIndex()]);
        Scenes.composeLetter.rvElement.swapItem(rveChooseConditionTypeItem, rveMoneyItem);
    }


    public void setValue(int value) {
        for (int i = 0; i < moneyValues.length; i++) {
            if (moneyValues[i] != value) continue;
            sliderElement.setValueIndex(i);;
            return;
        }
        sliderElement.setValueIndex(0);
    }


    public void setRveChooseConditionTypeItem(RveChooseConditionTypeItem rveChooseConditionTypeItem) {
        this.rveChooseConditionTypeItem = rveChooseConditionTypeItem;
    }
}
