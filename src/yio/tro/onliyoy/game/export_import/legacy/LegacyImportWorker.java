package yio.tro.onliyoy.game.export_import.legacy;

import yio.tro.onliyoy.game.core_model.*;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;
import yio.tro.onliyoy.game.core_model.core_provinces.ProvincesBuilder;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.export_import.ExportManager;
import yio.tro.onliyoy.game.export_import.ExportParameters;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.game.general.SizeManager;
import yio.tro.onliyoy.net.shared.NetValues;
import yio.tro.onliyoy.stuff.PointYio;
import yio.tro.onliyoy.stuff.RectangleYio;

import java.util.ArrayList;

public class LegacyImportWorker {

    private String source;
    private CoreModel bufferModel;
    private SizeManager sizeManager;
    private RectangleYio innerPosition;
    private ArrayList<LiwItem> items;
    private int delta1;
    private int delta2;
    private RectangleYio metrics;
    private PointYio tempPoint;
    private int coordinate1;
    private int coordinate2;


    public LegacyImportWorker() {
        source = "";
        bufferModel = null;
        sizeManager = new SizeManager(1);
        innerPosition = new RectangleYio();
        items = new ArrayList<>();
        metrics = new RectangleYio();
        tempPoint = new PointYio();
    }


    public String apply(String source) {
        this.source = source;
        if (!source.contains("antiyoy_level_code")) return "-";
        try {
            for (LevelSize levelSize : LevelSize.values()) {
                String levelCode = attempt(levelSize);
                if (levelCode.length() > 3) return levelCode;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "-";
    }


    private String attempt(LevelSize levelSize) {
        sizeManager.initLevelSize(levelSize);
        createBufferModel(levelSize);
        updateInnerPosition();
        updateItems();
        if (items.size() == 0) return "-";
        updatePositions(); // positions of old lands
        updateMetrics(); // bounds of old lands
        updateDeltas();
        prepareHexes();
        if (!tagHexes() && levelSize != LevelSize.giant) return "-"; // can be false if map size is too small
        removeNonTaggedHexes();
        restoreColors();
        restoreStaticPieces();
        restoreUnits();
        detectEntities();
        restoreHumanQuantity();
        doFixProvinces();
        return performExport(levelSize);
    }


    private void doFixProvinces() {
        ProvincesBuilder builder = bufferModel.provincesManager.builder;
        builder.doGrantPermission();
        builder.apply();
        for (Province province : bufferModel.provincesManager.provinces) {
            province.setMoney(10);
        }
    }


    private void restoreHumanQuantity() {
        String generalSection = getSection("general");
        if (generalSection == null) return;
        if (generalSection.length() < 3) return;
        String[] split = generalSection.split(" ");
        if (split.length < 3) return;
        int humans = Integer.valueOf(split[1]);
        PlayerEntity[] entities = bufferModel.entitiesManager.entities;
        humans = Math.min(humans, entities.length - 1);
        for (int i = humans; i > 0; i--) {
            entities[i].type = EntityType.human;
        }
    }


    private void restoreUnits() {
        int unitId = 1;
        String unitsSection = getSection("units");
        if (unitsSection == null) return;
        for (String token : unitsSection.split(",")) {
            if (token.length() < 3) continue;
            String[] split = token.split(" ");
            if (split.length < 4) continue;
            int c1 = Integer.valueOf(split[0]);
            int c2 = Integer.valueOf(split[1]);
            int strength = Integer.valueOf(split[2]);
            PieceType pieceType = Core.getUnitByStrength(strength);
            int tc1 = c1 + delta1;
            int tc2 = c2 + delta2;
            Hex hex = bufferModel.getHex(tc1, tc2);
            hex.setPiece(pieceType);
            hex.setUnitId(unitId);
            unitId++;
        }
        bufferModel.currentUnitId = unitId;
    }


    private void restoreColors() {
        String landSection = getSection("land");
        if (landSection == null) return;
        for (String token : landSection.split(",")) {
            if (token.length() < 3) continue;
            String[] split = token.split(" ");
            if (split.length < 4) continue;
            int c1 = Integer.valueOf(split[0]);
            int c2 = Integer.valueOf(split[1]);
            int fraction = Integer.valueOf(split[2]);
            HColor hColor = convertFractionIntoColor(fraction);
            int tc1 = c1 + delta1;
            int tc2 = c2 + delta2;
            Hex hex = bufferModel.getHex(tc1, tc2);
            if (hex == null) continue;
            hex.setColor(hColor);
        }
    }


    private HColor convertFractionIntoColor(int fraction) {
        switch (fraction) {
            default:
                System.out.println("LegacyImportWorker.convertFractionIntoColor: problem");
                return null;
            case 0:
                return HColor.aqua;
            case 1:
                return HColor.red;
            case 2:
                return HColor.lavender;
            case 3:
                return HColor.cyan;
            case 4:
                return HColor.yellow;
            case 5:
                return HColor.rose;
            case 6:
                return HColor.mint;
            case 7:
                return HColor.gray;
            case 8:
                return HColor.green;
            case 9:
                return HColor.orchid;
            case 10:
                return HColor.whiskey;
        }
    }


    private void restoreStaticPieces() {
        String landSection = getSection("land");
        if (landSection == null) return;
        for (String token : landSection.split(",")) {
            if (token.length() < 3) continue;
            String[] split = token.split(" ");
            if (split.length < 4) continue;
            int c1 = Integer.valueOf(split[0]);
            int c2 = Integer.valueOf(split[1]);
            int objetInside = Integer.valueOf(split[3]);
            if (objetInside == 0) continue; // empty
            PieceType pieceType = convertObjectInsideIntoPieceType(objetInside);
            int tc1 = c1 + delta1;
            int tc2 = c2 + delta2;
            Hex hex = bufferModel.getHex(tc1, tc2);
            if (hex == null) continue;
            hex.setPiece(pieceType);
        }
    }


    private PieceType convertObjectInsideIntoPieceType(int objectInside) {
        switch (objectInside) {
            default:
                System.out.println("LegacyImportWorker.convertObjectInsideIntoPieceType: problem");
                return null;
            case 1:
                return PieceType.pine;
            case 2:
                return PieceType.palm;
            case 3:
                return PieceType.city;
            case 4:
                return PieceType.tower;
            case 5:
                return PieceType.grave;
            case 6:
                return PieceType.farm;
            case 7:
                return PieceType.strong_tower;
        }
    }


    private void detectEntities() {
        ArrayList<HColor> colors = new ArrayList<>();
        for (Hex hex : bufferModel.hexes) {
            if (hex.isNeutral()) continue;
            if (colors.contains(hex.color)) continue;
            colors.add(hex.color);
        }
        if (colors.size() == 0) {
            colors.add(HColor.aqua);
        }
        ArrayList<PlayerEntity> entityArrayList = new ArrayList<>();
        for (HColor hColor : colors) {
            entityArrayList.add(new PlayerEntity(bufferModel.entitiesManager, EntityType.ai_balancer, hColor));
        }
        bufferModel.entitiesManager.initialize(entityArrayList);
    }


    private String performExport(LevelSize levelSize) {
        ExportParameters parameters = new ExportParameters();
        parameters.setInitialLevelSize(levelSize);
        parameters.setCoreModel(bufferModel);
        return (new ExportManager()).perform(parameters);
    }


    private void removeNonTaggedHexes() {
        ArrayList<Hex> hexes = bufferModel.hexes;
        for (int i = hexes.size() - 1; i >= 0; i--) {
            Hex hex = hexes.get(i);
            if (hex.flag) continue;
            bufferModel.removeHex(hex);
        }
    }


    private boolean tagHexes() {
        boolean problemDetected = false;
        for (LiwItem liwItem : items) {
            int c1 = liwItem.coordinate1 + delta1;
            int c2 = liwItem.coordinate2 + delta2;
            Hex hex = bufferModel.getHex(c1, c2);
            if (hex == null) {
                problemDetected = true;
                continue;
            }
            hex.flag = true;
        }
        return !problemDetected;
    }


    private void prepareHexes() {
        for (Hex hex : bufferModel.hexes) {
            hex.flag = false;
        }
    }


    private void updateDeltas() {
        PointYio oldCenterPosition = new PointYio();
        oldCenterPosition.set(
                metrics.x + metrics.width / 2,
                metrics.y + metrics.height / 2
        );
        updateCoordinatesByPoint(oldCenterPosition);
        delta1 = -coordinate1;
        delta2 = -coordinate2;
    }


    private void updateCoordinatesByPoint(PointYio point) {
        // this can be checked via CoreModel.applyCoordinatesToTempPoint()
        float hexRadius = NetValues.HEX_RADIUS;
        float dy = (float) (hexRadius * Math.cos(Math.PI / 6));
        float dx = 1.5f * hexRadius;
        RectangleYio bounds = sizeManager.position;
        float cx = bounds.x + bounds.width / 2;
        float cy = bounds.y + bounds.height / 2;
        float fc2 = (point.x - cx) / dx;
        float fc1 = (point.y - cy - dy * fc2) / (2 * dy);
        coordinate1 = getClosestInteger(fc1);
        coordinate2 = getClosestInteger(fc2);
    }


    private int getClosestInteger(double value) {
        if (value < 0) {
            return (int) (value - 0.5);
        }
        return (int) (value + 0.5);
    }


    private void updateMetrics() {
        LiwItem firstItem = items.get(0);
        float left = firstItem.position.x;
        float right = firstItem.position.x;
        float top = firstItem.position.y;
        float bottom = firstItem.position.y;
        for (LiwItem liwItem : items) {
            if (liwItem.position.x < left) {
                left = liwItem.position.x;
            }
            if (liwItem.position.x > right) {
                right = liwItem.position.x;
            }
            if (liwItem.position.y < bottom) {
                bottom = liwItem.position.y;
            }
            if (liwItem.position.y > top) {
                top = liwItem.position.y;
            }
        }
        metrics.set(left, bottom, right - left, top - bottom);
    }


    private void updatePositions() {
        for (LiwItem liwItem : items) {
            updatePosition(liwItem);
        }
    }


    private void updatePosition(LiwItem liwItem) {
        bufferModel.applyCoordinatesToTempPoint(liwItem.coordinate1, liwItem.coordinate2);
        liwItem.position.setBy(bufferModel.getTempPoint());
    }


    private void updateItems() {
        items.clear();
        String landSection = getSection("land");
        if (landSection == null) return;
        for (String token : landSection.split(",")) {
            if (token.length() < 3) continue;
            String[] split = token.split(" ");
            if (split.length < 4) continue;
            int c1 = Integer.valueOf(split[0]);
            int c2 = Integer.valueOf(split[1]);
            LiwItem liwItem = new LiwItem();
            liwItem.setCoordinates(c1, c2);
            items.add(liwItem);
        }
    }


    private void createBufferModel(LevelSize levelSize) {
        bufferModel = new CoreModel("liw_buffer_" + levelSize);
        bufferModel.buildGraph(sizeManager.position, NetValues.HEX_RADIUS);
        bufferModel.setRules(RulesType.def, -1);
    }


    private void updateInnerPosition() {
        innerPosition.setBy(sizeManager.position);
        innerPosition.increase(-NetValues.HEX_RADIUS);
    }


    protected String getSection(String name) {
        int sectionIndex = source.indexOf("#" + name);
        if (sectionIndex == -1) return null;
        int colonIndex = source.indexOf(":", sectionIndex);
        int hashIndex = source.indexOf("#", colonIndex);
        if (hashIndex - colonIndex < 2) return null;

        return source.substring(colonIndex + 1, hashIndex);
    }
}
