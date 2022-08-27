package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetRatingData implements ReusableYio, Encodeable {

    public String clientId;
    public String name;
    public long value;
    public AvatarType avatarType;


    @Override
    public void reset() {
        clientId = "-";
        name = "-";
        value = 0;
        avatarType = AvatarType.empty;
    }


    @Override
    public String encode() {
        return clientId + "/" + name + "/" + value + "/" + avatarType;
    }


    public void decode(String source) {
        reset();
        if (source == null) return;
        if (source.length() < 5) return;
        String[] split = source.split("/");
        if (split.length < 3) return;
        clientId = split[0];
        name = split[1];
        value = Long.valueOf(split[2]);
        if (split.length > 3) {
            avatarType = AvatarType.valueOf(split[3]);
        }
    }
}
