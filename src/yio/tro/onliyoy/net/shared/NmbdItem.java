package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.core_model.HColor;
import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NmbdItem implements ReusableYio, Encodeable {

    public String id;
    public String name;
    public HColor color;
    public AvatarType avatarType;


    public NmbdItem() {

    }


    @Override
    public void reset() {
        id = "-";
        name = "-";
        color = null;
        avatarType = AvatarType.empty;
    }


    @Override
    public String encode() {
        return id + "@" + name + "@" + color + "@" + avatarType;
    }


    public void decode(String source) {
        String[] split = source.split("@");
        if (split.length < 3) {
            System.out.println("NmbdItem.decode: " + source);
        }
        id = split[0];
        name = split[1];
        if (split[2].equals("null")) {
            color = null;
        } else {
            color = HColor.valueOf(split[2]);
        }
        if (split.length > 3) {
            avatarType = AvatarType.valueOf(split[3]);
        }
    }
}
