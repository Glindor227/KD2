package ru.cpc.smartflatview.IndicatorPackage.Relays;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseRelay;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.ScrollingDialog;

/**
 * Created by Вик on 027. 27. 12. 16.
 */

public class Fountain2 extends BaseRelay
{
    public Fountain2(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY, R.drawable.font_small_on, R.drawable.font_small_off, 5, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean ShowPopup(Context context)
    {
        ScrollingDialog.Init(m_sName, m_pSubsystem.m_sName);
        final ScrollingDialog.SFSwitcher pSwitcher =
                (ScrollingDialog.SFSwitcher)ScrollingDialog.AddSwitcher(
                        m_sVariable,
                        context.getString(R.string.sdPower), m_bValue, m_iReaction != 0 ?
                            null :
                            (CompoundButton.OnCheckedChangeListener) (buttonView, isChecked) -> SwitchOnOff(0, 0));


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