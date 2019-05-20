package cn.micaiw.mobile.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.activity.UserInfoDetailsActivity;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.util.CustomPopWindow;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者：凌涛 on 2018/5/15 10:23
 * 邮箱：771548229@qq..com
 */
public class UserInfoItemBean extends RVBaseCell {
    private String propertyName;
    private String propertyImgUrl;
    private String propertyContent;
//    private Context mContext;

    public UserInfoItemBean(String propertyName, String propertyImgUrl, String propertyContent) {
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

    public UserInfoItemBean() {
        super(null);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_userinfo_rv, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RVBaseViewHolder holder, int position) {
        holder.setText(R.id.propertyName, propertyName);
        if (position == 0) {
            ((CircleImageView) holder.getView(R.id.propertyImg)).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.propertyText).setVisibility(View.GONE);
//            Glide.with(holder.getItemView().getContext()).load(propertyImgUrl)
//                    .placeholder(R.mipmap.logo)
//                    .error(R.mipmap.logo)
//                    .into();
            Picasso.with(holder.getItemView().getContext())
                    .load(propertyImgUrl)
                    .error(R.mipmap.logo)
                    .placeholder(R.mipmap.logo)
                    .into(holder.getImageView(R.id.propertyImg));

        } else {
            if (position == 2) {
                holder.getImageView(R.id.tagImg).setVisibility(View.GONE);
            }
            holder.getTextView(R.id.propertyText).setVisibility(View.VISIBLE);
            ((CircleImageView) holder.getView(R.id.propertyImg)).setVisibility(View.GONE);
            if (!TextUtils.isEmpty(propertyContent)) {
                holder.setText(R.id.propertyText, propertyContent);
            }
        }

    }

}
