package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneSetupRestoreLevels extends ModalSceneYio{

    String id;
    long[] values;
    private SliderElement sliderElement;


    @Override
    protected void initialize() {
        createCloseButton();
        createDarken();
        createDefaultPanel(0.3);
        createLabel();
        initValues();
        createSlider();
        createApplyButton();
    }


    private void createApplyButton() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.4, 0.05)
                .alignRight(0.02)
                .alignBottom(0.02)
                .setBackground(BackgroundYio.gray)
                .applyText("Apply")
                .setReaction(getApplyReaction());
    }


    private Reaction getApplyReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                long chosenTimeInMillis = values[sliderElement.getValueIndex()];
                netRoot.sendMessage(NmType.request_restore_levels, id + " " + chosenTimeInMillis);
            }
        };
    }


    private void initValues() {
        long h = 60 * 60 * 1000;
        long d = 24 * h;
        values = new long[]{h, 2 * h, 3 * h, 6 * h, 12 * h, d, 7 * d};
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        sliderElement.setValueIndex(5);
    }


    private void createSlider() {
        String[] strings = new String[values.length];
        for (int i = 0; i < strings.length; i++) {
            long timeInMillis = values[i];
            int timeInFrames = Yio.convertMillisIntoFrames(timeInMillis);
            strings[i] = Yio.convertTimeToTurnDurationString(timeInFrames);
        }
        sliderElement = uiFactory.getSlider()
                .setParent(defaultPanel)
                .centerHorizontal()
                .alignTop(0.11)
                .setTitle("Duration")
                .setPossibleValues(strings);
    }


    private void createLabel() {
        uiFactory.getLabelElement()
                .setParent(defaultPanel)
                .setSize(0.01, 0.04)
                .centerHorizontal()
                .alignTop(0.01)
                .setTitle("Restore levels");
    }


    public void setId(String id) {
        this.id = id;
    }
}
