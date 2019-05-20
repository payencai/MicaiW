package cn.micaiw.mobile.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.entity.P2pBean;

public class P2pItemAdapter extends BaseQuickAdapter<P2pBean, BaseViewHolder> {

    private List<String> labelList = new ArrayList<>();//标签
    private Context mContext;
    public P2pItemAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, P2pBean item) {
        //显示图片
        if (mContext == null) {
            mContext = helper.itemView.getContext();
        }
        labelList = Arrays.asList(item.getPlatformLabel().split(","));
        if (mContext != null) {
            Glide.with(mContext).load(item.getPlatformLogo())
                    .placeholder(R.mipmap.logo)
                    .error(R.mipmap.logo)
                    .into((ImageView) helper.getView(R.id.companyLogo));

        }
        for (int i = 0; i < labelList.size(); i++) {
            String value = labelList.get(i);
            if (i == 0) {
                helper.setText(R.id.text1, value);
            }
            if (i == 1) {
                helper.setText(R.id.text2, value);
                helper.getView(R.id.text2).setVisibility(View.VISIBLE);
            }
            if (i == 2) {
                helper.setText(R.id.text3, value);
                helper.getView(R.id.text3).setVisibility(View.VISIBLE);
            }
            if (i == 3) {
                helper.setText(R.id.text4, value);
                helper.getView(R.id.text4).setVisibility(View.VISIBLE);
            }
        }

        String avgAnnualRate=item.getAvgAnnualRate();
        String annualRate=item.getAnnualRate();
        if(avgAnnualRate.contains("%")){
            avgAnnualRate=avgAnnualRate.replace("%","");
        }
        if(annualRate.contains("%")){
            annualRate=avgAnnualRate.replace("%","");
        }
        double zhonghe=Double.parseDouble(annualRate.trim())+Double.parseDouble(avgAnnualRate.trim());
        String total=formatDouble(zhonghe);
        helper.setText(R.id.zhonghe,subZeroAndDot(total)+"%");
        helper.setText(R.id.bidPlan,avgAnnualRate+"%");
        helper.setText(R.id.text,annualRate+"%");
        helper.setText(R.id.riskScore,"风控风 : "+item.getRiskScore());
        helper.setText(R.id.userNumber,"已参加"+item.getUserNumber()+"人");
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
}
