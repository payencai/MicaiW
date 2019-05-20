package cn.micaiw.mobile.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.micaiw.mobile.R;

/**
 * 作者：凌涛 on 2018/5/12 14:53
 * 邮箱：771548229@qq..com
 */
public class SignDayView extends View {
    private Paint mPaint;
    private boolean isSelect = false;
    private int size = 50;

    public void setSelect(boolean select) {
        isSelect = select;
        invalidate();
    }

    public void setSize(int size) {
        this.size = size;
        invalidate();

    }

    public SignDayView(Context context) {
        this(context, null);
    }

    public SignDayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignDayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        switch (widthMode) {
//            case MeasureSpec.AT_MOST:
//            case MeasureSpec.UNSPECIFIED:
//                widthSize = size;
//                break;
//            case MeasureSpec.EXACTLY:
//                break;
//        }
//        switch (heightMode) {
//            case MeasureSpec.AT_MOST:
//            case MeasureSpec.UNSPECIFIED:
//                heightSize = size;
//                break;
//            case MeasureSpec.EXACTLY:
//                break;
//        }
//        widthSize = heightSize = Math.min(widthSize, heightSize);
//        //设置测量结果
//        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if (isSelect) {
            Bitmap bitmapNormal = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sign_normal);
            Rect rect = new Rect(0, 0, size, size);
            Rect dst = new Rect(0, 0, size, size);
            canvas.drawBitmap(bitmapNormal, rect, dst, mPaint);

        } else {

            Bitmap bitmapSelect = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sign_selected);
            Rect rect = new Rect(0, 0, size, size);
            Rect dst = new Rect(0, 0, size, size);
            canvas.drawBitmap(bitmapSelect, rect, dst, mPaint);
        }

    }
}
