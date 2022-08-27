package yio.tro.onliyoy.game.editor;

import yio.tro.onliyoy.game.core_model.EntityType;
import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.core_model.PlayerEntity;
import yio.tro.onliyoy.game.core_model.generators.AbstractLevelGenerator;
import yio.tro.onliyoy.game.core_model.generators.LevelGeneratorFactory;
import yio.tro.onliyoy.game.core_model.generators.LgParameters;
import yio.tro.onliyoy.game.debug.DebugFlags;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.stuff.RepeatYio;

public class LgDebugManager {

    // handles debug of level generation process

    EditorManager editorManager;
    public AbstractLevelGenerator generator;
    RepeatYio<LgDebugManager> repeatMoveGenerator;


    public LgDebugManager(EditorManager editorManager) {
        this.editorManager = editorManager;
        generator = null;
        initRepeats();
    }


    private void initRepeats() {
        repeatMoveGenerator = new RepeatYio<LgDebugManager>(this, 25) {
            @Override
            public void performAction() {
                parent.moveGenerator();
            }
        };
    }


    void launch() {
        generator = LevelGeneratorFactory.create(editorManager.objectsLayer.viewableModel);
        LgParameters parameters = new LgParameters();
        parameters.setEntities(createEntities());
        generator.startProcess(parameters);
    }


    private PlayerEntity[] createEntities() {
        ViewableModel viewableModel = editorManager.objectsLayer.viewableModel;
        PlayerEntity[] entities = new PlayerEntity[5];
        entities[0] = new PlayerEntity(viewableModel.entitiesManager, EntityType.human, HColor.green);
        entities[1] = new PlayerEntity(viewableModel.entitiesManager, EntityType.ai_random, HColor.yellow);
        entities[2] = new PlayerEntity(viewableModel.entitiesManager, EntityType.ai_random, HColor.blue);
        entities[3] = new PlayerEntity(viewableModel.entitiesManager, EntityType.ai_random, HColor.aqua);
        entities[4] = new PlayerEntity(viewableModel.entitiesManager, EntityType.ai_random, HColor.red);
        return entities;
    }


    void move() {
        repeatMoveGenerator.move();
    }


    private void moveGenerator() {
        if (generator == null) return;
        generator.moveProcess();
        if (generator.isDone()) {
            generator = null;
            onGenerationFinished();
        }
    }


    private void onGenerationFinished() {

    }

}
