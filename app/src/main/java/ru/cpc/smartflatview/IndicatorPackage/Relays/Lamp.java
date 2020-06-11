package ru.cpc.smartflatview.IndicatorPackage.Relays;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseRelay;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.ScrollingDialog;

public class Lamp extends BaseRelay
{
	private static int getLampIndDis(boolean on, int type){
		if(on){
			if (type == 1) {
				return getIndDisPC(
						R.drawable.lamp03,
						R.drawable.lamp_on,
						R.drawable.lamp_on_p,
						R.drawable.lamp_on_p,
						R.drawable.lamp_on_p,
						R.drawable.lamp_on_p,
						R.drawable.lamp_on_c);
			}
		}
		else{
			if (type == 1) {
				return getIndDisPC(
						R.drawable.lamp04,
						R.drawable.lamp_off,
						R.drawable.lamp_off_p,
						R.drawable.lamp_off_p2,
						R.drawable.lamp_off_p3,
						R.drawable.lamp_off_p4,
						R.drawable.lamp_off_c);
			}
		}
		return 0;
	}
	public Lamp(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale,int type)
	{
			super(iX, iY,
					getLampIndDis(true,type),
					getLampIndDis(false,type),
					type, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

	}
	public Lamp(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getLampIndDis(true,1),
				getLampIndDis(false,1),
				1, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

	}

	@Override
	public boolean ShowPopup(Context context)
	{

		ScrollingDialog.Init(m_sName, m_pSubsystem.m_sName);
		final ScrollingDialog.SFSwitcher pSwitcher = (ScrollingDialog.SFSwitcher)ScrollingDialog.AddSwitcher(m_sVariable, context.getString(R.string.sdPower), m_bValue, m_iReaction != 0
                                                                                                                                                                         ? null : new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SwitchOnOff(0, 0);
			}
		});

		//Intent myIntent = new Intent(context, ScrollingActivity.class);
		//myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		//context.startActivity(myIntent);

		if(m_iReaction == 1)
			ScrollingDialog.AddAcceptBtn(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(m_bValue != pSwitcher.m_bChecked)
						SwitchOnOff(0,0);
				}
			});

		ScrollingDialog dlg = new ScrollingDialog();
        dlg.show(((Activity) context).getFragmentManager(), "dlg");

		Log.d(TAG, "startActivity...");

//		v.show();
		return false;
	}
}
