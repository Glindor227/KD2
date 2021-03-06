package ru.cpc.smartflatview.IndicatorPackage.AlarmSensor;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseAlarmSensor;
import ru.cpc.smartflatview.R;

public class MotionSensor extends BaseAlarmSensor
{
	public MotionSensor(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getIndDisC(R.drawable.guard_sensor_alarm_p,R.drawable.guard_sensor_alarm,R.drawable.id076,R.drawable.guard_sensor_alarm_c),
				getIndDisC(R.drawable.guard_sensor_on_p, R.drawable.guard_sensor_on,R.drawable.id077,R.drawable.guard_sensor_on_c),
				getIndDisC(R.drawable.guard_sensor_off_p,R.drawable.guard_sensor_off,R.drawable.id075,R.drawable.guard_sensor_off_c),
				2, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
		// TODO Auto-generated constructor stub
		m_iImitateAlarmChance = 6;
	}
}
