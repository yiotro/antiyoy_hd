package yio.tro.onliyoy.net.postpone;

import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.NetMatchResults;

public class AprFinishNetMatch extends AbstractPostponedReaction{

    String code;


    public AprFinishNetMatch(PostponedReactionsManager postponedReactionsManager) {
        super(postponedReactionsManager);
    }


    @Override
    boolean isReady() {
        if (viewableModel == null) return true; // another match finished
        return !viewableModel.isSomethingMovingCurrently() && viewableModel.isBufferEmpty();
    }


    @Override
    void apply() {
        NetMatchResults netMatchResults = new NetMatchResults();
        netMatchResults.decode(code);
        if (isCurrentMatch(netMatchResults)) {
            yioGdxGame.applyFullTransitionToUI();
            Scenes.netMatchResults.create();
            Scenes.netMatchResults.setNetMatchResults(netMatchResults);
            return;
        }
        Scenes.qmfResults.create();
        Scenes.qmfResults.setNetMatchResults(netMatchResults);
    }


    private boolean isCurrentMatch(NetMatchResults netMatchResults) {
        if (netMatchResults.winnerColor == null) return false;
        String currentMatchId = root.currentMatchData.matchId;
        return netMatchResults.matchId.equals(currentMatchId);
    }


    public void setCode(String code) {
        this.code = code;
    }
}
