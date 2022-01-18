package ru.cpc.smartflatview.indicatorPackage.relays;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import ru.cpc.smartflatview.indicatorPackage.base.BaseRelay;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.ScrollingDialog;
import ru.cpc.smartflatview.voice.VoiceDate;

public class Curtains extends BaseRelay
{
	public Curtains(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getIndDisC(R.drawable.curtains_close_p,R.drawable.curtains_close,R.drawable.id066,R.drawable.curtains_close_c),
				getIndDisC(R.drawable.curtains_open_p,R.drawable.curtains_open,R.drawable.id067,R.drawable.curtains_open_c),
				2, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
		voice = new VoiceDate("шторы");
		voice.addCommand("открыть",0);
		voice.addCommand("закрыть",0);
		voice.addCommand("поднять",0);
		voice.addCommand("опустить",0);

	}

	@Override
	public boolean ShowPopup(Context context)
	{
		ScrollingDialog.Init(m_sName, m_pSubsystem.m_sName);
		final ScrollingDialog.SFSwitcher pSwitcher = (ScrollingDialog.SFSwitcher)ScrollingDialog.AddSwitcher(m_sVariable, context.getString(
						R.string.sdCurtainsOpened), !m_bValue, m_iReaction != 0 ? null : new CompoundButton.OnCheckedChangeListener() {
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
					if(m_bValue == pSwitcher.m_bChecked)
						SwitchOnOff(0, 0);
				}
			});

		ScrollingDialog dlg = new ScrollingDialog();
		dlg.show(((Activity) context).getFragmentManager(), "dlg");


//		v.show();
		return false;
	}}
