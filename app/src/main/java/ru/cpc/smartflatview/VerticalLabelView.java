package ru.cpc.smartflatview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class VerticalLabelView extends View
{
    private int          _ascent           = 0;
    private int          _leftPadding      = 0;
    private int          _topPadding       = 0;
    private int          _rightPadding     = 0;
    private int          _bottomPadding    = 0;
    private int          _textSize         = 0;
    private int          _measuredWidth;
    private int          _measuredHeight;
    private Rect _textBounds;
    private TextPaint _textPaint;
    private String       _text             = "";
    private Typeface _typeface         = null;
    private boolean      _topToDown = false;

    public VerticalLabelView(Context context)
    {
        super(context);
        initLabelView();
    }

    public VerticalLabelView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initLabelView();
    }

    public VerticalLabelView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initLabelView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VerticalLabelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initLabelView();
    }

    private void initLabelView()
    {
        this._textBounds = new Rect();
        this._textPaint = new TextPaint();
        this._textPaint.setAntiAlias(true);
        this._textPaint.setTextAlign(Paint.Align.CENTER);
        int DEFAULT_TEXT_SIZE = 30;
        this._textPaint.setTextSize(DEFAULT_TEXT_SIZE);
        this._textSize = DEFAULT_TEXT_SIZE;
    }

    public void setText(String text)
    {
        this._text = text;
        requestLayout();
        invalidate();
    }

    public void topToDown(boolean topToDown)
    {
        this._topToDown = topToDown;
    }

    public void setPadding(int padding)
    {
        setPadding(padding, padding, padding, padding);
    }

    public void setPadding(int left, int top, int right, int bottom)
    {
        this._leftPadding = left;
        this._topPadding = top;
        this._rightPadding = right;
        this._bottomPadding = bottom;
        requestLayout();
        invalidate();
    }

    public void setTextSize(int size)
    {
        this._textSize = size;
        this._textPaint.setTextSize(size);
        requestLayout();
        invalidate();
    }

    public void setTextColor(int color)
    {
        this._textPaint.setColor(color);
        invalidate();
    }

    public void setTypeFace(Typeface typeface)
    {
        this._typeface = typeface;
        this._textPaint.setTypeface(typeface);
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        try
        {
            this._textPaint.getTextBounds(this._text, 0, this._text.length(), this._textBounds);

            TextView _tempView = new TextView(getContext());
            _tempView.setPadding(this._leftPadding, this._topPadding, this._rightPadding, this._bottomPadding);
            _tempView.setText(this._text);
            _tempView.setTextSize(TypedValue.COMPLEX_UNIT_PX, this._textSize);
            _tempView.setTypeface(this._typeface);

            _tempView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            this._measuredWidth = _tempView.getMeasuredHeight();
            this._measuredHeight = _tempView.getMeasuredWidth();

            this._ascent = this._textBounds.height() / 2 + this._measuredWidth / 2;

            setMeasuredDimension(this._measuredWidth, this._measuredHeight);
        }
        catch (Exception e)
        {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
            String LOG_TAG = "VerticalLabelView";
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (!this._text.isEmpty())
        {
            float textHorizontallyCenteredOriginX = this._measuredHeight / 2f;
            float textHorizontallyCenteredOriginY = this._ascent;

            canvas.translate(textHorizontallyCenteredOriginY, textHorizontallyCenteredOriginX);

            float rotateDegree = -90;
            float y = 0;

            if (this._topToDown)
            {
                rotateDegree = 90;
                y = this._measuredWidth / 2;
            }

            canvas.rotate(rotateDegree);
            canvas.drawText(this._text, 0, y, this._textPaint);
        }
    }
}
