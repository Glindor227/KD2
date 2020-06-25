package ru.cpc.smartflatview.indicatorPackage.base;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import ru.cpc.smartflatview.AddressString;
import ru.cpc.smartflatview.Indicator;
import ru.cpc.smartflatview.IndicatorUI;
import ru.cpc.smartflatview.SFServer;

public abstract class BaseRegulator extends Indicator
{
	protected static final String TAG = "SMARTFLAT" ;
	
	protected BaseRegulator(float fX, float fY, int iResID, int iSubType,
			String sName, boolean bMetaInd, boolean bProtected,
			boolean bDoubleSize, boolean bQuick, int iReaction, int iScale)
	{
		super(fX, fY, iResID, iSubType, sName, bMetaInd, bProtected, bDoubleSize, bQuick, iReaction,
				iScale);
		Log.d("Regul222", "конструктор (name:"+sName+")");


	}
	protected String m_sTemp2Text = "23.7";
	protected String m_sTemp3Text = "23.9";
	protected String m_sTemp4Text = "N/A";
	protected boolean init = true;

	protected boolean m_iState = true; // текущее значение состояния
	protected float m_iTemp = 0; // текущее значение комнатной температуры
	public float m_iValue = 0; // текущее значение целевой температуры
	public boolean m_bPower = false; //текущее значение вкл/выкл
	public int m_iMaxValue = 100;
	
	public String m_sVariableValue = "-1";
	public String m_sVariablePower = "-1";
	private String m_sVariableTemp = "-1";
	private String m_sVariableState = "-1";
	protected float m_fValueOn = 1;
	protected float m_fValueOff = 0;
	public float m_fValueMin = 16;
	public float m_fValueMax = 30;
	protected float m_fValueMed = 23;

	protected boolean m_bICP = false;
	private int m_prefix = 0;

	protected boolean m_bNoPower = false;

	public BaseRegulator Bind(String sAddressPower, String sAddressValue, String sValueOn, String sValueOff, boolean bNoPower, String sValueMin, String sValueMax, String sValueMed)
	{
		Log.d("Regul222", "Bind(name:"+ m_sName+")");

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
	public BaseRegulator BindAddParam(String sAddressTemp,String sAddressSate){

		m_bText2 = true;
		m_bText3 = true;
		m_sVariableTemp = sAddressTemp;

		if(sAddressSate!=null) {
			m_bText4 = true;
			m_sVariableState = sAddressSate;
		}

		return this;
	}
	@Override
	public void BindUI(SFServer pServer, Context context, IndicatorUI pUI)
	{
		Log.d("Regul222", "BindUI(sys:"+m_pSubsystem.m_sName+" name:"+ m_sName+")");


		super.BindUI(pServer, context, pUI);

//		m_pUI.m_pText2.setTextColor(context.getResources().getColor(android.R.color.primary_text_dark));//, context.getTheme()));
		if(m_bText2) {
			//m_pUI.m_pText2.setTextColor(context.getResources().getColor(R.color.ind_text1));//, context.getTheme()));
			m_pUI.m_pText2.setTypeface(m_pUI.m_pText2.getTypeface(), Typeface.BOLD);
			m_pUI.m_pText2.setText(m_sTemp2Text);
		}
		if(m_bText3) {
//			m_pUI.m_pText3.setTextColor(context.getResources().getColor(R.color.ind_text1));//, context.getTheme()));
			m_pUI.m_pText3.setTypeface(m_pUI.m_pText3.getTypeface(), Typeface.BOLD);
			m_pUI.m_pText3.setText(m_sTemp3Text);
		}
		if(m_bText4) {
//			m_pUI.m_pText4.setTextColor(context.getResources().getColor(R.color.ind_text1));//, context.getTheme()));
			m_pUI.m_pText4.setText(m_sTemp4Text);
		}
	}

	@Override
	public void FixLayout(int l, int t, int r, int b)
	{
		Log.d("Regul222", "FixLayout(sys:"+m_pSubsystem.m_sName+" name:"+ m_sName+") delta =" +Indicator.delta);

		int iWidth = r-l;
		int iHeight = b-t;

		int iText;
		if(m_bDoubleScale)
		{
			iText = iHeight/4;
			if(m_bText3) {
				m_pUI.m_pText3.layout(0, -(int) (iText * 0.2), iWidth, iText);//верхняя температура
				m_pUI.m_pText3.setTextSize(TypedValue.COMPLEX_UNIT_PX, iText);//верхняя температура
			}

			if(m_bText2) {
				int lH = (int) (iHeight * 0.80* Indicator.delta) + (m_iScale-2)*5;
				m_pUI.m_pText2.layout(0, lH - (int) (iText * 1.1), iWidth, lH);//нижняя температура
				m_pUI.m_pText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, iText * 0.9f);//нижняя температура
			}

			if(m_bText4) {
				m_pUI.m_pText4.layout((int)(iWidth*0.55f*Indicator.delta - (5-m_iScale)*30) , iHeight/2-iText, iWidth, iHeight/2 + iText);//справа состояние реле
				m_pUI.m_pText4.setTextSize(TypedValue.COMPLEX_UNIT_PX, iText);
			}
		}
		else
		{

			iText = iHeight/6;
			if(m_bText3) {
				m_pUI.m_pText3.layout(0, -(int) (iText * 0.17), iWidth, iText);//верхняя температура
				m_pUI.m_pText3.setTextSize(TypedValue.COMPLEX_UNIT_PX, iText);//верхняя температура
			}

			if(m_bText2) {
				int lH = (int) (iHeight * 0.61 * Indicator.delta) + (m_iScale-2)*5;
				m_pUI.m_pText2.layout(0, lH - (int) (iText * 1.1), iWidth, lH);//нижняя температура
				m_pUI.m_pText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, iText * 0.9f);//нижняя температура
			}

			if(m_bText4) {
				float k1 = 30;
				int k2 = 7;
				m_pUI.m_pText4.layout(
						(int)(( (Indicator.delta-1)*4 +1 )*(iWidth*1.05f)/2-(5-m_iScale)*k1),
						(int) (iHeight*0.45f-iText*1.125f)-(5-m_iScale)*k2,
						iWidth,
						(int)(iHeight*0.45f + iText));
				m_pUI.m_pText4.setTextSize(TypedValue.COMPLEX_UNIT_PX, iText);
			}

		}
	}


	@Override
	public void GetAddresses(AddressString sAddr)
	{
		sAddr.Add(m_sVariablePower, this);
		sAddr.Add(m_sVariableValue, this);
		if(!m_sVariableTemp.isEmpty())
			sAddr.Add(m_sVariableTemp, this);
		if(!m_sVariableState.isEmpty())
			sAddr.Add(m_sVariableState, this);

	}

	@Override
	public boolean Process(String sAddr, String sVal)
	{
		Log.d("Regul222", "BaseRegulator(sys:"+m_pSubsystem.m_sName+" name:"+ m_sName+").Process1("+sAddr+") m_iValue = "+sVal);

		boolean bPowerOld = m_bPower;
		float iValueOld = m_iValue;
		float iTempOld = m_iTemp;
		boolean iStateOld = m_iState;

		if(m_sVariableState.equalsIgnoreCase(sAddr))
			m_iState = Float.parseFloat(sVal) == 1;
		if(m_sVariablePower.equalsIgnoreCase(sAddr))
			m_bPower = Float.parseFloat(sVal) == m_fValueOn;
		if(m_sVariableTemp.equalsIgnoreCase(sAddr)) {
			Log.d("Regul", "BaseRegulator.Process2- m_iTemp = " + m_iTemp);
			m_iTemp = Float.parseFloat(sVal);
			Log.d("Regul", "BaseRegulator.Process2+ m_iTemp = " + m_iTemp);
		}
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

		if(init || m_bPower != bPowerOld || m_iValue != iValueOld || iTempOld != m_iTemp || iStateOld != m_iState){
			Log.d("Regul222", "BaseRegulator OK (sys:"+m_pSubsystem.m_sName+" name:"+ m_sName+")");

//			if(m_iSubType==9)
//				m_pUI.m_pText2.setText(String.valueOf(m_iValue));
			init = false;
		    return Update();
		}
		else {
			Log.d("Regul222", "BaseRegulator NO (sys:"+m_pSubsystem.m_sName+" name:"+ m_sName+")");

		}

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
//		if(m_iSubType==9)
//			m_pUI.m_pText2.setText(String.valueOf(iValue));

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
			serializer.attribute(null, "tempaddress", m_sVariableTemp);
			serializer.attribute(null, "stateaddress", m_sVariableState);
			serializer.attribute(null, "onvalue", String.valueOf(m_fValueOn));
			serializer.attribute(null, "offvalue", String.valueOf(m_fValueOff));
			serializer.attribute(null, "nopower", m_bNoPower ? "1":"0");
			serializer.attribute(null, "minvalue", String.valueOf(m_fValueMin));
			serializer.attribute(null, "maxvalue", String.valueOf(m_fValueMax));
			serializer.attribute(null, "mediumvalue", String.valueOf(m_fValueMed));
	        serializer.endTag(null, "regulator");
		} 
        catch (IllegalArgumentException | IllegalStateException | IOException e)
        {
			Log.v("Glindor",e.getMessage());
			e.printStackTrace();
		}
	}
}
