package cn.micaiw.mobile.entity;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;

/**
 * 作者：凌涛 on 2018/5/24 17:07
 * 邮箱：771548229@qq..com
 */
public class IntegralPrize extends RVBaseCell {


    /**
     * id : 4
     * imageKey : 上传/2018051016410079
     * imageUri : http://micainet.oss-cn-shenzhen.aliyuncs.com/%E4%B8%8A%E4%BC%A0/2018051016410079?Expires=1527154768&OSSAccessKeyId=LTAIKHMgefavXxmd&Signature=qLMIFj6XXp6pwJPtB085Yg2jskM%3D
     * name : 213
     * mark : 123
     * createTime : 2018-05-10 16:41:03
     * isCancel : 1
     */

    private int id;
    private String imageKey;
    private String imageUri;
    private String name;
    private int mark;
    private String createTime;
    private String isCancel;

    public IntegralPrize() {
        super(null);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVBaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_layout, null));
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        Glide.with(holder.getItemView().getContext()).load(imageUri)
                .placeholder(R.mipmap.logo)
                .error(R.mipmap.logo)
                .into(holder.getImageView(R.id.grid_image));
        holder.setText(R.id.grid_title, mark + "积分");
        holder.setText(R.id.deviceName, name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }
}
