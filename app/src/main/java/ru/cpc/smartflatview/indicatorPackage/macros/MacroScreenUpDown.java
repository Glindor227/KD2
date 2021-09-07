package ru.cpc.smartflatview.indicatorPackage.macros;

import android.content.Context;

import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.indicatorPackage.base.BaseMacro;

public class MacroScreenUpDown extends BaseMacro {
    public MacroScreenUpDown(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {
        super(iX, iY,
                getIndDisC(R.drawable.curtains_close_p,R.drawable.curtains_close,R.drawable.id066,R.drawable.curtains_close_c),
                getIndDisC(R.drawable.curtains_open_p,R.drawable.curtains_open,R.drawable.id067,R.drawable.curtains_open_c),
                14, sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
        m_bText2 = false;
    }

    @Override
    public boolean ShowPopup(Context context) {
        return false;
    }
}
