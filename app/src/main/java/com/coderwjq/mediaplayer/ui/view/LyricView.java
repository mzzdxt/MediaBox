package com.coderwjq.mediaplayer.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * @Created by coderwjq on 2017/5/8 16:22.
 * @Desc
 */

public class LyricView extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "LyricView";

    private static int mMinSize = 0;
    private static int mMaxSize = 120;
    private static int mCurrentSize = mMinSize;
    private static boolean isReduce = false;
    private Paint mPaint;
    private int mHalfViewWidth;
    private int mHalfViewHeight;
    private String mText = "正在加载歌词...";

    public LyricView(Context context) {
        super(context);
        initView();
    }

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 根据当前文字大小，获取渐变的文字大小
     *
     * @param currentSize
     * @return
     */
    public static int getTextSize(int currentSize) {
        if (isReduce) {
            if (currentSize == mMinSize) {
                isReduce = false;
                currentSize++;
            } else {
                currentSize--;
            }
        } else {
            if (currentSize == mMaxSize) {
                isReduce = true;
                currentSize--;
            } else {
                currentSize++;
            }
        }
        return currentSize;
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(mCurrentSize);
        mCurrentSize = mMinSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHalfViewWidth = w / 2;
        mHalfViewHeight = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect bounds = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), bounds);
        mCurrentSize = getTextSize(mCurrentSize);
        mPaint.setTextSize(mCurrentSize);

        int halfTextHeight = bounds.height() / 2;
        int halfTextWidth = bounds.width() / 2;

        int drawX = mHalfViewWidth - halfTextWidth;
        int drawY = mHalfViewHeight + halfTextHeight;

        canvas.drawText(mText, drawX, drawY, mPaint);
    }

    public void breathing() {
        invalidate();
    }
}
