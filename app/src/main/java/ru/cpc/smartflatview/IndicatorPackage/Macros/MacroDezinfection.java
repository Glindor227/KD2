package ru.cpc.smartflatview.IndicatorPackage.Macros;

import android.content.Context;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseMacro;
import ru.cpc.smartflatview.R;

public class MacroDezinfection extends BaseMacro
{
	public MacroDezinfection(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getIndDisC(R.drawable.st10_1_p,R.drawable.st10_1_2,R.drawable.st10_1,R.drawable.st10_1_c),
			getIndDisC(R.drawable.st10_0_p,R.drawable.st10_0_2,R.drawable.st10_0,R.drawable.st10_0_c),
				10, sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

		m_bText2 = false;
	}

	@Override
	public boolean ShowPopup(Context context)
	{
		return false;
	}
}
