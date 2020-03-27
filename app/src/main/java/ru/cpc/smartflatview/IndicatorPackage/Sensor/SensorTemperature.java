package ru.cpc.smartflatview.IndicatorPackage.Sensor;

import ru.cpc.smartflatview.BaseSensor;

/**
 * Created by Вик on 026. 26.04.16.
 */
public class SensorTemperature extends BaseSensor
{
    public SensorTemperature(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY, 1, sName, "°C", bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
        // TODO Auto-generated constructor stub
    }
}