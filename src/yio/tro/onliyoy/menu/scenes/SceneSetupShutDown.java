package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.AnnounceViewElement;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneSetupShutDown extends SceneYio{

    private AnnounceViewElement mainView;
    int[] timeValues;
    String[] stringValues;
    private SliderElement slider;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.red;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getOpenSceneReaction(Scenes.admin));
        createMainView();
        initValues();
        createSlider();
        createButtons();
    }


    private void createButtons() {
        uiFactory.getButton()
                .setParent(mainView)
                .setSize(0.3, 0.05)
                .alignBottom(0.01)
                .alignRight(0.02)
                .applyText("Apply")
                .setReaction(getApplyReaction());

        uiFactory.getButton()
                .setParent(mainView)
                .setSize(0.3, 0.05)
                .alignBottom(0.01)
                .alignLeft(0.02)
                .applyText("Cancel")
                .setReaction(getCancelReaction());
    }


    private Reaction getCancelReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                netRoot.sendMessage(NmType.cancel_shut_down, "");
                Scenes.admin.create();
            }
        };
    }


    private Reaction getApplyReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                int index = slider.getValueIndex();
                int delay = timeValues[index] + 3;
                netRoot.sendMessage(NmType.apply_shut_down, "" + delay);
                Scenes.admin.create();
            }
        };
    }


    private void createSlider() {
        slider = uiFactory.getSlider()
                .setParent(mainView)
                .centerHorizontal()
                .alignTop(0.1)
                .setTitle("Delay")
                .setPossibleValues(stringValues);
    }


    private void initValues() {
        timeValues = new int[]{10, 20, 30, 60, 120};
        stringValues = new String[timeValues.length];
        for (int i = 0; i < timeValues.length; i++) {
            int seconds = timeValues[i];
            int frames = 60 * seconds;
            stringValues[i] = Yio.convertTimeToUnderstandableString(frames);
        }
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        slider.setValueIndex(1);
    }


    private void createMainView() {
        mainView = uiFactory.getAnnounceViewElement()
                .setSize(0.9, 0.3)
                .alignBottom(0.3)
                .centerHorizontal()
                .setAnimation(AnimationYio.from_touch)
                .setTitle("Server shut down")
                .setText(" ")
                .setTouchable(false);
    }
}
