package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.net.NetRole;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetUserData implements ReusableYio, Encodeable {

    public String name;
    public String id;
    public int money;
    public NetRole role;
    public long experience;
    public int elp;
    public int fish;


    public NetUserData() {
        reset();
    }


    @Override
    public void reset() {
        name = "-";
        id = "-";
        money = 0;
        role = null;
        experience = 0;
        elp = 1000;
        fish = 0;
    }


    @Override
    public String encode() {
        return name + "/" + money + "/" + id + "/" + role + "/" + experience + "/" + elp + "/" + fish;
    }


    public void decode(String source) {
        reset();
        String[] split = source.split("/");
        name = split[0];
        money = Integer.valueOf(split[1]);
        id = split[2];
        role = NetRole.valueOf(split[3]);
        if (split.length > 4) {
            experience = Long.valueOf(split[4]);
        }
        if (split.length > 5) {
            elp = Integer.valueOf(split[5]);
        }
        if (split.length > 6) {
            fish = Integer.valueOf(split[6]);
        }
    }
}
