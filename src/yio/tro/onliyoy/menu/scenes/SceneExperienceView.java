package yio.tro.onliyoy.menu.scenes;

import yio.tro.onliyoy.menu.elements.experience.ExperienceViewElement;

public class SceneExperienceView extends ModalSceneYio{


    private double height;
    public ExperienceViewElement experienceViewElement;


    @Override
    protected void initialize() {
        height = 0.12;
        createDefaultPanel(height);
        defaultPanel.setResistantToAutoDestroy(true);

        experienceViewElement = uiFactory.getExperienceViewElement()
                .setParent(defaultPanel)
                .setResistantToAutoDestroy(true)
                .setSize(1, height);
    }


    @Override
    protected boolean shouldNotTouchFocusScene() {
        return true;
    }
}
