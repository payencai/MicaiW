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
import cn.micaiw.mobile.entity.P2PPage;


/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class P2PAdapter extends MSClickableAdapter<P2PAdapter.P2PViewHolder> {
    private Context mContext;
    private List<P2PPage.ListBean> mList;
    private OnItemSubviewClickListener<P2PPage.ListBean> subviewClickListener;

    public P2PAdapter(Context context, List<P2PPage.ListBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public void onBindVH(P2PViewHolder holder, int position) {

    }

    @Override
    public P2PViewHolder onCreateVH(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_p2p_product, parent, false);
        return new P2PViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class P2PViewHolder extends RecyclerView.ViewHolder {

        public TextView tvOrderTime;
        public RelativeLayout rlStatus;
        public TextView tvStatus;

        public P2PViewHolder(View itemView) {
            super(itemView);
            //tvOrderTime = itemView.findViewById(R.id.main_tv_first_order);
        }
    }
}
