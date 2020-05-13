package ru.cpc.smartflatview;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Subsystem
{
	int m_iIndex;
	String m_sID;

	public String m_sName;

	ArrayList<Indicator> m_cIndicators = new ArrayList<>();
	int m_iGridWidth;
	int m_iGridHeight;

	Room m_pRoom = null;

	protected SubsystemUI m_pUI = null;

	String m_sAlarm = "-1";
	
	public Subsystem(String sID, int index, int iScaleX, int iScaleY, String sName)
	{
		m_sID = sID;

		m_iIndex = index;

		m_iGridWidth = iScaleX;
		m_iGridHeight = iScaleY;
		m_sName = sName;
	}

	public Subsystem Bind(String sAddress)
	{
		m_sAlarm = sAddress;
		
		//Log.d("TEST", m_sName + ".alarmaddress = " + sAddress);
		
		return this;
	}

	void AddIndicator(Indicator pNewInd)
	{
		if(pNewInd != null)
		{
			m_cIndicators.add(pNewInd);
			pNewInd.m_pSubsystem = this;
		}
	}

	boolean m_bAlarmed = false;

	public void Process(String sAddr, String sVal)
	{
		if(m_sAlarm.equalsIgnoreCase(sAddr))
			m_bAlarmed = Float.parseFloat(sVal) == 1;

		m_pUI.Update();
	}	
	
	int getIndicatorsCount(){
		return m_cIndicators.size();
	}

	public int Load(String indicators, int iPos) 
	{
		for(Indicator pInd : m_cIndicators)
			pInd.Load(indicators.charAt(iPos++));
		
		return iPos;
	}

	public String Save() 
	{
		StringBuilder sRes = new StringBuilder();
		
		for(Indicator pInd : m_cIndicators)
			sRes.append(pInd.Save());
			
		return sRes.toString();
	}
	
	public void Imitate() 
	{
		for(Indicator pInd : m_cIndicators)
			pInd.Imitate();
		
		CheckDemoAlarm();
	}

	void CheckDemoAlarm()
	{
		m_bAlarmed = false;
		
		for(Indicator pInd : m_cIndicators)
			if(pInd.IsAlarmed())
			{
				m_bAlarmed = true;
				break;
			}

		if(m_pUI != null)
			m_pUI.Update();
	}

	int GetAlarmedCount()
	{
		int count = 0;
		for(Indicator pInd : m_cIndicators)
		{
			if (pInd.IsAlarmed())
			{
				count++;
			}
		}

		return count;
	}

	@NonNull
	@Override
    public String toString(){
        return m_sName;
    }
}
