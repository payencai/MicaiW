package cn.micaiw.mobile.entity;

/**
 * Created by fanzh on 2017/11/8.
 */

public class PhoneUserEntity {
     private String id;     //      "id":"1006",
    private String mobile;     //             "mobile":"17620943896",
    private String name;     //           "name":"饭饭",
    private String avatar;     //           "avatar":"",
    private String gender;     //           "gender":"0",
    private String birthday;     //           "birthday":"2000-00-00",
    private String recommend_code;     //           "recommend_code":"17569h4i",
    private String city;     //           "city":"广州"
    private String province;     //           "province": "广东",
    private String password;


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRecommend_code() {
        return recommend_code;
    }

    public void setRecommend_code(String recommend_code) {
        this.recommend_code = recommend_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
