package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;

public class SceneWaitForRejoin extends AbstractNetWaitScene{

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.yellow;
    }


    @Override
    protected String getKey() {
        return "return_to_match_noun";
    }
}
