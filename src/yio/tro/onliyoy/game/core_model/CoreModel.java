package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.core_provinces.ProvincesManager;
import yio.tro.onliyoy.game.core_model.events.*;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.core_model.ruleset.RulesetFactory;
import yio.tro.onliyoy.game.export_import.IwCoreDiplomacy;
import yio.tro.onliyoy.game.export_import.IwCoreMailBasket;
import yio.tro.onliyoy.game.export_import.IwCoreProvinces;
import yio.tro.onliyoy.game.export_import.IwReadiness;
import yio.tro.onliyoy.game.viewable_model.FogOfWarManager;
import yio.tro.onliyoy.game.viewable_model.UndoItem;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.TimeMeasureYio;
import yio.tro.onliyoy.stuff.posmap.AbstractPmObjectYio;
import yio.tro.onliyoy.stuff.posmap.PosMapYio;

import java.util.ArrayList;

public class CoreModel implements IEventListener {

    public ArrayList<Hex> hexes;
    public RectangleYio bounds;
    protected float hexRadius;
    private CoreGraphBuilder graphBuilder;
    public PosMapYio posMapYio;
    PointYio tempPoint;
    protected PmWorker pmWorker;
    public EventsManager eventsManager;
    public int currentUnitId;
    public CmSearchWorker searchWorker;
    private StringBuilder stringBuilder;
    public TurnsManager turnsManager;
    public EntitiesManager entitiesManager;
    public ProvincesManager provincesManager;
    public MoveZoneManager moveZoneManager;
    public ReadinessManager readinessManager;
    public ConstructionManager constructionManager;
    public AbstractRuleset ruleset;
    public EconomicsManager economicsManager;
    public EventsRefrigerator eventsRefrigerator;
    public CityManager cityManager;
    public RulesetFactory rulesetFactory;
    public QuickStatsManager quickStatsManager;
    public FinishMatchManager finishMatchManager;
    public final String name;
    public DiplomacyManager diplomacyManager;
    public LettersManager lettersManager;
    public DeathManager deathManager;
    public FogOfWarManager fogOfWarManager;


    public CoreModel() {
        this("");
    }


    public CoreModel(String name) {
        this.name = name;
        hexes = new ArrayList<>();
        bounds = new RectangleYio();
        graphBuilder = new CoreGraphBuilder(this);
        tempPoint = new PointYio();
        pmWorker = new PmWorker(this);
        eventsManager = new EventsManager(this);
        provincesManager = new ProvincesManager(this);
        readinessManager = new ReadinessManager(this);
        searchWorker = new CmSearchWorker(this);
        stringBuilder = new StringBuilder();
        turnsManager = new TurnsManager(this);
        entitiesManager = new EntitiesManager(this);
        moveZoneManager = new MoveZoneManager(this);
        constructionManager = new ConstructionManager(this);
        economicsManager = new EconomicsManager(this);
        deathManager = new DeathManager(this);
        eventsRefrigerator = new EventsRefrigerator(this);
        cityManager = new CityManager(this);
        rulesetFactory = new RulesetFactory(this);
        quickStatsManager = new QuickStatsManager(this);
        finishMatchManager = new FinishMatchManager();
        finishMatchManager.setCoreModel(this);
        diplomacyManager = new DiplomacyManager(this);
        lettersManager = new LettersManager(this);
        fogOfWarManager = new FogOfWarManager(this);
        ruleset = null;
        currentUnitId = 0;
    }


    public void buildSimilarGraph(CoreModel srcModel) {
        hexes.clear();
        buildGraph(srcModel.bounds, srcModel.hexRadius);
        // prepare lg flags
        for (Hex hex : hexes) {
            hex.lgFlag = false;
        }
        // tag hexes that will remain
        for (Hex srcHex : srcModel.hexes) {
            Hex hex = getHexWithSameCoordinates(srcHex);
            hex.lgFlag = true;
        }
        // remove links
        for (Hex hex : hexes) {
            if (!hex.lgFlag) continue;
            ArrayList<Hex> adjacentHexes = hex.adjacentHexes;
            for (int i = adjacentHexes.size() - 1; i >= 0; i--) {
                Hex adjacentHex = adjacentHexes.get(i);
                if (adjacentHex.lgFlag) continue;
                adjacentHexes.remove(i);
            }
        }
        // manually remove non flagged hexes
        for (int i = hexes.size() - 1; i >= 0; i--) {
            Hex hex = hexes.get(i);
            if (hex.lgFlag) continue;
            hexes.remove(i);
            posMapYio.removeObject(hex);
        }
    }


    private void buildSimilarGraphSlowly(CoreModel srcModel) {
        // this method is reliable but slow
        hexes.clear();
        buildGraph(srcModel.bounds, srcModel.hexRadius);
        for (int i = hexes.size() - 1; i >= 0; i--) {
            Hex hex = hexes.get(i);
            if (srcModel.getHexWithSameCoordinates(hex) != null) continue;
            removeHex(hex);
        }
    }


    public void setBy(CoreModel srcModel) {
        currentUnitId = srcModel.currentUnitId;
        for (Hex hex : hexes) {
            Hex srcHex = srcModel.getHex(hex.coordinate1, hex.coordinate2);
            hex.copyFrom(srcHex);
        }
        provincesManager.setBy(srcModel.provincesManager);
        diplomacyManager.setBy(srcModel.diplomacyManager);
        lettersManager.setBy(srcModel.lettersManager);
        fogOfWarManager.setBy(srcModel.fogOfWarManager);
    }


    public void initBy(CoreModel srcModel) {
        setBy(srcModel);
        setRulesBy(srcModel);
        entitiesManager.setBy(srcModel.entitiesManager);
        readinessManager.setBy(srcModel.readinessManager);
        turnsManager.setBy(srcModel.turnsManager);
        diplomacyManager.setBy(srcModel.diplomacyManager); // yes, it should be called once again because first time entities were not initialized
    }


    public void buildGraph(RectangleYio bounds, float hexRadius) {
        this.bounds.setBy(bounds);
        this.hexRadius = hexRadius;
        posMapYio = new PosMapYio(bounds, 3 * hexRadius);
        graphBuilder.apply(bounds, hexRadius);
    }


    public void resetHexes() {
        for (Hex hex : hexes) {
            hex.setPiece(null);
            hex.setUnitId(-1);
        }
    }


    public void rebuildGraph() {
        buildGraph(bounds, hexRadius);
    }


    public void applyCoordinatesToTempPoint(int coordinate1, int coordinate2) {
        tempPoint.set(
                bounds.x + bounds.width / 2,
                bounds.y + bounds.height / 2
        );

        float dy = (float) (hexRadius * Math.cos(Math.PI / 6));
        tempPoint.y += 2 * dy * coordinate1;

        float dx = 1.5f * hexRadius;
        tempPoint.x += dx * coordinate2;
        tempPoint.y += dy * coordinate2;
    }


    void applyCoordinatesToPosition(Hex hex) {
        applyCoordinatesToTempPoint(hex.coordinate1, hex.coordinate2);
        hex.position.center.setBy(tempPoint);
    }


    public Hex getHexWithSameCoordinates(Hex hex) {
        return getHex(hex.coordinate1, hex.coordinate2);
    }


    public Hex getHex(int coordinate1, int coordinate2) {
        applyCoordinatesToTempPoint(coordinate1, coordinate2);
        ArrayList<AbstractPmObjectYio> sectorByPosition = posMapYio.getSectorByPosition(tempPoint);
        if (sectorByPosition == null) return null;
        for (AbstractPmObjectYio abstractPmObjectYio : sectorByPosition) {
            Hex hex = (Hex) abstractPmObjectYio;
            if (hex.coordinate1 != coordinate1) continue;
            if (hex.coordinate2 != coordinate2) continue;
            return hex;
        }
        return null;
    }


    public Hex getClosestHex(PointYio pointYio) {
        return pmWorker.getClosestHex(pointYio);
    }


    public boolean areCoordinatesInsideBounds(int coordinate1, int coordinate2) {
        applyCoordinatesToTempPoint(coordinate1, coordinate2);
        return bounds.isPointInside(tempPoint, -1 * hexRadius);
    }


    public Hex addHex(int coordinate1, int coordinate2) {
        Hex hex = new Hex(this);
        hex.coordinate1 = coordinate1;
        hex.coordinate2 = coordinate2;
        hex.position.setRadius(hexRadius);
        hex.updateFarmDiversityIndex();
        applyCoordinatesToPosition(hex);
        hexes.add(hex);
        posMapYio.addObject(hex);
        return hex;
    }


    public void removeHex(Hex argHex) {
        hexes.remove(argHex);
        posMapYio.removeObject(argHex);
        for (Hex hex : hexes) {
            if (!hex.isLinkedTo(argHex)) continue;
            hex.adjacentHexes.remove(argHex);
        }
    }


    public void applyMatchStartedEvent() {
        eventsManager.applyEvent(eventsRefrigerator.getMatchStartedEvent());
    }


    @Override
    public void onEventValidated(AbstractEvent event) {

    }


    @Override
    public void onEventApplied(AbstractEvent event) {
        switch (event.getType()) {
            default:
                break;
            case piece_add:
                checkToIncreaseCurrentUnitId(((EventPieceAdd) event).unitId);
                break;
            case piece_build:
                checkToIncreaseCurrentUnitId(((EventPieceBuild) event).unitId);
                break;
            case merge_on_build:
                checkToIncreaseCurrentUnitId(((EventMergeOnBuild) event).unitId);
                break;
            case merge:
                checkToIncreaseCurrentUnitId(((EventMerge) event).unitId);
                break;
        }
    }


    private void checkToIncreaseCurrentUnitId(int id) {
        if (id < currentUnitId) return;
        currentUnitId = id + 10;
    }


    @Override
    public int getListenPriority() {
        return 9;
    }


    public void onQuickEventApplied() {
        provincesManager.onQuickEventApplied();
    }


    public void onUndoApplied(UndoItem undoItem) {
        // nothing by default
    }


    public Province getSelectedProvince() {
        return null; // no selection in core model
    }


    public void onHexColorChanged(Hex hex, HColor previousColor) {
        cityManager.onHexColorChanged(hex, previousColor);
    }


    public int getIdForNewUnit() {
        int id = currentUnitId;
        currentUnitId++;
        return id;
    }


    public String encodeHexes() {
        if (hexes.size() == 0) {
            return "-";
        }
        stringBuilder.setLength(0); // clear
        for (Hex hex : hexes) {
            stringBuilder.append(hex.encode()).append(",");
        }
        return stringBuilder.toString();
    }


    public String encodeInitialization() {
        return Yio.roundUp(bounds.width, 5) + " " +
                Yio.roundUp(bounds.height, 5) + " " +
                Yio.roundUp(hexRadius, 5);
    }


    public String encodeCurrentIds() {
        return currentUnitId + "";
    }


    public String encodeRules() {
        if (ruleset == null) return "null";
        return ruleset.getRulesType() + " " + ruleset.getVersionCode();
    }


    public void setRulesBy(CoreModel srcModel) {
        setRules(srcModel.ruleset.getRulesType(), srcModel.ruleset.getVersionCode());
    }


    public void setRules(RulesType rulesType, int versionCode) {
        ruleset = rulesetFactory.create(rulesType, versionCode);
    }


    public float getHexRadius() {
        return hexRadius;
    }


    public PointYio getTempPoint() {
        return tempPoint;
    }


    public void checkForTwoUnitsWithSameId() {
        // this method should be used only for debug
        ArrayList<Hex> tempList = new ArrayList<>();
        for (Hex hex : hexes) {
            if (!hex.hasUnit()) continue;
            tempList.add(hex);
        }
        for (int i = 0; i < tempList.size(); i++) {
            Hex hex1 = tempList.get(i);
            for (int j = i + 1; j < tempList.size(); j++) {
                Hex hex2 = tempList.get(j);
                if (hex1.unitId != hex2.unitId) continue;
                System.out.println(hex1 + " and " + hex2 + " have same unit id " + hex1.unitId);
                return;
            }
        }
        System.out.println("CoreModel.checkForTwoUnitsWithSameId: everything is fine");
    }


    @Override
    public String toString() {
        String s = super.toString();
        String n = getClass().getSimpleName();
        if (name.length() > 0) {
            n = name;
        }
        return "[" +
                n + " " +
                s.substring(s.indexOf("@")) +
                "]";
    }
}
