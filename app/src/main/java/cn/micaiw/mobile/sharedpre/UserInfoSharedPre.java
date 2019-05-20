package cn.micaiw.mobile.sharedpre;

import android.content.Context;
import android.content.SharedPreferences;

import cn.micaiw.mobile.entity.UserInfo;

/**
 * 作者：凌涛 on 2018/5/16 10:03
 * 邮箱：771548229@qq..com
 */
public class UserInfoSharedPre {

    private static UserInfoSharedPre sIntance;
    private static Context mContext;
    private SharedPreferences mPreferences;


    private UserInfoSharedPre() {
        mPreferences = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    public static UserInfoSharedPre getIntance(Context context) {
        if (sIntance == null) {
            synchronized (UserInfoSharedPre.class) {
                if (sIntance == null) {
                    mContext = context.getApplicationContext();
                    sIntance = new UserInfoSharedPre();
                }
            }
        }
        return sIntance;
    }

    //标记用户登录状态
    public void setUserIsLogin(boolean b) {
        mPreferences.edit().putBoolean("isLogin", b).commit();
    }

    public boolean getUserIsLogin() {
        return mPreferences.getBoolean("isLogin", false);
    }

    public void saveUserInfo(UserInfo userInfo, boolean isSavePassword) {
        saveUserId(userInfo.getId());
        saveName(userInfo.getName());
        saveUserName(userInfo.getUserName());
        if (isSavePassword) {
            savePassword(userInfo.getPassword());
        }
        saveOpenId(userInfo.getOpenId());
        saveQQId(userInfo.getQqId());
        saveCreateTime(userInfo.getCreateTime());
        saveEndLoginTime(userInfo.getEndLoginTime());
        saveState(userInfo.getState());
        saveAvatar(userInfo.getAvatar());
        saveAvatarKey(userInfo.getAvatarKey());
        saveToken(userInfo.getToken());
        saveWxId(userInfo.getWxId());
        saveQQ(userInfo.getQq());
        saveLastLoginIp(userInfo.getLastLoginIp());
        saveIp(userInfo.getIp());
        saveLastSignTime(userInfo.getLastSignTime());
        saveSignState(userInfo.getSignState());
        saveContinueSign(userInfo.getContinueSign());
        saveAgencyAccount(userInfo.getAgencyAccount());
        saveIosDownloadPath(userInfo.getIosDownloadPath());
    }

    public void clearUserInfo() {
//        SharedPreferences.Editor editor = mPreferences.edit();
//        editor.clear();
//        editor.commit();
        saveUserId(-1);
        saveName("");
        saveUserName("");

        saveOpenId("");
        saveQQId("");
        saveCreateTime("");
        saveEndLoginTime("");
        saveState(-1);
        saveAvatar("");
        saveAvatarKey("");
        saveToken("");
        saveWxId("");
        saveQQ("");
        saveLastLoginIp("");
        saveIp("");
        saveLastSignTime("");
        saveSignState(-1);
        saveContinueSign(-1);
        saveAgencyAccount("");
        saveIosDownloadPath("");
    }

    public int getUserId() {
        return mPreferences.getInt("id", -1);
    }

    public void saveUserId(int id) {
        mPreferences.edit().putInt("id", id).commit();
    }

    public String getName() {
        return mPreferences.getString("name", "");
    }

    public void saveName(String name) {
        mPreferences.edit().putString("name", name).commit();
    }

    public String getUserName() {
        return mPreferences.getString("userName", "");
    }

    public void saveUserName(String userName) {
        mPreferences.edit().putString("userName", userName).commit();
    }

    public String getPassword() {
        return mPreferences.getString("password", "");

    }

    public void savePassword(String password) {
        mPreferences.edit().putString("password", password).commit();
    }

    public String getOpenId() {
        return mPreferences.getString("openId", "");
    }

    public void saveOpenId(String openId) {
        mPreferences.edit().putString("openId", openId).commit();
    }

    public String getQQId() {
        return mPreferences.getString("qqId", "");
    }

    public void saveQQId(String qqId) {
        mPreferences.edit().putString("qqId", qqId).commit();
    }

    public String getCreateTime() {
        return mPreferences.getString("createTime", "");
    }

    public void saveCreateTime(String createTime) {
        mPreferences.edit().putString("createTime", createTime).commit();
    }

    public String getEndLoginTime() {
        return mPreferences.getString("endLoginTime", "");
    }

    public void saveEndLoginTime(String endLoginTime) {
        mPreferences.edit().putString("endLoginTime", endLoginTime).commit();
    }

    public int getState() {
        return mPreferences.getInt("state", -1);
    }

    public void saveState(int state) {
        mPreferences.edit().putInt("state", state).commit();
    }

    public String getAvatar() {
        return mPreferences.getString("avatar", "");
    }

    public void saveAvatar(String avatar) {
        mPreferences.edit().putString("avatar", avatar).commit();
    }

    public String getAvatarKey() {
        return mPreferences.getString("avatarKey", "");
    }

    public void saveAvatarKey(String avatarKey) {
        mPreferences.edit().putString("avatarKey", avatarKey).commit();
    }

    public String getToken() {
        return mPreferences.getString("token", "");
    }

    public void saveToken(String token) {
        mPreferences.edit().putString("token", token).commit();
    }

    public String getWxId() {
        return mPreferences.getString("wxId", "");
    }

    public void saveWxId(String wxId) {
        mPreferences.edit().putString("wxId", wxId).commit();
    }

    public String getQQ() {
        return mPreferences.getString("qq", "");
    }

    public void saveQQ(String qq) {
        mPreferences.edit().putString("qq", qq).commit();
    }

    public String getLastLoginIp() {
        return mPreferences.getString("lastLoginIp", "");

    }

    public void saveLastLoginIp(String lastLoginIp) {
        mPreferences.edit().putString("lastLoginIp", lastLoginIp).commit();
    }

    public String getIp() {
        return mPreferences.getString("ip", "");
    }

    public void saveIp(String ip) {
        mPreferences.edit().putString("ip", ip).commit();
    }

    public String getLastSignTime() {
        return mPreferences.getString("lastSignTime", "");
    }

    public void saveLastSignTime(String lastSignTime) {
        mPreferences.edit().putString("lastSignTime", lastSignTime).commit();
    }

    public int getSignState() {
        return mPreferences.getInt("signState", -1);
    }

    public void saveSignState(int signState) {
        mPreferences.edit().putInt("signState", signState).commit();
    }

    public int getContinueSign() {
        return mPreferences.getInt("continueSign", -1);
    }

    public void saveContinueSign(int continueSign) {
        mPreferences.edit().putInt("continueSign", continueSign).commit();
    }

    public String getAgencyAccount() {
        return mPreferences.getString("agencyAccount", "");
    }

    public void saveAgencyAccount(String agencyAccount) {
        mPreferences.edit().putString("agencyAccount", agencyAccount).commit();
    }

    public String getIosDownloadPath() {
        return mPreferences.getString("IosDownloadPath", "");
    }

    public void saveIosDownloadPath(String agencyAccount) {
        mPreferences.edit().putString("IosDownloadPath", agencyAccount).commit();
    }



}
