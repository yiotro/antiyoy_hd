package yio.tro.onliyoy.menu.scenes.info;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.CircleButtonYio;
import yio.tro.onliyoy.menu.scenes.SceneYio;
import yio.tro.onliyoy.stuff.RectangleYio;

public abstract class AbstractInfoScene extends SceneYio {


    protected ButtonYio infoPanel;
    protected CircleButtonYio backButton;
    protected RectangleYio infoLabelPosition;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    public BackgroundYio getButtonBackground() {
        return BackgroundYio.white;
    }


    protected void initInfoLabelPosition() {
        infoLabelPosition = new RectangleYio(0.05, 0.1, 0.9, 0.7);
    }


    protected void createInfoMenu(String key, Reaction backButtonBehavior) {
        backButton = spawnBackButton(backButtonBehavior);

        initInfoLabelPosition();
        infoPanel = uiFactory.getButton()
                .setPosition(infoLabelPosition.x, infoLabelPosition.y, infoLabelPosition.width, infoLabelPosition.height)
                .setTouchable(false)
                .setFont(Fonts.miniFont)
                .setAnimation(AnimationYio.from_touch)
                .applyManyTextLines(key);
    }
}
