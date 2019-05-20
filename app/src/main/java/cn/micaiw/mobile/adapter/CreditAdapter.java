package cn.micaiw.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.entity.CreditRecord;
import cn.micaiw.mobile.entity.FinanceRecord;

/**
 * 作者：凌涛 on 2018/12/15 11:25
 * 邮箱：771548229@qq..com
 */
public class CreditAdapter extends BaseAdapter {
    List<CreditRecord> mCreditRecords=new ArrayList<>();
    Context mContext;
    @Override
    public int getCount() {
        return mCreditRecords.size();
    }

    public CreditAdapter(List<CreditRecord> mCreditRecords, Context context) {
        this.mCreditRecords =mCreditRecords ;
        mContext = context;
    }

    @Override
    public Object getItem(int i) {
        return mCreditRecords.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_p2precord_rv_list,null);
        return v;
    }
}

