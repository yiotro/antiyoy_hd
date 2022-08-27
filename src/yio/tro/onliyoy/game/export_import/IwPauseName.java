package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.game.viewable_model.ViewableModel;

public class IwPauseName extends AbstractImportWorker{

    ViewableModel viewableModel;


    public IwPauseName(ViewableModel viewableModel) {
        this.viewableModel = viewableModel;
    }


    @Override
    protected String getDefaultSectionName() {
        return "pause_name";
    }


    @Override
    protected void apply() {
        if (source.length() < 2) return;
        viewableModel.setPauseName(source);
    }
}
