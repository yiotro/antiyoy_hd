package yio.tro.onliyoy.game.core_model.generators;

import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.ruleset.RulesType;

public class LevelGeneratorFactory {


    public static AbstractLevelGenerator create(CoreModel coreModel) {
        RulesType rulesType = coreModel.ruleset.getRulesType();
        switch (rulesType) {
            default:
                System.out.println("LevelGeneratorFactory.create: problem");
                return null;
            case def:
            case experimental:
            case duel:
                return new GeneratorDefault(coreModel);
            case classic:
                return new GeneratorClassic(coreModel);
        }
    }

}
