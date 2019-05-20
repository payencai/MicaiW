package cn.micaiw.mobile.common.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.common.entity.ContactInformEntity;
import cn.micaiw.mobile.common.util.EventBus;
import cn.micaiw.mobile.common.util.PhoneUtil;

/**
 * Created by ckerv on 2018/2/7.
 */

public class EditContactInformActivity extends BaseActivity {

    @BindView(R.id.titlebar_tv_title)
    TextView mTvTitle;
//    @BindView(R.id.titlebar_tv_right)
//    TextView mTvRight;

    @BindView(R.id.inform_et_name)
    EditText mEtName;
    @BindView(R.id.inform_et_phone)
    EditText mEtPhone;
    @BindView(R.id.inform_tv_region)
    TextView mTvRegion;
    @BindView(R.id.inform_et_detail_address)
    EditText mEtDetailAddr;

    private ContactInformEntity contactInformEntity;

    private static final int TYPE_NORMAL = 0;
    private int mStartType = TYPE_NORMAL;

    public static void startActivity(Context context, int startType) {
        Intent intent = new Intent(context, EditContactInformActivity.class);
        intent.putExtra("startType", startType);
        context.startActivity(intent);
    }



    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mStartType = getIntent().getIntExtra("startType", TYPE_NORMAL);
        initTitleBar();
        initView();
    }

    private void initView() {
        contactInformEntity = (ContactInformEntity) getIntent().getSerializableExtra("contactEntity");
        if(contactInformEntity != null) {
            mEtName.setText(contactInformEntity.name);
            mEtPhone.setText(contactInformEntity.phone);
            mTvRegion.setText(contactInformEntity.province + contactInformEntity.city + contactInformEntity.district);
            mTvRegion.setTextColor(Color.parseColor("#333333"));
            mEtDetailAddr.setText(contactInformEntity.detail);
        }
    }

    private void initTitleBar() {
        mTvTitle.setText("填写联系信息");
//        mTvRight.setText("完成");
    }

    @OnClick({R.id.titlebar_btn_left, /*R.id.titlebar_tv_right,*/ R.id.inform_rl_region})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_btn_left:
                setResult(RESULT_CANCELED, new Intent());
                finish();
                break;
            case R.id.inform_rl_region:
                startChooseAddressWebActivity();
                break;
//            case R.id.titlebar_tv_right:
//                finishEdit();
            default:
                break;
        }
    }

    private void finishEdit() {
        if(TextUtils.isEmpty(mEtName.getText().toString())) {
            showToast("联系人姓名不能为空");
        } else if(TextUtils.isEmpty(mEtPhone.getText().toString())) {
            showToast("联系方式不能为空");
        } else if(!PhoneUtil.isPhoneNumber(mEtPhone.getText().toString())) {
            showToast("请输入正确的手机号码");
        } else if(TextUtils.isEmpty(mTvRegion.getText().toString()) || "请选择".equals(mTvRegion.getText().toString())) {
            showToast("请选择联系地址");
        } else if(TextUtils.isEmpty(mEtDetailAddr.getText().toString())) {
            showToast("详细地址不能为空");
        } else {
            postBackDatas();
        }
    }

    private void postBackDatas() {
        contactInformEntity.detail = mEtDetailAddr.getText().toString();
        contactInformEntity.name = mEtName.getText().toString();
        contactInformEntity.phone = mEtPhone.getText().toString();
        if(mStartType == TYPE_NORMAL) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("contactEntity", contactInformEntity);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        } else {
//            EventBus.getInstance().post(new ChooseAddressEvent(mStartType, contactInformEntity));
        }
        finish();
    }

    private void startChooseAddressWebActivity() {
//        Intent intent = new Intent(EditContactInformActivity.this, ChooseAddressWebActivity.class);
//        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            contactInformEntity = (ContactInformEntity) data.getSerializableExtra("contactEntity");
            if (contactInformEntity != null) {
                if (contactInformEntity.detail != null) {
                    mTvRegion.setTextColor(Color.parseColor("#333333"));
                    mTvRegion.setText(contactInformEntity.province + contactInformEntity.city + contactInformEntity.district);
                    mEtDetailAddr.setText(contactInformEntity.detail);
                }
            }
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_edit_contact_inform;
    }
}
