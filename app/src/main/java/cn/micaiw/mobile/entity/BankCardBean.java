package cn.micaiw.mobile.entity;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.common.commonRv.base.RVBaseCell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.ToaskUtil;

/**
 * 作者：凌涛 on 2018/5/14 10:30
 * 邮箱：771548229@qq..com
 */
public class BankCardBean extends RVBaseCell {


    /**
     * id : 3
     * userId : 1
     * createTime : 2018-05-19 19:21:54
     * account : 6217003420000934100
     * bankName : 中国建设银行
     * telephone : 18878554275
     * bankImg : CCB
     */

    private int id;
    private int userId;
    private String createTime;
    private String account;
    private String bankName;
    private String telephone;
    private String bankImg ;
    public boolean isBankCard = false;

    public BankCardBean() {
        super(null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getBankImg() {
        return bankImg;
    }

    public void setBankImg(String bankImg) {
        this.bankImg = bankImg;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_backcard_rv, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        ConstraintLayout item = (ConstraintLayout) holder.getView(R.id.item);

        ImageView logoImg = holder.getImageView(R.id.logoImg);
        final Context context = holder.getItemView().getContext();
//        requestBankImg(context, bankImg, logoImg);
        Glide.with(context).load(bankImg).placeholder(R.mipmap.logo).error(R.mipmap.logo).into(logoImg);
        holder.setText(R.id.name, bankName);
        if (isBankCard) {
            item.setBackgroundResource(R.drawable.shape_rectangle_circle_bank);
            String account = this.account;
            String one = account.substring(0, 4);
            String two = account.substring(4, 8);
            String three = account.substring(8, 12);
            String four = account.substring(12, 16);
            String end = account.substring(16, account.length());
            String showText = one + " " + two + " " + three + " " + four + " " + end;
            holder.setText(R.id.cardNumber, showText);
        } else {
            item.setBackgroundResource(R.drawable.shape_rectangle_circle_alipay);
            holder.setText(R.id.cardNumber, account);
        }

    }

    private void requestBankImg(Context context, String tag, ImageView bankLogo) {
        String url = "http://120.78.77.138:8080/bank/" + tag + ".png";
        Glide.with(context).load(url).placeholder(R.mipmap.logo).error(R.mipmap.logo).into(bankLogo);

    }


}
