package com.hqyj.dev.farsighttoplogy.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by jiyangkang on 2016/9/17 0017.
 */
public class Line {

    private float[] startXY;
    private float[] endXY;

    private Paint linePaint;
    private Paint backgroudPaint;
    private boolean isDisconnect;

    public boolean isDisconnect() {
        return isDisconnect;
    }

    public void setDisconnect(boolean disconnect) {
        isDisconnect = disconnect;
    }

    public Line() {

    }

    public Line(float[] startXY, float[] endXY, Paint backgroudPaint) {

        this.startXY = startXY;
        this.endXY = endXY;
        this.linePaint = new Paint();
        setLinePaint(0xff, 0xff, 0xff);
        this.backgroudPaint = backgroudPaint;
    }

    public void setLinePaint(int r, int g, int b) {
        if (linePaint == null)
            linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.argb(0xff, r, g, b));
    }

    public float[] getStartXY() {
        return startXY;
    }

    public void setStartXY(float[] startXY) {
        this.startXY = startXY;
    }

    public float[] getEndXY() {
        return endXY;
    }

    public void setEndXY(float[] endXY) {
        this.endXY = endXY;
    }

    private float[] oldStartXY;
    private float[] oldEndXY;

    public void drawLine(Canvas canvas) {

        unDrawLine(canvas);
        oldEndXY = endXY;
        oldStartXY = startXY;
        canvas.drawLine(startXY[0], startXY[1], endXY[0], endXY[1], linePaint);
    }

    public void unDrawLine(Canvas canvas){
        if (oldEndXY != null && oldStartXY != null){
            canvas.drawLine(oldStartXY[0], oldStartXY[1], oldEndXY[0], oldEndXY[1], backgroudPaint);
        }
    }

}
