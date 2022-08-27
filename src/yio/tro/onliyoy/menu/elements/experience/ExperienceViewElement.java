package yio.tro.onliyoy.menu.elements.experience;

import yio.tro.onliyoy.menu.MenuControllerYio;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.RenderInterfaceElement;
import yio.tro.onliyoy.menu.scenes.Scenes;
import yio.tro.onliyoy.net.NetExperienceManager;
import yio.tro.onliyoy.net.shared.NetValues;
import yio.tro.onliyoy.stuff.factor_yio.FactorYio;
import yio.tro.onliyoy.stuff.factor_yio.MovementType;

public class ExperienceViewElement extends InterfaceElement<ExperienceViewElement> {

    public FactorYio flipFactor;
    public EvePanel panel0;
    public EvePanel panel1;
    long startExperience;
    long endExperience;
    public EveState currentState;
    boolean isFlipNeeded;
    public FactorYio fillFactor;
    FactorYio waitFactor;
    public FactorYio fadeFactor;


    public ExperienceViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        flipFactor = new FactorYio();
        panel0 = new EvePanel(this, 0);
        panel1 = new EvePanel(this, 1);
        currentState = null;
        fillFactor = new FactorYio();
        waitFactor = new FactorYio();
        fadeFactor = new FactorYio();
    }


    @Override
    protected ExperienceViewElement getThis() {
        return this;
    }


    public void setValues(long startExperience, long endExperience) {
        this.startExperience = startExperience;
        this.endExperience = endExperience;

        int startLevel = NetExperienceManager.convertExperienceToLevel(startExperience);
        int endLevel = NetExperienceManager.convertExperienceToLevel(endExperience);
        double startRatio = (double) (startExperience % NetValues.EXPERIENCE_PER_LEVEL) / (double) NetValues.EXPERIENCE_PER_LEVEL;
        double endRatio = (double) (endExperience % NetValues.EXPERIENCE_PER_LEVEL) / (double) NetValues.EXPERIENCE_PER_LEVEL;
        isFlipNeeded = (startLevel < endLevel);

        panel0.setLevel(startLevel);
        panel1.setLevel(endLevel);

        if (isFlipNeeded) {
            panel0.strip.setValues(startRatio, 1);
            panel1.strip.setValues(endRatio, 0);
        } else {
            panel0.strip.setValues(startRatio, endRatio);
        }
    }


    @Override
    public void onMove() {
        fillFactor.move();
        flipFactor.move();
        waitFactor.move();
        fadeFactor.move();
        panel0.move();
        panel1.move();
        checkToApplyState();
    }


    private void checkToApplyState() {
        switch (currentState) {
            default:
                break;
            case appearing:
                applyAppearingState();
                break;
            case filling:
                applyFillingState();
                break;
            case fading:
                applyFadingState();
                break;
            case waiting_to_flip:
                applyWaitingToFlipState();
                break;
            case flipping:
                applyFlippingState();
                break;
            case waiting_to_destroy:
                applyWaitingToDestroyState();
                break;
            case destroying:
                break;
        }
    }


    private void applyWaitingToDestroyState() {
        if (waitFactor.getValue() < 1) return;
        currentState = EveState.destroying;
        Scenes.experienceView.destroy();
    }


    private void applyFlippingState() {
        if (flipFactor.getValue() < 1) return;
        currentState = EveState.waiting_to_destroy;
        launchWaitFactor(1.5);
    }


    private void applyWaitingToFlipState() {
        if (waitFactor.getValue() < 1) return;
        currentState = EveState.flipping;
        flipFactor.appear(MovementType.inertia, 2);
    }


    private void applyFadingState() {
        if (fadeFactor.getValue() < 1) return;
        if (isFlipNeeded) {
            currentState = EveState.waiting_to_flip;
            launchWaitFactor(2);
        } else {
            currentState = EveState.waiting_to_destroy;
            launchWaitFactor(1.5);
        }
    }


    private void applyFillingState() {
        if (fillFactor.getValue() < 1) return;
        currentState = EveState.fading;
        fadeFactor.appear(MovementType.inertia, 1.2);
    }


    void launchWaitFactor(double speed) {
        waitFactor.reset();
        waitFactor.appear(MovementType.inertia, speed);
    }


    private void applyAppearingState() {
        if (getFactor().getValue() < 1) return;
        currentState = EveState.filling;
        fillFactor.appear(MovementType.inertia, 1.5);
    }


    @Override
    public void onDestroy() {
        currentState = EveState.destroying;
    }


    @Override
    public void onAppear() {
        currentState = EveState.appearing;
        fillFactor.reset();
        flipFactor.reset();
        fadeFactor.reset();
        waitFactor.reset();
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderExperienceViewElement;
    }
}
