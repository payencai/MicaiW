package cn.micaiw.mobile.entity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.common.commonRv.base.RVBaseAdapter;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;

/**
 * 作者：凌涛 on 2018/5/19 15:00
 * 邮箱：771548229@qq..com
 */
public class InvitationBean extends RVBaseCell {

    private String name;
    private String account;
    private String totalAward;
    private List<Entity> mList = new ArrayList<>();
    public boolean isOpen = false;

    public void addList(Entity entity) {
        mList.add(entity);
    }

    public List<Entity> getList() {
        return mList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTotalAward() {
        return totalAward;
    }

    public void setTotalAward(String totalAward) {
        this.totalAward = totalAward;
    }

    public InvitationBean() {
        super(null);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invitation_rv_list, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {

        holder.setText(R.id.name, name);
        holder.setText(R.id.account, account);
        if (position == 0) {
            holder.setText(R.id.account, account);
        } else {
            String start = account.substring(0, 3);
            String end = account.substring(account.length() - 4, account.length());
            String show = start + "****" + end;
            holder.setText(R.id.account, show);
        }
        holder.setText(R.id.totalAward, totalAward);
        final RecyclerView recyclerView = (RecyclerView) holder.getView(R.id.invitationRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(holder.getItemView().getContext()));
        RVBaseAdapter<Entity> adapter = new RVBaseAdapter<Entity>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }
        };
        adapter.setData(mList);
        recyclerView.setAdapter(adapter);
        if (isOpen) {
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
        if (position != 0) {
            holder.getView(R.id.itemHead).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerView.getVisibility() == View.GONE) {
                        recyclerView.setVisibility(View.VISIBLE);
                        isOpen = true;
                    } else if (recyclerView.getVisibility() == View.VISIBLE) {
                        recyclerView.setVisibility(View.GONE);
                        isOpen = false;
                    }
                }
            });
        }

    }

    public static class Entity extends RVBaseCell {


        /**
         * platformName : 九态日添金货币
         * account : null
         * term : null
         * award : null
         * total : null
         * commitTime : 2018-05-30 16:08:41
         */

        private String platformName;
        private String account;
        private String term;
        private int award;
        private int total;
        private String commitTime;


        @Override
        public String toString() {
            return "Entity{" +
                    "platformName='" + platformName + '\'' +
                    ", account='" + account + '\'' +
                    ", term='" + term + '\'' +
                    ", award=" + award +
                    ", total=" + total +
                    ", commitTime='" + commitTime + '\'' +
                    '}';
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invitation_rv_item_layout, parent, false);
            return new RVBaseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RVBaseViewHolder holder, int position) {
            holder.setText(R.id.name, platformName);
            holder.setText(R.id.inject, total + "元");
            if (TextUtils.isEmpty(term)) {
                term = "0";
            }
            holder.setText(R.id.days, term + "");
//            holder.setText(R.id.value, "该用户总提成:" + award);
            holder.setText(R.id.value, "提成:" + award);
        }

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public int getAward() {
            return award;
        }

        public void setAward(int award) {
            this.award = award;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getCommitTime() {
            return commitTime;
        }

        public void setCommitTime(String commitTime) {
            this.commitTime = commitTime;
        }
    }

}
