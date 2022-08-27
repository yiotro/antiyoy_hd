package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.menu.elements.setup_entities.EntitiesSetupElement;
import yio.tro.onliyoy.menu.elements.setup_entities.EseItem;
import yio.tro.onliyoy.menu.elements.setup_entities.SingleEntityConfigureElement;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneSingleEntityConfigure extends ModalSceneYio {


    public SingleEntityConfigureElement singleEntityConfigureElement;
    double bottomOffset = 0.17;


    @Override
    protected void initialize() {
        createCloseButton();
        createSecElement();
    }


    private void createSecElement() {
        singleEntityConfigureElement = uiFactory.getSingleEntityConfigureElement()
                .setSize(0.7, 0.35)
                .centerHorizontal()
                .alignBottom(0)
                .setAppearParameters(MovementType.inertia, 1.7)
                .setDestroyParameters(MovementType.inertia, 1.7);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        singleEntityConfigureElement.alignBottom(bottomOffset);
    }


    public void setBottomOffset(double bottomOffset) {
        this.bottomOffset = bottomOffset;
    }


    public void setItem(EntitiesSetupElement esElement, EseItem eseItem) {
        singleEntityConfigureElement.loadValues(esElement, eseItem);
    }
}
