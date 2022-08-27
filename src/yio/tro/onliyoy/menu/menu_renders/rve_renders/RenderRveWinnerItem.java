package yio.tro.onliyoy.menu.menu_renders.rve_renders;

import yio.tro.onliyoy.menu.elements.resizable_element.AbstractRveItem;
import yio.tro.onliyoy.menu.elements.resizable_element.RveWinnerItem;
import yio.tro.onliyoy.menu.menu_renders.MenuRenders;
import yio.tro.onliyoy.stuff.GraphicsYio;

public class RenderRveWinnerItem extends AbstractRveRender{


    private RveWinnerItem rveWinnerItem;


    @Override
    public void loadTextures() {

    }


    @Override
    public void renderItem(AbstractRveItem rveItem, double alpha) {
        rveWinnerItem = (RveWinnerItem) rveItem;
        GraphicsYio.renderTextOptimized(
                batch,
                blackPixel,
                rveWinnerItem.leftViewText,
                (float) alpha
        );
        if (rveWinnerItem.color != null) {
            GraphicsYio.drawByRectangle(
                    batch,
                    MenuRenders.renderUiColors.map.get(rveWinnerItem.color),
                    rveWinnerItem.incBounds
            );
        }
        GraphicsYio.renderTextOptimized(
                batch,
                blackPixel,
                rveWinnerItem.rightViewText,
                (float) alpha
        );
    }
}
