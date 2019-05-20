package cn.micaiw.mobile.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：凌涛 on 2018/5/24 10:22
 * 邮箱：771548229@qq..com
 */
public class MonthSignFormView extends View {

    private Paint mTextPaint;
    private Paint mCirclePaint;
    private int mHeightSize = 700;
    private String mTime;
    private int mRowSize;//每列的大小
    private int mLineSize;//每行的大小
    private int noDrawCount = 0;//每个月空出多个格不画
    private int mCurrentMonth;//当月的月数
    private int mWidtgSpace = 10;//行间距
    private int drawCircleCount = 0;//画的次数
    public static final String[] week = new String[]{"日", "一", "二", "三", "四", "五", "六",};

    //一个月签到的集合表
    public void setIsSign(Map<Integer, Boolean> isSign) {
        if (this.isSign != null) {
            this.isSign.clear();
        }
        this.isSign = isSign;
        invalidate();
    }

    private Map<Integer, Boolean> isSign;

    public void setWidtgSpace(int widtgSpace) {
        mWidtgSpace = widtgSpace;
    }


    public void setHeightSize(int heightSize) {
        mHeightSize = heightSize;
    }


    public MonthSignFormView(Context context) {
        this(context, null);
    }

    public MonthSignFormView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthSignFormView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        Log.d("lingtaoonDraw", "onDraw: 多少次");
        drawCircleCount = 0;
        mRowSize = getWidth() / 7;//每一列的宽度
        mLineSize = getHeight() / 7;
        mTextPaint.setColor(Color.BLACK);
        Rect textBounds = new Rect();

        mTextPaint.getTextBounds(mTime, 0, mTime.length(), textBounds);
        canvas.drawText(mTime, getWidth() / 2, mLineSize / 2 + textBounds.height() / 2, mTextPaint);
//        for (int i = 0; i < 7; i++) {
//            canvas.drawLine(i * mRowSize, 0, i * mRowSize, getHeight(), mTextPaint);
//        }
//        for (int i = 0; i < 7; i++) {
//            canvas.drawLine(0, i * mLineSize, getWidth(), i * mLineSize, mTextPaint);
//        }

        for (int i = 1; i < 7; i++) {//行数
            for (int j = 0; j < 7; j++) {//列数
                if (i == 1) {
                    String s = week[j];
                    Rect weekRounds = new Rect();
                    mTextPaint.getTextBounds(s, 0, s.length(), weekRounds);
                    int left = j * mRowSize + mRowSize / 2;
                    canvas.drawText(s, left, mLineSize + mLineSize / 2 + weekRounds.height() / 2, mTextPaint);
                } else {
                    if (i == 2) {
                        if (j < noDrawCount) {
                            continue;
                        } else {
                            drawCircle(canvas, i, j);
                        }
                    } else {
                        drawCircle(canvas, i, j);
                    }
                }
            }
        }
    }

    private void drawCircle(Canvas canvas, int i, int j) {
        if (drawCircleCount >= mCurrentMonth) {
            Log.d("isCurDaySign", "drawCircle: 你妹夫");
            return;
        }
        int left = j * mRowSize;//列宽
        int top = (i + 1) * mLineSize;
        int right = left + mRowSize;
        int bottom = top + mLineSize;
        Rect rect = new Rect(left, top, right, bottom);
        int min = Math.min(mRowSize, mLineSize);
        //得到正方形的大小
        int squareSize = min - mWidtgSpace;
        //得到圆心x坐标
        int x = j * mRowSize + mRowSize / 2;
        int y = i * mLineSize + mLineSize / 2;


        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.STROKE);

        int isCurDaySign = drawCircleCount + 1;

        Log.d("isCurDaySign", "drawCircle: " + isCurDaySign);
        //画圆
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.parseColor("#7b7b7b"));
        canvas.drawCircle(x, y, (squareSize / 2) - 3, mCirclePaint);
        canvas.drawCircle(x, y, (squareSize / 2) - 10, mCirclePaint);
        mCirclePaint.setStyle(Paint.Style.FILL);
        if (isSign == null) {
            isSign = new HashMap<>();
            for (int n = 1; n <= mCurrentMonth; n++) {
                isSign.put(n, false);
            }
        }
        Boolean aBoolean = isSign.get(isCurDaySign);
        if (aBoolean) {//签到的换一种颜色
            mCirclePaint.setColor(Color.parseColor("#ff9500"));
        } else {
            mCirclePaint.setColor(Color.parseColor("#F5D894"));
        }
        canvas.drawCircle(x, y, (squareSize / 2) - 10, mCirclePaint);

        //画月数
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.BLACK);
        String text = isCurDaySign + "";
        Rect weekRounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), weekRounds);
        canvas.drawText(text, left + (mRowSize / 2),
                (i * mLineSize) + mLineSize / 2 + weekRounds.height() / 2, mTextPaint);
        drawCircleCount++;
    }

    private void getCurrentMonthWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat format = new SimpleDateFormat("E");
        String format1 = format.format(calendar.getTime()).toUpperCase();
        mCurrentMonth = getCurrentMonthDay();

        if (format1.endsWith("日") || format1.endsWith("SUN")) {
            noDrawCount = 0;
        } else if (format1.endsWith("一") || format1.equals("MON")) {
            noDrawCount = 1;
        } else if (format1.endsWith("二") || format1.equals("TUE")) {
            noDrawCount = 2;
        } else if (format1.endsWith("三") || format1.equals("WED")) {
            noDrawCount = 3;
        } else if (format1.endsWith("四") || format1.equals("THU")) {
            noDrawCount = 4;
        } else if (format1.endsWith("五") || format1.equals("FRI")) {
            noDrawCount = 5;
        } else {
            noDrawCount = 6;
        }
    }

    /**
     * 获取当月的 天数
     */
    private static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


}
