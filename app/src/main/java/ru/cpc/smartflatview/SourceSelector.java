package ru.cpc.smartflatview;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;


public class SourceSelector extends Indicator
{
    private int m_iSource = 0;

    SourceSelector(float fX,
                   float fY, String sName,
                   boolean bDoubleSize, int iReaction, int iScale)
    {
        super(fX, fY,
                getIndDis(R.drawable.src_tv_p,R.drawable.src_tv_2,R.drawable.src_tv),
                1, sName, false, false, bDoubleSize, true, iReaction, iScale);

    }

    public String m_sVariable = "-1";

    public SourceSelector Bind(String sAddress)
    {
        m_sVariable = sAddress;

        return this;
    }

    @Override
    public boolean SwitchOnOff(float iX, float iY)
    {
        Log.d("111111111111111", "Clicked x=" + iX + "  y=" + iY);
        if(iX < m_pUI.m_iWidth/2)
        {
            if(iY < m_pUI.m_iHeight/2)
                m_iSource = 1;
            else
                m_iSource = 3;
        }
        else
        {
            if(iY < m_pUI.m_iHeight/2)
                m_iSource = 2;
            else
                m_iSource = 4;
        }

        return Update();
    }

    @Override
    public boolean SetValue(float iX, float iY) {
        return false;
    }

    @Override
    public boolean SetValue(float iValue) {
        return false;
    }

    @Override
    protected boolean Update()
    {
        int iResId = getIndDis(R.drawable.src_off_p,R.drawable.src_off_2,R.drawable.src_off);

        switch (m_iSource)
        {
            case 1:
                iResId = getIndDis(R.drawable.src_tv_p,R.drawable.src_tv_2,R.drawable.src_tv);
                break;
            case 2:
                iResId = getIndDis(R.drawable.src_sat_p,R.drawable.src_sat_2,R.drawable.src_sat);
                break;
            case 3:
                iResId = getIndDis(R.drawable.src_dvd_p,R.drawable.src_dvd_2,R.drawable.src_dvd);
                break;
            case 4:
                iResId =getIndDis(R.drawable.src_srv_p,R.drawable.src_srv_2,R.drawable.src_srv);
                break;
        }

        return m_pUI.StartAnimation(iResId);
    }

    @Override
    public void Load(char code) {
        m_iSource = code;

    }

    @Override
    public String Save() {
        return String.valueOf((char)m_iSource);
    }

    @Override
    public void SaveXML(XmlSerializer serializer) {
        try
        {
            serializer.startTag(null, "selector");
            WriteCommonAttributes(serializer);
            serializer.attribute(null, "sourceaddress", m_sVariable);
            serializer.endTag(null, "selector");
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

    @Override
    public void GetAddresses(AddressString sAddr) {   }

    @Override
    public boolean Process(String sAddr, String sVal) {return false;}

    @Override
    public boolean ShowPopup(Context context) {return false;}

}
