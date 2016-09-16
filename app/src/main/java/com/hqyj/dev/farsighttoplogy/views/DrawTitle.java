package com.hqyj.dev.farsighttoplogy.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jiyangkang on 2016/6/5 0005.
 */
public class DrawTitle extends View {

    private Rect dstRect, dstRect1;

    private Paint mPaint;

    private String projectName = "华清远见-拓扑图";

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public DrawTitle(Context context) {
        this(context, null);
    }

    public DrawTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        mPaint = new Paint();
        dstRect = new Rect(0, 0, width, height / 5);
        dstRect1 = new Rect(0, 0, width - 3, height / 5 - 3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.argb(0xaf, 0x00, 0x00, 0x00));
        canvas.drawRect(dstRect, mPaint);
        mPaint.setColor(Color.argb(0xff, 0x00, 0xa0, 0x60));
        canvas.drawRect(dstRect1, mPaint);

        mPaint.setColor(Color.argb(0xaf, 0x00, 0x00, 0x00));
        mPaint.setTextSize(dstRect.width() / 25);
        mPaint.setFakeBoldText(true);
        canvas.drawText(projectName,
                dstRect.left + dstRect.width() / 2 + 2 - projectName.length() * mPaint.getTextSize() / 2,
                dstRect.bottom - mPaint.getTextSize() / 2 + 2, mPaint);
        mPaint.setColor(Color.argb(0xff, 0xff, 0xff, 0xff));
        canvas.drawText(projectName,
                dstRect.left + dstRect.width() / 2 - projectName.length() * mPaint.getTextSize() / 2,
                dstRect.bottom - mPaint.getTextSize() / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getPaddingLeft() + getPaddingRight() + dstRect.width();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getPaddingTop() + dstRect.height() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }
}
