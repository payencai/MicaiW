package cn.micaiw.mobile.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class PlatformContans {
    public static String root = "http://www.micaiqb.com:8080/";//正式地址
    public static String rootUrl = root + "micai/";//
//    public static String root = "http://192.168.43.47:9999/";//诗安测试地址
//    public static String rootUrl = root + "MiCai/";//
    public static String weburl="http://www.micaiqb.com:8080/micai/manager/getButtonState";



    public static class User {
        public static final String sHead = rootUrl + "user/";
        public static final String sGetVerificationCode = "getVerificationCode";//获取验证码
        public static final String sAddUser = sHead + "addUser";//添加用户，注册
        public static final String sUserLogin = sHead + "userLogin";//账户密码登录
        public static final String sUserLoginByCode = sHead + "userLoginByCode";//通过短信验证码登录
        public static final String sGetUserByUserName = sHead + "getUserByUserName";//根据号码查询用户是否已经存在
        public static final String sGetPropagandaMap = sHead + "getPropagandaMap";//获取宣称图片
        public static final String sGetAppDataStatistical = sHead + "getAppDataStatistical";//获取app统计数据信息
        public static final String sGetFundPlatformByType = sHead + "getFundPlatformByType";//获取基金首页推荐数据
        public static final String sGetPtpPlatformByType = sHead + "getPtpPlatformByType";//获取P2p首页推荐数据
        public static final String sFindPtpPlatformCondition = sHead + "findPtpPlatformCondition";//根据过滤条件获取p2p平台信息
        public static final String sGetAllFundPlatform = sHead + "getAllFundPlatform";//分页查询所有的基金平台信息
        public static final String sGetUserDetailsById = sHead + "getUserDetailsById";//根据用户id查询用户详情信息, 我的页面的钱
        public static final String sGetUserInvestmentDataByUserName = sHead + "getUserInvestmentDataByUserName";//根据userName获取用户今日返现数据
        public static final String sFindUserInvestmentDataByUserName = sHead + "findUserInvestmentDataByUserName";//根据userName获取用户累计返现数据
        public static final String sSelectUserInvestmentDataByUserName = sHead + "selectUserInvestmentDataByUserName";//根据userName获取用户预计返现数据
        public static final String sGetPtpRecordDetailsByUserName = sHead + "getPtpRecordDetailsByUserName";//根据用户userName获取用户总投资金额和累计收益
        public static final String sFindPtpRecordByUserName = sHead + "findPtpRecordByUserName";//根据userName分页获取用户投资记录信息
        public static final String sAddBankCardByUserId = sHead + "addBankCardByUserId";//添加银行卡
        public static final String sAddAlipayByUserId = sHead + "addAlipayByUserId";//添加支付宝
        public static final String sGetAccountByUserId = sHead + "getAccountByUserId";//根据用户userId查询用户收款账号
        public static final String sDeleteAlipayById = sHead + "deleteAlipayById";//删除支付宝账号
        public static final String sDeleteBankCardById = sHead + "deleteBankCardById";//删除银行卡账号
        public static final String sGetInvestmentSchemeByPtpIdForApp = sHead + "getInvestmentSchemeByPtpIdForApp";//根据平台ptpPlatformId查询p2p平台投资方案
        public static final String sGetFundSchemeByFundIdForApp = sHead + "getFundSchemeByFundIdForApp";//根据平台查询基金平台投资方案
        public static final String sGetPtpRecordByPlatformId = sHead + "getPtpRecordByPlatformId";//根据platformId获取投资记录数据
        public static final String sGetFundRecordByPlatformId = sHead + "getFundRecordByPlatformId";//根据platformId获取投资记录数据
        public static final String sGetPlatformDetailsByPlatformId = sHead + "getPlatformDetailsByPlatformId";//根据p2p平台id查询公司信息
        public static final String sFindPasswordByUserName = sHead + "findPasswordByUserName";//找回密码
        public static final String sFindFundPlatformCondition = sHead + "findFundPlatformCondition";//根据条件分页获取基金平台信息
        public static final String sFindPlatformByCondition = sHead + "findPlatformByCondition";//分页获取结束的投资平台数据
        public static final String sUpdatePasswordByUserName = sHead + "updatePasswordByUserName";//修改登录密码
        public static final String sGetInvestmentSchemeByPtpId = sHead + "getInvestmentSchemeByPtpId";//获取p2p投资方案
        public static final String sGetFundSchemeByFundPlatformId = sHead + "getFundSchemeByFundPlatformId";//获取基金投资方案
        public static final String sGetScoreProportion = sHead + "getScoreProportion";//获取投资送积分比率计算积分
        public static final String sAddPtpRecord = sHead + "addPtpRecord";//添加用户P2P投资记录
        public static final String sAddFundRecord = sHead + "addFundRecord";//添加用户基金投资记录
        public static final String sIsSignByUserId = sHead + "isSignByUserId";//判断用户今天是否已签到
        public static final String sAddSignByUserId = sHead + "addSignByUserId";//用户签到
        public static final String sGetScoreRegulation = sHead + "getScoreRegulation";//获取积分规则
        public static final String sGetScoreByUserId = sHead + "getScoreByUserId";//获取用户可用积分
        public static final String sGetSignByUserId = sHead + "getSignByUserId";//获取用户当月签到情况
        public static final String sLoginByQq = sHead + "loginByQq";//qq登录
        public static final String sLoginByWechat = sHead + "loginByWechat";//用户通过微信登录
        public static final String sRegisterWithQq = sHead + "registerWithQq";//qq绑定账号
        public static final String sRegisterWithWechat = sHead + "registerWithWechat";//微信绑定
        public static final String sUpdateUser = sHead + "updateUser";//更新用户数据
        public static final String sGetVersion=sHead+"getVersion";


    }

    public static class Commodity {
        public static final String sHead = rootUrl + "commodity/";
        public static final String sGetCommoditiesForApp = sHead + "getCommoditiesForApp";//app获取商品积分列表

    }

    public static class Image {
        public static final String sHead = rootUrl + "image/";
        public static final String sUploadImage = sHead + "uploadImage";//更新图片

    }

    public static class News {
        public static final String sHead = rootUrl + "news/";
        public static final String sGetNewsOnline = sHead + "getNewsOnline";//线上新闻

    }

    public static class Message {
        public static final String sHead = rootUrl + "message/";
        public static final String sGetMessageOnline = sHead + "getMessageOnline";//app获取消息

    }

    public static class AgencyForA {
        //GET /agencyForA/getAgencyDataById
        public static final String sHead = rootUrl + "agencyForA/";
        public static final String sGetAgencyDataById = sHead + "getAgencyDataById";//根据用户userName获取用户代理中心数据
        public static final String sGetMyInvitationById = sHead + "getMyInvitationById";//根据用户id获取我的邀请数据
        public static final String sGetMyInvitation = sHead + "getMyInvitation";//根据用户id和邀请用户的账号account获取我的邀请数据列表数据

    }

    public static Map<String, Object> OBJECT_MAP = new HashMap<String, Object>();// 所有数据静态


}
