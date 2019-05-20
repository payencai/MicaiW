package cn.micaiw.mobile.entity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.activity.FundDetailsActivity;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;

public class FundBean extends RVBaseCell implements Serializable {


    /**
     * id : 3
     * platformName : ceshi
     * keyword : ceshi
     * isFirst : 2
     * annualRate : 50
     * platformBenner : http://micainet.oss-cn-shenzhen.aliyuncs.com/%E4%B8%8A%E4%BC%A0/2018051618424023?Expires=1526962579&OSSAccessKeyId=LTAIKHMgefavXxmd&Signature=tx%2B6b4BSYafMXndOjbb%2FdpvVcrA%3D
     * platformBennerKey : 上传/2018051618424023
     * platformDetailsImg : http://micainet.oss-cn-shenzhen.aliyuncs.com/%E4%B8%8A%E4%BC%A0/2018051618423958?Expires=1526962579&OSSAccessKeyId=LTAIKHMgefavXxmd&Signature=Y0ZxsZipbvD1rMPGeKXHquYJ7nE%3D
     * platformDetailsImgKey : 上传/2018051618423958
     * platformUrl : www.baidu.com
     * createTime : 2018-05-16 18:43:05
     * releaseTime : null
     * stopTime : null
     * state : 1
     * type : 2
     * watchword : dfsdf
     * subtitle : dsf
     * brightSpot : dsfsdf
     * userNumber : null
     */

    private int id;
    private String platformName;
    private String keyword;
    private int isFirst;
    private String annualRate;
    private String platformBenner;
    private String platformBennerKey;
    private String platformDetailsImg;
    private String platformDetailsImgKey;
    private String platformUrl;
    private String createTime;
    private String releaseTime;
    private String stopTime;
    private int state;
    private int type;
    private String watchword;
    private String subtitle;
    private String brightSpot;
    private String userNumber;

    private Context mContext;

    public FundBean() {
        super(null);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fund_rv, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        if (mContext == null) {
            mContext = holder.getItemView().getContext();
        }
        holder.setText(R.id.platformName, platformName);
        holder.setText(R.id.annualRate, "+" + annualRate + "%");
        holder.setText(R.id.watchword, watchword);
        holder.setText(R.id.brightSpot, brightSpot);

        holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FundDetailsActivity.class);
                intent.putExtra("platformName", platformName);
                intent.putExtra("annualRate", annualRate);
                intent.putExtra("platformBenner", platformBenner);
                intent.putExtra("platformDetailsImg", platformDetailsImg);
                intent.putExtra("platformUrl", platformUrl);
                intent.putExtra("brightSpot", brightSpot);
                mContext.startActivity(intent);
//                P2PDetailsActivity.startP2PDetailsActivity(mContext, Company.this);
            }
        });

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }

    public String getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(String annualRate) {
        this.annualRate = annualRate;
    }

    public String getPlatformBenner() {
        return platformBenner;
    }

    public void setPlatformBenner(String platformBenner) {
        this.platformBenner = platformBenner;
    }

    public String getPlatformBennerKey() {
        return platformBennerKey;
    }

    public void setPlatformBennerKey(String platformBennerKey) {
        this.platformBennerKey = platformBennerKey;
    }

    public String getPlatformDetailsImg() {
        return platformDetailsImg;
    }

    public void setPlatformDetailsImg(String platformDetailsImg) {
        this.platformDetailsImg = platformDetailsImg;
    }

    public String getPlatformDetailsImgKey() {
        return platformDetailsImgKey;
    }

    public void setPlatformDetailsImgKey(String platformDetailsImgKey) {
        this.platformDetailsImgKey = platformDetailsImgKey;
    }

    public String getPlatformUrl() {
        return platformUrl;
    }

    public void setPlatformUrl(String platformUrl) {
        this.platformUrl = platformUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWatchword() {
        return watchword;
    }

    public void setWatchword(String watchword) {
        this.watchword = watchword;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBrightSpot() {
        return brightSpot;
    }

    public void setBrightSpot(String brightSpot) {
        this.brightSpot = brightSpot;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    @Override
    public String toString() {
        return "FundBean{" +
                "id=" + id +
                ", platformName='" + platformName + '\'' +
                ", keyword='" + keyword + '\'' +
                ", isFirst=" + isFirst +
                ", annualRate='" + annualRate + '\'' +
                ", platformBenner='" + platformBenner + '\'' +
                ", platformBennerKey='" + platformBennerKey + '\'' +
                ", platformDetailsImg='" + platformDetailsImg + '\'' +
                ", platformDetailsImgKey='" + platformDetailsImgKey + '\'' +
                ", platformUrl='" + platformUrl + '\'' +
                ", createTime='" + createTime + '\'' +
                ", releaseTime='" + releaseTime + '\'' +
                ", stopTime='" + stopTime + '\'' +
                ", state=" + state +
                ", type=" + type +
                ", watchword='" + watchword + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", brightSpot='" + brightSpot + '\'' +
                ", userNumber='" + userNumber + '\'' +
                '}';
    }
}
