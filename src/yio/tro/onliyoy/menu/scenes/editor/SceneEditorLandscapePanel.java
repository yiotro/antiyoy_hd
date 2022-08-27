package yio.tro.onliyoy.menu.scenes.editor;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.editor.EditorPanelElement;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneEditorLandscapePanel extends ModalSceneYio {


    private EditorPanelElement editorPanelElement;


    @Override
    protected void initialize() {
        double rowHeight = 3 * (EditorPanelElement.bRadius / GraphicsYio.height);
        editorPanelElement = uiFactory.getEditorPanelElement()
                .setSize(1, 2 * rowHeight)
                .centerHorizontal()
                .alignBottom(0.01)
                .setAnimation(AnimationYio.down);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        editorPanelElement.initLandscapeButtons();
    }
}
