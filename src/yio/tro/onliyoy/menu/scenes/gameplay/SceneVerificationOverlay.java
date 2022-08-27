package yio.tro.onliyoy.menu.scenes.gameplay;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.menu.elements.AnimationYio;
import yio.tro.onliyoy.menu.elements.LabelElement;
import yio.tro.onliyoy.menu.reactions.Reaction;
import yio.tro.onliyoy.menu.scenes.ModalSceneYio;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class SceneVerificationOverlay extends ModalSceneYio {


    private TextureRegion selectionTexture;
    private double bSize;
    private double touchOffset;
    private LabelElement timeLabel;
    private LabelElement nameLabel;


    @Override
    protected void initialize() {
        loadTextures();
        bSize = GraphicsYio.convertToWidth(0.05);
        touchOffset = 0.06;
        createVerifyButton();
        createDeclineButton();
        createTimeLabel();
        createNameLabel();
    }


    private void createNameLabel() {
        nameLabel = uiFactory.getLabelElement()
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
        long creationTimeMillis = netRoot.verificationInfo.creationTime;
        int creationTimeFrames = Yio.convertMillisIntoFrames(creationTimeMillis);
        String timePrefix = languagesManager.getString("creation_time");
        String timeString = Yio.convertTimeToUnderstandableString(creationTimeFrames);
        timeLabel.setTitle(timePrefix + ": " + timeString);
        String name = netRoot.verificationInfo.name;
        nameLabel.setTitle(CharLocalizerYio.getInstance().apply(name));
    }


    private void createTimeLabel() {
        timeLabel = uiFactory.getLabelElement()
                .setBackgroundEnabled(true)
                .setSize(0.01, 0.04)
                .setLeftAlignEnabled(true)
                .alignTop(0.01)
                .alignLeft(0.03)
                .setAnimation(AnimationYio.up)
                .setFont(Fonts.miniFont)
                .setTitle("-");
    }


    private void createDeclineButton() {
        uiFactory.getButton()
                .setSize(bSize)
                .alignLeft(0)
                .alignBottom(0)
                .setTouchOffset(touchOffset)
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.down)
                .setCustomTexture(getTextureFromAtlas("decline"))
                .setSelectionTexture(selectionTexture)
                .setReaction(getDeclineReaction());
    }


    private Reaction getDeclineReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.confirmDeclineLevel.create();
            }
        };
    }


    private void createVerifyButton() {
        uiFactory.getButton()
                .setSize(bSize)
                .alignRight(0)
                .alignBottom(0)
                .setTouchOffset(touchOffset)
                .setIgnoreResumePause(true)
                .setAnimation(AnimationYio.down)
                .setCustomTexture(getTextureFromAtlas("verify"))
                .setSelectionTexture(selectionTexture)
                .setReaction(getVerifyReaction());
    }


    private Reaction getVerifyReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Scenes.confirmVerifyLevel.create();
            }
        };
    }


    private void loadTextures() {
        selectionTexture = getSelectionTexture();
    }
}
