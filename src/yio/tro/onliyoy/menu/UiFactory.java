package yio.tro.onliyoy.menu;

import yio.tro.onliyoy.menu.elements.*;
import yio.tro.onliyoy.menu.elements.button.ButtonYio;
import yio.tro.onliyoy.menu.elements.calendar.CalendarViewElement;
import yio.tro.onliyoy.menu.elements.choose_game_mode.CgmElement;
import yio.tro.onliyoy.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.onliyoy.menu.elements.editor.EditorPanelElement;
import yio.tro.onliyoy.menu.elements.experience.ExperienceViewElement;
import yio.tro.onliyoy.menu.elements.forefinger.ForefingerElement;
import yio.tro.onliyoy.menu.elements.gameplay.NetChatViewElement;
import yio.tro.onliyoy.menu.elements.gameplay.NetTurnViewElement;
import yio.tro.onliyoy.menu.elements.gameplay.ViewTouchModeElement;
import yio.tro.onliyoy.menu.elements.gameplay.income_graph.IncomeGraphElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.AdvancedConstructionPanelElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.ConstructionViewElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.EconomicsViewElement;
import yio.tro.onliyoy.menu.elements.gameplay.province_ui.MechanicsHookElement;
import yio.tro.onliyoy.menu.elements.plot_view.PlotViewElement;
import yio.tro.onliyoy.menu.elements.highlight_area.HighlightAreaElement;
import yio.tro.onliyoy.menu.elements.icon_label_element.IconLabelElement;
import yio.tro.onliyoy.menu.elements.keyboard.CustomKeyboardElement;
import yio.tro.onliyoy.menu.elements.keyboard.NativeKeyboardElement;
import yio.tro.onliyoy.menu.elements.multi_button.MultiButtonElement;
import yio.tro.onliyoy.menu.elements.net.*;
import yio.tro.onliyoy.menu.elements.replay_overlay.ReplayControlElement;
import yio.tro.onliyoy.menu.elements.resizable_element.ResizableViewElement;
import yio.tro.onliyoy.menu.elements.rules_picker.RulesPickerElement;
import yio.tro.onliyoy.menu.elements.setup_entities.EntitiesSetupElement;
import yio.tro.onliyoy.menu.elements.setup_entities.CondensedEntitiesViewElement;
import yio.tro.onliyoy.menu.elements.setup_entities.SingleEntityConfigureElement;
import yio.tro.onliyoy.menu.elements.shop.ShopViewElement;
import yio.tro.onliyoy.menu.elements.slider.SliderElement;
import yio.tro.onliyoy.menu.elements.smileys.SmileysKeyboardElement;
import yio.tro.onliyoy.menu.scenes.SceneYio;

public class UiFactory {

    MenuControllerYio menuControllerYio;
    SceneYio sceneYio;


    public UiFactory(SceneYio sceneYio) {
        this.sceneYio = sceneYio;
        menuControllerYio = sceneYio.menuControllerYio;
    }


    public ButtonYio getButton() {
        ButtonYio buttonYio = new ButtonYio(menuControllerYio);
        addElementToScene(buttonYio);

        return buttonYio;
    }


    public CheckButtonYio getCheckButton() {
        return (CheckButtonYio) addElementToScene(new CheckButtonYio(menuControllerYio));
    }


    public CircleButtonYio getCircleButton() {
        return (CircleButtonYio) addElementToScene(new CircleButtonYio(menuControllerYio));
    }


    public ScrollableAreaYio getScrollableAreaYio() {
        return (ScrollableAreaYio) addElementToScene(new ScrollableAreaYio(menuControllerYio));
    }


    public NotificationElement getNotificationElement() {
        return (NotificationElement) addElementToScene(new NotificationElement(menuControllerYio));
    }


    public ViewTouchModeElement getViewTouchModeElement() {
        return (ViewTouchModeElement) addElementToScene(new ViewTouchModeElement(menuControllerYio));
    }


    public CustomizableListYio getCustomizableListYio() {
        CustomizableListYio customizableListYio = new CustomizableListYio(menuControllerYio);
        return (CustomizableListYio) addElementToScene(customizableListYio);
    }


    public NativeKeyboardElement getNativeKeyboardElement() {
        return (NativeKeyboardElement) addElementToScene(new NativeKeyboardElement(menuControllerYio));
    }


    public ForefingerElement getForefingerElement() {
        return (ForefingerElement) addElementToScene(new ForefingerElement(menuControllerYio));
    }


    public LabelElement getLabelElement() {
        return (LabelElement) addElementToScene(new LabelElement(menuControllerYio));
    }


    public AdvancedLabelElement getAdvancedLabelElement() {
        return (AdvancedLabelElement) addElementToScene(new AdvancedLabelElement(menuControllerYio));
    }


    public ImportantConfirmationButton getImportantConfirmationButton() {
        return (ImportantConfirmationButton) addElementToScene(new ImportantConfirmationButton(menuControllerYio));
    }


    public LightBottomPanelElement getLightBottomPanelElement() {
        return (LightBottomPanelElement) addElementToScene(new LightBottomPanelElement(menuControllerYio));
    }


    public ExceptionViewElement getExceptionViewElement() {
        return (ExceptionViewElement) addElementToScene(new ExceptionViewElement(menuControllerYio));
    }


    public IconLabelElement getIconLabelElement() {
        return (IconLabelElement) addElementToScene(new IconLabelElement(menuControllerYio));
    }


    public AnnounceViewElement getAnnounceViewElement() {
        return (AnnounceViewElement) addElementToScene(new AnnounceViewElement(menuControllerYio));
    }


    public ScrollHelperElement getScrollHelperElement() {
        return (ScrollHelperElement) addElementToScene(new ScrollHelperElement(menuControllerYio));
    }


    public SliderElement getSlider() {
        return (SliderElement) addElementToScene(new SliderElement(menuControllerYio));
    }


    public MultiButtonElement getMultiButtonElement() {
        return (MultiButtonElement) addElementToScene(new MultiButtonElement(menuControllerYio));
    }


    public TopCoverElement getTopCoverElement() {
        return (TopCoverElement) addElementToScene(new TopCoverElement(menuControllerYio));
    }


    public CustomKeyboardElement getCustomKeyboardElement() {
        return (CustomKeyboardElement) addElementToScene(new CustomKeyboardElement(menuControllerYio));
    }


    public EntitiesSetupElement getEntitiesSetupElement() {
        return (EntitiesSetupElement) addElementToScene(new EntitiesSetupElement(menuControllerYio));
    }


    public SingleEntityConfigureElement getSingleEntityConfigureElement() {
        return (SingleEntityConfigureElement) addElementToScene(new SingleEntityConfigureElement(menuControllerYio));
    }


    public ReplayControlElement getReplayControlElement() {
        return (ReplayControlElement) addElementToScene(new ReplayControlElement(menuControllerYio));
    }


    public EconomicsViewElement getEconomicsViewElement() {
        return (EconomicsViewElement) addElementToScene(new EconomicsViewElement(menuControllerYio));
    }


    public ConstructionViewElement getConstructionViewElement() {
        return (ConstructionViewElement) addElementToScene(new ConstructionViewElement(menuControllerYio));
    }


    public ResizableViewElement getResizableViewElement() {
        return (ResizableViewElement) addElementToScene(new ResizableViewElement(menuControllerYio));
    }


    public IncomeGraphElement getIncomeGraphElement() {
        return (IncomeGraphElement) addElementToScene(new IncomeGraphElement(menuControllerYio));
    }


    public EditorPanelElement getEditorPanelElement() {
        return (EditorPanelElement) addElementToScene(new EditorPanelElement(menuControllerYio));
    }


    public NicknameViewElement getNicknameViewElement() {
        return (NicknameViewElement) addElementToScene(new NicknameViewElement(menuControllerYio));
    }


    public NetProcessViewElement getNetProcessViewElement() {
        return (NetProcessViewElement) addElementToScene(new NetProcessViewElement(menuControllerYio));
    }


    public MatchParametersViewElement getMatchParametersViewElement() {
        return (MatchParametersViewElement) addElementToScene(new MatchParametersViewElement(menuControllerYio));
    }


    public MatchPreparationViewElement getMatchPreparationViewElement() {
        return (MatchPreparationViewElement) addElementToScene(new MatchPreparationViewElement(menuControllerYio));
    }


    public ChooseColorElement getChooseMlColorElement() {
        return (ChooseColorElement) addElementToScene(new ChooseColorElement(menuControllerYio));
    }


    public NetTurnViewElement getNetTurnViewElement() {
        return (NetTurnViewElement) addElementToScene(new NetTurnViewElement(menuControllerYio));
    }


    public NetChatViewElement getNetChatViewElement() {
        return (NetChatViewElement) addElementToScene(new NetChatViewElement(menuControllerYio));
    }


    public QmsElement getQmsElement() {
        return (QmsElement) addElementToScene(new QmsElement(menuControllerYio));
    }


    public CgmElement getCgmElement() {
        return (CgmElement) addElementToScene(new CgmElement(menuControllerYio));
    }


    public CalendarViewElement getCalendarViewElement() {
        return (CalendarViewElement) addElementToScene(new CalendarViewElement(menuControllerYio));
    }


    public HighlightAreaElement getHighlightAreaElement() {
        return (HighlightAreaElement) addElementToScene(new HighlightAreaElement(menuControllerYio));
    }


    public CondensedEntitiesViewElement getCondensedEntitiesViewElement() {
        return (CondensedEntitiesViewElement) addElementToScene(new CondensedEntitiesViewElement(menuControllerYio));
    }


    public ExperienceViewElement getExperienceViewElement() {
        return (ExperienceViewElement) addElementToScene(new ExperienceViewElement(menuControllerYio));
    }


    public CoinsElpViewElement getCoinsElpViewElement() {
        return (CoinsElpViewElement) addElementToScene(new CoinsElpViewElement(menuControllerYio));
    }


    public DelayedActionElement getDelayedActionElement() {
        return (DelayedActionElement) addElementToScene(new DelayedActionElement(menuControllerYio));
    }


    public SmileysKeyboardElement getSmileysKeyboardElement() {
        return (SmileysKeyboardElement) addElementToScene(new SmileysKeyboardElement(menuControllerYio));
    }


    public FishNameViewElement getFishNameViewElement() {
        return (FishNameViewElement) addElementToScene(new FishNameViewElement(menuControllerYio));
    }


    public ShopViewElement getShopViewElement() {
        return (ShopViewElement) addElementToScene(new ShopViewElement(menuControllerYio));
    }


    public DarkenElement getDarkenElement() {
        return (DarkenElement) addElementToScene(new DarkenElement(menuControllerYio));
    }


    public AdvancedConstructionPanelElement getAdvancedConstructionPanelElement() {
        return (AdvancedConstructionPanelElement) addElementToScene(new AdvancedConstructionPanelElement(menuControllerYio));
    }


    public MechanicsHookElement getMechanicsHookElement() {
        return (MechanicsHookElement) addElementToScene(new MechanicsHookElement(menuControllerYio));
    }


    public RulesPickerElement getRulesPickerElement() {
        return (RulesPickerElement) addElementToScene(new RulesPickerElement(menuControllerYio));
    }


    public PlotViewElement getPlotViewElement() {
        return (PlotViewElement) addElementToScene(new PlotViewElement(menuControllerYio));
    }


    private InterfaceElement addElementToScene(InterfaceElement interfaceElement) {
        sceneYio.addLocalElement(interfaceElement);
        menuControllerYio.addElement(interfaceElement);
        return interfaceElement;
    }
}
