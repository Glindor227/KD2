package ru.cpc.smartflatview.IndicatorPackage.Regulators;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import ru.cpc.smartflatview.BaseRegulator;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.ScrollingDialog;

public class Conditioner extends BaseRegulator
{
	private static int imageCoolOn = getIndDisC(R.drawable.cond_cool_off_p,R.drawable.cond_cool_off,R.drawable.ccond2on,R.drawable.cond_cool_on_c);
	private static int imageCoolOff=  getIndDisC(R.drawable.cond_cool_on_p,R.drawable.cond_cool_on,R.drawable.ccond2off,R.drawable.cond_cool_off_c);
	private static int imageHotOn= getIndDisC(R.drawable.cond_hot_off_p,R.drawable.cond_hot_off,R.drawable.ccond3on,R.drawable.cond_hot_on_c);
	private static int imageHotOff= getIndDisC(R.drawable.cond_hot_on_p,R.drawable.cond_hot_on,R.drawable.ccond3off,R.drawable.cond_hot_off_c);

	public Conditioner(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY, imageCoolOff, 3, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
		
		m_iValue = 20;
	}

	public Conditioner setDemo(){
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
		
		if(m_bPower)
		{
			iResId = imageCoolOn;
			
			if(m_iValue > m_fValueMed)
				iResId = imageHotOn;
		}
		else
		{
			iResId = imageCoolOff;

			if(m_iValue > m_fValueMed)
				iResId = imageHotOff;
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
		Log.d("Glindor13","Conditioner ShowPopup m_sVariableValue="+m_sVariableValue);

		ScrollingDialog.Init(m_sName, m_pSubsystem.m_sName);
		final ScrollingDialog.SFSwitcher pSwitcher = m_bNoPower ? null : (ScrollingDialog.SFSwitcher) ScrollingDialog.AddSwitcher(m_sVariablePower, context.getString(R.string.sdPower), m_bPower, m_iReaction != 0
                                                                                                                                                                                                   ? null : new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SwitchOnOff(0, 0);
			}
		});
		Log.d("Regul", "Conditioner.ShowPopup m_iValue = "+m_iValue);

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
					if(m_iValue != pSeeker.m_iValue)
						SetValue((float)(pSeeker.m_iValue)/ScrollingDialog.factorSet);

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
