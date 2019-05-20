package cn.micaiw.mobile.entity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.custom.DotView;

/**
 * 作者：凌涛 on 2018/5/9 17:36
 * 邮箱：771548229@qq..com
 */
public class MineOptionBean extends RVBaseCell {

    public String name;
    public String viceName;
    public String color;


    public MineOptionBean() {
        super(null);
    }

    public MineOptionBean(String name, String viceName, String color) {
        super(null);
        this.name = name;
        this.viceName = viceName;
        this.color = color;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_option_rv_list, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {

        holder.setText(R.id.name, name);
        holder.setText(R.id.viceName, viceName);
        ((DotView) holder.getView(R.id.dotView)).setColor(Color.parseColor(color));
    }
}
