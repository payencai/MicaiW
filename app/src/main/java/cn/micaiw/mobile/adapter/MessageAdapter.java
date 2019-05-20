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
import cn.micaiw.mobile.entity.MessageItem;

/**
 * Created by Administrator on 2018/3/24 0024.
 */

public class MessageAdapter extends MSClickableAdapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<MessageItem> mList;

    public MessageAdapter(Context context, List<MessageItem> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public void onBindVH(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateVH(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView tvOrderTime;
        public RelativeLayout rlStatus;
        public TextView tvStatus;

        public MessageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
