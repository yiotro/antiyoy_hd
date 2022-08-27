package yio.tro.onliyoy.game.core_model;

import yio.tro.onliyoy.game.core_model.ruleset.RulesType;
import yio.tro.onliyoy.game.general.GameMode;
import yio.tro.onliyoy.game.general.LevelSize;
import yio.tro.onliyoy.net.shared.NetMatchStatisticsData;

public class MatchResults {

    public HColor winnerColor;
    public EntityType entityType;
    public NetMatchStatisticsData statisticsData;
    public LevelSize levelSize;
    public RulesType rulesType;
    public GameMode gameMode;
    public int year;
    public int day;
    public int month;


    public MatchResults() {
        winnerColor = null;
        entityType = null;
        statisticsData = new NetMatchStatisticsData();
        levelSize = null;
        rulesType = null;
        gameMode = null;
        year = 0;
        day = 0;
        month = 0;
    }

}
