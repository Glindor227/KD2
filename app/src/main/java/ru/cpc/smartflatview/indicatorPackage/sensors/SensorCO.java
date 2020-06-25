package ru.cpc.smartflatview.indicatorPackage.sensors;

import ru.cpc.smartflatview.indicatorPackage.base.BaseSensor;


public class SensorCO extends BaseSensor {
    public SensorCO(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale) {
        super(iX, iY, 4, sName, "", bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

    }
}
