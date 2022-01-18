package ru.cpc.smartflatview.indicatorPackage.regulators;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import ru.cpc.smartflatview.indicatorPackage.base.BaseRegulator;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.ScrollingDialog;
import ru.cpc.smartflatview.voice.VoiceDate;

public class DimmerLamp extends BaseRegulator
{
//	private static int on = newDez?(posDez?R.drawable.lamp_d100_p:R.drawable.lamp_d100):R.drawable.lamp03;
//	private static int off = newDez?getOffLamp(R.drawable.lamp_d_off):R.drawable.lamp04;
	private static int on = getIndDisC(R.drawable.lamp_d100_p,
		R.drawable.lamp_d100,
		R.drawable.lamp01,
		R.drawable.lamp_d100_c);
	private static int off = getIndDisPC(R.drawable.lamp02,
		R.drawable.lamp_d_off,
		R.drawable.lamp_d_off_p,
		R.drawable.lamp_d_off_p2,
		R.drawable.lamp_d_off_p3,
		R.drawable.lamp_d_off_p4,
			R.drawable.lamp_d_off_c);
/*
	static int getOffLamp(int inLamp){
		if(pos4Dez) return R.drawable.lamp_d_off_p4;
		if(pos3Dez) return R.drawable.lamp_d_off_p3;
		if(pos2Dez) return R.drawable.lamp_d_off_p2;
		if(posDez) return R.drawable.lamp_d_off_p;
		return inLamp;
	}

 */
	public DimmerLamp(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY, off, 1, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
		voice = new VoiceDate("лампа");
		voice.addCommand("включить",0);
		voice.addCommand("выключить",0);
		voice.addCommand("установить яркость на",1);
		voice.addCommand("задать яркость на",1);

		m_fValueMin = 0;
		m_fValueMax = 100;
	}

	public DimmerLamp setDemo(){
		Bind("1", "1", "1", "0", false, "0", "100", "23");
		return this;
	}

	@Override
	public boolean SetValue(float iX, float iY)
	{
		//int iBrightOld = m_iBright;
		m_iValue = (int) (iX * m_iMaxValue / m_pUI.m_iWidth);
		
		if(m_iValue > m_iMaxValue)
			m_iValue = m_iMaxValue;
		
		if(m_iValue < 0)
			m_iValue = 0;
		
		//if(m_iBright > iBrightOld)
			m_bPower = true;
		Log.d(TAG, "bright = " + m_iValue );

		m_pServer.SendCommand(m_sVariablePower, m_bPower ? "1" : "0");
		if(m_bICP)
			m_pServer.SendCommand(m_sVariableValue, String.valueOf(3584 + m_iValue));
		else
			m_pServer.SendCommand(m_sVariableValue, String.valueOf(m_iValue));
		
		return Update();
	}

	@Override
	public boolean ShowPopup(Context context)
	{
		Log.d("DimmerLamp227-", "m_iReaction ="+m_iReaction);

		ScrollingDialog.Init(m_sName, m_pSubsystem.m_sName);
		final ScrollingDialog.SFSwitcher pSwitcher = m_bNoPower ? null :
				(ScrollingDialog.SFSwitcher)ScrollingDialog.AddSwitcher(
						m_sVariablePower,
						context.getString(R.string.sdPower),
						m_bPower,
						m_iReaction != 0 ? null : new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.d("DimmerLamp227", "onCheckedChanged");
				SwitchOnOff(0, 0);
			}
		});
		final ScrollingDialog.SFSeeker pSeeker = (ScrollingDialog.SFSeeker)ScrollingDialog.AddSeekBar(m_sVariableValue, context.getString(
						R.string.sdBrightness), (int)m_iValue * 100 / m_iMaxValue, (int) m_fValueMin, (int) m_fValueMax, "%d %%", m_iReaction != 0
                                                                                                                             ? null : new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				//if (fromUser) {
				//	SetValue(progress*m_iMaxValue/100);
				//}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Log.d("DimmerLamp227", "onStopTrackingTouch");
				SetValue((seekBar.getProgress() + (int) m_fValueMin) * m_iMaxValue / 100);
			}
		});
		Log.d("DimmerLamp227+", "m_iReaction ="+m_iReaction);

		if(m_iReaction == 1)
			ScrollingDialog.AddAcceptBtn(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("DimmerLamp227", "onClick");
					if(m_iValue * 100 / m_iMaxValue != pSeeker.m_iValue)
						SetValue(pSeeker.m_iValue * m_iMaxValue / 100);

					if(pSwitcher != null && m_bPower != pSwitcher.m_bChecked)
						SwitchOnOff(0, 0);
				}
			});

		//Intent myIntent = new Intent(context, ScrollingActivity.class);
		//context.startActivity(myIntent);
		ScrollingDialog dlg = new ScrollingDialog();
		dlg.show(((Activity)context).getFragmentManager(), "dlg");

//		v.show();
		return false;
	}

	@Override
	protected boolean Update()
	{
		//m_iMaxValue = 100;
		//if(m_iMaxValue == 0)
		//{
		//	Log.d(TAG, "111 m_iMaxBright == 0");
		//	return false;
		//}
		
		int iResId = -1;
		
		int iBright = (int)m_iValue * 100 / m_iMaxValue;
		
		if(m_bPower)
		{
			iResId = on;

			if(iBright < 10)
				getIndDisC(R.drawable.lamp_d10_p,R.drawable.lamp_d10,on,R.drawable.lamp_d10_c);
//				iResId = newDez?(posDez?R.drawable.lamp_d10_p:R.drawable.lamp_d10):on;
			else if(iBright < 20)
				getIndDisC(R.drawable.lamp_d20_p,R.drawable.lamp_d20,on,R.drawable.lamp_d20_c);
//				iResId = newDez?(posDez?R.drawable.lamp_d20_p:R.drawable.lamp_d20):on;
			else if(iBright < 30)
				getIndDisC(R.drawable.lamp_d30_p,R.drawable.lamp_d30,on,R.drawable.lamp_d30_c);
//				iResId = newDez?(posDez?R.drawable.lamp_d30_p:R.drawable.lamp_d30):on;
			else if(iBright < 40)
				getIndDisC(R.drawable.lamp_d40_p,R.drawable.lamp_d40,on,R.drawable.lamp_d40_c);
//				iResId = newDez?(posDez?R.drawable.lamp_d40_p:R.drawable.lamp_d40):on;
			else if(iBright < 50)
				getIndDisC(R.drawable.lamp_d50_p,R.drawable.lamp_d50,on,R.drawable.lamp_d50_c);
//				iResId = newDez?(posDez?R.drawable.lamp_d50_p:R.drawable.lamp_d50):on;
			else if(iBright < 60)
				getIndDisC(R.drawable.lamp_d60_p,R.drawable.lamp_d60,on,R.drawable.lamp_d60_c);
//				iResId = newDez?(posDez?R.drawable.lamp_d60_p:R.drawable.lamp_d60):on;
			else if(iBright < 70)
				getIndDisC(R.drawable.lamp_d70_p,R.drawable.lamp_d70,on,R.drawable.lamp_d70_c);
//				iResId = newDez?(posDez?R.drawable.lamp_d70_p:R.drawable.lamp_d70):on;
			else if(iBright < 80)
				getIndDisC(R.drawable.lamp_d80_p,R.drawable.lamp_d80,on,R.drawable.lamp_d80_c);
//				iResId = newDez?(posDez?R.drawable.lamp_d80_p:R.drawable.lamp_d80):on;
			else if(iBright < 90)
				getIndDisC(R.drawable.lamp_d90_p,R.drawable.lamp_d90,on,R.drawable.lamp_d90_c);
//				iResId = newDez?(posDez?R.drawable.lamp_d90_p:R.drawable.lamp_d90):on;
		}
		else
		{
			iResId = off;
//			iResId = R.drawable.id045;
//
//			if(iBright < 10)
//				iResId = R.drawable.id056;
//			else if(iBright < 20)
//				iResId = R.drawable.id057;
//			else if(iBright < 30)
//				iResId = R.drawable.id058;
//			else if(iBright < 40)
//				iResId = R.drawable.id059;
//			else if(iBright < 50)
//				iResId = R.drawable.id060;
//			else if(iBright < 60)
//				iResId = R.drawable.id061;
//			else if(iBright < 70)
//				iResId = R.drawable.id062;
//			else if(iBright < 80)
//				iResId = R.drawable.id063;
//			else if(iBright < 90)
//				iResId = R.drawable.id064;
		}

		Log.d(TAG, "111 power = " + m_bPower + "bright = " + m_iValue );

		if(m_pUI == null)
		{
			m_iOldResID = iResId;
			m_iNewResID = iResId;
			return false;
		}

		return m_pUI.StartAnimation(iResId);
	}
}
