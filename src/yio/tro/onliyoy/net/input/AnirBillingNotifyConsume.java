package yio.tro.onliyoy.net.input;

import yio.tro.onliyoy.menu.scenes.Scenes;

import java.util.ArrayList;

public class AnirBillingNotifyConsume extends AbstractNetInputReaction{

    @Override
    public void apply() {
        System.out.println("AnirBillingNotifyConsume.apply: " + value);
        if (value.length() < 2) return;
        ArrayList<String> list = decodeStringList(value);
        for (String token : list) {
            yioGdxGame.billingManager.onProductConsumed(token);
        }
        Scenes.chooseFishToBuy.destroy();
        yioGdxGame.billingManager.finish();
    }


    private ArrayList<String> decodeStringList(String source) {
        ArrayList<String> list = new ArrayList<>();
        for (String string : source.split(" ")) {
            if (string.length() < 2) continue;
            list.add(string);
        }
        return list;
    }
}
