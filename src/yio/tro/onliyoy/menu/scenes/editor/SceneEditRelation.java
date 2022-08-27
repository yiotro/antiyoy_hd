package yio.tro.onliyoy.menu.scenes.editor;

import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.Relation;
import yio.tro.onliyoy.game.core_model.RelationType;
import yio.tro.onliyoy.game.core_model.events.EventSetRelationSoftly;
import yio.tro.onliyoy.game.core_model.events.EventsManager;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneEditRelation extends ModalSceneYio {

    SliderElement sliderRelation;
    SliderElement sliderLock;
    private int[] lockValues;
    PlayerEntity entity1;
    PlayerEntity entity2;


    @Override
    protected void initialize() {
        createCloseButton();
        createDefaultPanel(0.29);
        defaultPanel.setTitle("relation");
        initValues();
        createInternals();
    }


    private void createInternals() {
        sliderRelation = uiFactory.getSlider()
                .setParent(defaultPanel)
                .setWidth(0.7)
                .centerHorizontal()
                .alignTop(0.08)
                .setTitle("relation")
                .setPossibleValues(RelationType.class);

        sliderLock = uiFactory.getSlider()
                .setParent(defaultPanel)
                .setWidth(0.7)
                .centerHorizontal()
                .alignUnder(previousElement, 0.01)
                .setTitle("lock")
                .setPossibleValues(lockValues);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        defaultPanel.setTitle(entity1.name + " - " + entity2.name);
        Relation relation = entity1.getRelation(entity2);
        setRelationType(relation.type);
        setLock(relation.lock);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        applyValues();
    }


    private void applyValues() {
        RelationType targetRelationType = RelationType.values()[sliderRelation.getValueIndex()];
        int lock = lockValues[sliderLock.getValueIndex()];
        Relation relation = entity1.getRelation(entity2);
        relation.lock = 0;
        EventsManager eventsManager = getViewableModel().eventsManager;
        EventSetRelationSoftly event = eventsManager.factory.createEventSetRelationSoftly(
                targetRelationType,
                entity1.color,
                entity2.color,
                lock
        );
        eventsManager.applyEvent(event);
    }


    public void setRelationType(RelationType relationType) {
        for (int i = 0; i < RelationType.values().length; i++) {
            if (RelationType.values()[i] != relationType) continue;
            sliderRelation.setValueIndex(i);
            return;
        }
        sliderRelation.setValueIndex(0);
    }


    public void setLock(int value) {
        for (int i = 0; i < lockValues.length; i++) {
            if (lockValues[i] != value) continue;
            sliderLock.setValueIndex(i);;
            return;
        }
        sliderLock.setValueIndex(0);
    }


    private void initValues() {
        lockValues = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 999};
    }


    public void setEntity1(PlayerEntity entity1) {
        this.entity1 = entity1;
    }


    public void setEntity2(PlayerEntity entity2) {
        this.entity2 = entity2;
    }
}
