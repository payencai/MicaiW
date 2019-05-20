package cn.micaiw.mobile.entity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.activity.PlatformDetailsActivity;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;

/**
 * 作者：凌涛 on 2018/5/21 10:47
 * 邮箱：771548229@qq..com
 */
public class P2PDetailsBean extends RVBaseCell {

    public int TYPE = 0;//0为头，1为投资方案，2为投资记录
    /**
     * id : 9
     * name : 投资方案1
     * ptpPlatformId : 6
     * amount : 10000
     * term : 90天
     * accrual : 237
     * redEnvelopes : 30
     * reward : 40
     * totalIncome : 307
     * avgAnnualRate : 12.3%
     * agencyPrice : 5
     * description :
     * createTime : 2018-05-16 16:52:23
     */

    /*
     *以下是头信息
     * */
    public String platformLogo;
    public String platformName;
    public ArrayList<String> labelList;
    public String platformUrl;
    public String content;
    public String riskScore;
    public String annualRate;
    public String avgAnnualRate;
    public int userNumber;
    public int isFirst;

    private int id;
    private String name;
    private int ptpPlatformId;
    private int amount;
    private String term;
    private int accrual;
    private int redEnvelopes;
    private int reward;
    private int totalIncome;
    private int agencyPrice;
    private String description;
    private String createTime;

    private Context mContext;

    public P2PDetailsBean() {
        super(null);
    }

    @Override
    public int getItemType() {
        return TYPE;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (TYPE == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_p2pdetailsbean_head_rv_layout, parent, false);
        } else if (TYPE == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_p2pdetailsbean_investment_blue_rv_layout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_p2pdetailsbean_investment_record_rv_layout, parent, false);
        }
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {

        if (TYPE == 0) {
            disposeHead(holder, position);
        } else if (TYPE == 1) {

            disposeInvestmentBlue(holder, position);
        } else {

        }
    }

    private void disposeHead(RVBaseViewHolder holder, int position) {

//        double parseDouble;
//        try {
//            parseDouble = Double.parseDouble(avgAnnualRate);
//        } catch (Exception e) {
//            parseDouble = 0.00;
//        }
        holder.setText(R.id.annualRate, "米财钱包返现比例" + avgAnnualRate + "%");
        //显示图片
        if (mContext == null) {
            mContext = holder.getItemView().getContext();
        }
        if (mContext != null) {
            Glide.with(mContext).load(platformLogo).placeholder(R.mipmap.logo).error(R.mipmap.logo).into(holder.getImageView(R.id.companyLogo));
            int count = labelList.size() > 17 ? 17 : labelList.size();
            for (int i = 0; i < 5; i++) {
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


        String riskStr = "风控分：" + riskScore;
        holder.setText(R.id.riskScore, riskStr);
        holder.setText(R.id.userNumber, "已参加" + userNumber + "人");
        holder.setText(R.id.platformContent, content);
        holder.getView(R.id.checkPlatformDetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                WebViewActivity.starUi(mContext, platformUrl, "平台详情");
//                WebViewActivity.starUi(mContext, "", "平台详情");
                Intent intent = new Intent(mContext, PlatformDetailsActivity.class);
                intent.putExtra("platformId", ptpPlatformId);
                mContext.startActivity(intent);
            }
        });
    }

    private void disposeInvestmentBlue(RVBaseViewHolder holder, int position) {
        final TextView textView = holder.getTextView(R.id.openText);
        final LinearLayout projectDetailsLayout = (LinearLayout) holder.getView(R.id.projectDetailsLayout);
        projectDetailsLayout.setVisibility(View.GONE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (projectDetailsLayout.getVisibility() == View.GONE) {
                    projectDetailsLayout.setVisibility(View.VISIBLE);
                    textView.setText("点击收起");
                } else {
                    projectDetailsLayout.setVisibility(View.GONE);
                    textView.setText("点击展开详情");
                }
            }
        });
        holder.setText(R.id.schemeNumber, name);
        holder.setText(R.id.schemeNumber2, name);
        holder.setText(R.id.amount, "≥"+amount + "");
        holder.setText(R.id.term, term);
        holder.setText(R.id.accrual, accrual + "");
        holder.setText(R.id.redEnvelopes, redEnvelopes + "");
        holder.setText(R.id.reward, reward + "");
        holder.setText(R.id.totalIncome, totalIncome + "");
        holder.setText(R.id.avgAnnualRate, avgAnnualRate + "%");
        if (agencyPrice > 0) {
            holder.setText(R.id.proxyPrice, "代理价：" + agencyPrice);
        }
        holder.setText(R.id.detailsContentItem, description);

    }

    private void disposeFoot(RVBaseViewHolder holder, int position) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPtpPlatformId() {
        return ptpPlatformId;
    }

    public void setPtpPlatformId(int ptpPlatformId) {
        this.ptpPlatformId = ptpPlatformId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getAccrual() {
        return accrual;
    }

    public void setAccrual(int accrual) {
        this.accrual = accrual;
    }

    public int getRedEnvelopes() {
        return redEnvelopes;
    }

    public void setRedEnvelopes(int redEnvelopes) {
        this.redEnvelopes = redEnvelopes;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(int totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getAvgAnnualRate() {
        return avgAnnualRate;
    }

    public void setAvgAnnualRate(String avgAnnualRate) {
        this.avgAnnualRate = avgAnnualRate;
    }

    public int getAgencyPrice() {
        return agencyPrice;
    }

    public void setAgencyPrice(int agencyPrice) {
        this.agencyPrice = agencyPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
