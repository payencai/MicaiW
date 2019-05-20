package cn.micaiw.mobile.entity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;

/**
 * 作者：凌涛 on 2018/5/21 14:02
 * 邮箱：771548229@qq..com
 * 投资记录
 */
public class InvestmentRecordBean extends RVBaseCell {


    /**
     * id : 33
     * userName : 17688947788
     * createTime : 2018-05-19 15:31:14
     * total : 3600
     * score : 36
     * platformId : 6
     * platformName : 投哪网
     * alipayNo : 17688947788
     * alipayName : 燃哥
     * accountImg : null
     * investmentId : 9
     * investmentName : null
     * income : 307
     * reason : null
     * adminName : admin
     * state : 2
     * processingTime : 2018-05-19 15:57:15
     * investmentAccount : 17688947788
     * wxqq : hghfg
     * investmentScheme : null
     */

    private int id;
    private String userName;
    private String createTime;
    private int total;
    private int score;
    private int platformId;
    private String platformName;
    private String alipayNo;
    private String alipayName;
    private Object accountImg;
    private int investmentId;
    private Object investmentName;
    private int income;
    private Object reason;
    private String adminName;
    private int state;
    private String processingTime;
    private String investmentAccount;
    private String wxqq;
    private Object investmentScheme;

    public InvestmentRecordBean() {
        super(null);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_p2pdetailsbean_investment_record_rv_layout, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        String replace = createTime.replace(" ", ":");
        holder.setText(R.id.time, replace.split(":")[0]);

        String startStr = userName.substring(0, 3);
        String endStr = userName.substring(userName.length() - 2, userName.length());
        String showString = startStr + "......" + endStr;
        holder.setText(R.id.phoneNumber, showString);
        holder.setText(R.id.money, total + "元");
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

    public Object getAccountImg() {
        return accountImg;
    }

    public void setAccountImg(Object accountImg) {
        this.accountImg = accountImg;
    }

    public int getInvestmentId() {
        return investmentId;
    }

    public void setInvestmentId(int investmentId) {
        this.investmentId = investmentId;
    }

    public Object getInvestmentName() {
        return investmentName;
    }

    public void setInvestmentName(Object investmentName) {
        this.investmentName = investmentName;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public Object getReason() {
        return reason;
    }

    public void setReason(Object reason) {
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

    public Object getInvestmentScheme() {
        return investmentScheme;
    }

    public void setInvestmentScheme(Object investmentScheme) {
        this.investmentScheme = investmentScheme;
    }
}
