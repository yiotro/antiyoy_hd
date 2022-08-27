package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneFishExchangeDialog extends ModalSceneYio{

    int maxFish;
    private SliderElement sliderElement;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.25);
        createTitle();
        createSlider();
        createOkButton();
    }


    private void createOkButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.25, 0.055)
                .alignRight(0.1)
                .alignUnder(previousElement, 0.005)
                .setBackground(BackgroundYio.gray)
                .applyText("Ok")
                .setReaction(getOkReaction());
    }


    private Reaction getOkReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                int value = sliderElement.getValueIndex() + 1;
                netRoot.sendMessage(NmType.exchange_fish, "" + value);
                destroy();
            }
        };
    }


    private void createSlider() {
        sliderElement = uiFactory.getSlider()
                .setParent(defaultPanel)
                .setWidth(0.8)
                .alignUnder(previousElement, 0.04)
                .centerHorizontal()
                .setTitle("fish");
    }


    private void createTitle() {
        uiFactory.getLabelElement()
                .setParent(defaultPanel)
                .setSize(0.01)
                .alignTop(0.03)
                .centerHorizontal()
                .setFont(Fonts.gameFont)
                .setTitle(languagesManager.getString("exchange"));
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        String[] possibleValues = generatePossibleValues();
        sliderElement.setPossibleValues(possibleValues);
        sliderElement.setValueIndex(possibleValues.length - 1);
    }


    private String[] generatePossibleValues() {
        String[] array = new String[maxFish];
        for (int i = 0; i < array.length; i++) {
            array[i] = "" + (i + 1);
        }
        return array;
    }


    public void setMaxFish(int maxFish) {
        this.maxFish = maxFish;
    }
}
