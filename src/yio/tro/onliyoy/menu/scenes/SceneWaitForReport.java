package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;

public class SceneWaitForReport extends AbstractNetWaitScene{

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    protected String getKey() {
        return "loading_user_level";
    }
}
