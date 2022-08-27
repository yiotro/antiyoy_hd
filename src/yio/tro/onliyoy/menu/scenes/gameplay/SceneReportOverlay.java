package yio.tro.onliyoy.menu.scenes.gameplay;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneReportOverlay extends ModalSceneYio {

    private TextureRegion selectionTexture;
    private double bSize;
    private double touchOffset;
    private LabelElement nameLabel;
    private LabelElement messageLabel;


    @Override
    protected void initialize() {
        loadTextures();
        bSize = GraphicsYio.convertToWidth(0.05);
        touchOffset = 0.06;
        createDecideButton();
        createNameLabel();
        createMessageLabel();
    }


    private void createMessageLabel() {
        messageLabel = uiFactory.getLabelElement()
                .setBackgroundEnabled(true)
                .setSize(0.01, 0.04)
                .setLeftAlignEnabled(true)
                .alignUnder(previousElement, 0)
                .alignLeft(0.03)
                .setAnimation(AnimationYio.up)
                .setFont(Fonts.miniFont)
                .setTitle("-");
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    private void loadValues() {
        String name = netRoot.verificationInfo.name;
        String report = netRoot.verificationInfo.report;
        nameLabel.setTitle(CharLocalizerYio.getInstance().apply(name));
        messageLabel.setTitle(CharLocalizerYio.getInstance().apply(report));
    }


    private void createNameLabel() {
        nameLabel = uiFactory.getLabelElement()
                .setBackgroundEnabled(true)
                .setSize(0.01, 0.04)
                .setLeftAlignEnabled(true)
                .alignTop(0.01)
                .alignLeft(0.03)
                .setAnimation(AnimationYio.up)
                .setFont(Fonts.miniFont)
                .setTitle("-");
    }


    private void createDecideButton() {
        uiFactory.getButton()
                .setSize(bSize)
                .alignRight(0)
                .alignBottom(0)
                .setTouchOffset(touchOffset)
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.down)
                .setCustomTexture(getTextureFromAtlas("verify"))
                .setSelectionTexture(selectionTexture)
                .setReaction(getDecideReaction());
    }


    private Reaction getDecideReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.confirmDeleteLevel.create();
            }
        };
    }


    private void loadTextures() {
        selectionTexture = getSelectionTexture();
    }
}
