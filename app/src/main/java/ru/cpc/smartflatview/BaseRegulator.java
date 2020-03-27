package ru.cpc.smartflatview;

import android.util.Log;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public abstract class BaseRegulator extends Indicator 
{
	protected static final String TAG = "SMARTFLAT" ;
	
	protected BaseRegulator(float fX, float fY, int iResID, int iSubType,
			String sName, boolean bMetaInd, boolean bProtected,
			boolean bDoubleSize, boolean bQuick, int iReaction, int iScale)
	{
		super(fX, fY, iResID, iSubType, sName, bMetaInd, bProtected, bDoubleSize, bQuick, iReaction,
				iScale);
		// TODO Auto-generated constructor stub
	}

	public float m_iValue = 0;
	public boolean m_bPower = false;
	public int m_iMaxValue = 100;
	
	public String m_sVariableValue = "-1";
	public String m_sVariablePower = "-1";
	public float m_fValueOn = 1;
	public float m_fValueOff = 0;
	public float m_fValueMin = 16;
	public float m_fValueMax = 30;
	public float m_fValueMed = 23;

	protected boolean m_bICP = false;
	protected int m_prefix = 0;

	protected boolean m_bNoPower = false;

	public BaseRegulator Bind(String sAddressPower, String sAddressValue, String sValueOn, String sValueOff, boolean bNoPower, String sValueMin, String sValueMax, String sValueMed)
	{
		m_sVariablePower = sAddressPower;
		m_sVariableValue = sAddressValue;

		m_fValueOn = Float.parseFloat(sValueOn);
		m_fValueOff = Float.parseFloat(sValueOff);

		m_fValueMin = Float.parseFloat(sValueMin);
		m_fValueMax = Float.parseFloat(sValueMax);
		m_fValueMed = Float.parseFloat(sValueMed);

		if(m_sVariablePower.equalsIgnoreCase("-2"))
		{
			m_iMaxValue = 127;
			m_bICP = true;
		}
		if(m_iSubType!=3){
			m_prefix = 3584;
		}

		m_bNoPower = bNoPower;

		Log.d(TAG, "222 m_iMaxValue = " + m_iMaxValue);

		return this;
	}

	@Override
	public void GetAddresses(AddressString sAddr)
	{
		sAddr.Add(m_sVariablePower, this);
		sAddr.Add(m_sVariableValue, this);
	}

	@Override
	public boolean Process(String sAddr, String sVal)
	{
		Log.d("Regul", "BaseRegulator.Process1("+m_sVariableValue+") m_iValue = "+m_iValue);

		boolean bPowerOld = m_bPower;
		float iValueOld = m_iValue;

		if(m_sVariablePower.equalsIgnoreCase(sAddr))
			m_bPower = Float.parseFloat(sVal) == m_fValueOn;
		
		if(m_sVariableValue.equalsIgnoreCase(sAddr))
		{
			Log.d("Regul", "BaseRegulator.Process2 m_iValue = "+m_iValue);
			float iValue = Float.parseFloat(sVal);
			if(m_bICP)
			{
				if(iValue == m_prefix)
					m_bPower = false;
				else
				{
//					if(iValue==3711)
//						m_iValue = 0;
//					else
						m_iValue = iValue-m_prefix;
					m_bPower = true;
				}
			}
			else
				m_iValue = iValue;

		}
		Log.d("Regul", "BaseRegulator.Process3 m_iValue = "+m_iValue);

		if(m_bPower != bPowerOld || m_iValue != iValueOld)
		    return Update();

		return false;
	}	
	
	@Override
	public boolean SwitchOnOff(float iX, float iY)
	{
		if(m_bNoPower)
			return false;
		Log.d("Glin512", "Base Regulator SwitchOnOff m_bPower = " + !m_bPower );
		Log.d("2click", "SwitchOnOff("+m_sName+")");

		m_bPower = !m_bPower;

		if(m_bICP)
		{
			if(m_bPower)
			{
				if(m_iValue == 0)
					m_pServer.SendCommand(m_sVariableValue, String.valueOf(m_prefix+127));
				else
					m_pServer.SendCommand(m_sVariableValue, String.valueOf(m_prefix + m_iValue));
			}
			else
				m_pServer.SendCommand(m_sVariableValue,  String.valueOf(m_prefix));
		}
		else
			m_pServer.SendCommand(m_sVariablePower, String.valueOf(m_bPower ? m_fValueOn : m_fValueOff));
		
		return Update();
	}

	@Override
	public boolean SetValue(float iValue)
	{
		m_iValue = iValue;
		//29.08.2019
//		m_bPower = true;
		Log.d("NodeSeekBar", "Base Regulator("+m_sVariableValue+") SetValue bright = " + m_iValue );

		if(m_bICP)
			m_pServer.SendCommand(m_sVariableValue, String.valueOf(m_prefix + m_iValue));
		else
		{
			//29.08.2019
//			m_pServer.SendCommand(m_sVariablePower, String.valueOf(m_bPower ? m_fValueOn : m_fValueOff));
            Log.d("Regul", "SetValue "+m_iValue);

            m_pServer.SendCommand(m_sVariableValue, String.valueOf(m_iValue));
		}
		
		return Update();
	}	

	@Override
	public void Load(char code) 
	{
		m_bPower = code < 128;
		m_iValue = m_bPower ? code : code - 128;
		Update();
	}

	@Override
	public String Save() 
	{
		return m_bPower ? String.valueOf((char)m_iValue) : String.valueOf((char)(128 + m_iValue));
	}
	
	@Override
	public void SaveXML(XmlSerializer serializer) 
	{
        try 
        {
			serializer.startTag(null, "regulator");
			WriteCommonAttributes(serializer);
            serializer.attribute(null, "poweraddress", m_sVariablePower);
            serializer.attribute(null, "valueaddress", m_sVariableValue);
			serializer.attribute(null, "onvalue", String.valueOf(m_fValueOn));
			serializer.attribute(null, "offvalue", String.valueOf(m_fValueOff));
			serializer.attribute(null, "nopower", m_bNoPower ? "1":"0");
			serializer.attribute(null, "minvalue", String.valueOf(m_fValueMin));
			serializer.attribute(null, "maxvalue", String.valueOf(m_fValueMax));
			serializer.attribute(null, "mediumvalue", String.valueOf(m_fValueMed));
	        serializer.endTag(null, "regulator");
		} 
        catch (IllegalArgumentException e) 
        {
			Log.v("Glindor",e.getMessage());
			e.printStackTrace();
		} 
        catch (IllegalStateException e) 
        {
			Log.v("Glindor",e.getMessage());
			e.printStackTrace();
		} 
        catch (IOException e) 
        {
			Log.v("Glindor",e.getMessage());
			e.printStackTrace();
		}
    }
}
