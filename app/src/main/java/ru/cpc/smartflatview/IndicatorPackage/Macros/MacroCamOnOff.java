package ru.cpc.smartflatview.IndicatorPackage.Macros;

import android.content.Context;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseMacro;
import ru.cpc.smartflatview.R;



public class MacroCamOnOff extends BaseMacro
{
    public MacroCamOnOff(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale,boolean on)
    {
            super(iX, iY,
                on?
                        getIndDisC(R.drawable.cam_on_p,R.drawable.cam_on,R.drawable.cam2,R.drawable.cam_on_c)
                        :getIndDisC(R.drawable.cam_off_p,R.drawable.cam_off,R.drawable.cam3,R.drawable.cam_off_c),
                on?
                        getIndDisC(R.drawable.cam_on_p,R.drawable.cam_on,R.drawable.cam2,R.drawable.cam_on_c)
                        :getIndDisC(R.drawable.cam_off_p,R.drawable.cam_off,R.drawable.cam3,R.drawable.cam_off_c),
                    on?4:5, sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);


        m_bText2 = false;
    }


    @Override
    public boolean ShowPopup(Context context)
    {
        return false;
    }
}