package yio.tro.onliyoy.menu.elements.customizable_list;

import yio.tro.onliyoy.Fonts;
import yio.tro.onliyoy.SoundManager;
import yio.tro.onliyoy.SoundType;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.menu.menu_renders.render_custom_list.AbstractRenderCustomListItem;
import yio.tro.onliyoy.net.shared.CharLocalizerYio;
import yio.tro.onliyoy.net.shared.NetRenamingData;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.RectangleYio;
import yio.tro.onliyoy.stuff.RenderableTextYio;

public class RenamingListItem extends AbstractCustomListItem {

    public boolean darken;
    public RenderableTextYio currentNameView;
    public RenderableTextYio desiredNameView;
    public RenderableTextYio arrowView;
    public String targetClientId;
    private float offset;
    public boolean declined;
    public RectangleYio strikethrough;


    @Override
    protected void initialize() {
        currentNameView = new RenderableTextYio();
        currentNameView.setFont(Fonts.miniFont);
        desiredNameView = new RenderableTextYio();
        desiredNameView.setFont(Fonts.miniFont);
        arrowView = new RenderableTextYio();
        arrowView.setFont(Fonts.miniFont);
        arrowView.setString("->");
        arrowView.updateMetrics();
        offset = 0.02f * GraphicsYio.width;
        declined = false;
        strikethrough = new RectangleYio();
    }


    public void setValues(NetRenamingData netRenamingData) {
        targetClientId = netRenamingData.targetClientId;
        currentNameView.setString(CharLocalizerYio.getInstance().apply(netRenamingData.currentName));
        currentNameView.updateMetrics();
        desiredNameView.setString(CharLocalizerYio.getInstance().apply(netRenamingData.desiredName));
        desiredNameView.updateMetrics();
    }


    @Override
    protected void move() {
        updateCurrentNameView();
        updateArrowView();
        updateDesiredNameView();
        updateStrikethrough();
    }


    private void updateStrikethrough() {
        if (!declined) return;
        RectangleYio src = desiredNameView.bounds;
        strikethrough.height = 2 * GraphicsYio.borderThickness;
        strikethrough.y = src.y + src.height / 2 - strikethrough.height / 2;
        strikethrough.width = src.width + 0.01f * GraphicsYio.width;
        strikethrough.x = src.x + src.width / 2 - strikethrough.width / 2;
    }


    private void updateDesiredNameView() {
        desiredNameView.centerVertical(viewPosition);
        desiredNameView.position.x = viewPosition.x + viewPosition.width - offset - desiredNameView.width;
        desiredNameView.updateBounds();
    }


    private void updateArrowView() {
        arrowView.centerVertical(viewPosition);
        arrowView.centerHorizontal(viewPosition);
        arrowView.updateBounds();
    }


    private void updateCurrentNameView() {
        currentNameView.centerVertical(viewPosition);
        currentNameView.position.x = viewPosition.x + offset;
        currentNameView.updateBounds();
    }


    @Override
    protected double getWidth() {
        return getDefaultWidth();
    }


    @Override
    protected double getHeight() {
        return 0.05f * GraphicsYio.height;
    }


    @Override
    protected void onPositionChanged() {

    }


    @Override
    protected void onClicked() {
        declined = !declined;
        SoundManager.playSound(SoundType.button);
        updateStrikethrough();
    }


    @Override
    protected void onLongTapped() {

    }


    public void setDarken(boolean darken) {
        this.darken = darken;
    }


    @Override
    public AbstractRenderCustomListItem getRender() {
        return MenuRenders.renderRenamingListItem;
    }
}
