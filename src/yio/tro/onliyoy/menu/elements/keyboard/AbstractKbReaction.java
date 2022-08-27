package yio.tro.onliyoy.menu.elements.keyboard;

public abstract class AbstractKbReaction {

    public abstract void onInputFromKeyboardReceived(String input);


    public void onInputCancelled() {
        // nothing by default
    }

}
