package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.menu.elements.highlight_area.HighlightAreaElement;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;

public class SceneHighlightArea extends ModalSceneYio {


    public HighlightAreaElement highlightAreaElement;


    @Override
    protected void initialize() {
        highlightAreaElement = uiFactory.getHighlightAreaElement()
                .setSize(0.1)
                .centerHorizontal()
                .centerVertical();
    }


    @Override
    protected void onAppear() {
        forceElementsToTop();
    }
}
