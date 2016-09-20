package com.hqyj.dev.farsighttoplogy.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by jiyangkang on 2016/9/17 0017.
 */
public class Circular {

    private Paint circularPaint;
    private Paint backgroundPaint;
    private Paint textPaint;
    private float[] center;
    private float radius;
    private int size;
    private String innerText;

    private String textToShow;


    public void setSize(int size) {
        this.size = size;
    }

    public Circular() {

    }


    public void setTextToShow(String textToShow) {
        this.textToShow = textToShow;
    }

    public Circular(float[] center, float radius, String innerText, Paint backgroundPaint) {
        this.center = center;
        this.radius = radius;
        this.innerText = innerText;
        setPaint(0xff, 0x00, 0xa0, backgroundPaint);
    }


    public void setPaint(int r, int g, int b, Paint backgroundPaint) {
        if (circularPaint == null)
            circularPaint = new Paint();
        circularPaint.setAntiAlias(true);
        circularPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        circularPaint.setColor(Color.argb(0xff, r, g, b));
        this.backgroundPaint = backgroundPaint;
        if (textPaint == null)
            textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(13);
    }

    public float[] getCenter() {
        return center;
    }

    public void setCenter(float[] center) {
        this.center = center;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    private float[] oldXY;
    private String oldTextToShow;

    private boolean isDisconnect;

    public boolean isDisconnect() {
        return isDisconnect;
    }

    public void setDisconnect(boolean disconnect) {
        isDisconnect = disconnect;
    }

    public void drawCircular(Canvas canvas) {

        unDrawCircular(canvas);
        oldXY = center;
        canvas.drawCircle(center[0], center[1], radius, circularPaint);
        canvas.drawCircle(center[0], center[1], radius * 3 / 4, backgroundPaint);
        if (textToShow != null) {
            oldTextToShow = textToShow;
            canvas.drawText(textToShow,
                    center[0] - textToShow.getBytes().length * textPaint.getTextSize() / 2,
                    center[1] + radius + 2,
                    textPaint);
        }

        if (innerText != null){
            canvas.drawText(innerText,
                    center[0] - innerText.getBytes().length * textPaint.getTextSize() / 2,
                    center[1],
                    textPaint);
        }

    }

    public void unDrawCircular(Canvas canvas){
        if (oldXY != null){
            canvas.drawCircle(oldXY[0], oldXY[1], radius, backgroundPaint);
            if (oldTextToShow!=null){
                canvas.drawText(oldTextToShow,
                        oldXY[0] - oldTextToShow.getBytes().length * textPaint.getTextSize() / 2,
                        oldXY[1] + radius + 2,
                        backgroundPaint);
                oldTextToShow = null;
            }


            if (innerText != null){
                canvas.drawText(innerText,
                        oldXY[0] - innerText.getBytes().length * textPaint.getTextSize() / 2,
                        oldXY[1],
                        backgroundPaint);
                innerText = null;
            }
        }

    }
}
