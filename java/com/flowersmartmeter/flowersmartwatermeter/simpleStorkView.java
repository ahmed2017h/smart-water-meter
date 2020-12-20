package com.flowersmartmeter.flowersmartwatermeter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.lifecycle.ViewModel;


public class simpleStorkView extends View {
    // setup initial color
    private final int paintColor = Color.WHITE;
    // defines paint and canvas
    private Paint drawPaint;
    private Path path = new Path();
    public simpleStorkView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupPaint();
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(8);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, drawPaint);
        canvas.drawCircle(50, 50, 20, drawPaint);
        drawPaint.setColor(Color.WHITE);
    }
}
