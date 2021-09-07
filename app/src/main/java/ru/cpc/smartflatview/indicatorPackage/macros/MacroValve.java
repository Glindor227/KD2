package ru.cpc.smartflatview.indicatorPackage.macros;

import android.content.Context;

import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.indicatorPackage.base.BaseMacro;

public class MacroValve extends BaseMacro {
    public MacroValve(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY,
                getIndDisC(R.drawable.valve_on_p,R.drawable.valve_on,R.drawable.id083_cold,R.drawable.valve_on_c),
                getIndDisC(R.drawable.valve_off_p,R.drawable.valve_off,R.drawable.id085_cold,R.drawable.valve_off_c),
                14, sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

        m_bText2 = false;
    }

    @Override
    public boolean ShowPopup(Context context) {
        return false;
    }

}
