package ru.cpc.smartflatview.IndicatorPackage.Relays;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseRelay;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.ScrollingDialog;

public class PowerControl extends BaseRelay
{
	public PowerControl(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getIndDisC(R.drawable.power_on_p,R.drawable.power_on_2,R.drawable.power_on,R.drawable.power_on_c),
				getIndDisC(R.drawable.power_off_p,R.drawable.power_off_2,R.drawable.power_off,R.drawable.power_off_c),
				8, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
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
		//context.startActivity(myIntent);

		if(m_iReaction == 1)
			ScrollingDialog.AddAcceptBtn(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(m_bValue != pSwitcher.m_bChecked)
						SwitchOnOff(0, 0);
				}
			});

		ScrollingDialog dlg = new ScrollingDialog();
		dlg.show(((Activity) context).getFragmentManager(), "dlg");

//		v.show();
		return false;
	}
}
