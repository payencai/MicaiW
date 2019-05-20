package cn.micaiw.mobile.entity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.activity.MsgDetailsActivity;
import cn.micaiw.mobile.activity.WebViewActivity;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;

/**
 * 作者：凌涛 on 2018/5/9 14:30
 * 邮箱：771548229@qq..com
 * 消息类，
 */
public class MessageBean extends RVBaseCell implements Serializable {


    /**
     * id : 7a410991-b2d7-41aa-98cf-51e0b98a21f6
     * num : 201805191024061514
     * title : www
     * content : 上传/73425487-c6fd-4224-b596-10e46e5dd7f7
     * inst : sfsdffefw,sfsdffefw
     * contentUri : http://micainet.oss-cn-shenzhen.aliyuncs.com/%E4%B8%8A%E4%BC%A0/73425487-c6fd-4224-b596-10e46e5dd7f7?Expires=1526697081&OSSAccessKeyId=LTAIKHMgefavXxmd&Signature=ohxQ7pmFcvq8zW%2BoTWYVLZXRe3Q%3D
     * createTime : 2018-05-19 10:24:06
     * updateTime : 2018-05-19 10:24:06
     * pushType : 2
     * pushUserId : null
     * pushTime : 2018-05-19 10:23:37
     * isUse : 1
     * isCancel : 1
     */

    private String id;
    private String num;
    private String title;
    private String content;
    private String inst;
    private String contentUri;
    private String createTime;
    private String updateTime;
    private String pushType;
    private String pushUserId;
    private String pushTime;
    private String isUse;
    private String isCancel;

    public MessageBean() {
        super(null);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_rv_list, parent, false);
//
//        if (isHead == 0) {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.head_msg_item_layout, parent, false);
//        } else if (isHead == 1) {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_rv_list, parent, false);
//        }
        return new RVBaseViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RVBaseViewHolder holder, int position) {
        holder.getView(R.id.itemBody).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.getItemView().getContext();
//                String url = "http://www.micaiqb.com:8080/MiRich/new_detail/new_detail.html?id=" + id;
                String url = "http://www.micaiqb.com:8080/MiRich/new_detail/message.html?id=" + id;
                Log.d("onClickMiRich", "onClick: " + url);
                WebViewActivity.starUi(context, url, "消息详情");
            }
        });
        String replace = updateTime.replace(" ", ":");
        holder.setText(R.id.nftTime, replace.split(":")[0]);
        holder.setText(R.id.nftContent, inst);
        holder.setText(R.id.msgTitle, title);

    }

    private OnItemHeadClick mOnItemHeadClick;
    private OnItemBodyClick mOnItemBodyClick;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getPushUserId() {
        return pushUserId;
    }

    public void setPushUserId(String pushUserId) {
        this.pushUserId = pushUserId;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }


    public interface OnItemHeadClick {
        void onClick(int position);
    }

    public interface OnItemBodyClick {
        void onClick(int position);
    }

}
