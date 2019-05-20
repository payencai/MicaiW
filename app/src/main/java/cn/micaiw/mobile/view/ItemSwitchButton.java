package cn.micaiw.mobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.micaiw.mobile.R;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class ItemSwitchButton extends LinearLayout {

    private TextView swicthBtn1;
    private TextView swicthBtn2;
    private TextView swicthBtn3;
    private OnSwicthClickListener onSwicthClickListener;

    public ItemSwitchButton(Context context) {
        super(context);
        init();
    }

    public ItemSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.item_switch_btn, this, true);
        swicthBtn1 = (TextView) view.findViewById(R.id.item_swicth_btn_1);
        swicthBtn2 = (TextView) view.findViewById(R.id.item_swicth_btn_2);
        swicthBtn3 = (TextView) view.findViewById(R.id.item_swicth_btn_3);
        swicthBtn1.setOnClickListener(clickListener);
        swicthBtn2.setOnClickListener(clickListener);
        swicthBtn3.setOnClickListener(clickListener);
    }

    public void setSwitchBtnText(String leftText,String centerText,String rightText) {
        swicthBtn1.setText(leftText);
        swicthBtn2.setText(centerText);
        swicthBtn3.setText(rightText);
    }

    public void setSelectIndex(CurrentSelectedIndex index){
        switch (index) {
            case LEFT:
                changeBtnState(swicthBtn1.getId());
                break;
            case CENTER:
                changeBtnState(swicthBtn2.getId());
                break;
            case RIGHT:
                changeBtnState(swicthBtn3.getId());
                break;
        }
    }

    public void setSwicthBtnClickListener(OnSwicthClickListener l) {
        onSwicthClickListener = l;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (onSwicthClickListener != null) {
                changeBtnState(v.getId());
            }
        }
    };

    private void changeBtnState(int id) {
        if (id == R.id.item_swicth_btn_1) {
            String scoreString = swicthBtn1.getText().toString().trim();
            onSwicthClickListener.onSwicthClick(scoreString);
            swicthBtn1.setTextColor(0xFFFFFFFF);
            swicthBtn2.setTextColor(0xFF4A87ED);
            swicthBtn3.setTextColor(0xFF4A87ED);
            swicthBtn1.setBackgroundResource(R.mipmap.common_segmentedcontrol_left_on);
            swicthBtn2.setBackgroundResource(R.mipmap.common_segmentedcontrol_center_off);
            swicthBtn3.setBackgroundResource(R.mipmap.common_segmentedcontrol_right_off);
        } else if (id == R.id.item_swicth_btn_2) {
            String scoreString = swicthBtn2.getText().toString().trim();
            onSwicthClickListener.onSwicthClick(scoreString);
            swicthBtn1.setTextColor(0xFF4A87ED);
            swicthBtn2.setTextColor(0xFFFFFFFF);
            swicthBtn3.setTextColor(0xFF4A87ED);
            swicthBtn1.setBackgroundResource(R.mipmap.common_segmentedcontrol_left_off);
            swicthBtn2.setBackgroundResource(R.mipmap.common_segmentedcontrol_center_on);
            swicthBtn3.setBackgroundResource(R.mipmap.common_segmentedcontrol_right_off);
        } else if (id == R.id.item_swicth_btn_3) {
            String scoreString = swicthBtn3.getText().toString().trim();
            onSwicthClickListener.onSwicthClick(scoreString);
            swicthBtn1.setTextColor(0xFF4A87ED);
            swicthBtn2.setTextColor(0xFF4A87ED);
            swicthBtn3.setTextColor(0xFFFFFFFF);
            swicthBtn1.setBackgroundResource(R.mipmap.common_segmentedcontrol_left_off);
            swicthBtn2.setBackgroundResource(R.mipmap.common_segmentedcontrol_center_off);
            swicthBtn3.setBackgroundResource(R.mipmap.common_segmentedcontrol_right_on);
        }
    }

    public interface OnSwicthClickListener {
        public void onSwicthClick(String target);
    }

    public enum CurrentSelectedIndex{
        LEFT,RIGHT,CENTER;
    }
}
