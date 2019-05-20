package cn.micaiw.mobile.entity;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;

/**
 * 作者：凌涛 on 2018/5/15 10:23
 * 邮箱：771548229@qq..com
 */
public class SettingItemBean extends RVBaseCell {
    private String propertyName;
    private String propertyImgUrl;
    private String propertyContent;
    private Context mContext;

    public SettingItemBean(String propertyName, String propertyImgUrl, String propertyContent) {
        super(null);
        this.propertyName = propertyName;
        this.propertyImgUrl = propertyImgUrl;
        this.propertyContent = propertyContent;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyImgUrl() {
        return propertyImgUrl;
    }

    public void setPropertyImgUrl(String propertyImgUrl) {
        this.propertyImgUrl = propertyImgUrl;
    }

    public String getPropertyContent() {
        return propertyContent;
    }

    public void setPropertyContent(String propertyContent) {
        this.propertyContent = propertyContent;
    }

    public SettingItemBean() {
        super(null);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_userinfo_rv, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        holder.setText(R.id.propertyName, propertyName);
        holder.getImageView(R.id.propertyImg).setVisibility(View.GONE);
        if (!TextUtils.isEmpty(propertyContent)) {
            holder.setText(R.id.propertyText, propertyContent);
        }

    }
}
