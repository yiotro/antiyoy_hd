package yio.tro.onliyoy;

import yio.tro.onliyoy.net.shared.NetSignInData;

public interface ISignInManagerYio {


    void apply(NetSignInData netSignInData);


    boolean isReady();


}
