package ru.cpc.smartflatview.indicatorPackage.relays;

import android.app.Activity;
import android.content.Context;
import android.widget.CompoundButton;

import ru.cpc.smartflatview.indicatorPackage.base.BaseRelay;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.ScrollingDialog;



public class Radiator extends BaseRelay
{
    public Radiator(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY,
                getIndDisC(R.drawable.radiator_on_p,R.drawable.radiator_on_2,R.drawable.radiator_on,R.drawable.radiator_on_c),
                getIndDisC(R.drawable.radiator_off_p,R.drawable.radiator_off_2,R.drawable.radiator_off,R.drawable.radiator_off_c),
                5, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

    }

    @Override
    public boolean ShowPopup(Context context)
    {
        ScrollingDialog.Init(m_sName, m_pSubsystem.m_sName);
        final ScrollingDialog.SFSwitcher pSwitcher =
                (ScrollingDialog.SFSwitcher)ScrollingDialog.AddSwitcher(
                        m_sVariable,
                        context.getString(R.string.sdPower),
                        m_bValue,
                        m_iReaction != 0 ?
                                null :
                                (CompoundButton.OnCheckedChangeListener) (buttonView, isChecked) ->
                                        SwitchOnOff(0, 0));


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
