package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.Relation;
import yio.tro.onliyoy.game.core_model.RelationType;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.resizable_element.RveChooseConditionTypeItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveNotificationItem;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneSetupRveNotification extends ModalSceneYio {

    RveChooseConditionTypeItem rveChooseConditionTypeItem;
    SliderElement sliderElement;
    PlayerEntity recipient;


    @Override
    protected void initialize() {
        createCloseButton();
        createDefaultPanel(0.15);
        createInternals();
    }


    private void createInternals() {
        sliderElement = uiFactory.getSlider()
                .setParent(defaultPanel)
                .centerHorizontal()
                .alignTop(0.04)
                .setTitle("relation")
                .setPossibleValues(RelationType.class);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        PlayerEntity currentEntity = getViewableModel().entitiesManager.getCurrentEntity();
        Relation relation = currentEntity.getRelation(recipient);
        int index = indexOf(relation.type);
        String[] values = new String[index];
        for (int i = 0; i < values.length; i++) {
            values[i] = LanguagesManager.getInstance().getString("" + RelationType.values()[i]);
        }
        sliderElement.setPossibleValues(values);
        sliderElement.setValueIndex(0);
    }


    public void checkToSkipThisStep() {
        if (sliderElement.getPossibleValues().length > 1) return;
        destroy();
    }


    private int indexOf(RelationType relationType) {
        for (int i = 0; i < RelationType.values().length; i++) {
            if (RelationType.values()[i] == relationType) return i;
        }
        return -1;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RveNotificationItem rveNotificationItem = new RveNotificationItem();
        rveNotificationItem.setRelationType(RelationType.values()[sliderElement.getValueIndex()]);
        Scenes.composeLetter.rvElement.swapItem(rveChooseConditionTypeItem, rveNotificationItem);
    }


    public void setRveChooseConditionTypeItem(RveChooseConditionTypeItem rveChooseConditionTypeItem) {
        this.rveChooseConditionTypeItem = rveChooseConditionTypeItem;
    }


    public void setRecipient(PlayerEntity recipient) {
        this.recipient = recipient;
    }
}
