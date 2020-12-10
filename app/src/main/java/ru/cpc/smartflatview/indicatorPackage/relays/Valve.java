package ru.cpc.smartflatview.indicatorPackage.relays;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import ru.cpc.smartflatview.indicatorPackage.base.BaseRelay;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.ScrollingDialog;
import ru.cpc.smartflatview.indicatorPackage.base.VoiceDate;

public class Valve extends BaseRelay
{
	public Valve(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getIndDisC(R.drawable.valve_on_p,R.drawable.valve_on,R.drawable.id083_cold,R.drawable.valve_on_c),
			getIndDisC(R.drawable.valve_off_p,R.drawable.valve_off,R.drawable.id085_cold,R.drawable.valve_off_c),
				3, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
		voice = new VoiceDate("клапан");
		voice.addCommand("включить",0);
		voice.addCommand("выключить",0);
		voice.addCommand("открыть",0);
		voice.addCommand("закрыть",0);

	}

	@Override
	public boolean ShowPopup(Context context)
	{
		ScrollingDialog.Init(m_sName, m_pSubsystem.m_sName);
		final ScrollingDialog.SFSwitcher pSwitcher =
				(ScrollingDialog.SFSwitcher)ScrollingDialog.AddSwitcher(m_sVariable, context.getString(
						R.string.sdOpen), m_bValue, m_iReaction != 0 ? null : new CompoundButton.OnCheckedChangeListener() {
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
