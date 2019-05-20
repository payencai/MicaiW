package cn.micaiw.mobile.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.nohttp.Binary;
import com.nohttp.BitmapBinary;
import com.nohttp.NoHttp;
import com.nohttp.RequestMethod;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.common.commonRv.base.RVBaseAdapter;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.custom.KyLoadingBuilder;
import cn.micaiw.mobile.entity.UserInfo;
import cn.micaiw.mobile.entity.UserInfoItemBean;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.retrofit2_request.ApiService;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.ActivityManager;
import cn.micaiw.mobile.util.CustomPopWindow;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;
import cn.micaiw.mobile.util.UploadUtil;
import cn.nohttp.sample.HttpListener;
import cn.nohttp.sample.HttpResponseListener;
import cn.nohttp.tools.HttpJsonClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

public class UserInfoDetailsActivity extends BaseActivity {

    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.messageImg)
    ImageView messageImg;
    @BindView(R.id.userInfoRv)
    RecyclerView userInfoRv;
    @BindView(R.id.imgHead)
    ImageView imgHead;
    private RVBaseAdapter<UserInfoItemBean> mAdapter;

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_READ = 2;
    private static final int REQUEST_CODE_CLIP = 3;
    private static final int REQUEST_CODE_ACCREDIT = 4;


    private List<UserInfoItemBean> mList = new ArrayList<>();
    private UserInfo mUserInfo;
    //裁剪后的图片
    private File mTempImg;

    //拍照存储的图片
    private File mCameraTempImg;
    private KyLoadingBuilder mLoadView;
    private ImageView headImg;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        if (ContextCompat.checkSelfPermission(UserInfoDetailsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserInfoDetailsActivity.this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ACCREDIT);
        } else {
            initFile();
        }

        title.setText("个人信息");
        backImg.setVisibility(View.VISIBLE);
        backImg.setImageResource(R.mipmap.back);
        messageImg.setVisibility(View.GONE);
        mUserInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");
        initData();
        initUserInfoRv();
    }

    //初始化文件
    private void initFile() {
        String pathname = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "micai";
        Log.d("initFile", "initFile: " + pathname);
        File file = new File(pathname);
        if (!file.exists()) {
            file.mkdirs();
        }
        mCameraTempImg = new File(pathname + File.separator + "micaiw_camera_head.png");
        mTempImg = new File(pathname + File.separator + "micaiw_clip_head.png");
        if (!mCameraTempImg.exists() || !mTempImg.exists()) {
            try {
                mCameraTempImg.createNewFile();
                mTempImg.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void initData() {
        mList.clear();
        String avatar = mUserInfo.getAvatar();
        Log.d("initData", "initData: " + avatar);
        UserInfoItemBean headBean = new UserInfoItemBean("头像", mUserInfo.getAvatar(), "");
        mList.add(headBean);
        mList.add(new UserInfoItemBean("昵称", null, mUserInfo.getName()));
        mList.add(new UserInfoItemBean("账号", null, mUserInfo.getUserName()));
        mList.add(new UserInfoItemBean("找回登录密码", null, ""));
        mList.add(new UserInfoItemBean("修改登录密码", null, ""));
        mList.add(new UserInfoItemBean("设置", null, ""));
    }

    private void initUserInfoRv() {
        mAdapter = new RVBaseAdapter<UserInfoItemBean>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }

            @Override
            protected void onClick(RVBaseViewHolder holder, int position) {
                UserInfoItemBean userInfoItemBean = mData.get(position);
                final String propertyContent = userInfoItemBean.getPropertyContent();
                if (position == 0) {
                    headImg = holder.getImageView(R.id.propertyImg);
                    holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showImgSelect(view);
                        }
                    });
                }
                if (position == 1) {
                    holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View nameEditView = LayoutInflater.from(UserInfoDetailsActivity.this).inflate(R.layout.pw_name_edit_layout, null);
                            CustomPopWindow.PopupWindowBuilder builder = new CustomPopWindow.PopupWindowBuilder(UserInfoDetailsActivity.this);
                            builder.setOutsideTouchable(true)
                                    .setView(nameEditView)
                                    .sizeByPercentage(UserInfoDetailsActivity.this, 0.6f, 0.18f)
                                    .enableBackgroundDark(true)
                                    .setBgDarkAlpha(0.5f);
                            CustomPopWindow customPopWindow = builder.create();
                            customPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                            handlerNameEditView(nameEditView, propertyContent, customPopWindow);
                        }

                        private void handlerNameEditView(View view, String string, final CustomPopWindow customPopWindow) {
                            final EditText nickname = (EditText) view.findViewById(R.id.nicknameEdit);
                            if (!TextUtils.isEmpty(string)) {
                                nickname.setText(string);
                            }
                            view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nickname.setText("");
                                }
                            });
                            view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (customPopWindow != null) {
                                        customPopWindow.dissmiss();
                                    }
                                }
                            });
                            view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String s = nickname.getEditableText().toString();
                                    if (TextUtils.isEmpty(s)) {
                                        ToaskUtil.showToast(UserInfoDetailsActivity.this, "昵称不能为空");
                                        return;
                                    }
                                    mUserInfo.setName(s);
                                    initData();
                                    Map<String, Object> params = new HashMap<>();
                                    UserInfoSharedPre intance = UserInfoSharedPre.getIntance(UserInfoDetailsActivity.this);
                                    int id = intance.getUserId();
                                    params.put("name", s);
                                    params.put("id", id);
                                    updateUserInfo(params);
                                    mAdapter.resetData(mList);
                                    if (customPopWindow != null) {
                                        customPopWindow.dissmiss();
                                    }
                                }
                            });
                        }

                    });
                }
                if (position == 3) {//找回密码
                    holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(UserInfoDetailsActivity.this, RetrievePswActivity.class);
                            intent.putExtra("tel", mUserInfo.getUserName());
//                            startActivity(intent);
                            startActivityForResult(intent, 0);
                        }
                    });
                }
                if (position == 4) {//修改登录密码
                    final String userName = UserInfoSharedPre.getIntance(UserInfoDetailsActivity.this).getUserName();
                    if (TextUtils.isEmpty(userName)) {
                        ToaskUtil.showToast(UserInfoDetailsActivity.this, "您未登录");
                        return;
                    }
                    holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(UserInfoDetailsActivity.this, AmendLoginPswActivity.class);
                            intent.putExtra("tel", userName);
                            startActivity(intent);
                        }
                    });
                }
                if (position == 5) {//设置
                    holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(UserInfoDetailsActivity.this, SettingActivity.class));
                        }
                    });
                }
            }
        };
        mAdapter.setData(mList);
        userInfoRv.setLayoutManager(new LinearLayoutManager(this));
        userInfoRv.setAdapter(mAdapter);
    }

    @OnClick({R.id.backImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
        }
    }

    private void updateUserInfo(Map<String, Object> paramsMap) {
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
        int id = intance.getUserId();
        String token = intance.getToken();
        if (id == -1 || TextUtils.isEmpty(token)) {
            ToaskUtil.showToast(this, "未登录");
            return;
        }
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        HttpProxy.obtain().post(PlatformContans.User.sUpdateUser, tokenMap, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                MLog.log("sUpdateUser", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    ToaskUtil.showToast(UserInfoDetailsActivity.this, message);
                    if (resultCode == 0) {
                        EventBus.getDefault().post("更新用户信息");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                ToaskUtil.showToast(UserInfoDetailsActivity.this, "请检查网络");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 0) {
                UserInfoSharedPre.getIntance(this).clearUserInfo();
                ActivityManager.getInstance().finishAllActivity();
                startActivity(new Intent(this, LoginActivity.class));
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    if (mCameraTempImg != null) {
                        Uri uri = Uri.fromFile(mCameraTempImg);
                        startPhotoZoom(uri, 512);
                    }
                    break;
                case REQUEST_CODE_READ:
                    if (data != null) {
                        Uri uri = data.getData();
                        startPhotoZoom(uri, 512);
                    }
                    break;
                case REQUEST_CODE_CLIP:
                    if (data != null) {
                        if (mTempImg != null) {
                            String tempImgAbsolutePath = mTempImg.getAbsolutePath();
                            Bitmap bitmap = BitmapFactory.decodeFile(tempImgAbsolutePath);
                            if (bitmap != null) {
                                if (headImg != null) {
                                    headImg.setImageBitmap(bitmap);
                                }
                            }
                            //上传服务器
                            UserInfoSharedPre intance = UserInfoSharedPre.getIntance(UserInfoDetailsActivity.this);
                            int id = intance.getUserId();
                            if (id == -1) {
                                ToaskUtil.showToast(this, "未登录");
                                return;
                            }
                            mLoadView = openLoadView("请稍等...");
                            upImage(PlatformContans.Image.sUploadImage, tempImgAbsolutePath, id);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                camera();
            } else {
                // Permission Denied
                Toast.makeText(this, "未授权", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (requestCode == REQUEST_CODE_READ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                photos();
            } else {
                // Permission Denied
                Toast.makeText(this, "未授权", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (requestCode == REQUEST_CODE_ACCREDIT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initFile();
            } else {
                // Permission Denied
                finish();
            }
            return;
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void showImgSelect(View view) {
        View photoSelector = LayoutInflater.from(this).inflate(R.layout.pw_photo_selector, null);
        CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(this).setView(photoSelector)
//                .sizeByPercentage(this, 0.96f, 0.25f)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableBackgroundDark(true)
                .setOutsideTouchable(true)
                .setBgDarkAlpha(0.5f)
                .create();
        customPopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        handlerPhotoSelector(photoSelector, customPopWindow);
    }

    private void handlerPhotoSelector(View view, final CustomPopWindow customPopWindow) {
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customPopWindow != null) {
                    customPopWindow.dissmiss();
                }
            }
        });
        view.findViewById(R.id.photoSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customPopWindow != null) {
                    customPopWindow.dissmiss();
                }
//                if (ContextCompat.checkSelfPermission(UserInfoDetailsActivity.this,
//                        Manifest.permission.READ_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(UserInfoDetailsActivity.this,
//                            new String[]{
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                    Manifest.permission.READ_EXTERNAL_STORAGE},
//                            REQUEST_CODE_READ);
//                } else {
//                }
                photos();

            }
        });
        view.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customPopWindow != null) {
                    customPopWindow.dissmiss();
                }
                if (ContextCompat.checkSelfPermission(UserInfoDetailsActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserInfoDetailsActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CODE_CAMERA);
                } else {
                    camera();
                }
            }
        });

    }

    /**
     * 照相机
     */
    private void camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraTempImg));
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
//        try {
//            String pathname = Environment.getExternalStorageState() + File.separator + "micai";
//            File file = new File(pathname);
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//            mCameraTempImg = new File(pathname, "micaiw_camera_head.png");
//            if (!mCameraTempImg.exists()) {
//                boolean b = mCameraTempImg.createNewFile();
//                if (b) {
//
//                }
//            }
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraTempImg));
//            startActivityForResult(intent, REQUEST_CODE_CAMERA);
//        } catch (IOException e) {
//        }
    }

    /**
     * 图片选择
     */
    private void photos() {
        Intent getImage = new Intent(Intent.ACTION_PICK, null);
        getImage.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//这是图片类型
        startActivityForResult(getImage, REQUEST_CODE_READ);
    }


    //系统裁剪：
    public void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
        intent.putExtra("outputX", size);//图片输出大小
        intent.putExtra("outputY", size);
        intent.putExtra("output", Uri.fromFile(mTempImg));
        intent.putExtra("outputFormat", "jpg");// 返回格式
        startActivityForResult(intent, REQUEST_CODE_CLIP);
    }

    private void upImage(String url, String filePath, final int id) {
        OkHttpClient mOkHttpClent = new OkHttpClient();
        File file = new File(filePath);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image",
                        RequestBody.create(MediaType.parse("image/png"), file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = mOkHttpClent.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mLoadView != null) {
                            mLoadView.dismiss();
                        }
                        Toast.makeText(UserInfoDetailsActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.d("lingtaoshiwo", "onResponse: " + string);
                try {
                    JSONObject object = new JSONObject(string);
                    int resultCode = object.getInt("resultCode");
                    final String data = object.getString("data");
                    if (resultCode == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mLoadView != null) {
                                    mLoadView.dismiss();
                                }
                                Map<String, Object> params = new HashMap<>();
                                params.put("id", id);
                                params.put("avatar", data);
                                updateUserInfo(params);
                                Toast.makeText(UserInfoDetailsActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mLoadView != null) {
                                    mLoadView.dismiss();
                                }
                                Toast.makeText(UserInfoDetailsActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    if (mLoadView != null) {
                        mLoadView.dismiss();
                    }
                    e.printStackTrace();
                }

            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
