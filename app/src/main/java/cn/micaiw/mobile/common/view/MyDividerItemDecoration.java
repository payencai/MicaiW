package cn.micaiw.mobile.common.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by 王松 on 2016/10/9.
 */

public class MyDividerItemDecoration extends RecyclerView.ItemDecoration {
    //item之间的间隔
    private int space;

    public MyDividerItemDecoration(int space) {
        this.space = space;
    }

    //绘制分隔线，分隔线和item在同一平面
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    //绘制分隔线，分隔线和item不在同一平面
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        outRect.right = space;
//        outRect.bottom = space;
//        int childLayoutPosition = parent.getChildLayoutPosition(view);
//        if (childLayoutPosition == 0 || childLayoutPosition == 1 || childLayoutPosition == 2) {
//            outRect.top = space;
//        }else{
////            outRect.top = 0;
//        }
//        if (childLayoutPosition % 3 == 0) {
//            Log.d("google.sang", "getItemOffsets: "+childLayoutPosition);
//            outRect.left = space;
//        }else{
//        }

        outRect.bottom = space;
    }
}
