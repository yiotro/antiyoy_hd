package yio.tro.onliyoy.game.export_import;

import yio.tro.onliyoy.Yio;
import yio.tro.onliyoy.game.core_model.CoreModel;
import yio.tro.onliyoy.game.core_model.Hex;
import yio.tro.onliyoy.game.core_model.core_provinces.Province;

public class IwCoreProvinces extends AbstractImportWorker{

    CoreModel coreModel;


    public IwCoreProvinces(CoreModel coreModel) {
        this.coreModel = coreModel;
    }


    @Override
    protected String getDefaultSectionName() {
        return "provinces";
    }


    @Override
    protected void apply() {
        // provinces have to be detected at this point
        // because this worker has to be used in currentProcess.createAdvancedStuff()
        String[] innerSplit = source.split(">");
        coreModel.provincesManager.setCurrentId(Integer.valueOf(innerSplit[0]));
        if (innerSplit.length < 2) return;
        for (String token : innerSplit[1].split(",")) {
            String[] split = token.split("<");
            if (split.length < 5) continue;
            int c1 = Integer.valueOf(split[0]);
            int c2 = Integer.valueOf(split[1]);
            Hex hex = coreModel.getHex(c1, c2);
            if (hex == null) {
                System.out.println("IwCoreProvinces.apply: hex " + c1 + ", " + c2 + " is null");
                continue;
            }
            Province province = hex.getProvince();
            if (province == null) continue;
            int id = Integer.valueOf(split[2]);
            int money = Integer.valueOf(split[3]);
            String cityName = split[4];
            province.setId(id);
            province.setMoney(money);
            province.setCityName(cityName);
        }
    }
}
