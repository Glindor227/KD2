package ru.cpc.smartflatview.indicatorPackage.sensors;

import ru.cpc.smartflatview.indicatorPackage.base.BaseSensor;


public class SensorTemperature extends BaseSensor
{
    public SensorTemperature(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY, 1, sName, "°C", bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

    }
}