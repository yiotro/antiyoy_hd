package yio.tro.onliyoy.net.shared;

public class SfbProduct {

    // scfb = scene choose fish to buy
    public String id;
    public String price;


    public int getFishAmount() {
        if (id.contains("100")) return 100;
        if (id.contains("200")) return 200;
        if (id.contains("500")) return 500;
        return -1;
    }
}
