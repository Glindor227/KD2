package ru.cpc.smartflatview.IndicatorPackage.Regulators;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseRegulator;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.ScrollingDialog;


public class Conditioner2Temp extends BaseRegulator
{
    private static int condCoolOn = getIndDisC(R.drawable.cond_cool_off_p,R.drawable.cond_cool_off,R.drawable.ccond2on,R.drawable.cond_cool_on_c);//9
    private static int condCoolOff =  getIndDisC(R.drawable.cond_cool_on_p,R.drawable.cond_cool_on,R.drawable.ccond2off,R.drawable.cond_cool_off_c);//9
    private static int condHotOn = getIndDisC(R.drawable.cond_hot_off_p,R.drawable.cond_hot_off,R.drawable.ccond3on,R.drawable.cond_hot_on_c);//9
    private static int condHotOff = getIndDisC(R.drawable.cond_hot_on_p,R.drawable.cond_hot_on,R.drawable.ccond3off,R.drawable.cond_hot_off_c);//9
    private static int radiatorCool = getIndDis(R.drawable.radiator_cool_p,R.drawable.radiator_cool_2,R.drawable.radiator_cold);//11
    private static int radiatorHot = getIndDis(R.drawable.radiator_hot_p,R.drawable.radiator_hot_2,R.drawable.radiator_hot);//11
    private static int floorOn = getIndDisC(R.drawable.pol_on_p,R.drawable.pol_on,R.drawable.id087,R.drawable.pol_on_c);//10
    private static int floorOff = getIndDisC(R.drawable.pol_off_p,R.drawable.pol_off,R.drawable.id088,R.drawable.pol_off_c);//10

    public Conditioner2Temp(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale, int iType)
    {
        super(iX, iY, condCoolOff, 9, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
        m_iValue = 20;
        m_iType = iType;
    }
    private int m_iType;
    public Conditioner2Temp setDemo(){
        Bind("1", "1", "1", "0", false, "16", "32", "23");
        return this;
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
        if(m_pUI!=null) {
            if(m_bText2)
                m_pUI.m_pText2.setText(String.valueOf(m_iValue));
            if(m_bText3)
                m_pUI.m_pText3.setText(String.valueOf(m_iTemp));
        }
        if(m_iType==11) {
            if(m_iValue > m_fValueMed)
                iResId = radiatorHot;
            else
                iResId = radiatorCool;
        }
        else {
            if (m_bPower) {
                if (m_iType == 9) {
                    iResId = condCoolOn;
                    if (m_iValue > m_fValueMed)
                        iResId = condHotOn;
                }
                if (m_iType == 10) iResId = floorOn;

            } else {
                if (m_iType == 9){
                    iResId = condCoolOff;
                    if (m_iValue > m_fValueMed)
                        iResId = condHotOff;
                }
                if (m_iType == 10) iResId = floorOff;
            }
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
        Log.d("Glindor13","Conditioner2Temp ShowPopup m_sVariableValue="+m_sVariableValue);

        ScrollingDialog.Init(m_sName, m_pSubsystem.m_sName);
        final ScrollingDialog.SFSwitcher pSwitcher = m_bNoPower ? null : (ScrollingDialog.SFSwitcher) ScrollingDialog.AddSwitcher(m_sVariablePower, context.getString(R.string.sdPower), m_bPower, m_iReaction != 0
                ? null : new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SwitchOnOff(0, 0);
            }
        });
        Log.d("Regul", "Conditioner2Temp.ShowPopup m_iValue = "+m_iValue);

        final ScrollingDialog.SFSeeker pSeeker =
                (ScrollingDialog.SFSeeker)ScrollingDialog.
                        AddSeekBar(m_sVariableValue,
                                context.getString(R.string.sdTemperature),
                                (int)(m_iValue*ScrollingDialog.factorSet),
                                (int) m_fValueMin*ScrollingDialog.factorSet,
                                (int) m_fValueMax*ScrollingDialog.factorSet,
                                "%.1f °C"/*"%d °C"*/,
                                m_iReaction != 0 ? null : new SeekBar.OnSeekBarChangeListener() {
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
                                        int progress = seekBar.getProgress();
                                        Log.d("Regul", "onStopTrackingTouch "+progress);

                                        SetValue(((float)(progress + (int) m_fValueMin*ScrollingDialog.factorSet))/ScrollingDialog.factorSet );
                                    }
                                });

        //Intent myIntent = new Intent(context, ScrollingActivity.class);
        //context.startActivity(myIntent);

        if(m_iReaction == 1)
            ScrollingDialog.AddAcceptBtn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(m_iValue != pSeeker.m_iValue) {
                        float value = (float) (pSeeker.m_iValue) / ScrollingDialog.factorSet;
                        SetValue(value);
                        m_pUI.m_pText2.setText(String.valueOf(value));
                    }

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
