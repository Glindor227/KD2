package ru.cpc.smartflatview.indicatorPackage.macros;

import android.content.Context;

import ru.cpc.smartflatview.indicatorPackage.base.BaseMacro;
import ru.cpc.smartflatview.R;



public class MacroCamOnOff extends BaseMacro
{
    static int resOn  = getIndDisC(R.drawable.cam_on_p,R.drawable.cam_on,R.drawable.cam2,R.drawable.cam_on_c);
    static int resOff  = getIndDisC(R.drawable.cam_off_p,R.drawable.cam_off,R.drawable.cam3,R.drawable.cam_off_c);
    public MacroCamOnOff(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale,boolean on)
    {
            super(iX, iY,
                on? resOn: resOff,
                on? resOff: resOn,
                on? 4: 5,
                sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);


        m_bText2 = false;
    }


    @Override
    public boolean ShowPopup(Context context)
    {
        return false;
    }
}