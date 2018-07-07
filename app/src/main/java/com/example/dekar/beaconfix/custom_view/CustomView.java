package com.example.dekar.beaconfix.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static com.example.dekar.beaconfix.firebase.FBConnecting.value_x_int;
import static com.example.dekar.beaconfix.firebase.FBConnecting.value_y_int;

public class CustomView extends View {

    private float Height;
    private float Width;

    public CustomView(Context context) {
        super(context);
        init();
    }
    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        postInvalidate();
        setWillNotDraw(false);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        Height = canvas.getHeight();
        Width = canvas.getWidth();

        Paint iaAm = new Paint();
        iaAm.setColor(Color.RED);
        canvas.drawCircle(value_x_int * Width / 100, value_y_int * Height / 100, 40, iaAm);

        invalidate();
    }
}
