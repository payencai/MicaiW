package cn.micaiw.mobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changelcai.mothership.view.recycler.MSClickableAdapter;

import java.util.List;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.listener.OnItemSubviewClickListener;
import cn.micaiw.mobile.entity.FundPage;


/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class FundAdapter extends MSClickableAdapter<FundAdapter.FundViewHolder> {
    private Context mContext;
    private List<FundPage.ListBean> mList;
    private OnItemSubviewClickListener<FundPage.ListBean> subviewClickListener;

    public FundAdapter(Context context, List<FundPage.ListBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public void onBindVH(FundViewHolder holder, int position) {

    }

    @Override
    public FundViewHolder onCreateVH(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_fund_product, parent, false);
        return new FundViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class FundViewHolder extends RecyclerView.ViewHolder {

        public TextView tvOrderTime;
        public RelativeLayout rlStatus;
        public TextView tvStatus;

        public FundViewHolder(View itemView) {
            super(itemView);
        }
    }
}
