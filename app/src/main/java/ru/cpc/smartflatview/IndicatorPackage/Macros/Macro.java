package ru.cpc.smartflatview.IndicatorPackage.Macros;

import android.content.Context;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseMacro;
import ru.cpc.smartflatview.R;

public class Macro extends BaseMacro
{
	public Macro(int iX, int iY, String sName, String sButtonName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
	{
		super(iX, iY,
				getIndDisC(R.drawable.macro_on_p,R.drawable.macro_on,R.drawable.id112s,R.drawable.macro_on_c),
			getIndDisC(R.drawable.macro_off_p,R.drawable.macro_off,R.drawable.id111s,R.drawable.macro_off_c),
				1, sName, sButtonName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

		m_bText2 = false;
	}
	public Macro setDemo(){
		Bind("1", "1", "1", "0");
		return this;
	}

//	@Override
//	public void BindUI(SFServer pServer, Context context, IndicatorUI pUI)
//	{
//		super.BindUI(pServer, context, pUI);
//		m_pUI.m_pText2.setTextColor(context.getResources().getColor(android.R.color.primary_text_dark));//, context.getTheme()));
//		m_pUI.m_pText2.setText(m_sButtonText);
//	}
//
//	@Override
//	public void FixLayout(int l, int t, int r, int b)
//	{
//		int iWidth = r-l;
//		int iHeight = b-t;
//
//		int iText = iHeight/6;
//
//		iHeight -= iText*2;
//
//		if(m_bDoubleScale)
//		{
//			m_pUI.m_pText2.layout(-5, (int) (iHeight/2-iText), iWidth, iHeight/2 + iText);
//			m_pUI.m_pText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, iText*1.25f);
//		}
//		else
//		{
//			m_pUI.m_pText2.layout(-5, (int) (iHeight/2-iText), iWidth, iHeight/2 + iText);
//			m_pUI.m_pText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, iText*1.5f);
//		}
//	}

	@Override
	public boolean ShowPopup(Context context)
	{
		return false;
	}
}
