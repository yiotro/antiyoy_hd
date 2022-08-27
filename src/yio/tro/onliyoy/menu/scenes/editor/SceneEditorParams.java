package yio.tro.onliyoy.menu.scenes.editor;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.export_import.IwCoreDiplomacy;
import yio.tro.onliyoy.menu.elements.CheckButtonYio;
import yio.tro.onliyoy.menu.elements.ConditionYio;
import yio.tro.onliyoy.menu.elements.setup_entities.CondensedEntitiesViewElement;
import yio.tro.onliyoy.menu.elements.setup_entities.EntitiesSetupElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetRole;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SceneEditorParams extends ModalSceneYio {


    private EntitiesSetupElement entitiesSetupElement;
    private CondensedEntitiesViewElement condensedEntitiesViewElement;
    private CheckButtonYio chkDiplomacy;
    HashMap<String, Reaction> topMap;
    private CheckButtonYio chkFogOfWar;


    @Override
    protected void initialize() {
        createCloseButton();
        createPanel();
        createTopButtons();
        createCheckButtons();
        createEntitiesSetupElement();
        createCondensedEntitiesView();
    }


    private void createCondensedEntitiesView() {
        condensedEntitiesViewElement = uiFactory.getCondensedEntitiesViewElement()
                .setParent(defaultPanel)
                .setSize(0.72, 0.2)
                .centerHorizontal()
                .setReadOnly(false)
                .alignUnder(chkFogOfWar, 0.03)
                .setAllowedToAppear(getCondensedEntitiesCondition());
    }


    private ConditionYio getCondensedEntitiesCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return !canEseBeUsed();
            }
        };
    }


    private void createCheckButtons() {
        chkDiplomacy = uiFactory.getCheckButton()
                .setParent(defaultPanel)
                .setSize(0.8, 0.06)
                .centerHorizontal()
                .alignUnder(previousElement, 0.02)
                .setName("diplomacy");

        chkFogOfWar = uiFactory.getCheckButton()
                .setParent(defaultPanel)
                .setSize(0.8, 0.06)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setName("fog_of_war");
    }


    private void createEntitiesSetupElement() {
        entitiesSetupElement = uiFactory.getEntitiesSetupElement()
                .setParent(defaultPanel)
                .setSize(0.72, 0.2)
                .centerHorizontal()
                .setCalmAnimationMode(true)
                .setAllowedToAppear(getEseCondition())
                .setTitleCentered(true)
                .alignUnder(previousElement, 0.03);
    }


    private ConditionYio getEseCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                return canEseBeUsed();
            }
        };
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        loadEntities();
        chkDiplomacy.setChecked(getViewableModel().diplomacyManager.enabled);
        chkFogOfWar.setChecked(getViewableModel().fogOfWarManager.enabled);
    }


    private void loadEntities() {
        getObjectsLayer().editorManager.prepareLevelForSave(); // to fix entities
        PlayerEntity[] entities = getViewableModel().entitiesManager.entities;
        if (canEseBeUsed()) {
            entitiesSetupElement.copyFrom(entities);
        } else {
            condensedEntitiesViewElement.loadValues(entities);
        }
    }


    private int countColors() {
        ArrayList<HColor> colors = new ArrayList<>();
        for (Hex hex : getViewableModel().hexes) {
            if (hex.isNeutral()) continue;
            if (colors.contains(hex.color)) continue;
            colors.add(hex.color);
        }
        return colors.size();
    }


    private boolean canEseBeUsed() {
        return countColors() <= 8;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        applyValues();
    }


    private void applyValues() {
        String backupCode = backupRelations();
        if (canEseBeUsed()) {
            checkToFixEntities();
            entitiesSetupElement.applyToModel(getViewableModel());
        } else {
            condensedEntitiesViewElement.applyToModel(getViewableModel());
        }
        getViewableModel().diplomacyManager.resetRelations();
        (new IwCoreDiplomacy(getViewableModel())).perform(backupCode);
        getViewableModel().diplomacyManager.setEnabled(chkDiplomacy.isChecked());
        getViewableModel().fogOfWarManager.setEnabled(chkFogOfWar.isChecked());
    }


    private String backupRelations() {
        ExportParameters exportParameters = new ExportParameters();
        exportParameters.setCoreModel(getViewableModel());
        return getGameController().objectsLayer.exportManager.perform(exportParameters);
    }


    private void checkToFixEntities() {
        if (entitiesSetupElement.countColoredItems() == 0) {
            entitiesSetupElement.addDefaultItems();
            return;
        }
        if (entitiesSetupElement.countColoredItems() == 1) {
            entitiesSetupElement.onPlusClicked();
        }
    }


    private void createTopButtons() {
        initTopMap();
        double bSize = 0.09;
        double offset = 0.05;
        double rowWidth = topMap.size() * bSize + (topMap.size() - 1) * offset;
        double x = 0.5 - rowWidth / 2;

        for (Map.Entry<String, Reaction> entry : topMap.entrySet()) {
            uiFactory.getButton()
                    .setParent(defaultPanel)
                    .setSize(bSize)
                    .alignLeft(x)
                    .alignTop(GraphicsYio.convertToHeight(offset / 2))
                    .setTouchOffset(offset / 2)
                    .loadCustomTexture(entry.getKey())
                    .setSelectionTexture(getSelectionTexture())
                    .setReaction(entry.getValue());
            x += bSize + offset;
        }
    }


    private void initTopMap() {
        topMap = new LinkedHashMap<>();
        topMap.put("menu/editor/dice.png", getRandomizeReaction());
        topMap.put("menu/editor/trash_bin.png", getClearLevelReaction());
        if (canShareLevel()) {
            topMap.put("menu/editor/share.png", getShareReaction());
        }
    }


    private Reaction getShareReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Scenes.confirmShareLevel.create();
            }
        };
    }


    private boolean canShareLevel() {
        if (netRoot.offlineMode) return false;
        if (netRoot.isInLocalMode()) return true;
        return netRoot.userData.role.ordinal() > NetRole.guest.ordinal();
    }


    private Reaction getClearLevelReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Scenes.confirmClearLevel.create();
            }
        };
    }


    private Reaction getRandomizeReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                destroy();
                Scenes.randomizePanel.create();
            }
        };
    }


    private void createPanel() {
        createDefaultPanel(0.45);
    }
}
