package ru.cpc.smartflatview;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * Created by Вик on 022. 22.03.16.
 */
public class PlusMinus extends Indicator
{
    private int m_iPressed = 0;
    private int m_iType;

    private static int plus_minus_v = newDez?(posDez?R.drawable.plus_minus_v_p:R.drawable.plus_minus_v_2):R.drawable.plus_minus;
    private static int plus_minus_v_p = newDez?(posDez?R.drawable.plus_minus_v_p_p:R.drawable.plus_minus_v_p_2):R.drawable.plus_minus_plus;
    private static int plus_minus_v_m = newDez?(posDez?R.drawable.plus_minus_v_m_p:R.drawable.plus_minus_v_m_2):R.drawable.plus_minus_minus;

    private static int plus_minus_ch = newDez?(posDez?R.drawable.plus_minus_ch_p:R.drawable.plus_minus_ch_2):R.drawable.plus_minus2;
    private static int plus_minus_ch_p = newDez?(posDez?R.drawable.plus_minus_ch_p_p:R.drawable.plus_minus_ch_p_2):R.drawable.plus_minus2_plus;
    private static int plus_minus_ch_m = newDez?(posDez?R.drawable.plus_minus_ch_m_p:R.drawable.plus_minus_ch_m_2):R.drawable.plus_minus2_minus;

    PlusMinus(float fX,
              float fY, int iType, String sName, boolean bMetaInd, boolean bProtected,
              boolean bDoubleSize, boolean bQuick, int iReaction, int iScale)
    {
        super(fX, fY, iType == 0 ?plus_minus_v : plus_minus_ch, 1, sName, bMetaInd, bProtected, bDoubleSize, bQuick, iReaction, iScale);
        m_iType = iType;
        // TODO Auto-generated constructor stub
    }

    private String m_sVariableUp = "-1";
    private String m_sVariableDown = "-1";

    public PlusMinus Bind(String sAddressUp, String sAddressDown)
    {
        m_sVariableUp = sAddressUp;
        m_sVariableDown = sAddressDown;

        return this;
    }

    @Override
    public void BindUI(SFServer pServer, Context context, IndicatorUI pUI)
    {
        super.BindUI(pServer, context, pUI);

        m_pUI.m_pFadeInAnimation.setDuration(500);
        m_pUI.m_pFadeOutAnimation.setDuration(500);
    }

    @Override
    public boolean SwitchOnOff(float iX, float iY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean SetValue(float iX, float iY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean SetValue(int iValue)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void SaveXML(XmlSerializer serializer) {
        try
        {
            serializer.startTag(null, "swing");
            WriteCommonAttributes(serializer);
            serializer.attribute(null, "upaddress", m_sVariableUp);
            serializer.attribute(null, "downaddress", m_sVariableDown);
            serializer.endTag(null, "swing");
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
    public void Pressed(float iX, float iY)
    {
        if(iY < m_pUI.m_iHeight/2)
            m_iPressed = 1;
        else
            m_iPressed = -1;
        Update();
    }

    @Override
    public void Released()
    {
        super.Released();
        m_iPressed = 0;
        Update();
    }

    @Override
    protected boolean Update()
    {
        int iResId = m_iType == 0 ? plus_minus_v : plus_minus_ch;

        if(m_iPressed > 0)
            iResId = m_iType == 0 ? plus_minus_v_p : plus_minus_ch_p;

        if(m_iPressed < 0)
            iResId = m_iType == 0 ? plus_minus_v_m : plus_minus_ch_m;

        if(m_pUI == null)
        {
            if(iResId != -1)
            {
                m_iOldResID = iResId;
                m_iNewResID = iResId;
            }
            return false;
        }

        m_pUI.m_bLoopAnimation = m_iPressed != 0;

        return m_pUI.StartAnimation(iResId);
    }

    @Override
    public void Load(char code) {
        // TODO Auto-generated method stub

    }

    @Override
    public String Save() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void GetAddresses(AddressString sAddr) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean Process(String sAddr, String sVal) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean ShowPopup(Context context) {
        return false;
    }

}
