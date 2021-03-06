package ru.cpc.smartflatview.IndicatorPackage.Regulators;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseRegulator;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.ScrollingDialog;

/**
 * Created by Вик on 028. 28. 12. 16.
 */

public class Battery2 extends BaseRegulator
{
    public Battery2(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY, getIndDis(R.drawable.radiator_hot_p,R.drawable.radiator_off_2,R.drawable.radiator_off), 6, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

        m_iValue = 20;
    }

    @Override
    public boolean SetValue(float iX, float iY)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean Update()
    {
        int iResId = -1;

        if(m_bPower)
        {
            iResId = getIndDis(R.drawable.radiator_cool_p,R.drawable.radiator_cool_2,R.drawable.radiator_cold);

            if(m_iValue > m_fValueMed)
                iResId = getIndDis(R.drawable.radiator_hot_p,R.drawable.radiator_hot_2,R.drawable.radiator_hot);
        }
        else
        {
            iResId = getIndDis(R.drawable.radiator_hot_p,R.drawable.radiator_off_2,R.drawable.radiator_off);

//            if(m_iValue > m_fValueMed)
//                iResId = R.drawable.id252;
        }

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
        final ScrollingDialog.SFSwitcher pSwitcher = m_bNoPower ? null : (ScrollingDialog.SFSwitcher) ScrollingDialog.AddSwitcher(m_sVariablePower, "Включено", m_bPower, m_iReaction != 0
                                                                                                                                                                          ? null : new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SwitchOnOff(0, 0);
            }
        });
        final ScrollingDialog.SFSeeker pSeeker = (ScrollingDialog.SFSeeker)ScrollingDialog.AddSeekBar(m_sVariableValue, "Температура", (int)m_iValue, (int) m_fValueMin, (int) m_fValueMax, "%d °C", m_iReaction != 0
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
                SetValue(seekBar.getProgress() + (int) m_fValueMin);
            }
        });

        //Intent myIntent = new Intent(context, ScrollingActivity.class);
        //context.startActivity(myIntent);

        if(m_iReaction == 1)
            ScrollingDialog.AddAcceptBtn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(m_iValue != pSeeker.m_iValue)
                        SetValue(pSeeker.m_iValue);

                    if(pSwitcher != null && m_bPower != pSwitcher.m_bChecked)
                        SwitchOnOff(0, 0);
                }
            });

        ScrollingDialog dlg = new ScrollingDialog();
        dlg.show(((Activity) context).getFragmentManager(), "dlg");


//		v.show();
        return false;
    }
}
