package ru.cpc.smartflatview.IndicatorPackage.Macros;

import android.content.Context;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseMacro;
import ru.cpc.smartflatview.R;

/**
 * Created by Вик on 028. 28. 12. 16.
 */

public class MacroCamOff extends BaseMacro
{
    public MacroCamOff(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY,
                getIndDisC(R.drawable.cam_off_p,R.drawable.cam_off,R.drawable.cam3,R.drawable.cam_off_c),
                getIndDisC(R.drawable.cam_off_p,R.drawable.cam_off,R.drawable.cam3,R.drawable.cam_off_c),
                5, sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
        // TODO Auto-generated constructor stub

        m_bText2 = false;
    }

//    @Override
//    public void FixLayout(int l, int t, int r, int b)
//    {
//        int iWidth = r-l;
//        int iHeight = b-t;
//
//        int iText = iHeight/7;
//
//        iHeight -= iText;
//
//        if(m_bDoubleScale)
//        {
//            m_pUI.m_pText.layout(-5, (int) (iHeight-iText*2.2), iWidth, iHeight - iText);
//        }
//        else
//        {
//            m_pUI.m_pText.layout(-5, (int) (iHeight-iText*2.6), iWidth, iHeight - iText);
//        }
//    }

    @Override
    public boolean ShowPopup(Context context)
    {
        return false;
    }
}