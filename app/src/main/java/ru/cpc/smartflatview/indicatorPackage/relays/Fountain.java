package ru.cpc.smartflatview.indicatorPackage.relays;

import android.app.Activity;
import android.content.Context;
import android.widget.CompoundButton;

import ru.cpc.smartflatview.indicatorPackage.base.BaseRelay;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.ScrollingDialog;



public class Fountain extends BaseRelay
{
    public Fountain(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale,boolean big)
    {
        super(iX, iY,
                big?
                        getIndDisC(R.drawable.font_big_on_p,R.drawable.font_big_on_2,R.drawable.font_big_on,R.drawable.font_big_on_c):
                        getIndDisC(R.drawable.font_small_on_p,R.drawable.font_small_on_2,R.drawable.font_small_on,R.drawable.font_small_on_c),
                big?
                        getIndDisC(R.drawable.font_big_off_p,R.drawable.font_big_off_2,R.drawable.font_big_off,R.drawable.font_big_off_c):
                        getIndDisC(R.drawable.font_small_off_p,R.drawable.font_small_off_2,R.drawable.font_small_off,R.drawable.font_small_off_c),
                big?10:11, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
    }

    @Override
    public boolean ShowPopup(Context context)
    {
        ScrollingDialog.Init(m_sName, m_pSubsystem.m_sName);
        final ScrollingDialog.SFSwitcher pSwitcher =
                (ScrollingDialog.SFSwitcher)ScrollingDialog.AddSwitcher(m_sVariable, context.getString(R.string.sdPower), m_bValue, m_iReaction != 0 ?
                        null :
                        (CompoundButton.OnCheckedChangeListener) (buttonView, isChecked) -> SwitchOnOff(0, 0));


        if(m_iReaction == 1)
            ScrollingDialog.AddAcceptBtn(v -> {
                if(m_bValue != pSwitcher.m_bChecked)
                    SwitchOnOff(0, 0);
            });

        ScrollingDialog dlg = new ScrollingDialog();
        dlg.show(((Activity) context).getFragmentManager(), "dlg");

//		v.show();
        return false;
    }
}