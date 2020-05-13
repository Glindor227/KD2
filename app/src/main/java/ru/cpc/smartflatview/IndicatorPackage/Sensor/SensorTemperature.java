package ru.cpc.smartflatview.IndicatorPackage.Sensor;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseSensor;


public class SensorTemperature extends BaseSensor
{
    public SensorTemperature(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY, 1, sName, "°C", bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

    }
}