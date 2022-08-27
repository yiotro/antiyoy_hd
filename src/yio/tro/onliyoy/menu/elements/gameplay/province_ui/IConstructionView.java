package yio.tro.onliyoy.menu.elements.gameplay.province_ui;

import yio.tro.onliyoy.game.core_model.PieceType;

public interface IConstructionView {


    void onRulesDefined();


    void onPcKeyPressed(int keycode);


    void onUnitSelected();


    PieceType getCurrentlyChosenPieceType();


    void onPieceBuiltByHand();
}
