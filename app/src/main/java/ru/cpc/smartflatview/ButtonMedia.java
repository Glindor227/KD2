package ru.cpc.smartflatview;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;


public class ButtonMedia extends Indicator
{
    static final int EJECT = 1;
    static final int PLAY = 2;
    static final int STOP = 3;
    static final int PAUSE = 4;
    private static final int REC = 5;
    static final int FF = 6;
    static final int REW = 7;
    static final int SKIP_F = 8;
    static final int SKIP_B = 9;
    static final int MUTE = 10;
    static final int POWER = 11;

    private int m_iType;
    private boolean m_bPressed = false;

    ButtonMedia(float fX,
                float fY, int iType, String sName,
                boolean bDoubleSize, int iReaction, int iScale)
    {
        super(fX, fY, GetResId(iType, false), iType, "", false, false, bDoubleSize, true, iReaction, iScale);


        m_iType = iType;
    }

    public String m_sVariable = "-1";
    private String m_sStateVariable = "-1";

    public ButtonMedia Bind(String sAddress, String sAddress2)
    {
        m_sVariable = sAddress;
        m_sStateVariable = sAddress2;

        return this;
    }

    @Override
    public boolean SwitchOnOff(float iX, float iY) {
        if(m_sVariable.equals("-1")) {
            m_bPressed = !m_bPressed;
            Update();
        }
        return false;
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
    public void Pressed(float iX, float iY)
    {
        if(m_sVariable.equals("-1")) {
            m_bPressed = true;
            Update();
        }
    }

    @Override
    public void Released()
    {
        super.Released();
        if(m_sVariable.equals("-1")) {
            m_bPressed = false;
            Update();
        }
    }

    private static int GetResId(int iType, boolean bPressed)
    {
        int iResId = R.drawable.empty;

        switch (iType)
        {
            case EJECT:
                if(bPressed)
                    iResId = getIndDis(R.drawable.eject_p,R.drawable.eject_2,R.drawable.eject);
                else
                    iResId = getIndDis(R.drawable.eject_p,R.drawable.eject_2,R.drawable.eject);
                break;
            case PLAY:
                if(bPressed)
                    iResId = getIndDis(R.drawable.play_p,R.drawable.play_2,R.drawable.play);
                else
                    iResId = getIndDis(R.drawable.pause_p,R.drawable.pause_2,R.drawable.pause);
                break;
            case PAUSE:
                if(bPressed)
                    iResId = getIndDis(R.drawable.pause_p,R.drawable.pause_2,R.drawable.pause);
                else
                    iResId = getIndDis(R.drawable.play_p,R.drawable.play_2,R.drawable.play);
                break;
            case STOP:
                if(bPressed)
                    iResId =getIndDis(R.drawable.stop_p,R.drawable.stop_2,R.drawable.stop);
                else
                    iResId = getIndDis(R.drawable.stop_p,R.drawable.stop_2,R.drawable.stop);
                break;
            case REC:
                if(bPressed)
                    iResId = R.drawable.rec_pressed;
                else
                    iResId = R.drawable.rec;
                break;
            case FF:
                if(bPressed)
                    iResId = getIndDis(R.drawable.forward_p,R.drawable.forward_2,R.drawable.forward_pressed);
                else
                    iResId = getIndDis(R.drawable.forward_p,R.drawable.forward_2,R.drawable.forward);
                break;
            case REW:
                if(bPressed)
                    iResId = getIndDis(R.drawable.rewind_pressed_p,R.drawable.rewind_pressed_2,R.drawable.rewind_pressed);
                else
                    iResId = getIndDis(R.drawable.rewind_p,R.drawable.rewind_2,R.drawable.rewind);
                break;
            case SKIP_F:
                if(bPressed)
                    iResId = getIndDis(R.drawable.skip_frwd_pressed_p,R.drawable.skip_frwd_pressed_2,R.drawable.skip_frwd_pressed);
                else
                    iResId = getIndDis(R.drawable.skip_frwd_p,R.drawable.skip_frwd_2,R.drawable.skip_frwd);
                break;
            case SKIP_B:
                if(bPressed)
                    iResId = getIndDis(R.drawable.skip_back_pressed_p,R.drawable.skip_back_pressed_2,R.drawable.skip_back_pressed);
                else
                    iResId = getIndDis(R.drawable.skip_back_p,R.drawable.skip_back_2,R.drawable.skip_back);
                break;
            case MUTE:
                if(bPressed)
                    iResId =getIndDis(R.drawable.mute_pressed_p,R.drawable.mute_pressed_2, R.drawable.mute_pressed);
                else
                    iResId = getIndDis(R.drawable.mute_p,R.drawable.mute_2,R.drawable.mute);
                break;
            case POWER:
                if(bPressed)
                    iResId = getIndDis(R.drawable.power_p,R.drawable.power_2,R.drawable.power);
                else
                    iResId = getIndDis(R.drawable.power_p,R.drawable.power_2,R.drawable.power);
                break;
        }

        return iResId;
    }

    @Override
    protected boolean Update()
    {
        int iResId = GetResId(m_iType, m_bPressed);

//		m_bLoopAnimation = m_bPressed;

        if(m_pUI == null)
        {
            if(iResId != -1)
            {
                m_iOldResID = iResId;
                m_iNewResID = iResId;
            }
            return false;
        }

        return m_pUI.StartAnimation(iResId);
    }

    @Override
    public void Load(char code) {
        m_bPressed = code == '1';
    }

    @Override
    public String Save() {
        if(m_bPressed)
            return "1";
        else
            return "0";
    }

    @Override
    public void SaveXML(XmlSerializer serializer) {
        try
        {
            serializer.startTag(null, "mediabutton");
            WriteCommonAttributes(serializer);
            serializer.attribute(null, "valueaddress", m_sVariable);
            serializer.attribute(null, "stateaddress", m_sStateVariable);
            serializer.endTag(null, "mediabutton");
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
    public void GetAddresses(AddressString sAddr) {


    }

    @Override
    public boolean Process(String sAddr, String sVal) {

        return false;
    }

    @Override
    public boolean ShowPopup(Context context) {
        return false;
    }

}

