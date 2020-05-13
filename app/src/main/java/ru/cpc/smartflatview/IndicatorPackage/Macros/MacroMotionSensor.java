package ru.cpc.smartflatview.IndicatorPackage.Macros;

import android.content.Context;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseMacro;
import ru.cpc.smartflatview.R;



public class MacroMotionSensor extends BaseMacro
{
    public MacroMotionSensor(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY,
                getIndDisC(R.drawable.guard_sensor_on_p, R.drawable.guard_sensor_on,R.drawable.id077,R.drawable.guard_sensor_on_c),
                getIndDisC(R.drawable.guard_sensor_off_p,R.drawable.guard_sensor_off,R.drawable.id075,R.drawable.guard_sensor_off_c),
                6, sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
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