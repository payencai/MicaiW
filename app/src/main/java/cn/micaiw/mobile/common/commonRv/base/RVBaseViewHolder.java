package cn.micaiw.mobile.common.commonRv.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.micaiw.mobile.R;

/**
 * Created by HIAPAD on 2017/12/2.
 */

public class RVBaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;
    private View mItemView;

    public RVBaseViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
        mItemView = itemView;
    }

    /**
     * 获取itemView
     *
     * @return
     */
    public View getItemView() {
        return mItemView;
    }

    public View getView(int resId) {
        return retrieveView(resId);
    }

    public TextView getTextView(int resId) {
        return retrieveView(resId);
    }

    public TextView getTextViewByString(int resIdString) {
        int res = -1;
        switch (resIdString) {
            case 1:
                res = R.id.text1;
                break;
            case 2:
                res = R.id.text2;
                break;
            case 3:
                res = R.id.text3;
                break;
            case 4:
                res = R.id.text4;
                break;
            case 5:
                res = R.id.text5;
                break;
            case 6:
                res = R.id.text6;
                break;
            case 7:
                res = R.id.text7;
                break;
            case 8:
                res = R.id.text8;
                break;

        }
        return getTextView(res);
    }

    public ImageView getImageView(int resId) {
        return retrieveView(resId);
    }

    public Button getButton(int resId) {
        return retrieveView(resId);
    }

    @SuppressWarnings("unchecked")
    protected <V extends View> V retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (V) view;
    }

    public void setText(int resId, CharSequence text) {
        getTextView(resId).setText(text);
    }

    public void setText(int resId, int strId) {
        getTextView(resId).setText(strId);
    }


}
