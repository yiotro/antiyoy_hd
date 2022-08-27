package yio.tro.onliyoy.menu.scenes.editor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneEditorOverlay extends ModalSceneYio {

    private TextureRegion selectionTexture;
    private double bSize;


    @Override
    protected void initialize() {
        loadTextures();
        bSize = GraphicsYio.convertToWidth(0.05);
        createParamsButton();
        createLandscapeButton();
        createPiecesButton();
    }


    private void createPiecesButton() {
        uiFactory.getButton()
                .setSize(bSize)
                .alignBottom(0)
                .alignRight(previousElement, 0.02)
                .setTouchOffset(0.04)
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.down)
                .setCustomTexture(getTextureFromAtlas("palm_icon"))
                .setSelectionTexture(selectionTexture)
                .setHotkeyKeycode(Input.Keys.NUM_2)
                .setReaction(getOpenSceneReaction(Scenes.editorPiecesPanel));
    }


    private void createLandscapeButton() {
        uiFactory.getButton()
                .setSize(bSize)
                .alignLeft(0.03)
                .alignBottom(0)
                .setTouchOffset(0.04)
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.down)
                .setCustomTexture(getTextureFromAtlas("hex_icon"))
                .setSelectionTexture(selectionTexture)
                .setHotkeyKeycode(Input.Keys.NUM_1)
                .setReaction(getOpenSceneReaction(Scenes.editorLandscapePanel));
    }


    private void createParamsButton() {
        uiFactory.getButton()
                .setSize(bSize)
                .alignRight(0)
                .alignBottom(0)
                .setTouchOffset(0.06)
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.down)
                .setCustomTexture(getTextureFromAtlas("open"))
                .setSelectionTexture(selectionTexture)
                .setHotkeyKeycode(Input.Keys.Q)
                .setReaction(getOpenSceneReaction(Scenes.editorParams));
    }


    private void loadTextures() {
        selectionTexture = getSelectionTexture();
    }
}
