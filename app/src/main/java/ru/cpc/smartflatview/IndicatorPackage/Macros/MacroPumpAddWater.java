package ru.cpc.smartflatview.IndicatorPackage.Macros;

import android.content.Context;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseMacro;
import ru.cpc.smartflatview.R;

public class MacroPumpAddWater extends BaseMacro
{
	public MacroPumpAddWater(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getIndDisC(R.drawable.st16_p,R.drawable.st16_2,R.drawable.st16,R.drawable.st16_c),
				getIndDisC(R.drawable.st16_p,R.drawable.st16_2,R.drawable.st16,R.drawable.st16_c),
				16, sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

		m_bText2 = false;
	}

	@Override
	public boolean ShowPopup(Context context)
	{
		return false;
	}
}
