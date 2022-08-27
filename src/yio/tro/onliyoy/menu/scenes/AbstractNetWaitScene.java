package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.net.NetProcessViewElement;

public abstract class AbstractNetWaitScene extends SceneYio{

    public NetProcessViewElement netProcessViewElement;


    public abstract BackgroundYio getBackgroundValue();


    protected abstract String getKey();


    @Override
    protected void initialize() {
        netRoot.postponedReactionsManager.onNetWaitSceneInitialized(this);
        netProcessViewElement = uiFactory.getNetProcessViewElement()
                .setSize(0.01)
                .centerHorizontal()
                .centerVertical()
                .setTitle(getKey());
        onInitialized();
    }


    protected void onInitialized() {

    }


    public boolean isSomethingMovingCurrently() {
        return isCurrentlyVisible() && netProcessViewElement.getFactor().getValue() < 1;
    }


    @Override
    public boolean isOnlineTargeted() {
        return true;
    }
}
