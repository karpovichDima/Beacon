package com.example.dekar.beaconfix.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {

    private int value_X_after_hash;
    private int value_Y_after_hash;
    public float x;
    public float y;
    int[] arr_after_hashmap;


    public CustomView(Context context) {
        super(context);
        init(null);
    }
    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private float Height;
    private float Width;

    private void init(@Nullable AttributeSet set){
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);       // set background
        Height = canvas.getHeight();
        Width = canvas.getWidth();
        Width = canvas.getWidth();

        Paint iaAm = new Paint();
        iaAm.setColor(Color.RED);
        canvas.drawCircle(value_X_after_hash * Width / 100, value_Y_after_hash * Height / 100,40, iaAm);


        invalidate();
    }

    public void setValue_X_Y_after_hash(int value_X_after_hash, int value_Y_after_hash) {
        this.value_X_after_hash = value_X_after_hash;
        this.value_Y_after_hash = value_Y_after_hash;
    }

}
