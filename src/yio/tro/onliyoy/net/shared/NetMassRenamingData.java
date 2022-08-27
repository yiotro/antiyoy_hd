package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

import java.util.ArrayList;

public class NetMassRenamingData implements ReusableYio, Encodeable {

    public ArrayList<NetRenamingData> list;
    StringBuilder stringBuilder;


    public NetMassRenamingData() {
        list = new ArrayList<>();
        stringBuilder = new StringBuilder();
    }


    @Override
    public void reset() {
        list.clear();
    }


    @Override
    public String encode() {
        if (list.size() == 0) return "-";
        stringBuilder.setLength(0);
        for (NetRenamingData netRenamingData : list) {
            stringBuilder.append(netRenamingData.encode()).append("#");
        }
        return stringBuilder.toString();
    }


    public void decode(String source) {
        reset();
        if (source == null) return;
        if (source.length() < 3) return;
        String[] split = source.split("#");
        for (String token : split) {
            NetRenamingData netRenamingData = new NetRenamingData();
            netRenamingData.decode(token);
            list.add(netRenamingData);
        }
    }
}
