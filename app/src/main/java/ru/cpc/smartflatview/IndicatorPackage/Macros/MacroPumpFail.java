package ru.cpc.smartflatview.IndicatorPackage.Macros;

import android.content.Context;

import ru.cpc.smartflatview.BaseMacro;
import ru.cpc.smartflatview.R;

public class MacroPumpFail extends BaseMacro
{
	public MacroPumpFail(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getIndDisC(R.drawable.st11_1_p,R.drawable.st11_1_2,R.drawable.st11_1,R.drawable.st11_1_c),
				getIndDisC(R.drawable.st11_0_p,R.drawable.st11_0_2,R.drawable.st11_0,R.drawable.st11_0_c),
				11, sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);
		// TODO Auto-generated constructor stub
		m_bText2 = false;
	}

	@Override
	public boolean ShowPopup(Context context)
	{
		return false;
	}
}
