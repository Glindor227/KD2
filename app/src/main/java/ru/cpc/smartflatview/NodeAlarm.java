package ru.cpc.smartflatview;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Random;

import ru.cpc.smartflatview.ui.MainActivity;

public class NodeAlarm extends Indicator
{
    private final int m_iResIDAlarmOff;
    private final int m_iResIDAlarmOn;

    NodeAlarm(float fX, float fY,
              String sName, boolean bProtected,
              boolean bDoubleSize, boolean bQuick, int iReaction, int iScale)
    {
        super(fX, fY, R.drawable.alarmnode0, 1, sName, false, bProtected, bDoubleSize, bQuick, iReaction,
              iScale);

        m_iResIDAlarmOn = R.drawable.alarmnode1;
        m_iResIDAlarmOff = R.drawable.alarmnode0;
    }

    private boolean m_bAlarm = false;

    private String m_sVariableAlarm = "-1";
    private float m_fValueAlarm = 1;
    private String m_sSubsystemID = "";

    public NodeAlarm Bind(String sAddressAlarm, String sValueAlarm, String sSubsystemID)
    {
        m_sVariableAlarm = sAddressAlarm;
        m_fValueAlarm = Float.parseFloat(sValueAlarm);

        m_sSubsystemID = sSubsystemID;

        return this;
    }

    @Override
    public void GetAddresses(AddressString sAddr)
    {
        sAddr.Add(m_sVariableAlarm, this);
    }

    @Override
    public boolean Process(String sAddr, String sVal)
    {
        boolean bAlarmOld = m_bAlarm;

        if(m_sVariableAlarm.equalsIgnoreCase(sAddr))
            m_bAlarm = Float.parseFloat(sVal) == m_fValueAlarm;

        if(m_bAlarm != bAlarmOld)
            return Update();

        return false;
    }
    @Override
    public boolean SwitchOnOff(float iX, float iY)
    {
        final MainActivity mainActivity = m_pServer.getMainActivity();

        if(mainActivity != null)
        {
            mainActivity.runOnUiThread(() -> mainActivity.SwitchToSubsystem(m_sSubsystemID));
        }

        return Update();
    }

    @Override
    public boolean SetValue(float iX, float iY)
    {
        //if(m_bGuard)
        //	m_bAlarm = true;
        return Update();
    }

    @Override
    public boolean SetValue(float iValue)
    {
        //if(m_bGuard)
        //	m_bAlarm = true;
        return Update();
    }

    @Override
    public boolean IsAlarmed(){
        return m_bAlarm;
    }

    @Override
    public boolean ShowPopup(Context context) { return false; }

    @Override
    protected boolean Update()
    {
        int iResId;

        if(m_bAlarm && m_pUI!= null)
            iResId = m_iResIDAlarmOn;
        else
            iResId = m_iResIDAlarmOff;

        if(m_pUI == null)
        {
            m_iOldResID = iResId;
            m_iNewResID = iResId;
            return false;
        }

        m_pUI.m_bLoopAnimation = m_bAlarm;
        return m_pUI.StartAnimation(iResId);
    }

    @Override
    public void Load(char code)
    {

        m_bAlarm = code == '1';
        Update();
    }

    @Override
    public String Save()
    {
        return m_bAlarm?"1":"0";
    }

    @Override
    public void Imitate()
    {
        if(!m_bMetaIndicator)
        {
            Random rnd = new Random();
            int m_iImitateAlarmChance = 15;
            if(rnd.nextInt(m_iImitateAlarmChance) == 1)
            {
                m_bAlarm = true;
                Update();
            }
        }
    }

    @Override
    public void SaveXML(XmlSerializer serializer)
    {
        try
        {
            serializer.startTag(null, "link");
            WriteCommonAttributes(serializer);
            serializer.attribute(null, "alarmaddress", m_sVariableAlarm);
            serializer.attribute(null, "alarmvalue", String.valueOf(m_fValueAlarm));
            serializer.attribute(null, "subsystem", m_sSubsystemID);
            serializer.endTag(null, "link");
        }
        catch (IllegalArgumentException | IllegalStateException | IOException e)
        {
            Log.v("Glindor",e.getMessage());
            e.printStackTrace();
        }
    }
}
