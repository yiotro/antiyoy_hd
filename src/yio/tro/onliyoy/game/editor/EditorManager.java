package yio.tro.onliyoy.game.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.YioGdxGame;
import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.core_provinces.ProvincesManager;
import yio.tro.onliyoy.game.core_model.events.HistoryManager;
import yio.tro.onliyoy.game.core_model.generators.AbstractLevelGenerator;
import yio.tro.onliyoy.game.core_model.generators.LevelGeneratorFactory;
import yio.tro.onliyoy.game.core_model.generators.LgParameters;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.export_import.IwClientInit;
import yio.tro.onliyoy.game.export_import.IwCoreDiplomacy;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.IGameplayManager;
import yio.tro.onliyoy.game.general.ObjectsLayer;
import yio.tro.onliyoy.game.loading.LoadingType;
import yio.tro.onliyoy.game.save_system.SaveType;
import yio.tro.onliyoy.game.save_system.SavesManager;
import yio.tro.onliyoy.game.save_system.SmItem;
import yio.tro.onliyoy.game.touch_modes.TouchMode;
import yio.tro.onliyoy.game.viewable_model.ProvinceSelectionManager;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.game.viewable_model.VmCacheManager;
import yio.tro.onliyoy.menu.LanguagesManager;
import yio.tro.onliyoy.menu.elements.editor.EpbType;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RepeatYio;
import yio.tro.onliyoy.stuff.object_pool.ObjectPoolYio;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditorManager implements IGameplayManager, Encodeable {

    ObjectsLayer objectsLayer;
    public EpbType chosenType;
    public ArrayList<ViewableChange> viewableChanges;
    ObjectPoolYio<ViewableChange> poolViewableChanges;
    PointYio tempPoint;
    EmHexAdditionWorker hexAdditionWorker;
    RepeatYio<EditorManager> repeatUpdateCache;
    EmPieceAdditionWorker pieceAdditionWorker;
    EmProvinceUpdater provinceUpdater;
    public static String currentSlotKey = "";
    long timeSpentMaking;
    RepeatYio<EditorManager> repeatUpdateTimeSpent;
    private EmEntitiesFixer entitiesFixer;


    public EditorManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        viewableChanges = new ArrayList<>();
        tempPoint = new PointYio();
        hexAdditionWorker = new EmHexAdditionWorker(this);
        pieceAdditionWorker = new EmPieceAdditionWorker(this);
        provinceUpdater = new EmProvinceUpdater(this);
        entitiesFixer = new EmEntitiesFixer(objectsLayer);
        initPools();
        initRepeats();
        defaultValues();
    }


    private void initRepeats() {
        repeatUpdateCache = new RepeatYio<EditorManager>(this, 6) {
            @Override
            public void performAction() {
                parent.checkToUpdateCache();
            }
        };
        repeatUpdateTimeSpent = new RepeatYio<EditorManager>(this, 60) {
            @Override
            public void performAction() {
                parent.updateTimeSpent();
            }
        };
    }


    private void initPools() {
        poolViewableChanges = new ObjectPoolYio<ViewableChange>(viewableChanges) {
            @Override
            public ViewableChange makeNewObject() {
                return new ViewableChange(EditorManager.this);
            }
        };
    }


    @Override
    public void defaultValues() {
        timeSpentMaking = 0;
    }


    public boolean isAllowedToUpdateCache() {
        if (TouchMode.tmEditor.touchedCurrently) return false;
        if (isSomethingMoving()) return false;
        return true;
    }


    boolean isSomethingMoving() {
        for (ViewableChange change : viewableChanges) {
            if (change.isColorFactorInMovementMode()) return true;
            if (change.isPieceFactorInMovementMode()) return true;
        }
        return false;
    }


    @Override
    public void onBasicStuffCreated() {
        if (!isEnabled()) return;

    }


    ViewableModel getViewableModel() {
        return objectsLayer.viewableModel;
    }


    @Override
    public void moveActually() {
        if (!isEnabled()) return;
    }


    @Override
    public void moveVisually() {
        if (!isEnabled()) return;
        moveChanges();
        repeatUpdateCache.move();
        repeatUpdateTimeSpent.move();
    }


    private void updateTimeSpent() {
        timeSpentMaking += 1000;
    }


    @Override
    public String encode() {
        return timeSpentMaking + "";
    }


    void checkToUpdateCache() {
        if (viewableChanges.size() == 0) return;
        if (!isAllowedToUpdateCache()) return;
        boolean containsHexErasingChanges = containsHexErasingChanges();
        applyViewableChanges();
        VmCacheManager cacheManager = getViewableModel().cacheManager;
        if (containsHexErasingChanges) {
            cacheManager.setBackgroundVisible(true);
        }
        cacheManager.applyUpdate(true);
    }


    private boolean containsHexErasingChanges() {
        for (ViewableChange change : viewableChanges) {
            if (change.previousColor == null) continue;
            if (change.currentColor != null) continue;
            return true;
        }
        return false;
    }


    private void applyViewableChanges() {
        for (ViewableChange change : viewableChanges) {
            if (change.currentColor != null) {
                change.hex.color = change.currentColor;
            }
        }
        poolViewableChanges.clearExternalList();
    }


    private void moveChanges() {
        for (ViewableChange change : viewableChanges) {
            change.move();
        }
    }


    public void onHexClicked(Hex touchedHex) {
        pieceAdditionWorker.onHexClicked(touchedHex);
        checkToSelectProvince(touchedHex);
    }


    private void checkToSelectProvince(Hex touchedHex) {
        if (chosenType != null) return;
        provinceUpdater.apply();
        Province province = touchedHex.getProvince();
        ProvinceSelectionManager provinceSelectionManager = getViewableModel().provinceSelectionManager;
        provinceSelectionManager.changeSelectionExternally(province);
        if (province != null && provinceSelectionManager.selectedProvince != null) {
            Scenes.editProvince.create();
        }
    }


    public void deselectProvince() {
        getViewableModel().provinceSelectionManager.changeSelectionExternally(null);
        Scenes.editProvince.destroy();
    }


    public void onHexTouched(Hex touchedHex) {
        if (chosenType == null) return;
        if (touchedHex == null) return;
        checkToChangeColor(touchedHex);
        checkToRemoveHex(touchedHex);
        checkToRemovePiece(touchedHex);
    }


    private void checkToRemovePiece(Hex touchedHex) {
        if (chosenType != EpbType.piece_eraser) return;
        if (touchedHex.isEmpty()) return;
        touchedHex.setPiece(null);
        touchedHex.setUnitId(-1);
        getViewableModel().cacheManager.applyUpdate(true);
        SoundManager.playSound(SoundType.undo);
    }


    private void checkToRemoveHex(Hex touchedHex) {
        if (chosenType != EpbType.hex_eraser) return;
        removeHexViaViewableChange(touchedHex);
        SoundManager.playSound(SoundType.undo);
    }


    private void removeHexViaViewableChange(Hex touchedHex) {
        getViewableModel().removeHex(touchedHex);
        ViewableChange freshObject = poolViewableChanges.getFreshObject();
        freshObject.setHex(touchedHex);
        freshObject.currentColor = touchedHex.color;
        freshObject.applyColorChange(null);
    }


    private void checkToChangeColor(Hex touchedHex) {
        if (!hexAdditionWorker.isColorType(chosenType)) return;
        HColor color = convertToColor(chosenType);
        if (color == touchedHex.color) {
            SoundManager.playSound(SoundType.back);
            return;
        }
        ViewableChange change = getChange(touchedHex);
        if (change == null) {
            change = poolViewableChanges.getFreshObject();
            change.setHex(touchedHex);
            SoundManager.playSound(SoundType.slider_change);
        }
        change.applyColorChange(color);
    }


    ViewableChange getChange(Hex hex) {
        for (ViewableChange change : viewableChanges) {
            if (change.hex == hex) return change;
        }
        return null;
    }


    public void onPointTouched(PointYio touchPoint) {
        hexAdditionWorker.onPointTouched(touchPoint);
    }


    public void onClickedOutside() {
        deselectProvince();
    }


    public void onExitedToPauseMenu() {
        applySaveToCurrentSlot();
    }


    private void doUpdateEntityNames() {
        doFixMissingEntities();
        // this method transfers city names to entities
        ProvincesManager provincesManager = getViewableModel().provincesManager;
        for (PlayerEntity playerEntity : getViewableModel().entitiesManager.entities) {
            Province province = provincesManager.getLargestProvince(playerEntity.color);
            if (province == null) continue;
            playerEntity.setName(province.getCityName());
        }
    }


    public void applySaveToCurrentSlot() {
        if (currentSlotKey.length() == 0) return;
        if (getGameController().yioGdxGame.alreadyShownErrorMessageOnce) return;
        prepareLevelForSave();
        SavesManager savesManager = getGameController().savesManager;
        savesManager.rewriteLevelCode(currentSlotKey, exportLevelCode());
    }


    public void prepareLevelForSave() {
        provinceUpdater.apply();
        doUpdateEntityNames();
        getViewableModel().readinessManager.reset();
    }


    public void onGraphNodeAdded(Hex hex) {
        ViewableChange freshObject = poolViewableChanges.getFreshObject();
        freshObject.setHex(hex);
        HColor color = convertToColor(chosenType);
        freshObject.currentColor = null; // to enable fade in
        freshObject.applyColorChange(color);
        SoundManager.playSound(SoundType.tick);
    }


    HColor convertToColor(EpbType epbType) {
        if (!hexAdditionWorker.isColorType(epbType)) return null;
        return HColor.valueOf("" + epbType);
    }


    public void onLaunchButtonPressed() {
        doFixMissingEntities();
        doFixProvincesWithoutCities();
        prepareHistoryManagerForLaunch();
        getViewableModel().readinessManager.update();
        String levelCode = exportLevelCode();
        YioGdxGame yioGdxGame = getGameController().yioGdxGame;
        (new IwClientInit(yioGdxGame, LoadingType.training_import)).perform(levelCode);
    }


    public void onCompletionCheckRequested() {
        doFixTooManyHumansForUserLevel();
        doFixMissingEntities();
        doFixProvincesWithoutCities();
        prepareHistoryManagerForLaunch();
        String levelCode = exportLevelCode();
        YioGdxGame yioGdxGame = getGameController().yioGdxGame;
        yioGdxGame.netRoot.tempUlTransferData.creationTime = timeSpentMaking;
        yioGdxGame.netRoot.tempUlTransferData.levelCode = levelCode;
        (new IwClientInit(yioGdxGame, LoadingType.completion_check)).perform(levelCode);
    }


    void doFixMissingEntities() {
        String backupCode = backupRelations();
        entitiesFixer.doFixEntities();
        getViewableModel().diplomacyManager.resetRelations();
        (new IwCoreDiplomacy(getViewableModel())).perform(backupCode);
    }


    private String backupRelations() {
        ExportParameters exportParameters = new ExportParameters();
        exportParameters.setCoreModel(getViewableModel());
        return getGameController().objectsLayer.exportManager.perform(exportParameters);
    }


    private void doFixTooManyHumansForUserLevel() {
        int humansQuantity = getViewableModel().entitiesManager.count(EntityType.human);
        if (humansQuantity < 2) return;
        boolean skipped = false;
        for (PlayerEntity entity : getViewableModel().entitiesManager.entities) {
            if (entity.type != EntityType.human) continue;
            if (!skipped) {
                skipped = true;
                continue;
            }
            entity.type = EntityType.ai_balancer;
        }
    }


    private void doFixProvincesWithoutCities() {
        getViewableModel().cityManager.doFixProvincesWithoutCities();
    }


    private void prepareHistoryManagerForLaunch() {
        HistoryManager historyManager = objectsLayer.historyManager;
        CoreModel startingPosition = historyManager.startingPosition;
        historyManager.clearAll();
        startingPosition.buildSimilarGraph(getViewableModel());
        startingPosition.setBy(getViewableModel());
    }


    public void onAppPaused() {
        if (!isEnabled()) return;
        applySaveToCurrentSlot();
    }


    public static void prepareNewSaveSlot(SavesManager savesManager) {
        String name = LanguagesManager.getInstance().getString("slot") + "  " + getDateString();
        SmItem smItem = savesManager.addItem(SaveType.editor, name, "");
        currentSlotKey = smItem.key;
    }


    private GameController getGameController() {
        return objectsLayer.gameController;
    }


    public static String getDateString() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }


    public void performExportToClipboard() {
        doFixProvincesWithoutCities();
        prepareHistoryManagerForLaunch();
        String levelCode = exportLevelCode();
        Clipboard clipboard = Gdx.app.getClipboard();
        clipboard.setContents(levelCode);
        Scenes.notification.show("level_exported");
    }


    private String exportLevelCode() {
        doFixTurnIndex();
        ExportParameters instance = ExportParameters.getInstance();
        instance.setCoreModel(getViewableModel());
        instance.setCameraCode(getGameController().cameraController.encode());
        instance.setInitialLevelSize(getGameController().sizeManager.initialLevelSize);
        instance.setHistoryManager(objectsLayer.historyManager);
        instance.setEditorManager(this);
        instance.setPauseName(extractSlotName());
        return objectsLayer.exportManager.perform(instance);
    }


    private String extractSlotName() {
        if (EditorManager.currentSlotKey.length() == 0) return "-";
        SmItem smItem = getGameController().savesManager.getItem(EditorManager.currentSlotKey);
        if (smItem == null) return "-";
        return smItem.name;
    }


    private void doFixTurnIndex() {
        getViewableModel().turnsManager.turnIndex = 0;
    }


    public void onClearButtonPressed() {
        ArrayList<Hex> tempList = new ArrayList<>(getViewableModel().hexes);
        for (Hex hex : tempList) {
            removeHexViaViewableChange(hex);
        }
        SoundManager.playSound(SoundType.turn_end);
    }


    public void onGenerateButtonPressed(LgParameters lgParameters) {
        VmCacheManager cacheManager = getViewableModel().cacheManager;
        cacheManager.onEditorRandomGenerationBegan();
        AbstractLevelGenerator generator = LevelGeneratorFactory.create(getViewableModel());
        generator.generate(lgParameters);
        cacheManager.setBackgroundVisible(true);
        cacheManager.applyUpdate(true);
        applyDefaultValuesToAllProvinces();
        SoundManager.playSound(SoundType.hold_to_march);
    }


    private void applyDefaultValuesToAllProvinces() {
        for (Province province : getViewableModel().provincesManager.provinces) {
            provinceUpdater.applyDefaultValues(province);
        }
    }


    public boolean isCameraMovementEnabled() {
        return chosenType == null;
    }


    public void setChosenType(EpbType chosenType) {
        if (this.chosenType == chosenType) return;
        this.chosenType = chosenType;
        onChosenTypeChanged();
    }


    private void onChosenTypeChanged() {
        if (chosenType != null) {
            deselectProvince();
        }
    }


    private boolean isEnabled() {
        return getGameController().gameMode == GameMode.editor;
    }


    public void setTimeSpentMaking(long timeSpentMaking) {
        this.timeSpentMaking = timeSpentMaking;
    }
}
