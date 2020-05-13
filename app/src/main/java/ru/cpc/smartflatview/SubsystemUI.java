package ru.cpc.smartflatview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class SubsystemUI extends RelativeLayout
{
	protected Subsystem m_pSubsystem;
	Context context;
	@NonNull
	@Override
    public String toString(){
        return m_pSubsystem.toString();
    }

	public SubsystemUI(SFServer pServer, Context context, Subsystem pSubsystem)	{
		super(context);
		this.context = context;
		m_pSubsystem = pSubsystem;

		View pCog = new View(context);
		pCog.setBackgroundResource(R.drawable.oie_transparent);
		addView(pCog);
		
		for(Indicator pInd : m_pSubsystem.m_cIndicators)
	    {
			addView(new IndicatorUI(pServer, context, pInd));
	    }

		m_pSubsystem.m_pUI = this;

		Update();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int width = r-l;
		int height = b-t;
		boolean black = Prefs.getBlack(context);
		if(black)
			setBackgroundColor(getResources().getColor(R.color.colorBlack));
		Log.d("TEST", "Page1 height = " + height);

		int itemWidth;
		int itemHeight;
		itemWidth = width / m_pSubsystem.m_iGridWidth - 5;
		itemHeight = height / m_pSubsystem.m_iGridHeight - 5;
		itemWidth = itemWidth * 4 / 5;
		itemHeight = itemHeight * 4 / 5;

		Log.d("TEST", "Item height = " + itemHeight);

		for(Indicator pInd : m_pSubsystem.m_cIndicators) {
			int iRealItemWidth = itemWidth;
			int iRealItemHeight = itemHeight;

			if(pInd.m_bDoubleScale)	{
				iRealItemWidth *= 2;
				iRealItemHeight *= 2;
			}

            if(pInd.m_bDoubleWidth) {
                iRealItemWidth *= 2.2;
            }

			int x = (int)(pInd.m_fXPercent * width / 100 - iRealItemWidth/2);
			int y = (int)(pInd.m_fYPercent * height / 100 - iRealItemHeight/2);
			pInd.m_pUI.setBlack(black);
			pInd.m_pUI.layout(x, y, x + iRealItemWidth, y + iRealItemHeight);
		}
		Log.d("111111111111111", "Added " + m_pSubsystem.m_cIndicators.size() + " indicators.");
	}

	private boolean m_bAlarmedOld = false;
	
	protected void Update()
	{
		if(m_pSubsystem.m_bAlarmed != m_bAlarmedOld)
		{
			m_bAlarmedOld = m_pSubsystem.m_bAlarmed;
			
//			if(mOnRoomAlarmListener != null)
//				mOnRoomAlarmListener.onRoomAlarm(this);

//			m_pStateIcon.setBackgroundResource(m_pSubsystem.m_bAlarmed ? R.drawable.zone_alarm : R.drawable.zone_ok);
		}
	}
}
