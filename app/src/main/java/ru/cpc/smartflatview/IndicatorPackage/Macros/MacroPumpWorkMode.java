package ru.cpc.smartflatview.IndicatorPackage.Macros;

import android.content.Context;

import ru.cpc.smartflatview.BaseMacro;
import ru.cpc.smartflatview.R;

public class MacroPumpWorkMode extends BaseMacro
{
	public MacroPumpWorkMode(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getIndDisC(R.drawable.st14_1_p,R.drawable.st14_1_2,R.drawable.st14_1,R.drawable.st14_1_c),
				getIndDisC(R.drawable.st14_0_p,R.drawable.st14_0_2,R.drawable.st14_0,R.drawable.st14_0_c),
				14, sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
		// TODO Auto-generated constructor stub
		m_bText2 = false;
	}

	@Override
	public boolean ShowPopup(Context context)
	{
		return false;
	}
}
