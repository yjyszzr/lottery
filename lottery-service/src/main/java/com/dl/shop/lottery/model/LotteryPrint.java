package com.dl.shop.lottery.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "dl_print_lottery")
public class LotteryPrint {
    /**
     * 出票流水id
     */
    @Id
    @Column(name = "print_lottery_id")
    private Integer printLotteryId;

    /**
     * 订单编号
     */
    @Column(name = "order_sn")
    private String orderSn;

    /**
     * 请求时间
     */
    @Column(name = "accept_time")
    private Integer acceptTime;

    /**
     * 代理商编号
     */
    private String merchant;

    /**
     * 游戏编号
     */
    private String game;

    /**
     * 期次
     */
    private String issue;

    /**
     * 玩法
     */
    @Column(name = "playType")
    private String playtype;

    /**
     * 投注方式
     */
    @Column(name = "betType")
    private String bettype;

    /**
     * 倍数
     */
    private Integer times;

    /**
     * 彩票金额
     */
    private BigDecimal money;

    /**
     * 投注号码
     */
    private String stakes;

    /**
     * 出票时间
     */
    @Column(name = "ticket_time")
    private Integer ticketTime;

    /**
     * 返回码
     */
    @Column(name = "ret_code")
    private String retCode;

    /**
     * 返回码描述信息
     */
    @Column(name = "ret_desc")
    private String retDesc;

    /**
     * 处理结果
     */
    @Column(name = "error_code")
    private String errorCode;

    /**
     * 中心平台订单编号
     */
    @Column(name = "platform_id")
    private String platformId;

    /**
     * 获取出票流水id
     *
     * @return print_lottery_id - 出票流水id
     */
    public Integer getPrintLotteryId() {
        return printLotteryId;
    }

    /**
     * 设置出票流水id
     *
     * @param printLotteryId 出票流水id
     */
    public void setPrintLotteryId(Integer printLotteryId) {
        this.printLotteryId = printLotteryId;
    }

    /**
     * 获取订单编号
     *
     * @return order_sn - 订单编号
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 设置订单编号
     *
     * @param orderSn 订单编号
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    /**
     * 获取请求时间
     *
     * @return accept_time - 请求时间
     */
    public Integer getAcceptTime() {
        return acceptTime;
    }

    /**
     * 设置请求时间
     *
     * @param acceptTime 请求时间
     */
    public void setAcceptTime(Integer acceptTime) {
        this.acceptTime = acceptTime;
    }

    /**
     * 获取代理商编号
     *
     * @return merchant - 代理商编号
     */
    public String getMerchant() {
        return merchant;
    }

    /**
     * 设置代理商编号
     *
     * @param merchant 代理商编号
     */
    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    /**
     * 获取游戏编号
     *
     * @return game - 游戏编号
     */
    public String getGame() {
        return game;
    }

    /**
     * 设置游戏编号
     *
     * @param game 游戏编号
     */
    public void setGame(String game) {
        this.game = game;
    }

    /**
     * 获取期次
     *
     * @return issue - 期次
     */
    public String getIssue() {
        return issue;
    }

    /**
     * 设置期次
     *
     * @param issue 期次
     */
    public void setIssue(String issue) {
        this.issue = issue;
    }

    /**
     * 获取玩法
     *
     * @return playType - 玩法
     */
    public String getPlaytype() {
        return playtype;
    }

    /**
     * 设置玩法
     *
     * @param playtype 玩法
     */
    public void setPlaytype(String playtype) {
        this.playtype = playtype;
    }

    /**
     * 获取投注方式
     *
     * @return betType - 投注方式
     */
    public String getBettype() {
        return bettype;
    }

    /**
     * 设置投注方式
     *
     * @param bettype 投注方式
     */
    public void setBettype(String bettype) {
        this.bettype = bettype;
    }

    /**
     * 获取倍数
     *
     * @return times - 倍数
     */
    public Integer getTimes() {
        return times;
    }

    /**
     * 设置倍数
     *
     * @param times 倍数
     */
    public void setTimes(Integer times) {
        this.times = times;
    }

    /**
     * 获取彩票金额
     *
     * @return money - 彩票金额
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置彩票金额
     *
     * @param money 彩票金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 获取投注号码
     *
     * @return stakes - 投注号码
     */
    public String getStakes() {
        return stakes;
    }

    /**
     * 设置投注号码
     *
     * @param stakes 投注号码
     */
    public void setStakes(String stakes) {
        this.stakes = stakes;
    }

    /**
     * 获取出票时间
     *
     * @return ticket_time - 出票时间
     */
    public Integer getTicketTime() {
        return ticketTime;
    }

    /**
     * 设置出票时间
     *
     * @param ticketTime 出票时间
     */
    public void setTicketTime(Integer ticketTime) {
        this.ticketTime = ticketTime;
    }

    /**
     * 获取返回码
     *
     * @return ret_code - 返回码
     */
    public String getRetCode() {
        return retCode;
    }

    /**
     * 设置返回码
     *
     * @param retCode 返回码
     */
    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    /**
     * 获取返回码描述信息
     *
     * @return ret_desc - 返回码描述信息
     */
    public String getRetDesc() {
        return retDesc;
    }

    /**
     * 设置返回码描述信息
     *
     * @param retDesc 返回码描述信息
     */
    public void setRetDesc(String retDesc) {
        this.retDesc = retDesc;
    }

    /**
     * 获取处理结果
     *
     * @return error_code - 处理结果
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置处理结果
     *
     * @param errorCode 处理结果
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 获取中心平台订单编号
     *
     * @return platform_id - 中心平台订单编号
     */
    public String getPlatformId() {
        return platformId;
    }

    /**
     * 设置中心平台订单编号
     *
     * @param platformId 中心平台订单编号
     */
    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }
}