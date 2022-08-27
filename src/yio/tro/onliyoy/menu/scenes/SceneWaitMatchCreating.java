package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.net.NetProcessViewElement;

public class SceneWaitMatchCreating extends AbstractNetWaitScene{


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected String getKey() {
        return "creating_match";
    }
}
