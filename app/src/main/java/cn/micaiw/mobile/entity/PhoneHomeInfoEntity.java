package cn.micaiw.mobile.entity;

import java.util.List;

/**
 * Created by sdhcjhss on 2017/11/10.
 */

public class PhoneHomeInfoEntity {
    private List<Banner> banners;
    private String fans_num;

    public String getFans_num() {
        return fans_num;
    }

    public void setFans_num(String fans_num) {
        this.fans_num = fans_num;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }

}
