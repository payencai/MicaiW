package cn.micaiw.mobile.entity;

import java.io.Serializable;

/**
 * 作者：凌涛 on 2018/5/15 19:11
 * 邮箱：771548229@qq..com
 */
public class UserInfo implements Serializable {


    /**
     * id : 7
     * name : MIC18878554275
     * userName : 18878554275
     * password : null
     * openId : null
     * qqId : null
     * createTime : 2018-05-15 18:06:41
     * endLoginTime : 2018-05-15 19:05:41
     * state : 1
     * avatar : null
     * avatarKey : null
     * token : eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE1MjYzODIzNDEsInN1YiI6IntcInNpZ25TdGF0ZVwiOjAsXCJzdGF0ZVwiOjEsXCJ1c2VySWRcIjo3LFwidXNlck5hbWVcIjpcIjE4ODc4NTU0Mjc1XCJ9IiwiZXhwIjoxNTI2Mzg1OTQxfQ.HTtACLr2strOBVTCMMgS1JUrRmbmF3jBjwYND0PrVj0
     * wxId : null
     * qq : null
     * lastLoginIp : 112.96.109.37
     * ip : 183.36.81.182
     * lastSignTime : null
     * signState : 0
     * continueSign : 0
     * agencyAccount : null
     */

    private int id;
    private String name;
    private String userName;
    private String password;
    private String openId;
    private String qqId;
    private String createTime;
    private String endLoginTime;
    private int state;
    private String avatar;
    private String avatarKey;
    private String token;
    private String wxId;
    private String qq;
    private String lastLoginIp;
    private String ip;
    private String lastSignTime;
    private int signState;
    private int continueSign;
    private String agencyAccount;
    private boolean isLogin;
    private String iosDownloadPath;

    public String getIosDownloadPath() {
        return iosDownloadPath;
    }

    public void setIosDownloadPath(String iosDownloadPath) {
        this.iosDownloadPath = iosDownloadPath;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndLoginTime() {
        return endLoginTime;
    }

    public void setEndLoginTime(String endLoginTime) {
        this.endLoginTime = endLoginTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarKey() {
        return avatarKey;
    }

    public void setAvatarKey(String avatarKey) {
        this.avatarKey = avatarKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLastSignTime() {
        return lastSignTime;
    }

    public void setLastSignTime(String lastSignTime) {
        this.lastSignTime = lastSignTime;
    }

    public int getSignState() {
        return signState;
    }

    public void setSignState(int signState) {
        this.signState = signState;
    }

    public int getContinueSign() {
        return continueSign;
    }

    public void setContinueSign(int continueSign) {
        this.continueSign = continueSign;
    }

    public String getAgencyAccount() {
        return agencyAccount;
    }

    public void setAgencyAccount(String agencyAccount) {
        this.agencyAccount = agencyAccount;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", openId='" + openId + '\'' +
                ", qqId='" + qqId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", endLoginTime='" + endLoginTime + '\'' +
                ", state=" + state +
                ", avatar='" + avatar + '\'' +
                ", avatarKey='" + avatarKey + '\'' +
                ", token='" + token + '\'' +
                ", wxId='" + wxId + '\'' +
                ", qq='" + qq + '\'' +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", ip='" + ip + '\'' +
                ", lastSignTime='" + lastSignTime + '\'' +
                ", signState=" + signState +
                ", continueSign=" + continueSign +
                ", agencyAccount='" + agencyAccount + '\'' +
                '}';
    }
}
