package yio.tro.onliyoy.game.tutorial;

import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.general.CameraController;
import yio.tro.onliyoy.game.general.GameController;
import yio.tro.onliyoy.game.viewable_model.ViewableModel;
import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.forefinger.ForefingerElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RepeatYio;

public class ScriptPerformer {

    ScriptManager scriptManager;
    RepeatYio<ScriptPerformer> repeatPerform;


    public ScriptPerformer(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
        initRepeats();
    }


    private void initRepeats() {
        repeatPerform = new RepeatYio<ScriptPerformer>(this, 5) {
            @Override
            public void performAction() {
                tryToPerformScript(parent);
            }
        };
    }


    private void tryToPerformScript(ScriptPerformer parent) {
        try {
            parent.perform();
        } catch (Exception e) {
            scriptManager.clear();
        }
    }


    void perform() {
        ScriptItem script = getScript();
        if (script == null || !script.isReady()) return;

        switch (script.type) {
            default:
                System.out.println("Problem in ScriptPerformer.perform()");
                break;
            case message:
                showMessage(script);
                break;
            case point_at_ui_element:
                pointAtUiElement(script);
                break;
            case point_at_ui_tag:
                pointAtUiTag(script);
                break;
            case focus_lock_camera:
                focusLockCamera(script);
                break;
            case unlock_camera:
                unlockCamera();
                break;
            case wait:
                doWait(script);
                break;
            case point_at_hex:
                doPointAtHex(script);
                break;
            case highlight_ui_element:
                doHighlightUiElement(script);
                break;
            case point_at_position:
                doPointAtPosition(script);
                break;
        }

        if (script.isFastToPerform()) {
            repeatPerform.setCountDown(5);
        }

        script.kill();
    }


    private void doPointAtPosition(ScriptItem script) {
        String[] split = script.argument.split(" ");
        double x = Double.valueOf(split[0]);
        double y = Double.valueOf(split[1]);

        Scenes.forefinger.create();
        Scenes.forefinger.forefinger.setTarget(
                x * GraphicsYio.width,
                y * GraphicsYio.height
        );
    }


    private void doHighlightUiElement(ScriptItem script) {
        GameController gameController = scriptManager.gameController;
        MenuControllerYio menuControllerYio = gameController.yioGdxGame.menuControllerYio;

        String[] split = script.argument.split(" ");
        if (split.length < 2) return;
        InterfaceElement element = menuControllerYio.getElement(split[0]);
        if (element == null) return;

        Scenes.highlightArea.create();
        Scenes.highlightArea.highlightAreaElement.launch(element, split[1]);
    }


    private void doPointAtHex(ScriptItem script) {
        String[] split = script.argument.split(" ");
        if (split.length < 2) {
            System.out.println("ScriptPerformer.doPointAtHex: problem");
            return;
        }
        int c1 = Integer.valueOf(split[0]);
        int c2 = Integer.valueOf(split[1]);

        GameController gameController = scriptManager.gameController;
        ViewableModel viewableModel = gameController.objectsLayer.viewableModel;
        Hex hex = viewableModel.getHex(c1, c2);
        if (hex == null) return;

        Scenes.forefinger.create();
        Scenes.forefinger.forefinger.setTarget(hex);
    }


    private void doWait(ScriptItem script) {
        if (script.argument == null || script.argument.equals("null")) return;
        int value = Integer.valueOf(script.argument);
        value--;
        if (value <= 0) return;

        ScriptItem newItem = new ScriptItem(scriptManager);
        newItem.setType(ScriptType.wait);
        newItem.setArgument("" + value);
        scriptManager.items.add(0, newItem);
    }


    private void unlockCamera() {
        CameraController cameraController = scriptManager.gameController.cameraController;
        cameraController.setLocked(false);
    }


    private void focusLockCamera(ScriptItem script) {
        CameraController cameraController = scriptManager.gameController.cameraController;
        cameraController.setLocked(true);

        if (script.argument == null || script.argument.length() < 2) return;
        String[] split = script.argument.split(" ");
        if (split.length < 3) return;

        double x = Double.valueOf(split[0]);
        double y = Double.valueOf(split[1]);
        float zoom = Float.valueOf(split[2]);

        cameraController.focusOnPoint(x * GraphicsYio.width, y * GraphicsYio.width);
        cameraController.setTargetZoomLevel(zoom);
        cameraController.enableSlowMode();
    }


    public void prepareToExecuteNextScriptFaster() {
        repeatPerform.setCountDown(30);
    }


    private void pointAtUiTag(ScriptItem script) {
        Scenes.forefinger.create();

        ForefingerElement forefinger = Scenes.forefinger.forefinger;
        String[] split = script.argument.split(" ");
        GameController gameController = scriptManager.gameController;
        MenuControllerYio menuControllerYio = gameController.yioGdxGame.menuControllerYio;
        InterfaceElement element = menuControllerYio.getElement(split[0]);
        forefinger.setTarget(element, split[1]);
    }


    private void pointAtUiElement(ScriptItem script) {
        Scenes.forefinger.create();

        GameController gameController = scriptManager.gameController;
        MenuControllerYio menuControllerYio = gameController.yioGdxGame.menuControllerYio;

        InterfaceElement element = menuControllerYio.getElement(script.argument);
        if (element == null) return;

        ForefingerElement forefinger = Scenes.forefinger.forefinger;
        forefinger.setTarget(element);
    }


    private void showMessage(ScriptItem script) {
        Scenes.messageDialog.create();
        Scenes.messageDialog.updateText(script.argument);
    }


    ScriptItem getScript() {
        for (ScriptItem scriptItem : scriptManager.items) {
            if (!scriptItem.alive) continue;
            return scriptItem;
        }

        return null;
    }


    void move() {
        repeatPerform.move();
    }

}
