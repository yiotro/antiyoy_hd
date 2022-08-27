package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.SettingsManager;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetOptionsData implements ReusableYio, Encodeable {

    public boolean hideStatistics;


    public NetOptionsData() {
        reset();
    }


    @Override
    public void reset() {
        hideStatistics = false;
    }


    @Override
    public String encode() {
        return hideStatistics + "";
    }


    public void decode(String source) {
        reset();
        if (source == null) return;
        if (source.length() < 3) return;
        hideStatistics = Boolean.valueOf(source);
    }


    public void setBy(SettingsManager settingsManager) {
        hideStatistics = settingsManager.hideStatistics;
    }
}
