package yio.tro.onliyoy.net.shared;

import yio.tro.onliyoy.game.export_import.Encodeable;
import yio.tro.onliyoy.stuff.object_pool.ReusableYio;

public class NetSignInData implements Encodeable, ReusableYio {

    public String provider;
    public String idToken;
    public String name;


    public NetSignInData() {
        reset();
    }


    @Override
    public void reset() {
        provider = "-";
        idToken = "-";
        name = "-";
    }


    @Override
    public String encode() {
        return provider + " " + idToken + " " + name;
    }


    public void decode(String source) {
        reset();
        String[] split = source.split(" ");
        if (split.length < 3) return;
        provider = split[0];
        idToken = split[1];
        name = split[2];
    }
}
