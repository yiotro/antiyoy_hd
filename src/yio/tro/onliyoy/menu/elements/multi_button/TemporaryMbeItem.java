package yio.tro.onliyoy.menu.elements.multi_button;

import yio.tro.onliyoy.menu.elements.BackgroundYio;
import yio.tro.onliyoy.menu.reactions.Reaction;

public class TemporaryMbeItem {

    public TemporaryMbeItem(String key, BackgroundYio backgroundYio, Reaction reaction) {
        this.key = key;
        this.backgroundYio = backgroundYio;
        this.reaction = reaction;
    }


    public String key;
    public BackgroundYio backgroundYio;
    public Reaction reaction;


}
