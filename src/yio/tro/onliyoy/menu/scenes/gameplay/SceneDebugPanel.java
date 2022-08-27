package yio.tro.onliyoy.menu.scenes.gameplay;

import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.Relation;
import yio.tro.onliyoy.game.core_model.RelationType;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.elements.CheckButtonYio;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NmType;

public class SceneDebugPanel extends ModalSceneYio {


    public static int clickMode = 0;
    private CheckButtonYio chkDebugEnabled;
    private CheckButtonYio chkDirectRender;
    private SliderElement sliderElement;
    private CheckButtonYio chkShowRefModel;


    @Override
    protected void initialize() {
        createCloseButton();
        createDefaultPanel(0.56);
        createInternals();
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        sliderElement.setValueIndex(clickMode);
        chkDebugEnabled.setChecked(DebugFlags.debugEnabled);
        chkDirectRender.setChecked(DebugFlags.directRender);
        chkShowRefModel.setChecked(DebugFlags.showRefModel);
    }


    private void createInternals() {
        createCheckButtons();
        createSlider();
        createButtons();
    }


    private void createSlider() {
        sliderElement = uiFactory.getSlider()
                .setParent(defaultPanel)
                .alignUnder(previousElement, 0.03)
                .centerHorizontal()
                .setTitle("OnClick")
                .setPossibleValues(new String[]{"apply", "province", "inspect"})
                .setValueChangeReaction(getSliderReaction());
    }


    private Reaction getSliderReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                clickMode = sliderElement.getValueIndex();
                chkDebugEnabled.setChecked(true);
                onChkDebugEnabledPressed();
            }
        };
    }


    private void createButtons() {
        uiFactory.getButton()
                .setParent(defaultPanel)
                .setSize(0.8, 0.06)
                .centerHorizontal()
                .setBackground(BackgroundYio.gray)
                .alignBottom(0.04)
                .applyText("Open in editor")
                .setReaction(getOpenInEditorReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .centerHorizontal()
                .alignAbove(previousElement, 0.01)
                .applyText("Apply action")
                .setReaction(getApplyActionReaction());
    }


    private Reaction getApplyActionReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                applyAction();
            }
        };
    }


    public void applyAction() {
        netRoot.sendMessage(NmType.request_fake_event, "");
        Scenes.notification.show("Action applied");
    }


    private void doRandomizeRelations() {
        ViewableModel viewableModel = getObjectsLayer().viewableModel;
        PlayerEntity[] entities = viewableModel.entitiesManager.entities;
        RelationType relationType;
        RelationType[] types = RelationType.values();
        for (int i = 0; i < entities.length; i++) {
            PlayerEntity entity1 = entities[i];
            for (int j = i + 1; j < entities.length; j++) {
                PlayerEntity entity2 = entities[j];
                relationType = types[YioGdxGame.random.nextInt(types.length)];
                Relation relation = entity1.getRelation(entity2);
                relation.setType(relationType);
                relation.setLock(YioGdxGame.random.nextInt(6));
            }
        }
    }


    private Reaction getExportReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                doApplyExport();
            }
        };
    }


    private void doApplyExport() {
        GameController gameController = yioGdxGame.gameController;
        ObjectsLayer objectsLayer = getObjectsLayer();
        ExportParameters instance = ExportParameters.getInstance();
        instance.setCameraCode(gameController.cameraController.encode());
        instance.setInitialLevelSize(gameController.sizeManager.initialLevelSize);
        instance.setCoreModel(objectsLayer.viewableModel);
        instance.setHistoryManager(objectsLayer.historyManager);
        instance.setAiVersionCode(objectsLayer.aiManager.getUpdatedAiVersionCode());
        String string = objectsLayer.exportManager.performToClipboard(instance);
        Scenes.notification.show("Exported to clipboard");
        System.out.println("Exported: " + string);
    }


    private Reaction getOpenInEditorReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                if (getViewableModel().isNetMatch()) return;
                destroy();
                openCurrentLevelInEditor();
            }
        };
    }


    void openCurrentLevelInEditor() {
        Scenes.openInEditor.create();
    }


    private void createCheckButtons() {
        chkDebugEnabled = uiFactory.getCheckButton()
                .setParent(defaultPanel)
                .setName("Debug enabled")
                .alignTop(0.02)
                .setReaction(getDebugEnabledReaction());

        chkDirectRender = uiFactory.getCheckButton()
                .setParent(defaultPanel)
                .setName("Direct render")
                .alignUnder(previousElement, 0)
                .setReaction(getDirectRenderReaction());

        chkShowRefModel = uiFactory.getCheckButton()
                .setParent(defaultPanel)
                .setName("Show ref model")
                .alignUnder(previousElement, 0)
                .setReaction(getShowRefModelReaction());
    }


    private Reaction getShowRefModelReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                DebugFlags.showRefModel = chkShowRefModel.isChecked();
            }
        };
    }


    private Reaction getDirectRenderReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onChkDirectRenderPressed();
            }
        };
    }


    private void onChkDirectRenderPressed() {
        DebugFlags.directRender = chkDirectRender.isChecked();
    }


    private Reaction getDebugEnabledReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onChkDebugEnabledPressed();
            }
        };
    }


    private void onChkDebugEnabledPressed() {
        DebugFlags.debugEnabled = chkDebugEnabled.isChecked();
        getGameController().resetTouchMode();
    }

}
