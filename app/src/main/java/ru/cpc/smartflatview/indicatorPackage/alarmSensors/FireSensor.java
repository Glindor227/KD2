package ru.cpc.smartflatview.indicatorPackage.alarmSensors;

import ru.cpc.smartflatview.indicatorPackage.base.BaseAlarmSensor;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.indicatorPackage.base.VoiceDate;

public class FireSensor extends BaseAlarmSensor
{
	public FireSensor(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
			super(iX, iY,
					getIndDisC(R.drawable.fire_sensor_alarm_p,R.drawable.fire_sensor_alarm,R.drawable.id072,R.drawable.fire_sensor_alarm_c),
					getIndDisC(R.drawable.fire_sensor_on_p,R.drawable.fire_sensor_on,R.drawable.id073,R.drawable.fire_sensor_on_c),
					getIndDisC(R.drawable.fire_sensor_off_p,R.drawable.fire_sensor_off,R.drawable.id071,R.drawable.fire_sensor_off_c),
					1, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
		voice = new VoiceDate("Датчик пожарный");
		voice.addCommand("Поставить на охрану",0);
		voice.addCommand("Снять с охраны",0);

		m_iImitateAlarmChance = 15;
	}
}
