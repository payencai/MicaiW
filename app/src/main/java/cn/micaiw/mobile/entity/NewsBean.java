package cn.micaiw.mobile.entity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.activity.WebViewActivity;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;

/**
 * 作者：凌涛 on 2018/5/18 17:35
 * 邮箱：771548229@qq..com
 */
public class NewsBean extends RVBaseCell {

    /**
     * id : f14437d5-55bf-4161-b1e8-41ab93d4d017
     * num : 201805160620469822
     * title : 百亿私募源乐晟业绩变脸 14基金年内全亏9只跌超10%
     * cover : 上传/2018051618203670
     * coverUri : http://micainet.oss-cn-shenzhen.aliyuncs.com/%E4%B8%8A%E4%BC%A0/2018051618203670?Expires=1526636142&OSSAccessKeyId=LTAIKHMgefavXxmd&Signature=thr%2BeE3ltSOoVBv8iv1mGRDmplg%3D
     * content : 上传/72b2086e-b6a9-40d2-b7b8-dae6aea5ffd9
     * contentUri : http://micainet.oss-cn-shenzhen.aliyuncs.com/%E4%B8%8A%E4%BC%A0/72b2086e-b6a9-40d2-b7b8-dae6aea5ffd9?Expires=1526636142&OSSAccessKeyId=LTAIKHMgefavXxmd&Signature=0KL9TEcN6nxVFoTNz3E8VMeRYAw%3D
     * categoryId : 1
     * createTime : 2018-05-16 18:20:46
     * isUse : 1
     * isCancel : 1
     * requestNum : 1
     * updateTime : 2018-05-16 18:20:46
     */

    private String id;
    private String num;
    private String title;
    private String cover;
    private String coverUri;
    private String content;
    private String contentUri;
    private String categoryId;
    private String createTime;
    private String isUse;
    private String isCancel;
    private int requestNum;
    private String updateTime;

    public NewsBean() {
        super(null);
    }

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public int getRequestNum() {
        return requestNum;
    }

    public void setRequestNum(int requestNum) {
        this.requestNum = requestNum;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_rv_list, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RVBaseViewHolder holder, int position) {
        Log.d("654321", "onBindViewHolder: " + title);
        holder.setText(R.id.newsTitle, title);
        holder.setText(R.id.watchNumber, requestNum + "");
        String replace = updateTime.replace(" ", ":");
        final String timeText = replace.split(":")[0];
        holder.setText(R.id.newsTime, timeText);
        final Context context = holder.getItemView().getContext();
        Glide.with(context).load(coverUri)
                .placeholder(R.mipmap.ic_loading)
                .error(R.mipmap.ic_load_failure_)
                .into(holder.getImageView(R.id.newsImg));

        holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                WebViewActivity.starUi(context, contentUri, "米财头条");
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", contentUri);
                intent.putExtra("title", "米财头条");
                intent.putExtra("contentTitle", title);
                intent.putExtra("watchNumber", ""+requestNum);
                intent.putExtra("newsTime", timeText);
                context.startActivity(intent);
            }
        });
    }
}
