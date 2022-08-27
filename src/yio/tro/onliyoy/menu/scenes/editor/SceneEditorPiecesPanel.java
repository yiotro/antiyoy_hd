package yio.tro.onliyoy.menu.scenes.editor;

import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.editor.EditorPanelElement;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneEditorPiecesPanel extends ModalSceneYio {


    private EditorPanelElement editorPanelElement;


    @Override
    protected void initialize() {
        double rowHeight = 2 * (EditorPanelElement.bRadius / GraphicsYio.height);
        editorPanelElement = uiFactory.getEditorPanelElement()
                .setSize(1, rowHeight)
                .centerHorizontal()
                .alignBottom(0.01)
                .setAnimation(AnimationYio.down);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        editorPanelElement.initPiecesButtons();
    }
}
