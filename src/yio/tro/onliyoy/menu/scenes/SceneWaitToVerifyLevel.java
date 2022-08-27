package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;

public class SceneWaitToVerifyLevel extends AbstractNetWaitScene{

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    protected String getKey() {
        return "requesting_user_level";
    }
}
