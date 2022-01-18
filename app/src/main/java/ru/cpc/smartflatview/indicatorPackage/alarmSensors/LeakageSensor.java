package ru.cpc.smartflatview.indicatorPackage.alarmSensors;

import ru.cpc.smartflatview.indicatorPackage.base.BaseAlarmSensor;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.voice.VoiceDate;

public class LeakageSensor extends BaseAlarmSensor
{
	public LeakageSensor(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getIndDisC(R.drawable.leakage_sensor_alarm_p,R.drawable.leakage_sensor_alarm,R.drawable.id079,R.drawable.leakage_sensor_alarm_c),
				getIndDisC(R.drawable.leakage_sensor_on_p,R.drawable.leakage_sensor_on,R.drawable.id080,R.drawable.leakage_sensor_on_c),
				getIndDisC(R.drawable.leakage_sensor_off_p,R.drawable.leakage_sensor_off,R.drawable.id081,R.drawable.leakage_sensor_off_c),
				3, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
		voice = new VoiceDate("Датчик протечки");
		voice.addCommand("Поставить на охрану",0);
		voice.addCommand("Снять с охраны",0);


		m_iImitateAlarmChance = 9;
	}
}
