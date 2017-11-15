package com.dev.sunshine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Evgeniy on 14.11.2017.
 */

public class MyView extends View {

    private Paint paint;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attributeSet) {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int usableWidth = width - (paddingLeft + paddingRight);
        int usableHeight = height - (paddingTop + paddingBottom);

        int radius = Math.min(usableWidth, usableHeight) / 2;
        int cx = paddingLeft + (usableWidth / 2);
        int cy = paddingTop + (usableHeight / 2);

        paint.setColor(Color.RED);
        canvas.drawCircle(cx, cy, radius, paint);
    }
}
