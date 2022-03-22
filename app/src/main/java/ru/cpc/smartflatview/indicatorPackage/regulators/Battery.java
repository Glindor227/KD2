package ru.cpc.smartflatview.indicatorPackage.regulators;

import android.app.Activity;
import android.content.Context;
import android.widget.SeekBar;

import ru.cpc.smartflatview.indicatorPackage.base.BaseRegulator;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.ui.ScrollingDialog;


public class Battery extends BaseRegulator
{
    public Battery(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY, getIndDis(R.drawable.radiator_cool_p,R.drawable.radiator_cool_2,R.drawable.radiator_cold), 5, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

        m_iValue = 20;
        m_bPower = true;
    }

    @Override
    public boolean SwitchOnOff(float iX, float iY)
    {
        m_bPower = true;

        if(m_bICP)
        {
            if(m_iValue == 0)
                m_pServer.SendCommand(m_sVariableValue, "3711");
            else
                m_pServer.SendCommand(m_sVariableValue, String.valueOf(3584 + m_iValue));

//                m_pServer.SendCommand(m_sVariableValue, "3584"); ICP off
        }
        else
            m_pServer.SendCommand(m_sVariablePower, String.valueOf(m_fValueOn));

        return Update();
    }

    @Override
    public boolean SetValue(float iX, float iY)
    {

        return false;
    }

    @Override
    protected boolean Update()
    {
        int iResId;

        if(m_iValue > m_fValueMed)
            iResId = getIndDisC(R.drawable.radiator_hot_p,R.drawable.radiator_hot_2,R.drawable.radiator_hot,R.drawable.radiator_hot_c);
        else
            iResId = getIndDisC(R.drawable.radiator_cool_p,R.drawable.radiator_cool_2,R.drawable.radiator_cold,R.drawable.radiator_cool_c);

        if(m_pUI == null)
        {
            m_iOldResID = iResId;
            m_iNewResID = iResId;
            return false;
        }

        return m_pUI.StartAnimation(iResId);
    }

    @Override
    public boolean ShowPopup(Context context)
    {
        ScrollingDialog.Init(m_sName, m_pSubsystem.m_sName);
        final ScrollingDialog.SFSeeker pSeeker = (ScrollingDialog.SFSeeker)ScrollingDialog.AddSeekBar(m_sVariableValue, context.getString(
                        R.string.sdTemperature), (int)m_iValue, (int)m_fValueMin, (int)m_fValueMax, "%d °C", m_iReaction != 0
                                                                                                        ? null : new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //if (fromUser) {
                //    SetValue(16 + progress);
                //}
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SetValue(seekBar.getProgress() + (int)m_fValueMin);
            }
        });

        if(m_iReaction == 1)
            ScrollingDialog.AddAcceptBtn(v -> {
                if(m_iValue != pSeeker.m_iValue)
                    SetValue(pSeeker.m_iValue);
            });

        ScrollingDialog dlg = new ScrollingDialog();
        dlg.show(((Activity) context).getFragmentManager(), "dlg");


//		v.show();
        return false;
    }
}

