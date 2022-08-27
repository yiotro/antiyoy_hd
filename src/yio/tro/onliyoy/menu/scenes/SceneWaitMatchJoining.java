package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.net.NetProcessViewElement;

public class SceneWaitMatchJoining extends AbstractNetWaitScene {

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.yellow;
    }


    @Override
    protected String getKey() {
        return "joining_match";
    }
}