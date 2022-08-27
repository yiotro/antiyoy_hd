package yio.tro.onliyoy.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.onliyoy.menu.elements.InterfaceElement;
import yio.tro.onliyoy.menu.elements.calendar.CalendarViewElement;
import yio.tro.onliyoy.menu.elements.calendar.CveDayButton;
import yio.tro.onliyoy.menu.elements.calendar.CveTab;
import yio.tro.onliyoy.stuff.GraphicsYio;
import yio.tro.onliyoy.stuff.Masking;
import yio.tro.onliyoy.stuff.calendar.CveColorYio;

import java.util.HashMap;

public class RenderCalendarViewElement extends RenderInterfaceElement{

    private CalendarViewElement cvElement;
    HashMap<CveColorYio, TextureRegion> mapColors;
    private TextureRegion completedTexture;
    private TextureRegion lockedTexture;
    private TextureRegion backgroundTexture;


    @Override
    public void loadTextures() {
        mapColors = new HashMap<>();
        for (CveColorYio cveColorYio : CveColorYio.values()) {
            mapColors.put(cveColorYio, GraphicsYio.loadTextureRegion("menu/calendar/" + cveColorYio + ".png", false));
        }
        completedTexture = GraphicsYio.loadTextureRegion("menu/calendar/completed.png", true);
        lockedTexture = GraphicsYio.loadTextureRegion("menu/calendar/calendar_locked.png", true);
        backgroundTexture = GraphicsYio.loadTextureRegion("menu/calendar/background.png", false);
    }


    @Override
    public void render(InterfaceElement element) {
        cvElement = (CalendarViewElement) element;

        renderDarken();
        batch.end();
        Masking.begin();

        prepareShapeRenderer();
        drawRoundRectShape(cvElement.getViewPosition(), cvElement.cornerEngineYio.getCurrentRadius());
        shapeRenderer.end();

        batch.begin();
        Masking.continueAfterBatchBegin();
        renderInternals();
        Masking.end(batch);

        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderDarken() {
        if (cvElement.getFactor().getValue() == 1) return;
        if (cvElement.getFactor().getValue() == 0) return;
        GraphicsYio.setBatchAlpha(batch, 0.15 * alpha);
        GraphicsYio.drawByRectangle(batch, blackPixel, cvElement.screenPosition);
        GraphicsYio.setBatchAlpha(batch, 1);
    }


    private void renderInternals() {
        for (CveTab tab : cvElement.tabsList) {
            if (!tab.isCurrentlyVisible()) continue;
            renderTab(tab);
        }
    }


    private void renderTab(CveTab tab) {
        GraphicsYio.setBatchAlpha(batch, alpha);
        GraphicsYio.drawByRectangle(batch, backgroundTexture, tab.bottomArea);
        GraphicsYio.drawByRectangle(batch, mapColors.get(tab.month.color), tab.topArea);
        GraphicsYio.renderTextOptimized(batch, blackPixel, tab.title, alpha);
        if (tab.completed) return;
        for (CveDayButton dayButton : tab.dayButtons) {
            renderSingleDayButton(dayButton);
        }
    }


    private void renderSingleDayButton(CveDayButton dayButton) {
        switch (dayButton.state) {
            default:
                System.out.println("RenderCalendarViewElement.renderSingleDayButton");
                break;
            case locked:
                GraphicsYio.setBatchAlpha(batch, alpha);
                GraphicsYio.drawByCircle(batch, lockedTexture, dayButton.position);
                break;
            case unlocked:
                GraphicsYio.renderTextOptimized(batch, blackPixel, dayButton.title, alpha);
                break;
            case completed:
                GraphicsYio.setBatchAlpha(batch, alpha);
                GraphicsYio.drawByCircle(batch, completedTexture, dayButton.position);
                break;
        }
        if (dayButton.selectionEngineYio.isSelected()) {
            GraphicsYio.setBatchAlpha(batch, dayButton.selectionEngineYio.getAlpha());
            GraphicsYio.drawByCircle(batch, blackPixel, dayButton.position);
        }
    }
}
