package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.RelationType;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.resizable_element.RveChooseConditionTypeItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveRelationItem;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class SceneSetupRelationCondition extends ModalSceneYio implements IEntityChoiceListener{

    RveChooseConditionTypeItem rveChooseConditionTypeItem;
    SliderElement sliderRelation;
    SliderElement sliderLock;
    ButtonYio targetButton;
    private int[] lockValues;
    PlayerEntity targetEntity;


    @Override
    protected void initialize() {
        createCloseButton();
        createDefaultPanel(0.32);
        defaultPanel.setKey("setup_relation_panel");
        initValues();
        createInternals();
    }


    private void initValues() {
        lockValues = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    }


    private void createInternals() {
        targetButton = uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.6, 0.055)
                .centerHorizontal()
                .alignTop(0.02)
                .setBackground(BackgroundYio.gray)
                .applyText("-")
                .setReaction(getTargetReaction());

        sliderRelation = uiFactory.getSlider()
                .setParent(defaultPanel)
                .setWidth(0.7)
                .centerHorizontal()
                .alignUnder(previousElement, 0.035)
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
        sliderRelation.setValueIndex(2);
        sliderLock.setValueIndex(0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RveRelationItem rveRelationItem = new RveRelationItem();
        if (getViewableModel().entitiesManager.getCurrentEntity() == targetEntity) {
            targetEntity = null;
        }
        rveRelationItem.setValues(
                targetEntity,
                RelationType.values()[sliderRelation.getValueIndex()],
                lockValues[sliderLock.getValueIndex()]
        );
        rveRelationItem.setArrowUpMode(false);
        Scenes.composeLetter.rvElement.swapItem(rveChooseConditionTypeItem, rveRelationItem);
    }


    private Reaction getTargetReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.chooseEntity.create();
                Scenes.chooseEntity.setListener(SceneSetupRelationCondition.this);
            }
        };
    }


    @Override
    public void onEntityChosen(PlayerEntity playerEntity) {
        setTargetEntity(playerEntity);
    }


    public void setTargetEntity(PlayerEntity targetEntity) {
        this.targetEntity = targetEntity;
        targetButton.applyText(languagesManager.getString("target") + ": " + targetEntity.name);
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


    public void setRveChooseConditionTypeItem(RveChooseConditionTypeItem rveChooseConditionTypeItem) {
        this.rveChooseConditionTypeItem = rveChooseConditionTypeItem;
    }
}
