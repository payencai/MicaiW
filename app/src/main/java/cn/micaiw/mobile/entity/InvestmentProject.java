package cn.micaiw.mobile.entity;

import android.view.ViewGroup;

import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;

/**
 * 作者：凌涛 on 2018/5/21 11:09
 * 邮箱：771548229@qq..com
 */
public class InvestmentProject extends RVBaseCell {

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
    private int id;
    private String name;
    private int ptpPlatformId;
    private int amount;
    private String term;
    private int accrual;
    private int redEnvelopes;
    private int reward;
    private int totalIncome;
    private String avgAnnualRate;
    private int agencyPrice;
    private String description;
    private String createTime;

    public InvestmentProject() {
        super(null);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {

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
