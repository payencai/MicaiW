package cn.tool;

/**
 * Created by fanzh on 2017/11/9.
 */

public class StatusUtils {

    public static String getGenderName(String id){
        if (id.equals("0")){
            return "未知";
        }else if(id.equals("1")){
            return "男";
        }else{
            return "女";
        }
    }

    public static int getGenderId(String name){
        if (name.equals("男")){
            return 1;
        }else if (name.equals("女")){
            return 2;
        }else{
            return 0;
        }
    }

    public static String getShopTypeId(String name){
        if (name.equals("餐饮")){
            return "1001";
        }else if (name.equals("服装")){
            return "1002";
        }else if (name.equals("茶叶")){
            return "1003";
        }else if (name.equals("百货")){
            return "1004";
        }else if (name.equals("旅游")){
            return "1005";
        }else if (name.equals("健身")){
            return "1006";
        }else if (name.equals("酒店")){
            return "1007";
        }else if (name.equals("数码")){
            return "1008";
        }else if (name.equals("汽车")){
            return "1009";
        }else{
            return "";
        }
    }

//    0创建 1已付款 2已发货 3已收货 4用户申请退款 5商家确认退款  7已评价
    public static String getOrderStatusName(String id){
        if (id.equals("0")){
            return "全部";
        }else if(id.equals("1")){
            return "待发货";
        }else if(id.equals("2")){
            return "已发货";
        }else if(id.equals("3")){
            return "已完成";
        }else if(id.equals("4")){
            return "退款中";
        }else if(id.equals("5")){
            return "已退款";
        }else if(id.equals("6")){//超过7天无法申请退款
            return "已完成";
        }else{
            return "已评价";
        }
    }

    //0点赞收入，1下级用户成为代理提供的收入，2下下级用户成为代理
    public static String getMoneyFromName(String id){
        if (id.equals("0")){
            return "分享点赞";
        }else if(id.equals("1")){
            return "一级广告粉丝";
        }else if (id.equals("2")){
            return "二级广告粉丝";
        }else if (id.equals("3")){
            return "一级广告粉丝消费";
        }else if(id.equals("4")){
            return "二级广告粉丝消费";
        }else if (id.equals("5")){
            return "三级广告粉丝消费";
        }else if (id.equals("6")){
            return "三级广告粉丝";
        }else{
            return "";
        }
    }

}
