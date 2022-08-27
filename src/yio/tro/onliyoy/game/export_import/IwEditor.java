package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.editor.EditorManager;

public class IwEditor extends AbstractImportWorker {

    EditorManager editorManager;


    public IwEditor(EditorManager editorManager) {
        this.editorManager = editorManager;
    }


    @Override
    protected String getDefaultSectionName() {
        return "editor";
    }


    @Override
    protected void apply() {
        if (source.length() == 0) return;
        editorManager.setTimeSpentMaking(Long.valueOf(source));
    }
}
