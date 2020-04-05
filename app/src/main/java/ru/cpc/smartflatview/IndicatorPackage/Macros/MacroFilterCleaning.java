package ru.cpc.smartflatview.IndicatorPackage.Macros;

import android.content.Context;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseMacro;
import ru.cpc.smartflatview.R;

public class MacroFilterCleaning extends BaseMacro
{
	public MacroFilterCleaning(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getIndDisC(R.drawable.st15_1_p,R.drawable.st15_1_2,R.drawable.st15_1,R.drawable.st15_1_c),
				getIndDisC(R.drawable.st15_0_p,R.drawable.st15_0_2,R.drawable.st15_0,R.drawable.st15_0_c),
				15, sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
		// TODO Auto-generated constructor stub
		m_bText2 = false;
	}

	@Override
	public boolean ShowPopup(Context context)
	{
		return false;
	}
}
