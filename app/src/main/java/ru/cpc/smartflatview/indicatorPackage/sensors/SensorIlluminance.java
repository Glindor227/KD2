package ru.cpc.smartflatview.indicatorPackage.sensors;

import ru.cpc.smartflatview.indicatorPackage.base.BaseSensor;


public class SensorIlluminance extends BaseSensor
{
    public SensorIlluminance(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY, 3, sName, "lx", bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

    }
}