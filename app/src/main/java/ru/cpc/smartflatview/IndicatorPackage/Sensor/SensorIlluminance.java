package ru.cpc.smartflatview.IndicatorPackage.Sensor;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseSensor;


public class SensorIlluminance extends BaseSensor
{
    public SensorIlluminance(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY, 3, sName, "lx", bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

    }
}