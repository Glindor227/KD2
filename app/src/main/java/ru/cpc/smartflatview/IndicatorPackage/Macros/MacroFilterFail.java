package ru.cpc.smartflatview.IndicatorPackage.Macros;

import android.content.Context;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseMacro;
import ru.cpc.smartflatview.R;

public class MacroFilterFail extends BaseMacro
{
	public MacroFilterFail(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getIndDisC(R.drawable.st12_1_p,R.drawable.st12_1_b,R.drawable.st12_1,R.drawable.st12_1_c),
				getIndDisC(R.drawable.st12_0_p,R.drawable.st12_0_b,R.drawable.st12_0,R.drawable.st12_0_c),
				12, sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
		// TODO Auto-generated constructor stub
		m_bText2 = false;
	}

	@Override
	public boolean ShowPopup(Context context)
	{
		return false;
	}
}
