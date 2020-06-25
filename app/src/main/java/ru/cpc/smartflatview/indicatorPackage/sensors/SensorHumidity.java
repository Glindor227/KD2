package ru.cpc.smartflatview.indicatorPackage.sensors;

import ru.cpc.smartflatview.indicatorPackage.base.BaseSensor;


public class SensorHumidity extends BaseSensor
{
    public SensorHumidity(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY, 2, sName, "%", bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

    }
}