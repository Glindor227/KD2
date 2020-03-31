package ru.cpc.smartflatview.IndicatorPackage.Macros;

import android.content.Context;

import ru.cpc.smartflatview.BaseMacro;
import ru.cpc.smartflatview.R;

public class MacroAlarm extends BaseMacro
{
	public MacroAlarm(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getIndDisC(R.drawable.error_alarm_p,R.drawable.error_alarm,R.drawable.alarmnode1,R.drawable.error_alarm_p),
			getIndDisC(R.drawable.error_on_p,R.drawable.error_on,R.drawable.alarmnode0,R.drawable.error_on_c),
				17, sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
		// TODO Auto-generated constructor stub
		m_bText2 = false;
	}

	@Override
	public boolean ShowPopup(Context context)
	{
		return false;
	}
}
