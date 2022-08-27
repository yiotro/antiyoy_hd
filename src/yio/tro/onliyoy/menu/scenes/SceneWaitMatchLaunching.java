package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.net.NetProcessViewElement;

public class SceneWaitMatchLaunching extends AbstractNetWaitScene{


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void onInitialized() {
        netProcessViewElement.alignBottom(0.47);
    }


    @Override
    protected String getKey() {
        return "match_is_starting";
    }

}

