package cn.micaiw.mobile.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：凌涛 on 2018/5/12 14:35
 * 邮箱：771548229@qq..com
 */
public class WeekSignFormView extends View {

    private Paint mTextPaint;
    private Paint mCirclePaint;
    private int mHeightSize = 400;
    private String mTime;
    private int mRowSize;//每列的大小
    private int mLineSize;//每行的大小
    private int mCurrentMonth;//当月的月数
    private int mWidtgSpace = 10;//行间距
    private int drawCircleCount = 0;//画的次数

    private String[] integralArr = new String[7];

    public void setIntegralArr(String[] integralArr) {
        this.integralArr = integralArr;
        invalidate();
    }


    //一个月签到的集合表
    public void setIsSign(Map<Integer, Boolean> isSign) {
        drawCircleCount = 0;
        if (this.isSign != null) {
            this.isSign.clear();
        }
        this.isSign = isSign;
        invalidate();
    }

    private Map<Integer, Boolean> isSign;

    public void setWidtgSpace(int widtgSpace) {
        mWidtgSpace = widtgSpace;
        invalidate();
    }

    public static final String[] week = new String[]{"日", "一", "二", "三", "四", "五", "六",};

    public void setHeightSize(int heightSize) {
        mHeightSize = heightSize;
        invalidate();
    }

    public WeekSignFormView(Context context) {
        this(context, null);
    }

    public WeekSignFormView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekSignFormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextPaint = new Paint();
        mTextPaint.setStrokeWidth(2);//设置画笔宽度
        mTextPaint.setAntiAlias(true); //指定是否使用抗锯齿功能，如果使用，会使绘图速度变慢
        mTextPaint.setStyle(Paint.Style.FILL);//绘图样式，对于设文字和几何图形都有效
        mTextPaint.setTextAlign(Paint.Align.CENTER);//设置文字对齐方式，取值：align.CENTER、align.LEFT或align.RIGHT
        mTextPaint.setTextSize(28);//设置文字大小

        mCirclePaint = new Paint();
        mCirclePaint.setStrokeWidth(1);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.parseColor("#7b7b7b"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        mTime = sdf.format(new Date());
        getCurrentMonthWeek();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                heightSize = mHeightSize;
                break;
            case MeasureSpec.EXACTLY:
                break;
        }
        //设置测量结果
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        drawCircleCount = 0;
        mRowSize = getWidth() / 4;//每一列的宽度
        mLineSize = getHeight() / 2;//每一行的高度
        mTextPaint.setColor(Color.BLACK);


        for (int i = 0; i < 4; i++) {
            int x = i * mRowSize + mRowSize / 2;
            int y = mLineSize / 2;
            //画圆
            drawCircle(canvas, x, y);
        }

        for (int i = 0; i < 3; i++) {

            int x = (i + 1) * mRowSize;
            int y = mLineSize / 2 + mLineSize;
            //画圆
            drawCircle(canvas, x, y);
        }


    }

    /**
     * 圆心的位置
     *
     * @param canvas
     * @param x
     * @param y
     */
    private void drawCircle(Canvas canvas, int x, int y) {


        if (drawCircleCount >= 7) {
            return;
        }
        int min = Math.min(mRowSize, mLineSize);
        int squareSize = (min - mWidtgSpace);

        String stringDay = getStringDay(drawCircleCount + 1);
        Rect rect = new Rect();
        mTextPaint.getTextBounds(stringDay, 0, stringDay.length(), rect);
        int textY = y - ((squareSize / 3) - 3) - 30;
        canvas.drawText(stringDay, x, textY, mTextPaint);//因为文字居中对齐

        //得到正方形的大小
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.parseColor("#7b7b7b"));
        canvas.drawCircle(x, y, (squareSize / 3) - 3, mCirclePaint);
        canvas.drawCircle(x, y, (squareSize / 3) - 10, mCirclePaint);

        mCirclePaint.setStyle(Paint.Style.FILL);
        int isCurDaySign = drawCircleCount + 1;
        if (isSign == null) {
            isSign = new HashMap<>();
            for (int i = 1; i < 8; i++) {
                isSign.put(i, false);
            }
        }
        Boolean aBoolean = isSign.get(isCurDaySign);
        if (aBoolean) {//签到的换一种颜色
            mCirclePaint.setColor(Color.parseColor("#ff9500"));
        } else {
            mCirclePaint.setColor(Color.parseColor("#F5D894"));
        }
        canvas.drawCircle(x, y, (squareSize / 3) - 10, mCirclePaint);

        String stringIntegral = getStringIntegral(drawCircleCount + 1);
        Rect rect2 = new Rect();
        mTextPaint.getTextBounds(stringIntegral, 0, stringDay.length(), rect2);
        int textY2 = y + ((squareSize / 3) - 3) + 50;
        canvas.drawText(stringIntegral, x, textY2, mTextPaint);//因为文字居中对齐

        drawCircleCount++;
    }

    private void getCurrentMonthWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat format = new SimpleDateFormat("E");
        String format1 = format.format(calendar.getTime()).toUpperCase();
        mCurrentMonth = getCurrentMonthDay();
        Log.d("lingtao", "getCurrentMonthWeek: " + mCurrentMonth);
        Log.d("lingtao", "getCurrentMonthWeek: " + format1);

    }


    /**
     * 获取当月的 天数
     */
    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    private static String getStringDay(int count) {

        String result = null;
        switch (count) {
            case 1:
                result = "第一天";
                break;
            case 2:
                result = "第二天";
                break;
            case 3:
                result = "第三天";
                break;
            case 4:
                result = "第四天";
                break;
            case 5:
                result = "第五天";
                break;
            case 6:
                result = "第六天";
                break;
            case 7:
                result = "第七天";
                break;
        }

        return result;
    }

    private String getStringIntegral(int count) {

        String result = null;
        switch (count) {
            case 1:
                result = integralArr[0];
                break;
            case 2:
                result = integralArr[1];
                break;
            case 3:
                result = integralArr[2];
                break;
            case 4:
                result = integralArr[3];
                break;
            case 5:
                result = integralArr[4];
                break;
            case 6:
                result = integralArr[5];
                break;
            case 7:
                result = integralArr[6];
                break;
        }

        return result;
    }

    public void addSign() {
        int count = 1;
        for (int i = 1; i <= isSign.keySet().size(); i++) {
            Boolean aBoolean = isSign.get(i);
            if (aBoolean) {
                count++;
            }
        }

        for (int i = 1; i <= count; i++) {
            isSign.put(i, true);
        }
        invalidate();
    }


}
