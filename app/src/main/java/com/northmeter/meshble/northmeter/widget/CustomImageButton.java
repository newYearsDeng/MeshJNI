package com.northmeter.meshble.northmeter.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageButton;


/**
 * Created by dyd on 2018/5/21.
 */

@SuppressLint("AppCompatCustomView")
public class CustomImageButton extends ImageButton{
    public String text = null;
    public int color;

    public CustomImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(String text){
        this.text = text;
    }
    public void setColor(int color){
        this.color = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(79f);
        paint.setColor(color);

        float stringWidth = paint.measureText(text);
        float x = (getWidth() - stringWidth) / 2;
        //文字的y轴坐标
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float y = getHeight() / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;

        canvas.drawText(text, x, y, paint);  //绘制文字
    }
}
