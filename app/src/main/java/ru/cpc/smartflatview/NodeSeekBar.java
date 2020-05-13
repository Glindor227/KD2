package ru.cpc.smartflatview;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.SeekBar;

import ru.cpc.smartflatview.IndicatorPackage.Base.BaseRegulator;

public class NodeSeekBar extends BaseRegulator
{
    private String m_sButtonText;
    private String m_sPattern = "%.1f °C";

    NodeSeekBar(int iX, int iY, String sName, boolean bMetaInd, boolean bProtected, boolean bDoubleScale, boolean bQuick, int iReaction, int iScale)
    {

        super(iX, iY, -1, 8, sName, bMetaInd, bProtected, bDoubleScale, bQuick, iReaction, iScale);

        m_iValue = 20;
        m_bPower = true;

        m_bText2 = true;
        Logger.Instance.AddDebugInfo("NodeSeekBar: Конструктор  m_iValue=" + m_iValue);

        m_sButtonText = ScrollingDialog.SFSeeker.getFormatValue(m_sPattern,  (int)(m_iValue*ScrollingDialog.factorSet));

        m_bDoubleWidth = true;
    }

    @Override
    public void BindUI(SFServer pServer, Context context, IndicatorUI pUI)
    {
        super.BindUI(pServer, context, pUI);
        m_pUI.m_pText2.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));//, context.getTheme()));
        m_pUI.m_pText2.setText(m_sButtonText);
    }

    @Override 
    public boolean Process(String sAddr, String sVal)
    {
        if(m_bLockUpdate)
            return false;

        Logger.Instance.AddDebugInfo("NodeSeekBar: Process() addr = " + sAddr + ", value = " + sVal );
        //Log.d(TAG, "NodeSeekBar: Process() addr = " + sAddr + ", value = " + sVal );
        boolean bUpdated = super.Process(sAddr, sVal);

        if(bUpdated)
        {
            if (m_pBar != null){
                int progr = (int)(m_iValue*ScrollingDialog.factorSet - m_fValueMin*ScrollingDialog.factorSet);
                m_pBar.setProgress(progr);
            }

            Logger.Instance.AddDebugInfo("NodeSeekBar: Process m_iValue=" + m_iValue);

            m_sButtonText = ScrollingDialog.SFSeeker.getFormatValue(m_sPattern, (int)(m_iValue*ScrollingDialog.factorSet));

            return Update();
        }

        return false;
    }

    @Override
    protected boolean Update()
    {
        if(m_pUI == null)
            return false;

        m_pUI.m_pText2.setText(m_sButtonText);

        return true;
    }

    @Override
    public void Load(char code)
    {
        super.Load(code);
        m_sButtonText =ScrollingDialog.SFSeeker.getFormatValue(m_sPattern, (int)(m_iValue*ScrollingDialog.factorSet));
        Update();
    }

    @Override
    public boolean SetValue(float iX, float iY)
    {
        return false;
    }

    @Override
    public boolean ShowPopup(Context context)
    {
        return false;
    }

    @Override
    public void FixLayout(int l, int t, int r, int b)
    {
        int iWidth = r-l;
        int iHeight = b-t;

        IndicatorLayout pLayout = new IndicatorLayout(this, m_pUI.m_fK, l, t, r, b);

        int iText = (int)(1.2f*pLayout.m_iTextSize);

        iHeight -= pLayout.m_iTextSize*2.2;

        m_pUI.m_pOldView.layout(0, iHeight/2 - iText, iWidth, (int)(iHeight*0.65f + iText));//iHeight/2 + iText*2);

        if(m_bDoubleScale)
        {
            m_pUI.m_pText2.layout(0, (int) (iHeight/2-iText*2f), iWidth, iHeight/2 + iText);
            m_pUI.m_pText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, iText*1.5f);
        }
        else
        {
            m_pUI.m_pText2.layout(0, (int) (iHeight/2-iText*1.6f), iWidth, (int)(iHeight/2 + iText*0.8f));
            m_pUI.m_pText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, iText*1.25f);
        }
    }

    private boolean m_bLockUpdate = false;

    @Override
    public void Pressed(float iX, float iY)
    {
        super.Pressed(iX, iY);

        m_bLockUpdate = true;

        Logger.Instance.AddDebugInfo("NodeSeekBar: Pressed - set LockUpdate=true");
        //Log.d(TAG, "NodeSeekBar: Pressed - set LockUpdate=true" );
    }

    @Override
    public void Released()
    {
        super.Released();

        m_bLockUpdate = false;

        Logger.Instance.AddDebugInfo("NodeSeekBar: Released - set LockUpdate=false" );
        //Log.d(TAG, "NodeSeekBar: Released - set LockUpdate=false" );

        if(m_pBar != null)
            SetValue(m_pBar.getProgress()+ (int) m_fValueMin*ScrollingDialog.factorSet);
    }

    @Override
    public boolean SetValue(float iValue)
    {
        Logger.Instance.AddDebugInfo("NodeSeekBar: SetValue = " + iValue );
        //Log.d(TAG, "NodeSeekBar: SetValue = " + iValue );
//        return super.SetValue(iValue+ (int) m_fValueMin);
        return super.SetValue(iValue /ScrollingDialog.factorSet );
    }

    private SeekBar m_pBar = null;

    @Override
    public View GetViewComponent(Context context)
    {
        //if(m_pBar == null)
        {
            //Resources r = context.getResources();
            //int px = (int) TypedValue.applyDimension(
            //       TypedValue.COMPLEX_UNIT_DIP,
            //        16,
            //        r.getDisplayMetrics()
            //);

//            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
//            params.setMargins(px, px, px, px);
//            pText.setLayoutParams(params);

//            Logger.Instance.AddDebugInfo("NodeSeekBar: GetViewComponent - seekBar.progress = " + (m_iValue - (int) m_fValueMin));
            //Log.d(TAG, "NodeSeekBar: GetViewComponent - seekBar.progress = " + (m_iValue - (int) m_fValueMin));

            m_pBar = new SeekBar(context);
            int max = (int) m_fValueMax*ScrollingDialog.factorSet - (int) m_fValueMin*ScrollingDialog.factorSet;
            m_pBar.setMax(max);
            int progr = (int)(m_iValue*ScrollingDialog.factorSet - m_fValueMin*ScrollingDialog.factorSet);
            m_pBar.setProgress(progr);
            Logger.Instance.AddDebugInfo("NodeSeekBar: GetViewComponent(m_iValue="+m_iValue+" m_fValueMax="+m_fValueMax+" m_fValueMin=" + m_fValueMin + ")");
            Logger.Instance.AddDebugInfo("NodeSeekBar: GetViewComponent SeekBar(max=" + max + " progress=" + progr+")");

            m_pBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                {
                    int iValue = m_pBar.getProgress() + (int) m_fValueMin*ScrollingDialog.factorSet;
                    Logger.Instance.AddDebugInfo("NodeSeekBar: onProgressChanged SeekBar(iValue=" + iValue);
                    m_sButtonText =ScrollingDialog.SFSeeker.getFormatValue(m_sPattern, iValue);
                    Update();
                    //if(m_dListener != null)
                    // m_dListener.onProgressChanged(seekBar, progress, fromUser);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {
                    //if(m_dListener != null)
                    // m_dListener.onStartTrackingTouch(seekBar);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                    int iValue = m_pBar.getProgress() + (int) m_fValueMin*ScrollingDialog.factorSet;
                    Logger.Instance.AddDebugInfo("NodeSeekBar: onStopTrackingTouch" );
                    SetValue(iValue);
                }
            });
            /*m_pBar.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    int iValue = m_pBar.getProgress() + (int) m_fValueMin;
                    m_sButtonText = ScrollingDialog.SFSeeker.getFormatValue(m_sPattern, (int)(iValue*ScrollingDialog.factorSet));
                    Update();
                    return false;
                }
            });*/
            //m_pBar.setPadding(px, 0, px, 0);
        }

        return m_pBar;
    }
}
