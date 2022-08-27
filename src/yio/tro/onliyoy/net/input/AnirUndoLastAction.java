package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.game.viewable_model.ViewableModel;

public class AnirUndoLastAction extends AbstractNetInputReaction{

    @Override
    public void apply() {
        ViewableModel viewableModel = objectsLayer.viewableModel;
        if (viewableModel == null) return;
        viewableModel.onUndoRequested();
    }
}
