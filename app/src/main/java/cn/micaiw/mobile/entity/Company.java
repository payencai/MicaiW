package cn.micaiw.mobile.entity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.activity.P2PDetailsActivity;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;

/**
 * P2P实体类
 */
public class Company extends RVBaseCell implements Serializable {


    /**
     * id : 5
     * platformName : 团贷网
     * keyword :
     * isFirst : 1
     * platformSearch : 高评分，高返利，存管系，风投系，
     * platformLabel : C轮融资,知名风投
     * annualRate : 10
     * bidPlan : 30
     * investmentTotal : 10000
     * investmentIncome : 171
     * avgAnnualRate : 70
     * riskScore : 95
     * riskAssessment :
     * platformLogo : http://micainet.oss-cn-shenzhen.aliyuncs.com/%E4%B8%8A%E4%BC%A0/2018051615354788?Expires=1526522665&OSSAccessKeyId=LTAIKHMgefavXxmd&Signature=IietGHUoMxA5o6ZydNvO1k%2BUxYw%3D
     * platformLogoKey : 上传/2018051615354788
     * platformUrl : www.micaiw.cn
     * userNumber : 0
     * createTime : 2018-05-16 14:30:04
     * releaseTime : null
     * stopTime : null
     * state : 1
     * type : 2
     * content : 返利规则： 1：必须通过点击“立即投资”前往平台注册，注意中途不要刷新页面或者访问其他页面 2：活动仅限首投，因此出借方案仅可以选择其中一种方案参加 3、红包、体验金等平台奖励，请以平台实际活动情况为主；米财钱包公布的仅作为参考 4：提交返现信息时的注册手机号是注册平台的手机号 5：注册之后的3天之内完成投资，出借完成之后请在12小时内提交出借信息 6：必须通过米财钱包注册投资才有返现，p2p平台老用户不参与返利 7：返利奖励将返到提交的支付宝或银行卡，务必核对提交的银行卡和支付宝信息的准确性，由于提交的信息错误造成返现失败，米财钱包将不承担任何责任 8：返现无特别声明，将会在你提交投资信息后下一个工作日进行核对返现，节假日顺延至节后第一个工作日  免责声明:米财钱包仅为信息平台，合作平台不保证100%安全。 投资人根据自身资金和风险承受情况，进行分散投资，米财钱包不对用户投资平台承担本息或本金的保障！   温馨提示：投资有风险，理财需谨慎！
     */
    private int id;
    private String platformName;
    private String keyword;
    private int isFirst;
    private String platformSearch = "";
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
    private String releaseTime;
    private String stopTime;
    private int state;
    private int type;
    private String content;
    private boolean isRuntime = true;//
    private String mSeconds;//当前倒计时时间
    private int companyType;//1为p2p  2、为基金

    public boolean isFinish = false;

    private List<String> labelList = new ArrayList<>();//标签
    //    private Context mContext;
    private Context mContext;
    private double mADouble;

    public Company() {
        super(null);
    }

    public Company(String name) {
        super(null);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    private boolean isNum(String str){
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company_rv, parent, false);
        return new RVBaseViewHolder(view);
    }
    public String formatDouble(double s) {
        DecimalFormat fmt = new DecimalFormat("##0.000");
        return fmt.format(s);
    }
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
    @Override
    public void onBindViewHolder(final RVBaseViewHolder holder, int position) {
        if (isFinish) {
            holder.getTextView(R.id.isFinishText).setVisibility(View.VISIBLE);
        } else {
            holder.getTextView(R.id.isFinishText).setVisibility(View.GONE);
        }
//        LinearLayout labellayout= (LinearLayout) holder.getView(R.id.companyLabelLayout);
//        labellayout.removeAllViews();
        //显示图片
        if (mContext == null) {
            mContext = holder.getItemView().getContext();
        }
        if (mContext != null) {
            Glide.with(mContext).load(platformLogo).placeholder(R.mipmap.logo).error(R.mipmap.logo).into(holder.getImageView(R.id.companyLogo));
            int count = labelList.size() > 17 ? 17 : labelList.size();
            Log.e("count",count+"");
            for (int i = 0; i < 4; i++) {
                TextView textView = holder.getTextViewByString(i + 1);
                textView.setVisibility(View.GONE);
            }
            boolean isFrist = false;
            for (int i = 0; i < count; i++) {
//                if (i == 0) {
//                    if (isFirst == 1) {
//                        isFrist = true;
//                        TextView textView = holder.getTextViewByString(i + 1);
//                        textView.setText("首投");
//                        textView.setVisibility(View.VISIBLE);
//                    }
//                }
                TextView textView = null;
                if (isFrist) {
                    textView = holder.getTextViewByString(i + 2);
                } else {
                    textView = holder.getTextViewByString(i + 1);
                }
                textView.setText(labelList.get(i));
                textView.setVisibility(View.VISIBLE);
            }
        }
        holder.setIsRecyclable(false);
        final String annualRate = this.annualRate;
        double aDouble = 0;
        if (annualRate != null) {
            try {
                aDouble = Double.parseDouble(annualRate);
            } catch (RuntimeException e) {
            }
        }
        holder.setText(R.id.text, annualRate + "%");
        TextView textView = holder.getTextView(R.id.text);
        if (aDouble < 0) {
            textView.setTextColor(Color.parseColor("#00FF22"));
        } else {
            //textView.setTextColor(Color.parseColor("#F5333D"));
        }
        if(avgAnnualRate.contains("%")){
            avgAnnualRate=avgAnnualRate.replace("%","");
        }
        holder.setText(R.id.bidPlan, avgAnnualRate+"%");
        double value=Double.parseDouble(annualRate.trim())+Double.parseDouble(avgAnnualRate.trim());
        String a=formatDouble(value);
        holder.setText(R.id.zhonghe,subZeroAndDot(a)+"%");
       // TextView investment = holder.getTextView(R.id.investment);
        String investmentText = "投" + investmentTotal + "获得" + investmentIncome;
        int start = investmentText.lastIndexOf("得") + 1;
        int end = investmentText.length();
        SpannableStringBuilder style = new SpannableStringBuilder(investmentText);
        style.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);     //设置指定位置文字的颜色
        style.setSpan(new AbsoluteSizeSpan(56), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       // investment.setText(style);

        String riskStr = "风控分：" + riskScore;
        if (!TextUtils.isEmpty(riskAssessment)) {
            riskStr += "  " + riskAssessment;
        }
        holder.setText(R.id.riskScore, riskStr);
        holder.setText(R.id.userNumber, "已参加" + userNumber + "人");
        TextView stopTimeTV = holder.getTextView(R.id.stopTime);

        if (state == 1) {
            stopTimeTV.setTextColor(Color.parseColor("#d0021b"));
            if (!TextUtils.isEmpty(stopTime)) {
                try {
                    stopTimeTV.setVisibility(View.VISIBLE);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    long futureTime = (long) sdf.parse(stopTime).getTime();
                    long currentTime = System.currentTimeMillis();               // 1544713200000
                    long offsetTime = futureTime - currentTime;
                    long l = offsetTime / 1000;
                    setRuntime(stopTimeTV, l);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else if (state == 2) {//暂停
            if (!TextUtils.isEmpty(releaseTime)) {
                stopTimeTV.setTextColor(Color.parseColor("#7ed321"));
                try {
                    stopTimeTV.setVisibility(View.VISIBLE);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    long futureTime = (long) sdf.parse(releaseTime).getTime();
                    long currentTime = System.currentTimeMillis();               // 1544713200000
                    long offsetTime = futureTime - currentTime;
                    long l = offsetTime / 1000;
                    setRuntime(stopTimeTV, l);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {

                }
            }
        }

        if (!isFinish) {
            holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, P2PDetailsActivity.class);
                    intent.putExtra("platformLogo", platformLogo);//logo
                    intent.putExtra("platformName", platformName);//名称
                    intent.putExtra("labelList", (Serializable) labelList);
                    intent.putExtra("content", content);//内容
                    intent.putExtra("annualRate", annualRate);//内容
                    intent.putExtra("avgAnnualRate", avgAnnualRate);//内容
                    intent.putExtra("riskScore", riskScore + "  " + riskAssessment);//风控分
//                intent.putExtra("riskAssessment", riskAssessment );//风控分内容
                    intent.putExtra("userNumber", userNumber);//参加人数
                    intent.putExtra("isFirst", isFirst);//是否首投
                    intent.putExtra("platformUrl", platformUrl);//直达连接
                    intent.putExtra("ptpPlatformId", id);//
                    intent.putExtra("companyType", companyType);
                    mContext.startActivity(intent);
//                P2PDetailsActivity.startP2PDetailsActivity(mContext, Company.this);
                }
            });
        }

    }


    public List<String> getLabelList() {
        return labelList;
    }

    public void addLabelList(String str) {
        labelList.add(str);
    }

    public void removeLabelList(String str) {
        labelList.remove(str);
    }

    public void removeLabelList(int index) {
        if (index >= labelList.size()) {
            return;
        }
        labelList.remove(index);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyType() {
        return companyType;
    }

    public void setCompanyType(int companyType) {
        this.companyType = companyType;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", platformName='" + platformName + '\'' +
                ", keyword='" + keyword + '\'' +
                ", isFirst=" + isFirst +
                ", platformSearch='" + platformSearch + '\'' +
                ", platformLabel='" + platformLabel + '\'' +
                ", annualRate='" + annualRate + '\'' +
                ", bidPlan='" + bidPlan + '\'' +
                ", investmentTotal='" + investmentTotal + '\'' +
                ", investmentIncome='" + investmentIncome + '\'' +
                ", avgAnnualRate='" + avgAnnualRate + '\'' +
                ", riskScore='" + riskScore + '\'' +
                ", riskAssessment='" + riskAssessment + '\'' +
                ", platformLogo='" + platformLogo + '\'' +
                ", platformLogoKey='" + platformLogoKey + '\'' +
                ", platformUrl='" + platformUrl + '\'' +
                ", userNumber=" + userNumber +
                ", createTime='" + createTime + '\'' +
                ", releaseTime='" + releaseTime + '\'' +
                ", stopTime='" + stopTime + '\'' +
                ", state=" + state +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", labelList=" + labelList +
                '}';
    }

    //    public void addLabel(Label label) {
//        this.labelList.add(label);
//    }
//
//    public void removeLabel(Label label) {
//        this.labelList.remove(label);
//    }
//
//    public List<Label> getLabelList() {
//        return labelList;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getRiskScore() {
//        return riskScore;
//    }
//
//    public void setRiskScore(int riskScore) {
//        this.riskScore = riskScore;
//    }
//
//    public int getInvestmentState() {
//        return investmentState;
//    }
//
//    public void setInvestmentState(int investmentState) {
//        this.investmentState = investmentState;
//    }
//
//    public String getPauseTime() {
//        return pauseTime;
//    }
//
//    public void setPauseTime(String pauseTime) {
//        this.pauseTime = pauseTime;
//    }
//
//    public int getJoinPeople() {
//        return joinPeople;
//    }
//
//    public void setJoinPeople(int joinPeople) {
//        this.joinPeople = joinPeople;
//    }


    private void setRuntime(final TextView stopTimeTV, final long l) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                long mLong = l;
                while (isRuntime) {
                    if (mLong <= 0) {
                        isRuntime = false;
                        if (mContext instanceof Activity) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (stopTimeTV != null) {
                                        stopTimeTV.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        break;
                    }
                    mSeconds = formatSeconds(mLong);
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (stopTimeTV != null) {
                                    stopTimeTV.setText("暂停投资:" + mSeconds);
                                }
                            }
                        });
                    }
                    SystemClock.sleep(1000);
                    mLong--;
                }
            }
        }).start();
    }

    /**
     * 秒转化为天小时分秒字符串
     *
     * @param seconds
     * @return String
     */
    public static String formatSeconds(long seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }
}
