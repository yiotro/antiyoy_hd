package yio.tro.onliyoy.game.core_model.ruleset;

import yio.tro.onliyoy.game.core_model.AbstractRuleset;
import yio.tro.onliyoy.game.core_model.CoreModel;

public class RulesetFactory {

    CoreModel coreModel;


    public RulesetFactory(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    public AbstractRuleset create(RulesType rulesType, int versionCode) {
        switch (rulesType) {
            default:
                System.out.println("RulesetFactory.create: problem");
                return null;
            case def:
                return createRulesetDefault(versionCode);
            case classic:
                return createRulesetClassic(versionCode);
            case experimental:
                return createRulesetExperimental(versionCode);
            case duel:
                return createRulesetDuel(versionCode);
        }
    }


    private AbstractRuleset createRulesetDuel(int versionCode) {
        switch (versionCode) {
            case 0:
                System.out.println("RulesetFactory.createRulesetDuel: problem");
                return null;
            default:
            case 1:
                return new RulesetDuelV1(coreModel);
        }
    }


    private AbstractRuleset createRulesetExperimental(int versionCode) {
        switch (versionCode) {
            case 0:
                System.out.println("RulesetFactory.createRulesetExperimental: problem");
                return null;
            default:
            case 1:
                return new RulesetExperimentalV1(coreModel);
        }
    }


    private AbstractRuleset createRulesetDefault(int versionCode) {
        switch (versionCode) {
            case 0:
                System.out.println("RulesetFactory.createRulesetDefault: problem");
                return null;
            default:
            case 1:
                return new RulesetDefaultV1(coreModel);
        }
    }


    private AbstractRuleset createRulesetClassic(int versionCode) {
        switch (versionCode) {
            case 0:
                System.out.println("RulesetFactory.createRulesetClassic: problem");
                return null;
            default:
            case 1:
                return new RulesetClassicV1(coreModel);
        }
    }
}
