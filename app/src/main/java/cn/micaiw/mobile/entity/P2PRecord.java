package cn.micaiw.mobile.entity;

import android.graphics.Color;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;

/**
 * 作者：凌涛 on 2018/5/18 15:30
 * 邮箱：771548229@qq..com
 */
public class P2PRecord extends RVBaseCell {

    /**
     * id : 30
     * userName : 18312704429
     * createTime : 2018-05-18 15:16:23
     * total : 36000
     * score : 360
     * platformId : 3
     * platformName : 小安科技（后台测试）
     * alipayNo : 985871026@qq.com
     * alipayName : 小安
     * accountImg : null
     * investmentId : 5
     * investmentName : null
     * income : 200
     * reason : null
     * adminName : admin
     * state : 2
     * processingTime : 2018-05-18 15:22:45
     * investmentAccount : 18312704429
     * wxqq : 985871026
     * investmentScheme : null
     */

    //credit
    private String cardType;
    private int creditId;
    private String creditName;
    private String idCard;
    private String keyword;
    private String name;
    private String openTime;
    private String telephone;
    private String userId;
    //finance
    private String account;
    private int financeId;
    private String financeName;
    private String financeType;

    //insurance

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public int getCreditId() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId = creditId;
    }

    public String getCreditName() {
        return creditName;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getFinanceId() {
        return financeId;
    }

    public void setFinanceId(int financeId) {
        this.financeId = financeId;
    }

    public String getFinanceName() {
        return financeName;
    }

    public void setFinanceName(String financeName) {
        this.financeName = financeName;
    }

    public String getFinanceType() {
        return financeType;
    }

    public void setFinanceType(String financeType) {
        this.financeType = financeType;
    }

    public int getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(int insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    private int insuranceId;
    private String insuranceName;
    private String insuranceType;

    //
    private int id;
    private String userName;
    private String createTime;
    private int total;
    private int score;
    private int platformId;
    private String platformName;
    private String alipayNo;
    private String alipayName;
    private String accountImg;
    private int investmentId;
    private String investmentName;
    private int income;
    private String reason;
    private String adminName;
    private int state;
    private String processingTime;
    private String investmentAccount;
    private String wxqq;
    private String investmentScheme;

    private int type;


    public P2PRecord() {
        super(null);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getAlipayNo() {
        return alipayNo;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo;
    }

    public String getAlipayName() {
        return alipayName;
    }

    public void setAlipayName(String alipayName) {
        this.alipayName = alipayName;
    }

    public String getAccountImg() {
        return accountImg;
    }

    public void setAccountImg(String accountImg) {
        this.accountImg = accountImg;
    }

    public int getInvestmentId() {
        return investmentId;
    }

    public void setInvestmentId(int investmentId) {
        this.investmentId = investmentId;
    }

    public String getInvestmentName() {
        return investmentName;
    }

    public void setInvestmentName(String investmentName) {
        this.investmentName = investmentName;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(String processingTime) {
        this.processingTime = processingTime;
    }

    public String getInvestmentAccount() {
        return investmentAccount;
    }

    public void setInvestmentAccount(String investmentAccount) {
        this.investmentAccount = investmentAccount;
    }

    public String getWxqq() {
        return wxqq;
    }

    public void setWxqq(String wxqq) {
        this.wxqq = wxqq;
    }

    public String getInvestmentScheme() {
        return investmentScheme;
    }

    public void setInvestmentScheme(String investmentScheme) {
        this.investmentScheme = investmentScheme;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_p2precord_rv_list, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        TextView stateText = holder.getTextView(R.id.stateText);
        TextView itemState = holder.getTextView(R.id.itemState);
        ImageView itemImg = holder.getImageView(R.id.itemImg);
        Log.d("onBindViewHolder", "onBindViewHolder: " + reason);
        if (type == 1) {
            stateText.setVisibility(View.VISIBLE);
            itemImg.setVisibility(View.GONE);
            stateText.setText("投入");
            stateText.setTextColor(Color.GREEN);
        } else if (type == 2) {
            stateText.setVisibility(View.GONE);
            itemImg.setVisibility(View.VISIBLE);
            Glide.with(holder.getItemView().getContext()).load(accountImg)
                    .placeholder(R.mipmap.logo)
                    .error(R.mipmap.logo)
                    .into(itemImg);
        } else if (type == 3) {
            stateText.setVisibility(View.VISIBLE);
            itemImg.setVisibility(View.GONE);
            if (state == 3) {
                stateText.setText("错误");
                stateText.setTextColor(Color.parseColor("#d3b321"));
                if (!TextUtils.isEmpty(reason)) {
                    itemState.setText(reason);
                    itemState.setTextColor(Color.parseColor("#d3b321"));
                }
            } else {
                stateText.setText("等待");
                stateText.setTextColor(Color.parseColor("#7ed321"));
                itemState.setTextColor(Color.GREEN);
                itemState.setText("");
            }
        }
        holder.setText(R.id.itemName, platformName);
        holder.setText(R.id.itemPump, total + "元");
        holder.setText(R.id.itemReturnMoney, "返现:" + income);
        String replace = createTime.replace(" ", ":");
        holder.setText(R.id.itemTime, replace.split(":")[0]);
        holder.setText(R.id.itemIntegral, "+" + score + "积分");
       if(type==5){
           itemImg.setVisibility(View.VISIBLE);
           Glide.with(holder.getItemView().getContext()).load(accountImg)
                   .placeholder(R.mipmap.logo)
                   .error(R.mipmap.logo)
                   .into(itemImg);
           TextView money= (TextView) holder.getView(R.id.itemPump);
           TextView itemTime= (TextView) holder.getView(R.id.itemTime);
           itemTime.setText(createTime.substring(0,10));
           money.setText("");
           TextView itemname= (TextView) holder.getView(R.id.itemName);
           itemname.setText(creditName);
       }
       if(type==4){
           itemImg.setVisibility(View.VISIBLE);
           Glide.with(holder.getItemView().getContext()).load(accountImg)
                   .placeholder(R.mipmap.logo)
                   .error(R.mipmap.logo)
                   .into(itemImg);
           TextView itemTime= (TextView) holder.getView(R.id.itemTime);
           itemTime.setText(createTime.substring(0,10));
           TextView itemname= (TextView) holder.getView(R.id.itemName);
           itemname.setText(financeName);
       }
       if(type==6){
           itemImg.setVisibility(View.VISIBLE);
           Glide.with(holder.getItemView().getContext()).load(accountImg)
                   .placeholder(R.mipmap.logo)
                   .error(R.mipmap.logo)
                   .into(itemImg);
           TextView itemTime= (TextView) holder.getView(R.id.itemTime);
           itemTime.setText(createTime.substring(0,10));
           TextView itemname= (TextView) holder.getView(R.id.itemName);
           itemname.setText(insuranceName);
       }

    }
}
