package cn.micaiw.mobile.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.myEventBus.Eventbus;

/**
 * 作者：凌涛 on 2018/5/23 11:05
 * 邮箱：771548229@qq..com
 */
public class PlatfromBean extends RVBaseCell {


    /**
     * id : 7
     * platformName : 合拍在线
     * keyword :
     * isFirst : 1
     * platformSearch : 小额区，民营系，存管系，
     * platformLabel : 网贷前20名
     * annualRate : 12
     * bidPlan : 60
     * investmentTotal : 10000
     * investmentIncome : 227
     * avgAnnualRate : 0.4
     * riskScore : 95
     * riskAssessment :
     * platformLogo : http://micainet.oss-cn-shenzhen.aliyuncs.com/%E4%B8%8A%E4%BC%A0/2018052109573679?Expires=1527045864&OSSAccessKeyId=LTAIKHMgefavXxmd&Signature=bnbqkcAWtsc6Yls216RKyP2eVUo%3D
     * platformLogoKey : 上传/2018052109573679
     * platformUrl : http://www.micaiw.cn/client/netloanDetail.html?id=83
     * userNumber : 0
     * createTime : 2018-05-16 18:33:51
     * releaseTime : null
     * stopTime : null
     * state : 1
     * type : 2
     * content : 2011-06-18上线，恒丰银行资金存管，网贷排名前20。总成交规模853.4亿元。
     */

    private int id;
    private String platformName;
    private String keyword;
    private int isFirst;
    private String platformSearch;
    private String platformLabel;
    private String annualRate;
    private String bidPlan;
    private String investmentTotal;
    private String investmentIncome;
    private String avgAnnualRate;
    private String riskScore;
    private String riskAssessment;
    private String platformLogo;
    private String platformLogoKey;
    private String platformUrl;
    private int userNumber;
    private String createTime;
    private Object releaseTime;
    private Object stopTime;
    private int state;
    private int type;
    private String content;

    public PlatfromBean() {
        super(null);
    }

    @Override
    public int getItemType() {
        return 0;
    }


    private OnSelectPlatfrom mOnSelectPlatfrom;

    public interface OnSelectPlatfrom {
        void onSelectPlatfromBean(int id,String platfromName);
    }

    public void setOnSelectPlatfrom(OnSelectPlatfrom onSelectPlatfrom) {
        mOnSelectPlatfrom = onSelectPlatfrom;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_platfrom_info_rv_layout, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RVBaseViewHolder holder, int position) {
        ImageView imageView = holder.getImageView(R.id.platformLogo);
        TextView textView = holder.getTextView(R.id.platformName);
        Glide.with(holder.getItemView().getContext()).load(platformLogo).placeholder(R.mipmap.logo).error(R.mipmap.logo).into(imageView);
        textView.setText(platformName);
        holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnSelectPlatfrom.onSelectPlatfromBean(id, platformName);
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

    public String getPlatformSearch() {
        return platformSearch;
    }

    public void setPlatformSearch(String platformSearch) {
        this.platformSearch = platformSearch;
    }

    public String getPlatformLabel() {
        return platformLabel;
    }

    public void setPlatformLabel(String platformLabel) {
        this.platformLabel = platformLabel;
    }

    public String getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(String annualRate) {
        this.annualRate = annualRate;
    }

    public String getBidPlan() {
        return bidPlan;
    }

    public void setBidPlan(String bidPlan) {
        this.bidPlan = bidPlan;
    }

    public String getInvestmentTotal() {
        return investmentTotal;
    }

    public void setInvestmentTotal(String investmentTotal) {
        this.investmentTotal = investmentTotal;
    }

    public String getInvestmentIncome() {
        return investmentIncome;
    }

    public void setInvestmentIncome(String investmentIncome) {
        this.investmentIncome = investmentIncome;
    }

    public String getAvgAnnualRate() {
        return avgAnnualRate;
    }

    public void setAvgAnnualRate(String avgAnnualRate) {
        this.avgAnnualRate = avgAnnualRate;
    }

    public String getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(String riskScore) {
        this.riskScore = riskScore;
    }

    public String getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(String riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public String getPlatformLogo() {
        return platformLogo;
    }

    public void setPlatformLogo(String platformLogo) {
        this.platformLogo = platformLogo;
    }

    public String getPlatformLogoKey() {
        return platformLogoKey;
    }

    public void setPlatformLogoKey(String platformLogoKey) {
        this.platformLogoKey = platformLogoKey;
    }

    public String getPlatformUrl() {
        return platformUrl;
    }

    public void setPlatformUrl(String platformUrl) {
        this.platformUrl = platformUrl;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Object releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Object getStopTime() {
        return stopTime;
    }

    public void setStopTime(Object stopTime) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
