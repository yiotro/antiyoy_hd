package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetMlpData implements ReusableYio, Encodeable {

    public String clientId;
    public String name;
    public HColor color;
    public AvatarType avatarType;


    public NetMlpData() {
        reset();
    }


    @Override
    public void reset() {
        clientId = "-";
        name = "-";
        color = null;
        avatarType = AvatarType.empty;
    }


    @Override
    public String encode() {
        return clientId + "/" + name + "/" + color + "/" + avatarType;
    }


    public void decode(String source) {
        reset();
        if (source == null) return;
        if (source.length() < 5) return;
        String[] split = source.split("/");
        if (split.length < 4) return;
        try {
            clientId = split[0];
            name = split[1];
            color = HColor.valueOf(split[2]);
            avatarType = AvatarType.valueOf(split[3]);
        } catch (IllegalArgumentException e) {
            reset();
        }
    }
}
