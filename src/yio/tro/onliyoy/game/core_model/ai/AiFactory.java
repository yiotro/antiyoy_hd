package yio.tro.onliyoy.game.core_model.ai;

import yio.tro.onliyoy.game.core_model.CoreModel;

public class AiFactory {

    AiManager aiManager;
    CoreModel coreModel;


    public AiFactory(AiManager aiManager) {
        this.aiManager = aiManager;
        coreModel = aiManager.coreModel;
    }


    public AbstractAI createAiRandom(int versionCode) {
        return new AiRandom(coreModel);
    }


    public AbstractAI createAiBalancerDuel(int versionCode) {
        switch (versionCode) {
            case 0:
                System.out.println("AiFactory.createAiBalancerDuel: problem");
                return null;
            default:
            case 1:
                return new AiBalancerDuelV1(coreModel);
        }
    }


    public AbstractAI createAiBalancerExperimental(int versionCode) {
        switch (versionCode) {
            case 0:
                System.out.println("AiFactory.createAiBalancerExperimental: problem");
                return null;
            default:
            case 1:
                return new AiBalancerExperimentalV1(coreModel);
        }
    }


    public AbstractAI createAiBalancerDefault(int versionCode) {
        switch (versionCode) {
            case 0:
                System.out.println("AiFactory.createAiBalancerDefault: problem");
                return null;
            default:
            case 1:
                return new AiBalancerDefaultV1(coreModel);
        }
    }


    public AbstractAI createAiBalancerClassic(int versionCode) {
        switch (versionCode) {
            case 0:
                System.out.println("AiFactory.createAiBalancerClassic: problem");
                return null;
            default:
            case 1:
                return new AiBalancerClassicV1(coreModel);
        }
    }

}
