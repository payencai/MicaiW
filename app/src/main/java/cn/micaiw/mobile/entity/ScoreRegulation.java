package cn.micaiw.mobile.entity;

/**
 * 作者：凌涛 on 2018/5/24 11:32
 * 邮箱：771548229@qq..com
 */
public class ScoreRegulation {


    /**
     * monday : 5
     * tuesday : 10
     * wednesday : 15
     * thurday : 15
     * friday : 25
     * saturday : 30
     * sunday : 35
     * regulation : 1、积分为米财钱包赠送积分，累积达到相应积分可以兑换相应奖励。2、用户每次签到、投资、邀请好友都可以获得积分。3、本积分最终解释权归米财钱包所有。
     * total : 10000
     * incomeScore : 100
     * scoreAward : 80
     */

    private int monday;
    private int tuesday;
    private int wednesday;
    private int thurday;
    private int friday;
    private int saturday;
    private int sunday;
    private String regulation;
    private int total;
    private int incomeScore;
    private int scoreAward;

    private int signState;//连续签到次数

    public int getSignState() {
        return signState;
    }

    public void setSignState(int signState) {
        this.signState = signState;
    }

    public int getMonday() {
        return monday;
    }

    public void setMonday(int monday) {
        this.monday = monday;
    }

    public int getTuesday() {
        return tuesday;
    }

    public void setTuesday(int tuesday) {
        this.tuesday = tuesday;
    }

    public int getWednesday() {
        return wednesday;
    }

    public void setWednesday(int wednesday) {
        this.wednesday = wednesday;
    }

    public int getThurday() {
        return thurday;
    }

    public void setThurday(int thurday) {
        this.thurday = thurday;
    }

    public int getFriday() {
        return friday;
    }

    public void setFriday(int friday) {
        this.friday = friday;
    }

    public int getSaturday() {
        return saturday;
    }

    public void setSaturday(int saturday) {
        this.saturday = saturday;
    }

    public int getSunday() {
        return sunday;
    }

    public void setSunday(int sunday) {
        this.sunday = sunday;
    }

    public String getRegulation() {
        return regulation;
    }

    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getIncomeScore() {
        return incomeScore;
    }

    public void setIncomeScore(int incomeScore) {
        this.incomeScore = incomeScore;
    }

    public int getScoreAward() {
        return scoreAward;
    }

    public void setScoreAward(int scoreAward) {
        this.scoreAward = scoreAward;
    }
}
