package cn.micaiw.mobile.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewConfigurationCompat;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import cn.micaiw.mobile.R;

/**
 * 作者：凌涛 on 2018/5/16 17:24
 * 邮箱：771548229@qq..com
 */
public class ScrollerLayout extends ViewGroup {

    private final String TAG = "ScrollerLayout";

    /**
     * 用于滚动的实例对象
     */
    private Scroller mScroller;

    /**
     * 判定为拖动的最小移动像素数
     */
    private int mTouchSlop;

    /**
     * 手指按下是事的x坐标
     */
    private float mXDown;

    /**
     * 手指触摸时的位置
     */
    private float mXMove;

    /**
     * 上次手指移动时的位置
     */
    private float mXLastMove;

    //视图可移动的左边界
    private int leftBorder;

    //视图可移动的最大右边界
    private int rightBorder;

    /**
     * 手指按下是事的y坐标
     */
    private float mYDown;

    /**
     * 手指触摸时的位置
     */
    private float mYMove;

    /**
     * 上次手指移动时的位置
     */
    private float mYLastMove;

    //视图可移动的上边界
    private int topBorder;

    //视图可移动的最大下边界
    private int bottomBorder;

    //翻页方向（左右 or 上下）
    private int direction;

    private String[] contents;

    public ScrollerLayout(Context context) {
        this(context, null);
    }

    public ScrollerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext());
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScrollerLayout, defStyleAttr, 0);
        direction = ta.getInt(R.styleable.ScrollerLayout_direction, 1);
        ta.recycle();
    }

    private void initViews() {
        for (int i = 0; i < contents.length; i++) {
            TextView textView = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.CENTER;
            textView.setTextColor(Color.parseColor("#333333"));
            textView.setLayoutParams(layoutParams);
            textView.setMaxLines(1);
            textView.setGravity(Gravity.VERTICAL_GRAVITY_MASK);
            textView.setClickable(true);
            textView.setFocusable(true);
            textView.setFocusableInTouchMode(true);
            addView(textView, i);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    //摆放所有子view）
    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int childCount = getChildCount();
        if (b) {
            for (int j = 0; j < childCount; j++) {
                View childView = getChildAt(j);
                //左上右下(横向摆放)
                if (direction == 1) {
                    childView.layout(j * childView.getMeasuredWidth(), 0, childView.getMeasuredWidth() * (j + 1), childView.getMeasuredHeight());
                } else { //左上右下(纵向摆放)
                    childView.layout(0, j * childView.getMeasuredHeight(), childView.getMeasuredWidth(), childView.getMeasuredHeight() * (j + 1));
                }
            }
            if (getChildAt(0) == null) {
                return;
            }
            leftBorder = getChildAt(0).getLeft(); //0
            rightBorder = getChildAt(childCount - 1).getRight(); //页数*页面宽度
            topBorder = getChildAt(0).getTop(); //0
            bottomBorder = getChildAt(childCount - 1).getBottom(); //页数*页面高度
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                mXLastMove = mXDown;

                mYDown = ev.getRawY();
                mYLastMove = mYDown;
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                mXLastMove = mXMove;
                mYMove = ev.getRawY();
                mYLastMove = mYMove;
                //滑动了多少距离
                float diff;
                if (direction == 1) {
                    diff = Math.abs(mXMove - mXDown);
                } else {
                    diff = Math.abs(mYMove - mYDown);
                }
                //滚动其实是父控件的滚动
                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if (diff > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setContents(String[] contents) {
        this.contents = contents;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mXMove = event.getRawX();
                mYMove = event.getRawY();
                if (direction == 1) {//水平滑动
                    //移动了多少距离 向左滑动时值为正数，向右滑动时值为负数
                    int scrolledX = (int) (mXLastMove - mXMove);
                    //此判断只针对向右滑动时（即第一页就向右滑动）
                    if (getScrollX() + scrolledX < leftBorder) {
                        scrollTo(leftBorder, 0);
                        Log.d("lingtao", "onTouchEvent: 已经是第一页啦");
                        return true;
                    }
                    //此判断只针对最后一页向左滑动时
                    //最后一页时getScrollX() = （页数-1）* screenWidth，向左滑动时scrollX大于0
                    else if (getScrollX() + getWidth() + scrolledX > rightBorder) {
                        scrollTo(rightBorder - getWidth(), 0);
                        Log.d("lingtao", "onTouchEvent: 已经是最后一页啦");
                        return true;
                    }
                    scrollBy(scrolledX, 0);
                } else {//上下滑动
                    //移动了多少距离 向上滑动时值为正数，向下滑动时值为负数
                    int scrolledY = (int) (mYLastMove - mYMove);
                    //此判断只针对向右滑动时（即第一页就向下滑动）0 + 负数
                    if (getScrollY() + scrolledY < topBorder) { //判定为第一页时向下滑动
                        scrollTo(0, topBorder);
                        Log.d("lingtao", "onTouchEvent: 已经是顶部啦");
                        return true;
                    }
                    //此判断只针对最后一页向上滑动时
                    //getHeight为当前整个可见页面的高度
                    //最后一页时getScrollY() = （页数-1）* screenHeight，向上滑动时scrollY大于0
                    else if (getScrollY() + getHeight() + scrolledY > bottomBorder) {
                        scrollTo(0, bottomBorder - getHeight());
                        Log.d("lingtao", "onTouchEvent: 已经是底部啦");
                        return true;
                    }
                    scrollBy(0, scrolledY);
                }
                mXLastMove = mXMove;
                mYLastMove = mYMove;
                break;
            case MotionEvent.ACTION_UP:
                if (direction == 1) {
                    // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                    int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                    int dx = targetIndex * getWidth() - getScrollX();
                    // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                    // (起始点的x坐标,起始点的y坐标,终点x坐标,终点y坐标)
                    mScroller.startScroll(getScrollX(), 0, dx, 0);
                } else {
                    int targetIndex = (getScrollY() + getHeight() / 2) / getHeight();
                    int dy = targetIndex * getHeight() - getScrollY();
                    mScroller.startScroll(0, getScrollY(), 0, dy);
                }
                invalidate(); //必须调用该方法
                break;
        }
        return super.onTouchEvent(event);
    }

    //移动了多少距离 向上滑动时值为正数，向下滑动时值为负数
    public void scrollBy(int y) {
        scrollBy(0, y);
    }

    @Override
    public void computeScroll() {
        //super.computeScroll();
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }


}
