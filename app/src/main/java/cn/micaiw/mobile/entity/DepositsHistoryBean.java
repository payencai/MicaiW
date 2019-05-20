package cn.micaiw.mobile.entity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.common.commonRv.base.RVBaseAdapter;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.common.view.MyDividerItemDecoration;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.ActivityManager;

/**
 * 作者：凌涛 on 2018/5/12 10:26
 * 邮箱：771548229@qq..com
 */
public class DepositsHistoryBean extends RVBaseCell implements Serializable {


    private int type;
    private Head mHead;
    private Context mContext;
    private List<Entity> mList = new ArrayList<>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Head getHead() {
        return mHead;
    }

    public void setHead(Head head) {
        mHead = head;
    }

    public List<Entity> getList() {
        return mList;
    }

    public void addList(Entity entity) {
        mList.add(entity);
    }

    public void remove(Entity entity) {
        mList.remove(entity);
    }

    public void remove(int index) {
        if (index >= mList.size()) {
            return;
        }
        mList.remove(index);
    }

    public DepositsHistoryBean() {
        super(null);
    }

    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view = null;
        if (type == 0) {//头布局
            view = LayoutInflater.from(mContext).inflate(R.layout.item_deposits_history_rv_head, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_deposits_history_rv_body, parent, false);
        }

        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {

        if (type == 0) {
            holder.setText(R.id.investmentMoney, mHead.totalInvestment + "");
            holder.setText(R.id.p2pTotalIncome, mHead.accumulativeFor + "");
            holder.getImageView(R.id.backImg).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityManager.getInstance().finishTopActivity();
                }
            });
//            if (mList.size() > 0) {
//                holder.getView(R.id.showP2ptitle).setVisibility(View.VISIBLE);
//            } else {
//                holder.getView(R.id.showP2ptitle).setVisibility(View.GONE);
//            }
        } else {

            RecyclerView recyclerView = (RecyclerView) holder.getView(R.id.itemRv);
            RVBaseAdapter<Entity> adapter = new RVBaseAdapter<Entity>() {
                @Override
                protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

                }
            };
            adapter.setData(mList);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(adapter);
        }

    }


    public static class Head {
        public double totalInvestment = 0.0d;
        public double accumulativeFor = 0.0d;

    }

    public static class Entity extends RVBaseCell {

        /**
         * id : 31
         * userName : 18312704429
         * createTime : 2018-05-18 15:17:03
         * total : 50000
         * score : 500
         * platformId : 6
         * platformName : 投哪网
         * alipayNo : 985871026@qq.com
         * alipayName : 小安
         * accountImg : null
         * investmentId : 9
         * investmentName : null
         * income : 307
         * reason : 数据有误！请重新提交~
         * adminName : admin
         * state : 3
         * processingTime : 2018-05-18 15:23:11
         * investmentAccount : 18312704429
         * wxqq : 2955984540
         * investmentScheme : {"id":9,"name":"投资方案1","ptpPlatformId":6,"amount":10000,"term":"90天","accrual":237,"redEnvelopes":30,"reward":40,"totalIncome":307,"avgAnnualRate":"12.3%","agencyPrice":5,"description":"","createTime":"2018-05-16 16:52:23"}
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
        private InvestmentSchemeBean investmentScheme;
        private String investmentSchemeString;

        public String getInvestmentSchemeString() {
            return investmentSchemeString;
        }

        public void setInvestmentSchemeString(String investmentSchemeString) {
            this.investmentSchemeString = investmentSchemeString;
        }

        public Entity() {
            super(null);
        }

        @Override
        public int getItemType() {
            return 0;
        }

        @Override
        public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deposits_history_rv_body_item, parent, false);
            return new RVBaseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RVBaseViewHolder holder, int position) {
            holder.setText(R.id.name, platformName);
            holder.setText(R.id.money, total + "元");
            if (investmentScheme != null) {
                holder.setText(R.id.days, investmentScheme.name);
            }
            holder.setText(R.id.returnMoney, "返现:" + income);
            String replace = createTime.replace(" ", ":");
            holder.setText(R.id.time, replace.split(":")[0]);
            if (!TextUtils.isEmpty(reason)) {
                holder.setText(R.id.returnMoneyState, reason);
            }
            holder.setText(R.id.userName, UserInfoSharedPre.getIntance(holder.getItemView().getContext()).getName());
            holder.setText(R.id.tel, investmentAccount);
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

        public InvestmentSchemeBean getInvestmentScheme() {
            return investmentScheme;
        }

        public void setInvestmentScheme(InvestmentSchemeBean investmentScheme) {
            this.investmentScheme = investmentScheme;
        }

        public static class InvestmentSchemeBean {
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
    }
}
