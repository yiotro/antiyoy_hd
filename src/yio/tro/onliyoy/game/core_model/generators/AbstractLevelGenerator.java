package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.core_provinces.ProvincesBuilder;
import yio.tro.onliyoy.game.debug.DebugFlags;

import java.util.Random;

public abstract class AbstractLevelGenerator {

    CoreModel coreModel;
    Random random;
    public LgPlanManager planManager;
    protected int phaseIndex;
    LgChunkSpawner chunkSpawner;
    LgParameters parameters;
    public AbstractLgProvinceSpawner provinceSpawner;
    public AbstractLgProvinceBalancer provinceBalancer;
    public LgProvinceAnalyzer provinceAnalyzer;
    public AbstractLgPiecesSpawner piecesSpawner;
    long startTime;


    public AbstractLevelGenerator(CoreModel coreModel) {
        this.coreModel = coreModel;
        random = new Random();
        if (DebugFlags.determinedRandom) {
            random = new Random(0);
        }
        planManager = new LgPlanManager(this);
        chunkSpawner = new LgChunkSpawner(this);
        createManagers();
    }


    abstract void createManagers();


    public void generate(LgParameters lgParameters) {
        startProcess(lgParameters);
        while (!isDone()) {
            moveProcess();
        }
    }


    public boolean isDone() {
        return phaseIndex == -1;
    }


    public void startProcess(LgParameters lgParameters) {
        parameters = lgParameters;
        phaseIndex = 0;
        startTime = System.currentTimeMillis();
        onProcessStarted();
    }


    protected abstract void onProcessStarted();


    public void moveProcess() {
        switch (phaseIndex) {
            default:
                System.out.println("AbstractLevelGenerator.moveProcess: problem");
                break;
            case 0:
                coreModel.rebuildGraph();
                break;
            case 1:
                planManager.generateNodes();
                break;
            case 2:
                planManager.linkNodes();
                break;
            case 3:
                planManager.guaranteeLinkedState();
                break;
            case 4:
                planManager.connectTwoOppositeNodes();
                break;
            case 5:
                planManager.alignToCenter();
                break;
            case 6:
                chunkSpawner.turnPlanIntoLand();
                planManager.clear();
                break;
            case 7:
                provinceSpawner.apply();
                break;
            case 8:
                ProvincesBuilder builder = coreModel.provincesManager.builder;
                builder.doGrantPermission();
                builder.apply();
                provinceBalancer.ensureCities();
                break;
            case 9:
                if (!parameters.balancerEnabled) break;
                provinceBalancer.apply();
                break;
            case 10:
                piecesSpawner.spawnTrees();
                break;
            case 11:
                if (!parameters.graves) break;
                piecesSpawner.spawnGraves();
                break;
            case 12:
                if (!parameters.neutralTowers) break;
                piecesSpawner.spawnTowers();
                break;
            case 13:
                if (!parameters.neutralCities) break;
                piecesSpawner.spawnNeutralCities();
                break;
            case 14:
                finish();
                return;
        }
        checkForProfile();
        goToNextPhase();
    }


    private void checkForProfile() {
        if (!DebugFlags.profileRandomGeneration) return;
        System.out.println("Phase " + phaseIndex + ": " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
    }


    protected void goToNextPhase() {
        phaseIndex++;
    }


    public void setSeed(long seed) {
        random.setSeed(seed);
        coreModel.searchWorker.setSeed(seed);
    }


    protected void finish() {
        phaseIndex = -1;
    }
}
