package ru.cpc.smartflatview.IndicatorPackage.AlarmSensor;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseAlarmSensor;
import ru.cpc.smartflatview.R;

public class FireSensor extends BaseAlarmSensor
{
	public FireSensor(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
			super(iX, iY,
					getIndDisC(R.drawable.fire_sensor_alarm_p,R.drawable.fire_sensor_alarm,R.drawable.id072,R.drawable.fire_sensor_alarm_c),
					getIndDisC(R.drawable.fire_sensor_on_p,R.drawable.fire_sensor_on,R.drawable.id073,R.drawable.fire_sensor_on_c),
					getIndDisC(R.drawable.fire_sensor_off_p,R.drawable.fire_sensor_off,R.drawable.id071,R.drawable.fire_sensor_off_c),
					1, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);



		m_iImitateAlarmChance = 15;
	}
}
